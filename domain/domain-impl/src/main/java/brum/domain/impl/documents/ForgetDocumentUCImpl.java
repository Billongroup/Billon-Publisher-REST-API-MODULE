package brum.domain.impl.documents;

import brum.domain.documents.ForgetDocumentUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.documents.ForgettingStatus;
import brum.model.exception.BemException;
import brum.persistence.DocumentPersistenceGateway;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ForgetDocumentUCImpl implements ForgetDocumentUC {

    private final DocumentProxy documentProxy;
    private final DocumentPersistenceGateway documentPersistenceGateway;

    public ForgetDocumentUCImpl(DocumentProxy documentProxy, DocumentPersistenceGateway documentPersistenceGateway) {
        this.documentProxy = documentProxy;
        this.documentPersistenceGateway = documentPersistenceGateway;
    }

    @Override
    public void forgetDocument(String blockchainAddress) {
        BemResponse<String> response = documentProxy.forgetDocument(blockchainAddress);
        if (!response.isSuccess()) {
            throw response.getException();
        }
        documentPersistenceGateway.setForgettingStatus(blockchainAddress, response.getResponse(), ForgettingStatus.FORGETTING_IN_PROGRESS);
    }
}
