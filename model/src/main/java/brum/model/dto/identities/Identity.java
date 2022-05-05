package brum.model.dto.identities;

import brum.common.views.DocumentViews;
import brum.common.views.IdentityViews;
import brum.model.dto.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Identity {
    @JsonIgnore
    private Long id;
    @JsonView({DocumentViews.AddPrivate.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    @JsonProperty("id")
    private String externalId;
    @JsonView(IdentityViews.GetIdentity.class)
    private User createdBy;
    @JsonView(IdentityViews.GetIdentity.class)
    private IdentityStatus status;
    @JsonView({IdentityViews.Modify.class, IdentityViews.Add.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String firstName;
    @JsonView({IdentityViews.Modify.class, IdentityViews.Add.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String lastName;
    @JsonView({IdentityViews.Add.class, IdentityViews.GetIdentity.class})
    private String documentNumber;
    @JsonView({IdentityViews.Modify.class, IdentityViews.Add.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String email;
    @JsonView({IdentityViews.Modify.class, IdentityViews.Add.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String phoneNumber;
    private String additionalInformation;
    @JsonView(IdentityViews.GetIdentity.class)
    private LocalDateTime createdAt;
    @JsonView({IdentityViews.Modify.class, IdentityViews.Add.class, IdentityViews.GetIdentity.class})
    private Boolean isActive;
    @JsonView({IdentityViews.GdprSuspended.class, IdentityViews.GetIdentity.class})
    private Boolean isGdprSuspended;
}
