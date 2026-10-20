package com.fiskmods.pika.lightsabers.item;

import java.util.List;

import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ItemPurificationElixir extends Item {

	private final boolean isPristine;

	public ItemPurificationElixir(boolean isPristine, Properties properties) {
		super(properties.stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
		this.isPristine = isPristine;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 32;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!level.isClientSide && entity instanceof ServerPlayer player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && data.isForceSensitive()) {
				int refundedFP = 0;

				if (this.isPristine) {
					refundedFP = data.resetAllPowersPristine();
					player.sendSystemMessage(
							Component.translatable("message.lightsabersfork.elixir.pristine_success", refundedFP));
				} else {
					refundedFP = data.resetFactionSkills();
					player.sendSystemMessage(
							Component.translatable("message.lightsabersfork.elixir.faction_success", refundedFP));
				}

				data.sync(player);

				ServerLevel serverLevel = (ServerLevel) level;
				serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE,
						SoundSource.PLAYERS, 1.0F, 1.2F);
				serverLevel.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0D, player.getZ(), 40,
						0.4D, 0.6D, 0.4D, 0.1D);
				serverLevel.sendParticles(ParticleTypes.ENCHANT, player.getX(), player.getY() + 1.2D, player.getZ(), 50,
						0.5D, 0.5D, 0.5D, 0.2D);
			} else {
				player.sendSystemMessage(Component.translatable("message.lightsabersfork.elixir.no_connection"));
			}

			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
		}

		return super.finishUsingItem(stack, level, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (this.isPristine) {
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.pristine.title")
					.withStyle(ChatFormatting.AQUA));
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.pristine.desc"));
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.pristine.refund")
					.withStyle(ChatFormatting.GOLD));
		} else {
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.faction.title")
					.withStyle(ChatFormatting.GREEN));
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.faction.desc"));
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.faction.refund")
					.withStyle(ChatFormatting.GOLD));
			tooltip.add(Component.translatable("tooltip.lightsabersfork.elixir.faction.neutral_safe"));
		}
		super.appendHoverText(stack, context, tooltip, flag);
	}
}