package brum.model.dto.tree;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Node {
    private String id;
    private String type;
    private NodeData data;
}
