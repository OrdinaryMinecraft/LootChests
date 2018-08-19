package ru.flametaichou.chestsloot.service;

import ru.flametaichou.chestsloot.LootChestsBase;
import ru.flametaichou.chestsloot.Logger;
import ru.flametaichou.chestsloot.model.LootList;
import ru.flametaichou.chestsloot.model.LootListsXml;

import java.util.ArrayList;

public class LootListsService implements ILootLists {

    private static LootListsXml lists = new LootListsXml();

    public LootListsService() {
        lists.setLists(new ArrayList<LootList>());
    }

    @Override
    public boolean addLootList(LootList lootList, boolean created) {
        if (LootChestsBase.lootListsService.listExist(lootList)) {
            Logger.log("list already exist. Coordinates: " + Logger.getCoordinatesString(lootList.getX(), lootList.getY(), lootList.getZ()));
        } else {
            String word = created ? "created" : "loaded";
            if (lootList.getName() != "") {
                lists.getLists().add(lootList);
                Logger.log("lootList " + word + "! Coordinates: " + Logger.getCoordinatesString(lootList.getX(), lootList.getY() ,lootList.getZ()));
                return true;
            } else {
                Logger.log("lootList not " + word + ": not all parameters present! Coordinates: " + Logger.getCoordinatesString(lootList.getX(), lootList.getY() ,lootList.getZ()));
            }
        }
        return false;
    }

    @Override
    public void removeLootList(LootList list) {
        for (LootList l : lists.getLists()) {
            if (list.getWorldId() == (l.getWorldId()) &&
                    list.getX() == l.getX() &&
                    list.getY() == l.getY() &&
                    list.getZ() == l.getZ()) {
                lists.getLists().remove(l);
                Logger.log("list removed! Coordinates: " + Logger.getCoordinatesString(list.getX(), list.getY(), list.getZ()));
            }
        }
    }

    @Override
    public boolean listExist(LootList list) {
        for (LootList l : lists.getLists()) {
            if (list.getWorldId() == (l.getWorldId()) &&
                    list.getX() == l.getX() &&
                    list.getY() == l.getY() &&
                    list.getZ() == l.getZ()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setLootListsXML(LootListsXml lootLists) {
        lists = lootLists;
    }

    @Override
    public LootListsXml getLootListsXML() {
        return lists;
    }

    @Override
    public LootList findByName(String name) {
        for (LootList list : lists.getLists()) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        Logger.log("can't find Loot List by name: " + name);
        return null;
    }
}
