package brum.service;

import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.PaginatedResponse;

public interface CategoryService {
    PaginatedResponse<Category> getCategories(CategoryListFilters filters);
    Category getCategory(Long id, CategoryFilters filters);
    void addCategory(Category category);
    void modifyCategory(Category category);
}
