package brum.domain.impl.categories;

import brum.domain.categories.AddCategoryUC;
import brum.model.dto.categories.Category;
import brum.model.dto.common.BemResponse;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.proxy.CategoryProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AddCategoryUCImpl implements AddCategoryUC {

    private final CategoryProxy categoryProxy;

    public AddCategoryUCImpl(CategoryProxy categoryProxy) {
        this.categoryProxy = categoryProxy;
    }

    @Override
    public void addCategory(Category category) {
        if (category.getParentId() != null) {
            setParentPath(category);
        }
        BemResponse<?> response = categoryProxy.addCategory(category);
        if (!response.isSuccess()) {
            throw response.getException();
        }
    }

    private void setParentPath(Category category) {
        BemResponse<Category> response = categoryProxy.getCategory(category.getParentId());
        if (!response.isSuccess()) {
            throw new BRUMGeneralException(ErrorStatusCode.CATEGORY_NOT_FOUND, LocalDateTime.now());
        }
        category.setParentPath(response.getResponse().getFullPath());
    }
}
