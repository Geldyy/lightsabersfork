package com.fiskmods.pika.lightsabers.block;

import org.jetbrains.annotations.Nullable;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.block.entity.HolocronBlockEntity;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;

public class HolocronBlock extends BaseEntityBlock {
    public static final MapCodec<HolocronBlock> CODEC = simpleCodec(props -> new HolocronBlock(props, "jedi"));

    private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
    private final String treeType;

    public HolocronBlock(Properties properties, String treeType) {
        super(properties);
        this.treeType = treeType;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public String getTreeType() {
        return this.treeType;
    }

    public boolean isSith() {
        return "sith".equalsIgnoreCase(this.treeType);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public net.minecraft.world.level.block.RenderShape getRenderShape(net.minecraft.world.level.block.state.BlockState state) {
        // Zwingt Minecraft dazu, keinen massiven Block, sondern nur die BlockEntity zu rendern
        return net.minecraft.world.level.block.RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HolocronBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Verbindet den Block mit unserer neuen Tick-Methode
        return createTickerHelper(type, ALBlockEntities.HOLOCRON.get(), HolocronBlockEntity::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            boolean isCurrentlyMeditating = MeditationStateManager.isMeditating();
            
            if (!isCurrentlyMeditating) {
                // Wir rufen die Methode nun mit den neuen Parametern (pos und isSith()) auf
                MeditationStateManager.startMeditation(player, pos, this.isSith());
            } else {
                MeditationStateManager.stopMeditation();
            }
            
            // Der Payload bekommt nun ebenfalls pos und isSith() übergeben
            PacketDistributor.sendToServer(new MeditationTogglePayload(!isCurrentlyMeditating, pos, this.isSith()));
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be.triggerEvent(id, param);
    }
}