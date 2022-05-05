package brum.model.dto.tree;


public enum NodeType {

    INPUT("input"),
    OUTPUT("output"),
    DEFAULT("default");

    NodeType(String humanName) {
        this.humanName = humanName;
    }

    public String humanName;


}
