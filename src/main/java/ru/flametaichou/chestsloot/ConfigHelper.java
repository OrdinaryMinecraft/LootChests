package ru.flametaichou.chestsloot;

public class ConfigHelper {

    public static boolean debugMode = false;

    public static boolean percentBySlot = true;
    public static double lastItemInListPercent = 0.05;

    // Эта настройка и TODO: наследование
    // должны включаться только если percentBySlot выключен
    public static boolean getItemsFromAllChestsNearListSign = false;

}
