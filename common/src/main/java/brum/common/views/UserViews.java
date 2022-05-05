package brum.common.views;

public class UserViews {
    private UserViews() {}

    public interface GetList extends PaginatedView.PaginatedUsers {}
    public interface Get extends GetList {}
    public interface Login {}
    public interface Add {}
    public interface Modify {}

    public interface Password {}
    public interface ResetPassword {}
}
