package ru.flametaichou.chestsloot.service;

import ru.flametaichou.chestsloot.model.LootList;
import ru.flametaichou.chestsloot.model.LootListsXml;

public interface ILootLists {

    boolean addLootList(LootList lootList, boolean created);
    void removeLootList(LootList lootList);
    boolean listExist(LootList lootList);
    void setLootListsXML(LootListsXml lootLists);
    LootListsXml getLootListsXML();
    LootList findByName(String name);
}
