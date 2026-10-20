package com.fiskmods.pika.lightsabers.block.gathering;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.block.entity.ResonantKyberBlockEntity;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ResonantKyberBlock extends DormantKyberClusterBlock implements EntityBlock {

	public ResonantKyberBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ResonantKyberBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null
				: (lvl, p, s, be) -> {
					if (be instanceof ResonantKyberBlockEntity rBe) {
						ResonantKyberBlockEntity.tick(lvl, p, s, rBe);
					}
				};
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && player instanceof ServerPlayer sp) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof ResonantKyberBlockEntity rBe && rBe.isAttunedTo(sp)) {
				// Attuned reward!
				ItemStack drop = new ItemStack(ALItems.LIGHT_BLUE_CRYSTAL.get());
				popResource(level, pos, drop);

				level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.5F, 1.2F);
				level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.5F);

				PlayerForceData data = PlayerForceData.get(sp);
				if (data != null) {
					data.addForcePoints(1);
					data.sync(sp);
				}
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}
}
