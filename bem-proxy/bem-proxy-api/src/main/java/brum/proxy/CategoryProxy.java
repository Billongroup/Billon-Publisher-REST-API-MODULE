package brum.proxy;

import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.PaginatedResponse;

public interface CategoryProxy {
    BemResponse<Category> getCategory(Long id);
    BemResponse<Category> getCategory(Long id, CategoryFilters filters);
    BemResponse<PaginatedResponse<Category>> getCategories(CategoryListFilters filters, String parentPath);
    BemResponse<Object> addCategory(Category category);
    BemResponse<Object> toggleCategory(Category category);
}
