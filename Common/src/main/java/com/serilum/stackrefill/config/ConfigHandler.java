package com.serilum.stackrefill.config;

import com.natamus.collective.config.DuskConfig;
import com.serilum.stackrefill.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<>();

	@Entry public static boolean refillBlocksAndItems = true;
	@Entry public static boolean refillToolsAndWeapons = true;
	@Entry public static boolean refillAfterDroppingLastItem = true;
	@Entry public static boolean refillBuckets = true;
	@Entry public static boolean refillFromOtherHotbarSlots = false;
	@Entry public static String itemBlacklist = "";
	@Entry public static String itemWhitelist = "";

	public static void initConfig() {
		configMetaData.put("refillBlocksAndItems", Arrays.asList(
			"Whether the hand is refilled when the last item of a stack is placed, used, eaten or thrown. For example blocks, food, bone meal, eggs and potions."
		));
		configMetaData.put("refillToolsAndWeapons", Arrays.asList(
			"Whether an item with durability is replaced by the same item from the inventory when it breaks or is thrown. For example tools, swords, fishing rods and tridents."
		));
		configMetaData.put("refillAfterDroppingLastItem", Arrays.asList(
			"Whether the main hand is refilled when its last item is dropped with the drop key."
		));
		configMetaData.put("refillBuckets", Arrays.asList(
			"Whether buckets are refilled. When disabled, a bucket that is emptied or filled stays in the hand instead of being swapped for another bucket from the inventory."
		));
		configMetaData.put("refillFromOtherHotbarSlots", Arrays.asList(
			"Whether the other hotbar slots are searched as well. When disabled, only the main inventory is used to refill the hand."
		));
		configMetaData.put("itemBlacklist", Arrays.asList(
			"A comma separated list of item ids that are never refilled. Use * as a wildcard. An id without a namespace matches items of every mod. For example 'minecraft:totem_of_undying,*shulker_box'."
		));
		configMetaData.put("itemWhitelist", Arrays.asList(
			"When not empty, only the items in this comma separated list are refilled. Same format as itemBlacklist, for example 'minecraft:*_planks,cobblestone'."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}
