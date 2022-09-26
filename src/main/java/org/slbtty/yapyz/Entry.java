package org.slbtty.yapyz;

public class Entry {
    private final String name;
    private final String desc;

    public Entry(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
