package com.fiskmods.pika.lightsabers.block;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.block.entity.DisassemblyStationBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DisassemblyStationBlock extends BaseEntityBlock {
    public static final MapCodec<DisassemblyStationBlock> CODEC = simpleCodec(DisassemblyStationBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<StationPart> PART = EnumProperty.create("part", StationPart.class);

    public enum StationPart implements StringRepresentable {
        BOTTOM_LEFT("bottom_left"),
        BOTTOM_RIGHT("bottom_right"),
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right");

        private final String name;
        StationPart(String name) { this.name = name; }
        @Override public String getSerializedName() { return this.name; }
    }

    public DisassemblyStationBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, StationPart.BOTTOM_LEFT));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        Direction right = facing.getCounterClockWise();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (level.getBlockState(pos.relative(right)).canBeReplaced(context) &&
            level.getBlockState(pos.above()).canBeReplaced(context) &&
            level.getBlockState(pos.relative(right).above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(FACING, facing).setValue(PART, StationPart.BOTTOM_LEFT);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);
            Direction right = facing.getCounterClockWise();

            level.setBlock(pos.relative(right), state.setValue(PART, StationPart.BOTTOM_RIGHT), 3);
            level.setBlock(pos.above(), state.setValue(PART, StationPart.TOP_LEFT), 3);
            level.setBlock(pos.relative(right).above(), state.setValue(PART, StationPart.TOP_RIGHT), 3);
        }
    }

    public static BlockPos getBasePos(BlockPos pos, BlockState state) {
        StationPart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        Direction left = facing.getClockWise();

        return switch (part) {
            case BOTTOM_LEFT -> pos;
            case BOTTOM_RIGHT -> pos.relative(left);
            case TOP_LEFT -> pos.below();
            case TOP_RIGHT -> pos.relative(left).below();
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(PART) == StationPart.BOTTOM_LEFT ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == StationPart.BOTTOM_LEFT ? new DisassemblyStationBlockEntity(pos, state) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return state.getValue(PART) == StationPart.BOTTOM_LEFT ? 
            createTickerHelper(type, ALBlockEntities.DISASSEMBLY_STATION.get(), DisassemblyStationBlockEntity::tick) : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockPos basePos = getBasePos(pos, state);
            BlockEntity be = level.getBlockEntity(basePos);
            if (be instanceof DisassemblyStationBlockEntity station) {
                player.openMenu(station, basePos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            BlockPos basePos = getBasePos(pos, state);
            Direction facing = state.getValue(FACING);
            Direction right = facing.getCounterClockWise();

            BlockPos[] allPositions = {
                basePos,
                basePos.relative(right),
                basePos.above(),
                basePos.relative(right).above()
            };

            for (BlockPos p : allPositions) {
                if (!p.equals(pos)) {
                    BlockState s = level.getBlockState(p);
                    if (s.is(this)) {
                        level.setBlock(p, Blocks.AIR.defaultBlockState(), 35);
                        level.levelEvent(player, 2001, p, Block.getId(s));
                    }
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (state.getValue(PART) == StationPart.BOTTOM_LEFT) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof DisassemblyStationBlockEntity station) {
                    Containers.dropContents(level, pos, station.getInventoryAsList());
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}