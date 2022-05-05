package brum.model.dto.tree;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Edge {
    private String id;
    private String source;
    private String target;
    private final String type = "smoothstep";
    private String label;

}
