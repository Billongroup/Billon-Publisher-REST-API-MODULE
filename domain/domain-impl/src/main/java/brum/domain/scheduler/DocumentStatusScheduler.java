package brum.domain.scheduler;

import brum.domain.documents.CheckDocumentStatusUC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DocumentStatusScheduler {

    private final CheckDocumentStatusUC checkDocumentStatusUC;


    public DocumentStatusScheduler(CheckDocumentStatusUC checkDocumentStatusUC) {
        this.checkDocumentStatusUC = checkDocumentStatusUC;
    }

    @Scheduled(cron = "5/15 * * * * *")
    public void checkDocumentsStatus() {
        checkDocumentStatusUC.checkPublishingStatus();
        checkDocumentStatusUC.checkForgettingStatus();
    }


}
