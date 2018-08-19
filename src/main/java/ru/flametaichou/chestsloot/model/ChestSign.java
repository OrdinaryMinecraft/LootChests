package ru.flametaichou.chestsloot.model;

import java.util.Date;

public class ChestSign {

    private int x;
    private int y;
    private int z;
    private int worldId;

    private long refreshTime;
    private long cooldown;
    private String listName;
    private int minCount;
    private int maxCount;

    public ChestSign() {

    }

    public ChestSign(int x, int y, int z, int worldId, long refreshTime, long cooldown, String listName, int minCount, int maxCount) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldId = worldId;
        this.refreshTime = refreshTime;
        this.cooldown = cooldown;
        this.listName = listName;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
}
