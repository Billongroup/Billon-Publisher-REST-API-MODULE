package brum.proxy.mapper;

import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.categories.CategoryStatus;
import brum.model.dto.documents.DocumentStatus;
import com.zunit.dm.common.module.dto.CategoryFilterDto;
import com.zunit.dm.common.module.enums.ListOrder;
import com.zunit.dm.publisher.services.category.dto.CategoryDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryProxyMapper {

    private CategoryProxyMapper() {
    }

    public static List<Category> map(List<CategoryDto> categoryDtoList) {
        List<Category> result = new ArrayList<>();
        for (CategoryDto categoryBem : categoryDtoList) {
            result.add(map(categoryBem));
        }
        return result;
    }

    public static Category map(CategoryDto categoryBem) {
        Category category = new Category();
        category.setId(categoryBem.getId());
        category.setName(categoryBem.getName());
        category.setIsActive(categoryBem.getActive());
        category.setNoOfDocs(categoryBem.getNoOfDocs());
        category.setNoOfDocsInPath(categoryBem.getNoOfDocsInPath());
        if (StringUtils.hasText(categoryBem.getParentPath())) {
            category.setFullPath(categoryBem.getParentPath() + "/");
        } else {
            category.setFullPath("");
        }
        category.setFullPath(category.getFullPath() + categoryBem.getName());
        if (categoryBem.isAddingError()) {
            category.setStatus(CategoryStatus.ADDING_ERROR);
        } else if (Boolean.TRUE.equals(categoryBem.getInProgress())) {
            category.setStatus(CategoryStatus.IN_PROGRESS);
        } else {
            category.setStatus(CategoryStatus.SUCCESS);
        }
        return category;
    }

    public static CategoryFilterDto map(CategoryListFilters filters, String parentPath) {
        CategoryFilterDto out = map(filters);
        out.setSingleNestingLevel(true);
        out.setIncludeAddingError(true);
        if (filters == null) {
            return out;
        }
        out.setParentPath(parentPath);
        if (filters.getPagination() != null) {
            out.setMaxPageSize(filters.getPagination().getLimit());
            out.setOffset(filters.getPagination().getLimit() * filters.getPagination().getPage());
        }
        if (filters.getOrder() != null) {
            out.setOrder(ListOrder.valueOf(filters.getOrder().name()));
        }
        out.setCategoryName(filters.getName());
        return out;
    }

    public static CategoryFilterDto map(CategoryFilters filters) {
        CategoryFilterDto out = new CategoryFilterDto();
        if (filters == null) {
            return out;
        }
        out.setAccountingNumberOfDocuments(filters.isAccountingNumberOfDocuments());
        if (filters.getDocumentStatusList() != null && !filters.getDocumentStatusList().isEmpty()) {
            for (DocumentStatus status : filters.getDocumentStatusList()) {
                out.getDocumentStatusList().add(https.types_dm_billongroup.DocumentStatus.valueOf(status.name()));
            }
        }
        return out;
    }
}
