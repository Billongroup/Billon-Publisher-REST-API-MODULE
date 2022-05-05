package brum.domain.impl.documents;

import brum.domain.documents.CheckDocumentStatusUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.ForgettingStatus;
import brum.model.dto.documents.PublicationStatus;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.IdentityPersistenceGateway;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckDocumentStatusUCImpl implements CheckDocumentStatusUC {

    private final DocumentPersistenceGateway documentPersistenceGateway;
    private final IdentityPersistenceGateway identityPersistenceGateway;
    private final DocumentProxy documentProxy;

    public CheckDocumentStatusUCImpl(DocumentPersistenceGateway documentPersistenceGateway,
                                     IdentityPersistenceGateway identityPersistenceGateway,
                                     DocumentProxy documentProxy) {
        this.documentPersistenceGateway = documentPersistenceGateway;
        this.identityPersistenceGateway = identityPersistenceGateway;
        this.documentProxy = documentProxy;
    }

    @Override
    public void checkPublishingStatus() {
        List<String> jobIdList = documentPersistenceGateway.getPublishingDocumentsJobIdList();
        for (String jobId : jobIdList) {
            checkPublishingStatus(jobId);
        }
    }

    private void checkPublishingStatus(String jobId) {
        BemResponse<Document> status = documentProxy.getDocumentStatus(jobId);
        if (!status.isSuccess()) {
            throw status.getException();
        }
        Document document = status.getResponse();
        if (document.getDocumentPublicationStatus().equals(PublicationStatus.PUBLISHING_OK)) {
            documentPersistenceGateway.setPublicationStatus(jobId, document.getDocumentBlockchainAddress(), PublicationStatus.PUBLISHING_OK);
        } else if (document.getDocumentPublicationStatus().equals(PublicationStatus.PUBLISHING_ERROR)) {
            documentPersistenceGateway.setPublicationStatus(jobId, null, PublicationStatus.PUBLISHING_ERROR);
        }
    }

    @Override
    public void checkForgettingStatus() {
        List<String[]> forgettingJobIdList = documentPersistenceGateway.getForgettingDocumentsJobIdList();
        for (String[] jobIds : forgettingJobIdList) {
            checkForgettingStatus(jobIds[0], jobIds[1]);
        }
    }

    private void checkForgettingStatus(String forgettingJobId, String jobId) {
        BemResponse<Document> status = documentProxy.getDocumentStatus(forgettingJobId);
        if (!status.isSuccess()) {
            throw status.getException();
        }
        Document document = status.getResponse();
        if (document.getForgettingStatus().equals(ForgettingStatus.FORGETTING_OK)) {
            processSuccessfulForgetting(jobId);
        } else if (document.getForgettingStatus().equals(ForgettingStatus.FORGETTING_EXCEPTION)) {
            documentPersistenceGateway.setForgettingStatus(jobId, null, ForgettingStatus.FORGETTING_EXCEPTION);
        }
    }

    private void processSuccessfulForgetting(String jobId) {
        Document document = documentPersistenceGateway.getDocument(jobId);
        documentPersistenceGateway.deleteByJobId(jobId);
        List<Document> identityDocuments = documentPersistenceGateway.getDocumentsByIdentityId(document.getIdentity().getId());
        if (identityDocuments == null || identityDocuments.isEmpty()) {
            identityPersistenceGateway.deleteIdentityById(document.getIdentity().getId());
        }
    }

}
