package ru.flametaichou.chestsloot.service;

import net.minecraft.tileentity.TileEntitySign;
import ru.flametaichou.chestsloot.LootChestsBase;
import ru.flametaichou.chestsloot.Logger;
import ru.flametaichou.chestsloot.model.LootList;
import ru.flametaichou.chestsloot.model.LootListsXml;

import java.util.ArrayList;
import java.util.Iterator;

public class LootListsService implements ILootLists {

    private static LootListsXml lists = new LootListsXml();

    public LootListsService() {
        lists.setLists(new ArrayList<LootList>());
    }

    @Override
    public boolean addLootList(LootList lootList, boolean created) {
        if (LootChestsBase.lootListsService.listExist(lootList)) {
            Logger.debug("list already exist. Coordinates: " + Logger.getCoordinatesString(lootList));
        } else {
            String word = created ? "created" : "loaded";
            if (lootList.getName() != "") {
                lists.getLists().add(lootList);
                Logger.debug("lootList " + word + "! Coordinates: " + Logger.getCoordinatesString(lootList));
                return true;
            } else {
                Logger.debug("lootList not " + word + ": not all parameters present! Coordinates: " + Logger.getCoordinatesString(lootList));
            }
        }
        return false;
    }

    @Override
    public void removeLootList(LootList list) {
        for (Iterator<LootList> iterator = lists.getLists().iterator(); iterator.hasNext(); ) {
            LootList l = iterator.next();
            if (list.getWorldId() == (l.getWorldId()) &&
                    list.getX() == l.getX() &&
                    list.getY() == l.getY() &&
                    list.getZ() == l.getZ()) {
                iterator.remove();
                Logger.debug("list removed! Coordinates: " + Logger.getCoordinatesString(list));
            }
        }
    }

    @Override
    public boolean listExist(LootList list) {
        for (Iterator<LootList> iterator = lists.getLists().iterator(); iterator.hasNext(); ) {
            LootList l = iterator.next();
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
        for (Iterator<LootList> iterator = lists.getLists().iterator(); iterator.hasNext(); ) {
            LootList list = iterator.next();
            if (list.getName().equals(name)) {
                return list;
            }
        }
        Logger.error("can't find Loot List by name: " + name);
        return null;
    }
}
