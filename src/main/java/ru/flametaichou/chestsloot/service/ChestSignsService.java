package ru.flametaichou.chestsloot.service;

import ru.flametaichou.chestsloot.Logger;
import ru.flametaichou.chestsloot.model.ChestSign;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestSignsService implements IChestSigns {

    private List<ChestSign> chestSigns;

    public ChestSignsService() {
        chestSigns = new ArrayList<ChestSign>();
    }

    @Override
    public boolean chestExist(ChestSign chestSign) {
        for (ChestSign c : chestSigns) {
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
        Logger.log("CHEST SIGNS");
        System.out.println(chestSigns);
        return chestSigns;
    }

    @Override
    public void removeChestSign(ChestSign chestSign) {
        for (ChestSign c : chestSigns) {
            if (chestSign.getWorldId() == (c.getWorldId()) &&
                    chestSign.getX() == c.getX() &&
                    chestSign.getY() == c.getY() &&
                    chestSign.getZ() == c.getZ()) {
                chestSigns.remove(c);
                Logger.log("chestSign removed! Coordinates: " + Logger.getCoordinatesString(chestSign.getX(), chestSign.getY() ,chestSign.getZ()));
            }
        }
    }

    @Override
    public boolean addChestSign(ChestSign chestSign, boolean created) {
        if (chestExist(chestSign)) {
            Logger.log("chestSign already exist. Coordinates: " + Logger.getCoordinatesString(chestSign.getX(), chestSign.getY() ,chestSign.getZ()));
        } else {
            String word = created ? "created" : "loaded";
            if (chestSign.getCooldown() != 0 &&
                    chestSign.getListName() != "" &&
                    chestSign.getMaxCount() != 0 &&
                    chestSign.getMinCount() != 0) {
                chestSigns.add(chestSign);
                Logger.log("chestSign " + word + "! Coordinates: " + Logger.getCoordinatesString(chestSign.getX(), chestSign.getY() ,chestSign.getZ()));
                return true;
            } else {
                Logger.log("chestSign not " + word + ": not all parameters present! Coordinates: " + Logger.getCoordinatesString(chestSign.getX(), chestSign.getY() ,chestSign.getZ()));
            }
        }
        return false;
    }
}
