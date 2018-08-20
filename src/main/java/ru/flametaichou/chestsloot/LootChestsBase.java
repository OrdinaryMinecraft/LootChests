package ru.flametaichou.chestsloot;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import ru.flametaichou.chestsloot.model.LootList;
import ru.flametaichou.chestsloot.model.LootListsXml;
import ru.flametaichou.chestsloot.service.ChestSignsService;
import ru.flametaichou.chestsloot.service.LootListsService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Mod (modid = "chestsloot", name = "Chests Loot", version = "0.1", acceptableRemoteVersions = "*")

public class LootChestsBase {

	public static ChestSignsService chestSignsService;
	public static LootListsService lootListsService;
	private static long lastSaveTime;
	public static List<TileEntitySign> tileEntitySigns;

	@EventHandler
	public void initialize(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
		FMLCommonHandler.instance().bus().register(new WorldEventHandler());
		tileEntitySigns = new ArrayList<TileEntitySign>();
		chestSignsService = new ChestSignsService();
		lootListsService = new LootListsService();
		lastSaveTime = 0;
		readLists();
	}

	public static WorldServer getWorld(int dimId) {
		return DimensionManager.getWorld(dimId);
	}

	public static void readLists() {
		try {
			JAXBContext jc = JAXBContext.newInstance(LootListsXml.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File file = new File("lists.xml");
			if (file.exists()) {
				lootListsService.setLootListsXML((LootListsXml) unmarshaller.unmarshal(new FileReader("config/lists.xml")));
				for (LootList list : lootListsService.getLootListsXML().getLists()) {
					Logger.log("List loaded: " + list.getName());
				}
			} else {
				Logger.error("lists.xml file not found!");
			}
		} catch (JAXBException e) {
			Logger.error("can't load lists.xml! Reason: " + e.getCause());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			Logger.error("can't load lists.xml! Reason: " + e.getCause());
			e.printStackTrace();
		} catch (IOException e) {
			Logger.error("can't load lists.xml! Reason: " + e.getCause());
			e.printStackTrace();
		}
	}

	// TODO: сохранение списков на создании, а не по таймеру!
	public static void writeLists(long time) {
		if (lastSaveTime != time) {
			try {
				JAXBContext jc = JAXBContext.newInstance(LootListsXml.class);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(lootListsService.getLootListsXML(), new FileWriter("config/lists.xml"));
			} catch (JAXBException e) {
				Logger.error("can't write to lists.xml! Reason: " + e.getCause());
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				Logger.error("can't write to lists.xml! Reason: " + e.getCause());
				e.printStackTrace();
			} catch (IOException e) {
				Logger.error("can't write to lists.xml! Reason: " + e.getCause());
				e.printStackTrace();
			}
			lastSaveTime = time;
		}
	}
	
}
