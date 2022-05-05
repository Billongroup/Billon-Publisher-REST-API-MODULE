package brum.domain.impl.categories;

import brum.domain.categories.ToggleCategoryUC;
import brum.model.dto.categories.Category;
import brum.model.dto.common.BemResponse;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import brum.model.exception.validation.ValidationException;
import brum.proxy.CategoryProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ToggleCategoryUCImpl implements ToggleCategoryUC {

    private final CategoryProxy categoryProxy;

    public ToggleCategoryUCImpl(CategoryProxy categoryProxy) {
        this.categoryProxy = categoryProxy;
    }

    @Override
    public void toggleCategory(Category category) {
        if (category.getIsActive() == null) {
            throw new ValidationException(InvalidField.IS_ACTIVE, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        setCategoryName(category);
        BemResponse<?> response = categoryProxy.toggleCategory(category);
        if (!response.isSuccess()) {
            throw response.getException();
        }
    }

    private void setCategoryName(Category category) {
        BemResponse<Category> response = categoryProxy.getCategory(category.getId());
        if (!response.isSuccess()) {
            throw response.getException();
        }
        Category bemCategory = response.getResponse();
        category.setName(bemCategory.getName());
        category.setParentPath(bemCategory.getParentPath());
    }
}
