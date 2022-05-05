package brum.domain.utilities;

import brum.model.dto.common.Parameter;

import java.util.List;

public interface UtilitiesUC {
    List<Parameter> getSettings();
    void getSmsCode(String username);
}
