package brum.domain.file.resolvers;

import brum.domain.file.handlers.FileHeader;
import brum.model.dto.common.DataFileType;
import brum.model.dto.identities.Identity;

import java.util.*;

import static brum.domain.file.handlers.FileHeader.*;

public class IdentityFileResolver extends AbstractFileResolver {

    private static final Map<FileHeader, Boolean> headersToRead = new EnumMap<>(FileHeader.class);

    static {
        headersToRead.put(FIRST_NAME, true);
        headersToRead.put(LAST_NAME, true);
        headersToRead.put(DOCUMENT_NUMBER, true);
        headersToRead.put(EMAIL, true);
        headersToRead.put(PHONE_NUMBER, true);
        headersToRead.put(IS_ACTIVE, false);
    }

    public IdentityFileResolver(byte[] file, DataFileType fileType) {
        super(file, fileType, headersToRead);
    }

    public List<Identity> resolve() {
        List<Identity> identitiesList = new ArrayList<>();
        int count = 0;
        for (String[] data : data) {
            count++;
            Identity identity = new Identity();
            identity.setDocumentNumber(data[valueIndex.get(DOCUMENT_NUMBER)]);
            identity.setPhoneNumber(formatPhoneNumber(data[valueIndex.get(PHONE_NUMBER)]));
            identity.setFirstName(data[valueIndex.get(FIRST_NAME)]);
            identity.setLastName(data[valueIndex.get(LAST_NAME)]);
            identity.setEmail(data[valueIndex.get(EMAIL)]);
            if (valueIndex.containsKey(IS_ACTIVE)) {
                identity.setIsActive(parseToBooleanValue(data[valueIndex.get(IS_ACTIVE)], count));
            }
            identitiesList.add(identity);
        }
        return identitiesList;
    }
}
