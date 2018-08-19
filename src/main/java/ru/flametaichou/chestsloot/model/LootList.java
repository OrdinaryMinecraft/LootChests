package ru.flametaichou.chestsloot.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LootList {

    private String name;
    private int x;
    private int y;
    private int z;
    private int worldId;

    public LootList() {

    }

    public LootList(String name, int x, int y, int z, int worldId) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldId = worldId;
    }

    @XmlAttribute(name = "name")

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "x")
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @XmlAttribute(name = "y")
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @XmlAttribute(name = "z")
    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @XmlAttribute(name = "world")
    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
}
