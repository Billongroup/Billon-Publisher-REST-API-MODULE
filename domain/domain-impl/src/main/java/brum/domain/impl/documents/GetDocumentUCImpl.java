package brum.domain.impl.documents;

import brum.domain.documents.GetDocumentUC;
import brum.domain.file.writers.WriteRecipientsExcelFile;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.DataFileType;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentFilters;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.documents.DocumentType;
import brum.model.dto.recipients.Recipient;
import brum.model.dto.tree.*;
import brum.model.dto.users.User;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.RecipientPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import brum.proxy.DocumentProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetDocumentUCImpl implements GetDocumentUC {

    private final DocumentProxy documentProxy;
    private final DocumentPersistenceGateway documentPersistenceGateway;
    private final UserPersistenceGateway userPersistenceGateway;
    private final RecipientPersistenceGateway recipientPersistenceGateway;

    public GetDocumentUCImpl(DocumentProxy documentProxy, DocumentPersistenceGateway documentPersistenceGateway,
                             UserPersistenceGateway userPersistenceGateway,
                             RecipientPersistenceGateway recipientPersistenceGateway) {
        this.documentProxy = documentProxy;
        this.documentPersistenceGateway = documentPersistenceGateway;
        this.userPersistenceGateway = userPersistenceGateway;
        this.recipientPersistenceGateway = recipientPersistenceGateway;
    }

    @Override
    public Document getDocument(String id) {
        Document document = getDocumentFromDatabase(id);
        if (document.getDocumentType().equals(DocumentType.PRIVATE)) {
            setPrivateDocumentsAdditionalData(document);
        }
        User creator = userPersistenceGateway.getUserBasicInfoByUsername(document.getPublishingPersonId());
        if (creator == null) {
            creator = new User();
            creator.setUsername(document.getPublishingPersonId());
        }
        document.setPublishedBy(creator);
        return document;
    }

    @Override
    public DataFile downloadDocumentFile(String id) {
        BemResponse<Document> response = documentProxy.getDocumentByJobId(id, true);
        if (!response.isSuccess()) {
            response = documentProxy.getDocumentByBlockchainAddress(id, true);
        }
        if (!response.isSuccess()) {
            throw response.getException();
        }
        DataFile document = new DataFile();
        document.setFile(response.getResponse().getSource());
        document.setFileName(response.getResponse().getFileName());
        document.setFileType(DataFileType.PDF);
        return document;
    }

    @Override
    public DataFile downloadNotificationReceiversFile(String id) {
        List<Recipient> recipients = recipientPersistenceGateway.getDocumentRecipients(id);
        if (recipients == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        DataFile report = new DataFile();
        report.setFile(new WriteRecipientsExcelFile().generateRecipientsReport(recipients));
        report.setFileName("recipients.xlsx");
        report.setFileType(DataFileType.EXCEL);
        return report;
    }

    @Override
    public DocumentTree getDocumentTree(String id) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        int i = 0;
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        try {
            //find default document
            Document defaultDocument = getDocumentFromDatabase(id);
            PwcAdditionalDetails additionalDetails = objectMapper.treeToValue(defaultDocument.getAdditionalDetails(), PwcAdditionalDetails.class);
            Node defaultNode = getNodeFromDocument(additionalDetails, i++, NodeType.DEFAULT);
            if (defaultNode == null) {
                throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
            } else {
                nodes.add(defaultNode);
                //add node outcome
                if (additionalDetails.getComponents() != null) {
                    for (Component component : additionalDetails.getComponents()) {
                        if(component.getComponentPassportId() != null) {
                            Document outcomeDocument = getDocumentFromDatabase(component.getComponentPassportId());
                            PwcAdditionalDetails outcomeAdditionalDetails = objectMapper.treeToValue(outcomeDocument.getAdditionalDetails(), PwcAdditionalDetails.class);
                            Node outcomeNode = getNodeFromDocument(outcomeAdditionalDetails, i++, NodeType.INPUT);
                            if (outcomeNode != null) {
                                nodes.add(outcomeNode);
                                edges.add(getEdgeFromNodes(outcomeNode, defaultNode, component));
                            }
                        }
                    }
                }
                //add nodes income
                DocumentSearchCriteria documentSearchCriteria = new DocumentSearchCriteria();
                DocumentFilters filters = new DocumentFilters();
                filters.setComponentPassportId(defaultDocument.getDocumentBlockchainAddress());
                filters.setDocumentType(DocumentType.ALL);
                documentSearchCriteria.setFilters(filters);



                BemResponse<PaginatedResponse<Document>> response = documentProxy.getDocumentList(documentSearchCriteria, null);
                List<Document> incomeDocuments = response.getResponse().getRows();
                for(Document document : incomeDocuments) {
                    PwcAdditionalDetails incomeAdditionalDetails = objectMapper.treeToValue(document.getAdditionalDetails(), PwcAdditionalDetails.class);
                    Node incomeNode = getNodeFromDocument(incomeAdditionalDetails, i++, NodeType.OUTPUT);
                    Component selectedComponent = incomeAdditionalDetails.getComponents().stream().filter(p-> defaultDocument.getDocumentBlockchainAddress().equals(p.getComponentPassportId())).collect((Collectors.toList())).get(0);
                    if (incomeNode != null){
                        nodes.add(incomeNode);
                        edges.add(getEdgeFromNodes(defaultNode, incomeNode, selectedComponent));
                    }
                }

            }
            return DocumentTree.builder()
                    .edges(edges)
                    .nodes(nodes)
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while gettingTree: ", e);
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }

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
    private void setPrivateDocumentsAdditionalData(Document document) {
        Document documentFromDb = documentPersistenceGateway.getDocument(document.getJobId());
        if(documentFromDb != null) {
            document.setIdentity(documentFromDb.getIdentity());
        }
    }

    private Document getDocumentFromDatabase(String id) {
        BemResponse<Document> response = documentProxy.getDocumentByJobId(id, false);
        if (!response.isSuccess()) {
            response = documentProxy.getDocumentByBlockchainAddress(id, false);
        }
        if (!response.isSuccess()) {
            throw response.getException();
        }
        return response.getResponse();
    }

    private Node getNodeFromDocument(PwcAdditionalDetails additionalDetails, int id, NodeType nodeType) {
        try {
            NodeData nodeData = NodeData.builder()
                    .name(additionalDetails.getProductName())
                    .origin(additionalDetails.getCountryOfOrigin())
                    .recycleRate(additionalDetails.getPercentRecycled())
                    .build();
            return Node.builder().id(Integer.toString(id))
                    .data(nodeData)
                    .type(nodeType.humanName)
                    .build();
        } catch (Exception e) {
            log.warn("Error on deserializing json", e);
            return null;
        }

    }

    private Edge getEdgeFromNodes(Node source, Node target, Component component) {
        return Edge.builder()
                .label(component.getPercentShare() + "% share")
                .id(source.getId() + "_" + target.getId())
                .source(source.getId())
                .target(target.getId())
                .build();

    }
}
