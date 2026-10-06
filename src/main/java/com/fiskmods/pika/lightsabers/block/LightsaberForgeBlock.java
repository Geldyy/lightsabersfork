package com.fiskmods.pika.lightsabers.block;

import org.jetbrains.annotations.Nullable;

import com.fiskmods.pika.lightsabers.block.entity.LightsaberForgeBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightsaberForgeBlock extends BaseEntityBlock {
    public static final MapCodec<LightsaberForgeBlock> CODEC = simpleCodec(LightsaberForgeBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ForgePart> PART = EnumProperty.create("part", ForgePart.class);

    // Fisks originale Tischhöhe: 13 Pixel (0.8125 Blöcke)
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);

    public enum ForgePart implements StringRepresentable {
        LEFT("left"),
        RIGHT("right");

        private final String name;
        ForgePart(String name) { this.name = name; }
        @Override public String getSerializedName() { return this.name; }
    }

    public LightsaberForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, ForgePart.LEFT));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    public static Direction getRightDirection(Direction facing) {
        return facing.getCounterClockWise();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        Direction right = getRightDirection(facing);
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (level.getBlockState(pos.relative(right)).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(FACING, facing).setValue(PART, ForgePart.LEFT);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);
            Direction right = getRightDirection(facing);
            level.setBlock(pos.relative(right), state.setValue(PART, ForgePart.RIGHT), 3);
        }
    }

    public static BlockPos getBasePos(BlockPos pos, BlockState state) {
        ForgePart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        Direction left = getRightDirection(facing).getOpposite();
        return part == ForgePart.LEFT ? pos : pos.relative(left);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(PART) == ForgePart.LEFT ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == ForgePart.LEFT ? new LightsaberForgeBlockEntity(pos, state) : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockPos basePos = getBasePos(pos, state);
            BlockEntity be = level.getBlockEntity(basePos);
            if (be instanceof LightsaberForgeBlockEntity forge) {
                player.openMenu(forge, basePos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            BlockPos basePos = getBasePos(pos, state);
            Direction facing = state.getValue(FACING);
            Direction right = getRightDirection(facing);

            BlockPos otherPos = state.getValue(PART) == ForgePart.LEFT ? basePos.relative(right) : basePos;
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this)) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, otherPos, Block.getId(otherState));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (state.getValue(PART) == ForgePart.LEFT) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof LightsaberForgeBlockEntity forge) {
                    Containers.dropContents(level, pos, forge.getInventoryAsList());
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}