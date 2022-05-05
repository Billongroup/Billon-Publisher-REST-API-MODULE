package brum.domain.impl.documents;

import brum.domain.documents.UpdateDocumentUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentType;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import brum.model.exception.validation.ValidationException;
import brum.proxy.DocumentProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UpdateDocumentUCImpl implements UpdateDocumentUC {

    private final DocumentProxy documentProxy;

    public UpdateDocumentUCImpl(DocumentProxy documentProxy) {
        this.documentProxy = documentProxy;
    }

    @Override
    public void updateDocument(String id, Document document) {
        if(document.getFavourite() == null) {
            throw new ValidationException(InvalidField.FAVOURITE, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        BemResponse<Document> response = documentProxy.getDocumentByJobId(id, false);
        if (!response.isSuccess()) {
            throw response.getException();
        }
        documentProxy.updateDocument(id, document, response.getResponse().getDocumentType());
    }
}
