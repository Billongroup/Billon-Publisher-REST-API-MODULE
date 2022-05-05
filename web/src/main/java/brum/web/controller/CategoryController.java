package brum.web.controller;

import brum.common.views.CategoryViews;
import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.service.CategoryService;
import brum.model.dto.common.PaginatedResponse;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import static brum.common.enums.security.EndpointConstants.CategoryControllerConstants.*;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @JsonView(CategoryViews.Get.class)
    @GetMapping(CATEGORY_URI)
    @ApiOperation(GET_CATEGORIES_DESCRIPTION)
    public PaginatedResponse<Category> getCategories(
            @ApiParam(name = "Category filters")
                    CategoryListFilters filters) {
        return categoryService.getCategories(filters);
    }

    @JsonView(CategoryViews.Get.class)
    @GetMapping(CATEGORY_WITH_ID_URI)
    @ApiOperation(GET_CATEGORY_DESCRIPTION)
    public Category getCategory(
            @PathVariable
                    Long id,
            @ApiParam(name = "Category filters")
                    CategoryFilters filters) {
        return categoryService.getCategory(id, filters);
    }

    @JsonView(CategoryViews.Add.class)
    @PostMapping(CATEGORY_URI)
    @ApiOperation(POST_CATEGORY_DESCRIPTION)
    public void postCategory(@JsonView(CategoryViews.Add.class) @RequestBody Category addCategory) {
        categoryService.addCategory(addCategory);
    }

    @JsonView(CategoryViews.Modify.class)
    @PatchMapping(CATEGORY_WITH_ID_URI)
    @ApiOperation(PATCH_CATEGORY_DESCRIPTION)
    public void toggleCategory(@JsonView(CategoryViews.Modify.class) @RequestBody Category toggleCategoryInData,
                               @PathVariable Long id) {
        toggleCategoryInData.setId(id);
        categoryService.modifyCategory(toggleCategoryInData);
    }
}
