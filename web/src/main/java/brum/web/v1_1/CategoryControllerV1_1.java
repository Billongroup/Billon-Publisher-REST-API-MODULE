package brum.web.v1_1;

import brum.common.views.CategoryViews;
import brum.model.dto.categories.Category;
import brum.model.dto.categories.CategoryFilters;
import brum.model.dto.categories.CategoryListFilters;
import brum.model.dto.common.PaginatedResponse;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.service.CategoryService;
import brum.web.controller.CategoryController;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static brum.common.enums.security.EndpointConstants.CategoryControllerConstants.*;

@RestController
@RequestMapping("/v1.1")
@Api(tags = TAG)
public class CategoryControllerV1_1 extends CategoryController {

    public CategoryControllerV1_1(CategoryService categoryService) {
        super(categoryService);
    }

    @JsonView(CategoryViews.Get.class)
    @GetMapping("/categories/test/hello")
    @ApiOperation(GET_CATEGORIES_DESCRIPTION)
    public PaginatedResponse<Category> getCategories(
            @ApiParam(name = "Category filters")
                    CategoryListFilters filters) {
        PaginatedResponse<Category> response = new PaginatedResponse<>();
        response.setCount(200);
        return response;
    }

    @JsonView(CategoryViews.Get.class)
    @GetMapping(CATEGORY_WITH_ID_URI)
    @ApiOperation(GET_CATEGORY_DESCRIPTION)
    public Category getCategory(
            @PathVariable
                    Long id,
            @ApiParam(name = "Category filters")
                    CategoryFilters filters) {
        Category result = new Category();
        result.setName("hello from v1.1");
        return result;
    }

    @Override
    public void postCategory(@JsonView(CategoryViews.Add.class) @RequestBody Category addCategory) {
        throw new BRUMGeneralException(ErrorStatusCode.NOTIFICATION_SENDING_ERROR, LocalDateTime.now());
    }

    @JsonView(CategoryViews.Add.class)
    @PostMapping("/categories/add")
    @ApiOperation(POST_CATEGORY_DESCRIPTION)
    public String addCategory(@JsonView(CategoryViews.Add.class) @RequestBody Category addCategory) {
        return "added";
    }
}
