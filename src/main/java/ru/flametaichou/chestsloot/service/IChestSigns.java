package ru.flametaichou.chestsloot.service;

import ru.flametaichou.chestsloot.model.ChestSign;

import java.util.List;

public interface IChestSigns {

    boolean addChestSign(ChestSign chestSign, boolean created);
    void removeChestSign(ChestSign chestSign);
    boolean chestExist(ChestSign chestSign);
    List<ChestSign> getChestGigns();
}
