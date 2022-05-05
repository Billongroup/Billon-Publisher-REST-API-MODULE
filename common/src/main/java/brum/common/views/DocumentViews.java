package brum.common.views;

public class DocumentViews {
    private DocumentViews() {}

    public interface GetDocumentList extends PaginatedView.PaginatedDocuments {}
    public interface GetDocument extends GetDocumentList {}

    public interface Add {}
    public interface AddPrivate extends Add {}

    public interface AddResponse {}

    public interface DocumentsReport {}

    public interface UpdateDocument{}
}
