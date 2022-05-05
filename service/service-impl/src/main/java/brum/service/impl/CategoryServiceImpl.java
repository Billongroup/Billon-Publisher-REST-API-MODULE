package brum.service.impl;


import brum.domain.categories.AddCategoryUC;
import brum.domain.categories.ToggleCategoryUC;
import brum.model.dto.categories.Category;
import brum.domain.categories.GetCategoryUC;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.PaginatedResponse;
import brum.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final GetCategoryUC getCategoryUC;
    private final ToggleCategoryUC toggleCategoryUC;
    private final AddCategoryUC addCategoryUC;

    public CategoryServiceImpl(GetCategoryUC getCategoryUC, ToggleCategoryUC toggleCategoryUC, AddCategoryUC addCategoryUC) {
        this.getCategoryUC = getCategoryUC;
        this.toggleCategoryUC = toggleCategoryUC;
        this.addCategoryUC = addCategoryUC;
    }

    @Override
    public PaginatedResponse<Category> getCategories(CategoryListFilters filters) {
        return getCategoryUC.getCategories(filters);
    }

    @Override
    public Category getCategory(Long id, CategoryFilters filters) {
        return getCategoryUC.getCategory(id, filters);
    }

    @Override
    public void addCategory(Category category) {
        addCategoryUC.addCategory(category);
    }

    @Override
    public void modifyCategory(Category category) {
        toggleCategoryUC.toggleCategory(category);
    }
}
