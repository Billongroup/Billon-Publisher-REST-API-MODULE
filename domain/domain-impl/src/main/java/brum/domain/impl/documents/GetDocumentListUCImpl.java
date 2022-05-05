package brum.domain.impl.documents;

import brum.domain.documents.GetDocumentListUC;
import brum.domain.file.writers.WriteDocumentsExcelFile;
import brum.model.dto.categories.Category;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.DataFileType;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.*;
import brum.model.dto.users.User;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import brum.proxy.CategoryProxy;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetDocumentListUCImpl implements GetDocumentListUC {

    private final DocumentProxy documentProxy;
    private final CategoryProxy categoryProxy;
    private final DocumentPersistenceGateway documentPersistenceGateway;
    private final UserPersistenceGateway userPersistenceGateway;

    public GetDocumentListUCImpl(DocumentProxy documentProxy, CategoryProxy categoryProxy,
                                 DocumentPersistenceGateway documentPersistenceGateway,
                                 UserPersistenceGateway userPersistenceGateway) {
        this.documentProxy = documentProxy;
        this.categoryProxy = categoryProxy;
        this.documentPersistenceGateway = documentPersistenceGateway;
        this.userPersistenceGateway = userPersistenceGateway;
    }

    @Override
    public PaginatedResponse<Document> getDocumentList(DocumentSearchCriteria searchCriteria) {
        searchCriteria = setDefaults(searchCriteria);
        String categoryPath = getCategoryPath(searchCriteria.getFilters());
        BemResponse<PaginatedResponse<Document>> response = documentProxy.getDocumentList(searchCriteria, categoryPath);
        if (!response.isSuccess()) {
            throw response.getException();
        }
        if (Arrays.asList(DocumentType.ALL, DocumentType.PRIVATE).contains(searchCriteria.getFilters().getDocumentType())) {
            setPrivateDocumentsAdditionalData(response.getResponse().getRows());
        }
        PaginatedResponse<Document> paginatedList = response.getResponse();
        setPublishedBy(paginatedList.getRows());
        return paginatedList;
    }

    @Override
    public DataFile downloadDocumentsReport(DocumentSearchCriteria searchCriteria) {
        List<Document> documents = getDocumentList(searchCriteria).getRows();
        DataFile documentsFile = new DataFile();
        documentsFile.setFile(new WriteDocumentsExcelFile().generateDocumentsReport(documents));
        documentsFile.setFileName("report.xlsx");
        documentsFile.setFileType(DataFileType.EXCEL);
        return documentsFile;
    }

    private String getCategoryPath(DocumentFilters filters) {
        if (filters.getCategoryId() == null) {
            return null;
        }
        BemResponse<Category> response = categoryProxy.getCategory(filters.getCategoryId());
        if (!response.isSuccess()) {
            throw response.getException();
        }
        return response.getResponse().getFullPath();
    }

    private DocumentSearchCriteria setDefaults(DocumentSearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            searchCriteria = new DocumentSearchCriteria();
        }
        if (searchCriteria.getFilters() == null) {
            searchCriteria.setFilters(new DocumentFilters());
        }
        if (searchCriteria.getFilters().getDocumentType() == null) {
            searchCriteria.getFilters().setDocumentType(DocumentType.ALL);
        }
        return searchCriteria;
    }

    private void setPrivateDocumentsAdditionalData(List<Document> documents) {
        for (Document document : documents) {
            if (document.getDocumentType().equals(DocumentType.PRIVATE)) {
                Document documentFromDb = documentPersistenceGateway.getDocument(document.getJobId());
                if(documentFromDb != null) {
                    document.setIdentity(documentFromDb.getIdentity());
                }
            }
        }
    }

    private void setPublishedBy(List<Document> documents) {
        Map<String, User> foundUsers = new HashMap<>();
        for (Document document : documents) {
            if (foundUsers.containsKey(document.getPublishingPersonId())) {
                document.setPublishedBy(foundUsers.get(document.getPublishingPersonId()));
                continue;
            }
            User creator = userPersistenceGateway.getUserBasicInfoByUsername(document.getPublishingPersonId());
            if (creator == null) {
                creator = new User();
                creator.setUsername(document.getPublishingPersonId());
            }
            document.setPublishedBy(creator);
            foundUsers.put(document.getPublishingPersonId(), creator);
        }
    }
}
