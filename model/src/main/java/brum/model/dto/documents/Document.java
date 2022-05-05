package brum.model.dto.documents;

import brum.common.views.DocumentViews;
import brum.model.dto.identities.Identity;
import brum.model.dto.recipients.Recipient;
import brum.model.dto.users.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    private Long id;
    @JsonView({DocumentViews.AddResponse.class, DocumentViews.GetDocumentList.class})
    private String jobId;
    @JsonView(DocumentViews.Add.class)
    private String previousDocumentBlockchainAddress;
    @JsonView(DocumentViews.GetDocumentList.class)
    private String documentBlockchainAddress;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private String title;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private String versionName;
    @JsonView(DocumentViews.GetDocument.class)
    private String fileName;
    @JsonView(DocumentViews.Add.class)
    private Long categoryId;
    @JsonView(DocumentViews.GetDocumentList.class)
    private String category;
    private String signatoryId;
    private String publishingPersonId;
    @JsonView(DocumentViews.GetDocumentList.class)
    private User publishedBy;
    private byte[] source;
    @JsonView(DocumentViews.GetDocumentList.class)
    private Integer publicationDelayMinutes;
    @JsonView(DocumentViews.GetDocument.class)
    private Integer fileSize;
    @JsonView(DocumentViews.GetDocumentList.class)
    private LocalDateTime publicationStartDate;
    @JsonView(DocumentViews.GetDocumentList.class)
    private LocalDateTime publicationDate;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private LocalDateTime validSince;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private LocalDateTime validUntil;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private LocalDateTime retainUntil;
    @JsonView(DocumentViews.GetDocumentList.class)
    private DocumentType documentType;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private PublicationMode publicationMode;
    @JsonView(DocumentViews.GetDocumentList.class)
    private PublicationStatus documentPublicationStatus;
    @JsonView(DocumentViews.GetDocumentList.class)
    private String ckkStatus;
    @JsonView(DocumentViews.GetDocumentList.class)
    private DocumentStatus status;
    @JsonView(DocumentViews.GetDocument.class)
    private List<Document> history;
    @JsonView({DocumentViews.AddPrivate.class, DocumentViews.GetDocumentList.class})
    private Identity identity;
    private List<Recipient> notificationReceivers;
    @JsonView(DocumentViews.GetDocumentList.class)
    private ForgettingStatus forgettingStatus;
    private String forgettingJobId;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private JsonNode additionalDetails;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private String receiverPublisherId;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private String senderPublisherId;
    @JsonView({DocumentViews.GetDocumentList.class})
    private Boolean read;
    @JsonView({DocumentViews.GetDocumentList.class, DocumentViews.UpdateDocument.class})
    private Boolean favourite;
    @JsonView({DocumentViews.Add.class, DocumentViews.GetDocumentList.class})
    private Boolean draft;
}
