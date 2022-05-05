package brum.common.views;

public class CategoryViews {
    private CategoryViews() {}
    public interface Modify {}
    public interface Add {}
    public interface Get extends PaginatedView.PaginatedCategories {}
}
