package brum.domain.impl.categories;


import brum.domain.categories.GetCategoryUC;
import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.SortOrder;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.proxy.CategoryProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetCategoryUCImpl implements GetCategoryUC {

    private final CategoryProxy categoryProxy;

    public GetCategoryUCImpl(CategoryProxy categoryProxy) {
        this.categoryProxy = categoryProxy;
    }

    @Override
    public PaginatedResponse<Category> getCategories(CategoryListFilters filters) {
        if (filters == null) {
            filters = new CategoryListFilters();
        }
        setDefaultsToFilters(filters);
        String parentPath = getParentPath(filters);
        BemResponse<PaginatedResponse<Category>> response = categoryProxy.getCategories(filters, parentPath);
        if (!response.isSuccess()) {
            throw response.getException();
        }
        return response.getResponse();
    }

    @Override
    public Category getCategory(Long id, CategoryFilters filters) {
        BemResponse<Category> response = categoryProxy.getCategory(id, filters);
        if (!response.getStatus().equals(ErrorStatusCode.CATEGORY_NOT_FOUND)) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        return response.getResponse();
    }

    private void setDefaultsToFilters(CategoryListFilters filters) {
        if (filters.getOrder() == null) {
            filters.setOrder(SortOrder.ASCENDING);
        }
        if (filters.getPagination() == null) {
            PaginationFilter pagination = new PaginationFilter();
            pagination.setPage(0);
            pagination.setLimit(0);
            filters.setPagination(pagination);
        }
        if (filters.getPagination().getPage() == null) {
            filters.getPagination().setPage(0);
        }
        if (filters.getPagination().getLimit() == null) {
            filters.getPagination().setLimit(0);
        }
    }

    private String getParentPath(CategoryListFilters filters) {
        if (filters.getParentId() == null) {
            return null;
        }
        BemResponse<Category> response = categoryProxy.getCategory(filters.getParentId());
        if (!response.isSuccess()) {
            throw response.getException();
        }
        return response.getResponse().getFullPath();
    }
}
