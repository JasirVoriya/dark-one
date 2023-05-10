package cn.darkone.entity.enums;

public enum Sex {
    MALE("男"), FEMALE("女");
    private final String description;

    Sex(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
