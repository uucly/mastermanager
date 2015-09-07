package de.master.manager.myproject.menueBar;

public enum MenuItemEnum {
    
    Start("Start"),
    DRAG_DROP("Drag and Drop"),
    MAP("Map"),
    ABOUT_US("About us"),
    CONTACT("Contact"),
    NONE("");

    private String label;

    private MenuItemEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}