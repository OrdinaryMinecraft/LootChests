package ru.flametaichou.chestsloot;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import ru.flametaichou.chestsloot.model.ChestSign;
import ru.flametaichou.chestsloot.model.LootList;

import java.util.Objects;

public class PlayerInteractHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            // Если кликнули по контейнеру
            Block block = event.world.getBlock(event.x,event.y,event.z);
            if (block instanceof BlockContainer) {
                // Ищем таблички
                checkForSigns(event.x, event.y, event.z, event.world);
            }
        }
    }

    private void checkForSigns(int eventx, int eventy, int eventz, World world) {
        int radius = 1;
        for (int x = eventx - radius; x <= eventx + radius; x++) {
            for (int y = eventy - radius; y <= eventy + radius; y++) {
                for (int z = eventz - radius; z <= eventz + radius; z++) {
                    if (world.getBlock(x, y ,z) == Blocks.standing_sign || world.getBlock(x, y ,z) == Blocks.wall_sign) {
                        TileEntitySign sign = (TileEntitySign) world.getTileEntity(x, y ,z);
                        String[] signText = sign.signText;
                        if (signText[0].contains("[Loot]")) {
                            addChestSign(signText, x, y ,z, world.provider.dimensionId);
                        } else if (signText[0].contains("[List]")) {
                            addList(signText, x, y ,z, world.provider.dimensionId);
                        }
                    }
                }
            }
        }
    }

    private void addChestSign(String[] lines, int x, int y, int z, int worldId) {
        try {
            String listName = lines[1];
            long cooldown = Long.parseLong(lines[2]);
            String[] minmaxcount = lines[3].split("-");
            int minCount = Integer.parseInt(minmaxcount[0]);
            int maxCount = Integer.parseInt(minmaxcount[1]);
            ChestSign chestSign = new ChestSign(x, y, z, worldId, -cooldown, cooldown, listName, minCount, maxCount);
            LootChestsBase.chestSignsService.addChestSign(chestSign, false);
        } catch (Exception e) {
            Logger.error("can't add chest. Coordinates: " + Logger.getCoordinatesString(x, y ,z) + " Reason: " + e.getCause());
        }
    }

    private void removeChestSign(String[] lines, int x, int y, int z, int worldId, EntityPlayer player) {
        try {
            String listName = lines[1];
            long cooldown = Long.parseLong(lines[2]);
            String[] minmaxcount = lines[3].split("-");
            int minCount = Integer.parseInt(minmaxcount[0]);
            int maxCount = Integer.parseInt(minmaxcount[1]);
            ChestSign chestSign = new ChestSign(x, y, z, worldId, -cooldown, cooldown, listName, minCount, maxCount);
            LootChestsBase.chestSignsService.removeChestSign(chestSign);
            player.addChatComponentMessage(new ChatComponentText("Chest Sign removed!"));
        } catch (Exception e) {
            Logger.error("can't remove chest. Coordinates: " + Logger.getCoordinatesString(x, y ,z) + " Reason: " + e.getCause());
            player.addChatComponentMessage(new ChatComponentText("Error! Chest Sign not removed!"));
        }
    }

    private void addList(String[] lines, int x, int y, int z, int worldId) {
        try {
            String listName = lines[1];
            LootList list = new LootList(listName, x, y, z, worldId);
            LootChestsBase.lootListsService.addLootList(list, false);
        } catch (Exception e) {
            Logger.error("can't add list. Coordinates: " + Logger.getCoordinatesString(x, y ,z) + " Reason: " + e.getMessage());
        }
    }

    private void removeList(String[] lines, int x, int y, int z, int worldId, EntityPlayer player) {
        try {
            String listName = lines[1];
            LootList list = new LootList(listName, x, y, z, worldId);
            LootChestsBase.lootListsService.removeLootList(list);
            player.addChatComponentMessage(new ChatComponentText("Loot List removed!"));
        } catch (Exception e) {
            Logger.error("can't remove list. Coordinates: " + Logger.getCoordinatesString(x, y ,z) + " Reason: " + e.getCause());
            player.addChatComponentMessage(new ChatComponentText("Error! Loot List not removed!"));
        }
    }

//    @SubscribeEvent(priority = EventPriority.LOW)
//    public void onSignTextSave(GuiScreenEvent.ActionPerformedEvent event) {
//        if (event.gui instanceof GuiEditSign) {
//            GuiEditSign gui = (GuiEditSign) event.gui;
//            // Ищем таблички
//            checkForSigns((int) event.gui.mc.thePlayer.posX, (int) event.gui.mc.thePlayer.posY, (int) event.gui.mc.thePlayer.posZ, event.gui.mc.thePlayer.worldObj);
//        }
//    }
//
//    // Если поставили контейнер
//    @SubscribeEvent(priority = EventPriority.LOW)
//    public void onSignTextSave(BlockEvent.PlaceEvent event) {
//        if (event.placedBlock instanceof BlockContainer) {
//            // Ищем таблички
//            checkForSigns(event.x, event.y, event.z, event.world);
//        }
//    }

    // Если поставили табличку
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onSignTextSave(BlockEvent.PlaceEvent event) {
        if (event.placedBlock instanceof BlockSign) {
            // Ищем таблички
            TileEntitySign sign = (TileEntitySign) event.world.getTileEntity(event.x, event.y, event.z);
            if (Objects.nonNull(sign)) {
                LootChestsBase.tileEntitySigns.add(sign);
            } else {
                Logger.error("SIGN IS NULL");
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBlockDestroy(BlockEvent.BreakEvent event) {
        // Проверяем, не табличка ли это из списка
        if (event.block == Blocks.standing_sign || event.block == Blocks.wall_sign) {
            TileEntitySign sign = (TileEntitySign) event.world.getTileEntity(event.x, event.y ,event.z);
            String[] signText = sign.signText;
            if (signText[0].contains("[Loot]")) {
                removeChestSign(signText, event.x, event.y ,event.z, event.world.provider.dimensionId, event.getPlayer());
            } else if (signText[0].contains("[List]")) {
                removeList(signText, event.x, event.y ,event.z, event.world.provider.dimensionId, event.getPlayer());
            }
        }
    }
}
