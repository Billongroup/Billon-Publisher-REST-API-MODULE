package brum.model.dto.tree;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeData {
    private String name;
    private String origin;
    private long recycleRate;
}
