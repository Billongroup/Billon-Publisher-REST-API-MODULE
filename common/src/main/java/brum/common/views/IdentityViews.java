package brum.common.views;

public class IdentityViews {
    private IdentityViews() {}
    public interface GetIdentity extends PaginatedView.PaginatedIdentities {}
    public interface Modify {}
    public interface Add {}
    public interface GdprSuspended {}
}
