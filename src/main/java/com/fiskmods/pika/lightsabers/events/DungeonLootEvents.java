package com.fiskmods.pika.lightsabers.events;

import java.util.Set;

import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class DungeonLootEvents {

	private static final Set<String> GENERAL_DUNGEON_CHESTS = Set.of("chests/simple_dungeon", "chests/desert_pyramid",
			"chests/jungle_temple", "chests/stronghold_corridor", "chests/stronghold_crossing",
			"chests/stronghold_library", "chests/ancient_city", "chests/nether_bridge");

	private static final Set<String> END_CHESTS = Set.of("chests/end_city_treasure");

	@SubscribeEvent
	public static void onChestOpen(PlayerInteractEvent.RightClickBlock event) {
		if (event.getLevel().isClientSide)
			return;
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;

		BlockPos pos = event.getPos();
		if (event.getLevel().getBlockEntity(pos) instanceof RandomizableContainerBlockEntity container) {
			ResourceKey<LootTable> lootTable = container.getLootTable();
			if (lootTable == null)
				return;

			String path = lootTable.location().getPath();
			RandomSource rand = event.getLevel().getRandom();

			if (END_CHESTS.contains(path)) {

				if (rand.nextFloat() < 0.04F) {
					insertLoot((Container) container,
							rand.nextBoolean() ? new ItemStack(ALItems.PRISTINE_PURIFICATION_ELIXIR.get())
									: new ItemStack(ALItems.PURIFICATION_ELIXIR.get()));
				}
			} else if (GENERAL_DUNGEON_CHESTS.contains(path)) {

				if (rand.nextFloat() < 0.02F) {
					insertLoot((Container) container, new ItemStack(ALItems.PURIFICATION_ELIXIR.get()));
				}
			}
		}
	}

	private static void insertLoot(Container container, ItemStack stack) {
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (container.getItem(i).isEmpty()) {
				container.setItem(i, stack);
				break;
			}
		}
	}
}