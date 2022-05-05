package brum.model.dto.categories;

import brum.common.views.CategoryViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    @JsonView(CategoryViews.Get.class)
    private Long id;
    @JsonView(CategoryViews.Add.class)
    private Long parentId;
    @JsonView({CategoryViews.Get.class, CategoryViews.Add.class})
    private String name;
    @JsonIgnore
    private String parentPath;
    @JsonIgnore
    private String fullPath;
    @JsonView({CategoryViews.Get.class, CategoryViews.Add.class, CategoryViews.Modify.class})
    private Boolean isActive;
    @JsonView(CategoryViews.Get.class)
    private CategoryStatus status;
    @JsonView(CategoryViews.Get.class)
    private Long noOfDocs;
    @JsonView(CategoryViews.Get.class)
    private Long noOfDocsInPath;
}
