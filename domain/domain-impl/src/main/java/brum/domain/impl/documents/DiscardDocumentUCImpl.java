package brum.domain.impl.documents;

import brum.domain.documents.DiscardDocumentUC;
import brum.model.dto.common.BemResponse;
import brum.model.exception.BemException;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DiscardDocumentUCImpl implements DiscardDocumentUC {

    private final DocumentProxy documentProxy;

    public DiscardDocumentUCImpl(DocumentProxy documentProxy) {
        this.documentProxy = documentProxy;
    }

    @Override
    public void discardDocument(String jobId) {
        BemResponse<?> response = documentProxy.discardDocument(jobId);
        if (!response.isSuccess()) {
            throw response.getException();
        }
    }
}
