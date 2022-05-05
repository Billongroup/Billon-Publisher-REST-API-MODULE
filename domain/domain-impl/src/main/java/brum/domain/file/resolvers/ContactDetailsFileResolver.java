package brum.domain.file.resolvers;

import brum.domain.file.handlers.FileHeader;
import brum.model.dto.common.DataFileType;
import brum.model.dto.recipients.ContactDetailsType;
import brum.model.dto.recipients.Recipient;
import org.springframework.util.StringUtils;

import java.util.*;

import static brum.domain.file.handlers.FileHeader.*;

public class ContactDetailsFileResolver extends AbstractFileResolver {

    private static final Map<FileHeader, Boolean> headersToRead = new EnumMap<>(FileHeader.class);

    static {
        headersToRead.put(SOURCE_SYSTEM, true);
        headersToRead.put(ID, true);
        headersToRead.put(EMAIL, true);
        headersToRead.put(PHONE_NUMBER, false);
    }

    public ContactDetailsFileResolver(byte[] file, DataFileType fileType) {
        super(file, fileType, headersToRead);
    }

    public List<Recipient> resolve() {
        List<Recipient> recipientList = new ArrayList<>();
        for (String[] data : data) {
            Recipient recipient = new Recipient();
            recipient.setExternalId(getNullable(data[valueIndex.get(SOURCE_SYSTEM)]) + "_" + getNullable(data[valueIndex.get(ID)]));
            recipient.setPhoneNumber(formatPhoneNumber(data[valueIndex.get(PHONE_NUMBER)]));
            recipient.setEmail(data[valueIndex.get(EMAIL)]);
            recipientList.add(recipient);
        }
        return recipientList;
    }


}
