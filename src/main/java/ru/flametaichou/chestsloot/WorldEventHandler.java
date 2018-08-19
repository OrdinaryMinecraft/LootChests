package ru.flametaichou.chestsloot;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import ru.flametaichou.chestsloot.model.ChestSign;
import ru.flametaichou.chestsloot.model.LootList;

import java.util.*;

public class WorldEventHandler {

    private Random random = new Random();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void savingLists(TickEvent.WorldTickEvent event) {
        if (event.world.getWorldTime() % 1000 == 0) {
            LootChestsBase.writeLists(event.world.getWorldTime());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void checkingSigns(TickEvent.WorldTickEvent event) {
        if (event.world.getWorldTime() % 20 == 0) {
            for (Iterator<TileEntitySign> iterator = LootChestsBase.tileEntitySigns.iterator(); iterator.hasNext(); ) {
                TileEntitySign sign = iterator.next();
                if (sign.signText[0].contains("[Loot]") || sign.signText[0].contains("[List]")) {
                    EntityPlayer player = sign.func_145911_b();
                    if (Objects.isNull(player)) {
                        sign.getWorldObj().setBlockToAir(sign.xCoord, sign.yCoord, sign.zCoord);
                        Logger.error("player is null! Replacing sign. Coordinates: " + Logger.getCoordinatesString(sign.xCoord, sign.yCoord, sign.zCoord));
                        return;
                    }
                    if (!player.capabilities.isCreativeMode) {
                        sign.getWorldObj().setBlockToAir(sign.xCoord, sign.yCoord, sign.zCoord);
                        player.inventory.addItemStackToInventory(new ItemStack(Items.sign, 1));
                        player.addChatComponentMessage(new ChatComponentText("You cant create Loot Signs!"));
                    } else {
                        if (sign.signText[0].contains("[Loot]")) {
                            String listName = sign.signText[1];
                            long cooldown = Long.parseLong(sign.signText[2]);
                            String[] minmaxcount = sign.signText[3].split("-");
                            int minCount = Integer.parseInt(minmaxcount[0]);
                            int maxCount = Integer.parseInt(minmaxcount[1]);
                            ChestSign chestSign = new ChestSign(sign.xCoord, sign.yCoord, sign.zCoord, sign.getWorldObj().provider.dimensionId, -cooldown, cooldown, listName, minCount, maxCount);
                            if (LootChestsBase.chestSignsService.addChestSign(chestSign, true)) {
                                player.addChatComponentMessage(new ChatComponentText("Chest Sign created!"));
                            }
                        } else if (sign.signText[0].contains("[List]")) {
                            String listName = sign.signText[1];
                            LootList lootList = new LootList(listName, sign.xCoord, sign.yCoord, sign.zCoord, sign.getWorldObj().provider.dimensionId);
                            if (LootChestsBase.lootListsService.addLootList(lootList, true)) {
                                player.addChatComponentMessage(new ChatComponentText("Loot List created!"));
                            }
                        }
                    }
                    iterator.remove();
                } else {
                    for (String s : sign.signText) {
                        if (s != "") {
                            iterator.remove();
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void fillChests(TickEvent.WorldTickEvent event) {
        if (event.world.getWorldTime() % 1000 == 0) {
            for (Iterator<ChestSign> iterator = LootChestsBase.chestSignsService.getChestGigns().iterator(); iterator.hasNext(); ) {
                ChestSign chestSign = iterator.next();
                World world = LootChestsBase.getWorld(chestSign.getWorldId());
                long cooldownInTicks = chestSign.getCooldown() * 60 * 20;
                Logger.log("worldtime " + world.getWorldTime() + " refreshTime " + chestSign.getRefreshTime() + " cooldown " + cooldownInTicks);
                if (world.getWorldTime() - chestSign.getRefreshTime() > cooldownInTicks) {
                    // Значит его надо наполнить
                    // Достаем список предметов
                    LootList lootList = LootChestsBase.lootListsService.findByName(chestSign.getListName());
                    if (Objects.isNull(lootList)) {
                        return;
                    }
                    World lootListWorld = LootChestsBase.getWorld(lootList.getWorldId());
                    // Берем вещи из всех контейнеров рядом с табличкой-листом
                    List<ItemStack> items = new ArrayList<ItemStack>();
                    List<IInventory> lootListInventories = findContainers(lootList.getX(), lootList.getY(), lootList.getZ(), lootListWorld);
                    for (IInventory lootInventory : lootListInventories) {
                        for (int i = 0; i < lootInventory.getSizeInventory(); i++) {
                            if (Objects.nonNull(lootInventory.getStackInSlot(i))) {
                                ItemStack is = lootInventory.getStackInSlot(i).copy();
                                int lootCount = randomBetween(1, is.stackSize);
                                is.stackSize = lootCount;
                                items.add(is);
                            }
                        }
                    }

                    // Теперь ищем все контейнеры в радиусе 1 блока
                    List<IInventory> inventories = findContainers(chestSign.getX(), chestSign.getY(), chestSign.getZ(), world);
                    for (IInventory inventory : inventories) {
                        // Очистка
                        for (int i = 0; i < inventory.getSizeInventory(); i++) {
                            inventory.setInventorySlotContents(i, null);
                        }
                        int min = chestSign.getMaxCount() > chestSign.getMinCount() ? chestSign.getMinCount() : 1;
                        int count = randomBetween(min, chestSign.getMaxCount());

                        // Если слотов меньше чем нужно положить вещей
                        if (count > inventory.getSizeInventory()) {
                            count = inventory.getSizeInventory();
                        }

                        if (items.size() < 1) {
                            Logger.log("can't find items in Loot List! Name: " + lootList.getName());
                            return;
                        }

                        for (int i = 0; i < count; i++) {
                            int itemNum = randomBetween(0, items.size() - 1);
                            inventory.setInventorySlotContents(i, items.get(itemNum));
                        }
                        chestSign.setRefreshTime(world.getWorldTime());
                    }
                }
            }
        }
    }

    public int randomBetween(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private List<IInventory> findContainers(int eventx, int eventy, int eventz, World world) {
        List<IInventory> inventories = new ArrayList<IInventory>();
        int radius = 1;
        for (int x = eventx - radius; x <= eventx + radius; x++) {
            for (int y = eventy - radius; y <= eventy + radius; y++) {
                for (int z = eventz - radius; z <= eventz + radius; z++) {
                    if (world.getTileEntity(x, y ,z) instanceof IInventory) {
                        IInventory inventory = (IInventory) world.getTileEntity(x, y, z);
                        inventories.add(inventory);
                    }
                }
            }
        }
        return inventories;
    }

}