package brum.domain.categories;

import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.PaginatedResponse;

public interface GetCategoryUC {
    PaginatedResponse<Category> getCategories(CategoryListFilters filters);
    Category getCategory(Long id, CategoryFilters filters);
}
