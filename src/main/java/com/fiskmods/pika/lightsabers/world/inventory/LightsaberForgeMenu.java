package com.fiskmods.pika.lightsabers.world.inventory;

import com.fiskmods.pika.lightsabers.ALBlocks;
import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.ALMenuTypes;
import com.fiskmods.pika.lightsabers.block.entity.LightsaberForgeBlockEntity;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;
import com.fiskmods.pika.lightsabers.item.crystals.ItemFocusingCrystal;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class LightsaberForgeMenu extends AbstractContainerMenu {

	private final LightsaberForgeBlockEntity blockEntity;

	public LightsaberForgeMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
		this(containerId, playerInventory,
				(LightsaberForgeBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
	}

	public LightsaberForgeMenu(int containerId, Inventory playerInventory, LightsaberForgeBlockEntity blockEntity) {
		super(ALMenuTypes.LIGHTSABER_FORGE.get(), containerId);
		this.blockEntity = blockEntity;

		for (int i = 0; i < 4; i++) {
			final int slotIndex = i;
			this.addSlot(new SlotItemHandler(blockEntity.getInventory(), slotIndex, 20, 17 + i * 18) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					if (stack.getItem() instanceof ItemLightsaberPart part) {
						return switch (slotIndex) {
						case 0 -> part.getPartType() == PartType.EMITTER;
						case 1 -> part.getPartType() == PartType.SWITCH_SECTION;
						case 2 -> part.getPartType() == PartType.BODY;
						case 3 -> part.getPartType() == PartType.POMMEL || part.getPartType() == PartType.EMITTER;
						default -> false;
						};
					}
					return false;
				}
			});
		}

		this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 4, 43, 71) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(ALItems.CIRCUITRY.get());
			}
		});

		this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 5, 66, 71) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ItemColorCrystal;
			}
		});

		for (int i = 0; i < 2; i++) {
			this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 6 + i, 89 + i * 18, 71) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() instanceof ItemFocusingCrystal;
				}
			});
		}

		this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 8, 136, 87) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public void onTake(Player player, ItemStack stack) {
				for (int i = 0; i < 8; i++) {
					blockEntity.getInventory().extractItem(i, 1, false);
				}

				if (player instanceof ServerPlayer serverPlayer) {
					PlayerForceData data = PlayerForceData.get(serverPlayer);
					if (data != null) {
						if (data != null) {
							data.rewardFirstLightsaber(serverPlayer);

							LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
							if (lsData != null && !lsData.focusCrystals().isEmpty()) {
								data.rewardFocusedLightsaber(serverPlayer);
							}
						}
						data.rewardFirstLightsaber(serverPlayer);
					}
				}

				super.onTake(player, stack);
			}
		});

		addPlayerInventory(playerInventory);
		addPlayerHotbar(playerInventory);
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player,
				ALBlocks.LIGHTSABER_FORGE_LIGHT.get());
	}

	private void addPlayerInventory(Inventory playerInventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 114 + row * 18));
			}
		}
	}

	private void addPlayerHotbar(Inventory playerInventory) {
		for (int col = 0; col < 9; ++col) {
			this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 172));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack stackInSlot = slot.getItem();
			itemstack = stackInSlot.copy();

			if (index == 8) {
				if (!this.moveItemStackTo(stackInSlot, 9, 45, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(stackInSlot, itemstack);
			} else if (index >= 9) {
				if (!this.moveItemStackTo(stackInSlot, 0, 8, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(stackInSlot, 9, 45, false)) {
				return ItemStack.EMPTY;
			}

			if (stackInSlot.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stackInSlot.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stackInSlot);
		}

		return itemstack;
	}
}