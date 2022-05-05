package brum.model.dto.tree;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentTree {
    private List<Node> nodes;
    private List<Edge> edges;
}
