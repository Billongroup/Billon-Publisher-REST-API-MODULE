package brum.model.dto.documents;

import brum.model.dto.common.DateRange;
import brum.model.dto.identities.IdentityFilters;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class DocumentFilters {
    private DocumentType documentType;
    private String blockchainAddress;
    private String title;
    private Long categoryId;
    private String signatoryId;
    private String publishedBy;
    private DateRange creationDateRange;
    private DateRange publicationDateRange;
    private DateRange validSinceDateRange;
    private DateRange validUntilDateRange;
    private DateRange retainUntilDateRange;
    private List<PublicationStatus> publicationStatusList;
    private List<DocumentStatus> documentStatusList;
    private IdentityFilters identity;
    private String senderPublisherId;
    private Boolean favourite;
    private Boolean read;
    private String lcReferenceNumber;
    private String lcStatus;
    private String componentPassportId;
    private Boolean draft;
}
