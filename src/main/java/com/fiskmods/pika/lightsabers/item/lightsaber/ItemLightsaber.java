package com.fiskmods.pika.lightsabers.item.lightsaber;

import java.util.List;

import com.fiskmods.pika.lightsabers.ALConfig;
import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALSounds;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ItemLightsaber extends Item {

	public ItemLightsaber(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		stack.set(ALDataComponents.LIGHTSABER_DATA.get(), LightsaberData.createDefault());
		return stack;
	}

	@Override
	public boolean canAttackBlock(net.minecraft.world.level.block.state.BlockState state,
			net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos,
			net.minecraft.world.entity.player.Player player) {
		return !player.isCreative();
	}

	public static boolean isActive(ItemStack stack) {
		return stack != null && stack.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
	}

	public static boolean isSingleBladeMode(ItemStack stack) {

		return stack != null && stack.getOrDefault(ALDataComponents.BLADE_MODE.get(), false);
	}

	public static void setBladeActive(ItemStack stack, boolean active) {
		stack.set(ALDataComponents.IS_ACTIVE.get(), active);
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(stack, active));
	}

	public static ItemAttributeModifiers createAttributes(ItemStack stack, boolean active) {
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

		ResourceLocation damageId = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
				"lightsaber_damage");
		ResourceLocation speedId = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "lightsaber_speed");

		if (active) {
			double damage = ALConfig.ACTIVE_DAMAGE.get();
			double speed = ALConfig.ACTIVE_SPEED.get();

			LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
			boolean singleMode = isSingleBladeMode(stack);

			if (data != null && data.isDoubleBladed()) {
				if (singleMode) {

					damage += 2.0;
					speed -= 0.2;
				} else {

					damage += 6.0;
					speed -= 0.6;
				}
			}

			builder.add(Attributes.ATTACK_DAMAGE,
					new AttributeModifier(damageId, damage, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
			builder.add(Attributes.ATTACK_SPEED,
					new AttributeModifier(speedId, speed, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
		} else {
			builder.add(Attributes.ATTACK_DAMAGE,
					new AttributeModifier(damageId, 1.0, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
			builder.add(Attributes.ATTACK_SPEED,
					new AttributeModifier(speedId, -3.0, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
		}

		return builder.build();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		boolean active = isActive(stack);

		if (player.isShiftKeyDown()) {
			boolean newState = !active;
			setBladeActive(stack, newState);
			level.playSound(null, player.getX(), player.getY(), player.getZ(),
					newState ? ALSounds.LIGHTSABER_ON.get() : ALSounds.LIGHTSABER_OFF.get(), SoundSource.PLAYERS, 1.0F,
					1.0F);
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		} else if (active) {

			PlayerForceData data = net.minecraft.world.entity.player.Player.class.isAssignableFrom(player.getClass())
					? com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData.get(player)
					: null;

			if (data != null
					&& data.hasSkill(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_block"))) {
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(stack);
			}
		}

		return super.use(level, player, hand);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return isActive(stack) ? UseAnim.BLOCK : UseAnim.NONE;
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity, net.minecraft.world.InteractionHand hand) {
		if (isActive(stack)) {
			entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ALSounds.LIGHTSABER_SWING.get(),
					SoundSource.PLAYERS, 0.85F, 0.9F + entity.getRandom().nextFloat() * 0.2F);
		}
		return super.onEntitySwing(stack, entity, hand);
	}

	@Override
	public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (isActive(stack)) {
			target.level().playSound(null, target.getX(), target.getY(), target.getZ(), ALSounds.LIGHTSABER_HIT.get(),
					SoundSource.PLAYERS, 0.95F, 0.9F + target.getRandom().nextFloat() * 0.2F);

			if (target.level() instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.CRIT, target.getX(), target.getEyeY() - 0.2, target.getZ(), 10,
						0.2, 0.2, 0.2, 0.05);
				serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getEyeY() - 0.2,
						target.getZ(), 8, 0.15, 0.15, 0.15, 0.1);
			}
		}
		super.postHurtEnemy(stack, target, attacker);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
		if (itemAbility == ItemAbilities.SWORD_SWEEP) {
			return isActive(stack);
		}
		return super.canPerformAction(stack, itemAbility);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
			TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

		LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
		if (data == null)
			return;

		tooltipComponents.add(Component.empty());
		tooltipComponents.add(Component.translatable("tooltip.lightsabersfork.parts").withStyle(ChatFormatting.GRAY));

		boolean isDouble = data.isDoubleBladed();
		boolean isSingleMode = isSingleBladeMode(stack);

		if (isDouble && isSingleMode) {
			tooltipComponents.add(Component.translatable("tooltip.lightsabersfork.single_blade_active")
					.withStyle(ChatFormatting.AQUA));
		}

		if (data.colorCrystal() != null && data.colorCrystal() != Items.AIR) {
			MutableComponent comp = Component.literal(" - ").append(new ItemStack(data.colorCrystal()).getHoverName());
			if (isDouble) {
				comp.append(Component.translatable("tooltip.lightsabersfork.primary"));
			}
			tooltipComponents.add(comp.withStyle(ChatFormatting.DARK_GRAY));
		}

		if (data.focusCrystals() != null) {
			for (Item focusItem : data.focusCrystals()) {
				if (focusItem != null && focusItem != Items.AIR) {
					MutableComponent comp = Component.literal(" - ").append(new ItemStack(focusItem).getHoverName());
					if (isDouble) {
						comp.append(Component.translatable("tooltip.lightsabersfork.primary"));
					}
					tooltipComponents.add(comp.withStyle(ChatFormatting.DARK_GRAY));
				}
			}
		}

		if (isDouble && data.secColorCrystal() != null && data.secColorCrystal() != Items.AIR) {
			tooltipComponents.add(Component.literal(" - ").append(new ItemStack(data.secColorCrystal()).getHoverName())
					.append(Component.translatable("tooltip.lightsabersfork.secondary")).withStyle(ChatFormatting.DARK_GRAY));
		}

		if (isDouble && data.secFocusCrystals() != null) {
			for (Item focusItem : data.secFocusCrystals()) {
				if (focusItem != null && focusItem != Items.AIR) {
					tooltipComponents.add(Component.literal(" - ").append(new ItemStack(focusItem).getHoverName())
							.append(Component.translatable("tooltip.lightsabersfork.secondary")).withStyle(ChatFormatting.DARK_GRAY));
				}
			}
		}

		Item emitter = data.emitter();
		Item switchSec = data.switchSection();
		Item grip = data.grip();
		Item pommel = data.pommel();
		Item secSwitch = data.secSwitchSection();

		if (!isDouble) {
			String setEmitter = getPartSetName(emitter);
			String setSwitch = getPartSetName(switchSec);
			String setGrip = getPartSetName(grip);
			String setPommel = getPartSetName(pommel);

			boolean isFullSet = emitter != null && emitter != Items.AIR && setEmitter != null
					&& setEmitter.equalsIgnoreCase(setSwitch) && setEmitter.equalsIgnoreCase(setGrip)
					&& setEmitter.equalsIgnoreCase(setPommel);

			if (isFullSet) {
				tooltipComponents.add(Component.translatable("tooltip.lightsabersfork.set_bonus", formatSetName(setEmitter))
						.withStyle(ChatFormatting.DARK_GRAY));
			} else {
				addPartTooltip(tooltipComponents, emitter, null);
				addPartTooltip(tooltipComponents, switchSec, null);
				addPartTooltip(tooltipComponents, grip, null);
				addPartTooltip(tooltipComponents, pommel, null);
			}
		} else {
			addPartTooltip(tooltipComponents, emitter, Component.translatable("tooltip.lightsabersfork.part.primary_emitter"));
			addPartTooltip(tooltipComponents, switchSec, Component.translatable("tooltip.lightsabersfork.part.primary_switch"));
			addPartTooltip(tooltipComponents, grip, Component.translatable("tooltip.lightsabersfork.part.primary_grip"));
			if (data.secGrip() != null && data.secGrip() != Items.AIR) {
				addPartTooltip(tooltipComponents, data.secGrip(), Component.translatable("tooltip.lightsabersfork.part.secondary_grip"));
			}
			if (secSwitch != null && secSwitch != Items.AIR) {
				addPartTooltip(tooltipComponents, secSwitch, Component.translatable("tooltip.lightsabersfork.part.secondary_switch"));
			}
			addPartTooltip(tooltipComponents, pommel, Component.translatable("tooltip.lightsabersfork.secondary_emitter"));
		}
	}

	private static void addPartTooltip(List<Component> tooltipComponents, Item part, Component suffix) {
		if (part != null && part != Items.AIR) {
			MutableComponent comp = Component.literal(" - ").append(new ItemStack(part).getHoverName());
			if (suffix != null) {
				comp.append(suffix);
			}
			tooltipComponents.add(comp.withStyle(ChatFormatting.DARK_GRAY));
		}
	}

	private static String getPartSetName(Item item) {
		if (item instanceof ItemLightsaberPart part) {
			try {
				String name = part.getHiltName();
				if (name != null && !name.isEmpty())
					return name;
			} catch (Exception ignored) {
			}
		}
		if (item != null && item != Items.AIR) {
			String path = BuiltInRegistries.ITEM.getKey(item).getPath().toLowerCase();
			return path.replace("emitter_", "").replace("_emitter", "").replace("switch_section_", "")
					.replace("_switch_section", "").replace("switch_", "").replace("_switch", "").replace("body_", "")
					.replace("_body", "").replace("grip_", "").replace("_grip", "").replace("pommel_", "")
					.replace("_pommel", "");
		}
		return null;
	}

	private static String formatSetName(String rawName) {
		if (rawName == null || rawName.isEmpty())
			return "";
		String[] parts = rawName.split("_");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.length; i++) {
			if (!parts[i].isEmpty()) {
				sb.append(Character.toUpperCase(parts[i].charAt(0))).append(parts[i].substring(1).toLowerCase());
				if (i < parts.length - 1)
					sb.append(" ");
			}
		}
		return sb.toString();
	}
}