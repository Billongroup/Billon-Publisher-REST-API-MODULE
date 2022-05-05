package brum.proxy.impl;

import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.PaginatedResponse;
import brum.model.exception.ErrorStatusCode;
import brum.proxy.CategoryProxy;
import brum.proxy.mapper.CategoryProxyMapper;
import com.zunit.dm.publisher.services.category.dto.CategoryDto;
import com.zunit.dm.publisher.services.category.dto.CategoryResponseDto;
import com.zunit.dm.publisher.services.category.dto.CategoryResponseListDto;
import com.zunit.dm.publisher.services.category.facade.mvp.MVPCategoryFacade;
import org.springframework.stereotype.Component;

import static https.types_dm_billongroup.InternalSystemStatusErrors.*;

@Component
public class CategoryProxyImpl implements CategoryProxy {

    private final MVPCategoryFacade categoryFacade;

    public CategoryProxyImpl(MVPCategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    @Override
    public BemResponse<Category> getCategory(Long id) {
        return getCategory(id, null);
    }

    @Override
    public BemResponse<Category> getCategory(Long id, CategoryFilters filters) {
        CategoryDto category = categoryFacade.getCategory(id, CategoryProxyMapper.map(filters));
        if (category == null) {
            return BemResponse.<Category>builder().status(ErrorStatusCode.CATEGORY_NOT_FOUND).build();
        }
        return BemResponse.success(CategoryProxyMapper.map(category));
    }

    @Override
    public BemResponse<PaginatedResponse<Category>> getCategories(CategoryListFilters filters, String parentPath) {
        CategoryResponseListDto response = categoryFacade.listFilteredCategories(CategoryProxyMapper.map(filters, parentPath));
        if (!response.getErrorMessage().equals(SUCCESS)) {
            return BemResponse.<PaginatedResponse<Category>>builder().status(ErrorStatusCode.CATEGORY_NOT_FOUND).build();
        }
        PaginatedResponse<Category> categoryList = new PaginatedResponse<>();
        categoryList.setCount(response.getNumberOfCategoriesFound());
        categoryList.setRows(CategoryProxyMapper.map(response.getCategoryDto()));
        return BemResponse.success(categoryList);
    }

    @Override
    public BemResponse<Object> addCategory(Category category) {
        CategoryDto newCategory = new CategoryDto();
        newCategory.setName(category.getName());
        newCategory.setActive(category.getIsActive());
        newCategory.setParentPath(category.getParentPath());
        CategoryResponseDto response = categoryFacade.addCategory(newCategory);
        if (!response.getErrorMessage().equals(SUCCESS)) {
            return BemResponse.bemError(response.getErrorMessage().name());
        }
        return BemResponse.success();
    }

    @Override
    public BemResponse<Object> toggleCategory(Category category) {
        CategoryDto categoryData = new CategoryDto();
        categoryData.setName(category.getName());
        categoryData.setParentPath(category.getParentPath());
        categoryData.setActive(category.getIsActive());
        CategoryResponseDto response = categoryFacade.modifyCategory(categoryData);
        if (!response.getErrorMessage().equals(SUCCESS)) {
            return BemResponse.bemError(response.getErrorMessage().name());
        }
        return BemResponse.success();
    }
}
