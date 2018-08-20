package ru.flametaichou.chestsloot.service;

import ru.flametaichou.chestsloot.Logger;
import ru.flametaichou.chestsloot.model.ChestSign;
import ru.flametaichou.chestsloot.model.LootList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ChestSignsService implements IChestSigns {

    private List<ChestSign> chestSigns;

    public ChestSignsService() {
        chestSigns = new ArrayList<ChestSign>();
    }

    @Override
    public boolean chestExist(ChestSign chestSign) {
        for (Iterator<ChestSign> iterator = chestSigns.iterator(); iterator.hasNext(); ) {
            ChestSign c = iterator.next();
            if (chestSign.getWorldId() == (c.getWorldId()) &&
                    chestSign.getX() == c.getX() &&
                    chestSign.getY() == c.getY() &&
                    chestSign.getZ() == c.getZ()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ChestSign> getChestGigns() {
        return chestSigns;
    }

    @Override
    public void removeChestSign(ChestSign chestSign) {
        for (Iterator<ChestSign> iterator = chestSigns.iterator(); iterator.hasNext(); ) {
            ChestSign c = iterator.next();
            if (chestSign.getWorldId() == (c.getWorldId()) &&
                    chestSign.getX() == c.getX() &&
                    chestSign.getY() == c.getY() &&
                    chestSign.getZ() == c.getZ()) {
                iterator.remove();
                Logger.debug("chestSign removed! Coordinates: " + Logger.getCoordinatesString(chestSign));
            }
        }
    }

    @Override
    public boolean addChestSign(ChestSign chestSign, boolean created) {
        if (chestExist(chestSign)) {
            Logger.debug("chestSign already exist. Coordinates: " + Logger.getCoordinatesString(chestSign));
        } else {
            String word = created ? "created" : "loaded";
            if (chestSign.getCooldown() != 0 &&
                    chestSign.getListName() != "" &&
                    chestSign.getMaxCount() != 0 &&
                    chestSign.getMinCount() != 0) {
                chestSigns.add(chestSign);
                Logger.debug("chestSign " + word + "! Coordinates: " + Logger.getCoordinatesString(chestSign));
                return true;
            } else {
                Logger.debug("chestSign not " + word + ": not all parameters present! Coordinates: " + Logger.getCoordinatesString(chestSign));
            }
        }
        return false;
    }
}
