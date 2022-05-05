package brum.proxy.mapper;

import brum.model.dto.identities.Identity;
import brum.model.dto.recipients.Recipient;
import com.zunit.dm.common.module.dto.ContactDetailsDto;
import com.zunit.dm.common.module.dto.IdentityDocumentDto;
import com.zunit.dm.common.module.dto.IdentityDocumentExtendedDto;
import com.zunit.dm.common.module.dto.IdentityDto;
import https.types_dm_billongroup.Country;
import https.types_dm_billongroup.DurableMediaIdentificationType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IdentityProxyMapper {
    private IdentityProxyMapper() {}

    public static List<ContactDetailsDto> mapNotificationReceivers(List<Recipient> recipients) {
        List<ContactDetailsDto> result = new ArrayList<>();
        for (Recipient recipient : recipients) {
            if (StringUtils.hasText(recipient.getEmail())) {
                ContactDetailsDto contactDetails = new ContactDetailsDto();
                contactDetails.setExternalId(recipient.getExternalId());
                contactDetails.setEmailAddress(recipient.getEmail());
                result.add(contactDetails);
            }
            if (StringUtils.hasText(recipient.getPhoneNumber())) {
                ContactDetailsDto contactDetails = new ContactDetailsDto();
                contactDetails.setExternalId(recipient.getExternalId());
                contactDetails.setPhoneNumber(recipient.getPhoneNumber());
                result.add(contactDetails);
            }
        }
        return result;
    }

    public static IdentityDto mapIdentity(Identity identity) {
        IdentityDto result = new IdentityDto();
        if (identity == null) {
            return null;
        }
        result.setPublisherCif(identity.getDocumentNumber());

        List<ContactDetailsDto> contactDetailsList = new ArrayList<>();
        ContactDetailsDto contactDetails = new ContactDetailsDto();
        contactDetails.setPhoneNumber(identity.getPhoneNumber());
        contactDetails.setEmailAddress(identity.getEmail());
        contactDetailsList.add(contactDetails);
        result.setContactDetailsList(contactDetailsList);

        List<IdentityDocumentDto> identityDocumentList = new ArrayList<>();
        IdentityDocumentExtendedDto identityDocument = new IdentityDocumentExtendedDto();
        identityDocument.setDocumentType(DurableMediaIdentificationType.OTHER);
        identityDocument.setIssueingCountry(Country.PL);
        identityDocument.setDocumentNumber(identity.getDocumentNumber());
        identityDocument.setForename(identity.getFirstName());
        identityDocument.setSurname(identity.getLastName());
        identityDocumentList.add(identityDocument);
        result.setIdDocumentList(identityDocumentList);

        return result;
    }
}
