
package brum.model.dto.tree;


import lombok.Data;

import java.util.List;

@Data
public class PwcAdditionalDetails {

    private Long batchNumber;
    private String countryOfOrigin;
    private String factory;
    private String itemId;
    private Long percentRecycled;
    private Long productId;
    private String productName;
    private String region;
    private List<Component> components;
}
