package ru.flametaichou.chestsloot;

import ru.flametaichou.chestsloot.model.ChestSign;
import ru.flametaichou.chestsloot.model.LootList;

public class Logger {

    private static final String prefix = "[LootChests] ";
    private static final String prefixInfo = "(INFO) ";
    private static final String prefixEror= "(ERROR) ";

    public static void log(String string) {
        System.out.println(prefix + prefixInfo + string);
    }

    public static void error(String string) {
        System.out.println(prefix + prefixEror + string);
    }

    public static String getCoordinatesString(int x, int y, int z) {
        return "x:" + x + " y:" + y + " z:" + z;
    }

    public static String getCoordinatesString(ChestSign chestSign) {
        return "x:" + chestSign.getX() + " y:" + chestSign.getY() + " z:" + chestSign.getZ();
    }

    public static String getCoordinatesString(LootList lootList) {
        return "x:" + lootList.getX() + " y:" + lootList.getY() + " z:" + lootList.getZ();
    }
}
