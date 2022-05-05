package brum.domain.impl.documents;

import brum.domain.documents.GetDocumentUC;
import brum.domain.documents.PublishDocumentUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentType;
import brum.model.dto.documents.PublicationStatus;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.ParameterPersistenceGateway;
import brum.proxy.DocumentProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PublishDocumentUCImpl implements PublishDocumentUC {

    private final DocumentProxy documentProxy;
    private final DocumentPersistenceGateway documentPersistenceGateway;
    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private final GetDocumentUC getDocumentUC;


    public PublishDocumentUCImpl(DocumentProxy documentProxy, DocumentPersistenceGateway documentPersistenceGateway,
                                 ParameterPersistenceGateway parameterPersistenceGateway, GetDocumentUC getDocumentUC) {
        this.documentProxy = documentProxy;
        this.documentPersistenceGateway = documentPersistenceGateway;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        this.getDocumentUC = getDocumentUC;
    }

    @Override
    public void publishDocument(byte[] document, String jobId, String publisherUsername, DocumentType documentType) {
        //Boolean isOneStep = parameterPersistenceGateway.getParameterValue(ParameterKey.ONE_STEP_PUBLISH);
//        if (Boolean.TRUE.equals(isOneStep)) {
//            throw new BRUMGeneralException(ErrorStatusCode.ONE_STEP_PUBLISH_ENABLED, LocalDateTime.now());
//        }
        Document databaseDocument = getDocumentUC.getDocument(jobId);
        DataFile dataFile = getDocumentUC.downloadDocumentFile(jobId);
        databaseDocument.setSource(dataFile.getFile());
        databaseDocument.setPublishingPersonId(publisherUsername);
        databaseDocument.setDraft(false);

        BemResponse<?> response = documentProxy.publishDocument(databaseDocument);
        if (!response.isSuccess()) {
            throw response.getException();
        }
        documentPersistenceGateway.setPublicationStatus(jobId, null, PublicationStatus.PUBLISHING_INITIATED);
    }
}
