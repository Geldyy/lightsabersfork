package com.fiskmods.pika.lightsabers.entity;

import java.util.List;
import java.util.Optional;

import com.fiskmods.pika.lightsabers.ALEntities;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TelekineticBlockEntity extends Entity {

	private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE = SynchedEntityData
			.defineId(TelekineticBlockEntity.class, EntityDataSerializers.BLOCK_STATE);
	private static final EntityDataAccessor<Float> DATA_DISTANCE = SynchedEntityData
			.defineId(TelekineticBlockEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> DATA_OWNER_ID = SynchedEntityData
			.defineId(TelekineticBlockEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_THROWN = SynchedEntityData
			.defineId(TelekineticBlockEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_TIER = SynchedEntityData
			.defineId(TelekineticBlockEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_SITH = SynchedEntityData
			.defineId(TelekineticBlockEntity.class, EntityDataSerializers.BOOLEAN);

	public TelekineticBlockEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = false;
	}

	public TelekineticBlockEntity(Level level, double x, double y, double z, BlockState state, Player owner, int tier,
			float initialDist, boolean isSith) {
		this(ALEntities.TELEKINETIC_BLOCK.get(), level);
		this.setPos(x, y, z);
		this.setBlockState(state);
		this.setOwnerId(owner.getId());
		this.setTier(tier);
		this.setTargetDistance(initialDist);
		this.setSith(isSith);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DATA_BLOCK_STATE, Blocks.DIRT.defaultBlockState());
		builder.define(DATA_DISTANCE, 4.0F);
		builder.define(DATA_OWNER_ID, -1);
		builder.define(DATA_THROWN, false);
		builder.define(DATA_TIER, 1);
		builder.define(DATA_SITH, false);
	}

	@Override
	public void tick() {
		super.tick();

		if (!isThrown()) {
			Player owner = getOwner();

			if (owner == null || !owner.isAlive() || this.distanceToSqr(owner) > 1024.0D) {
				dropBlock();
				return;
			}

			if (owner.isCrouching()) {
				dropBlock();
				return;
			}

			if (!this.level().isClientSide) {
				PlayerForceData data = PlayerForceData.get(owner);
				if (data == null) {
					dropBlock();
					return;
				}

				if (this.tickCount % 20 == 0) {
					net.minecraft.resources.ResourceLocation tkId = net.minecraft.resources.ResourceLocation
							.fromNamespaceAndPath(com.fiskmods.pika.lightsabers.AdvancedLightsabers.MODID, "force_telekinesis");
					data.addAmfP(tkId, 0.5f);
					data.addAmfI(tkId, 0.5f);
				}

				if (this.tickCount % 10 == 0) {
					int tier = getTier();
					float drain10 = (tier >= 3) ? 0.0F : ((tier >= 2) ? 0.375F : 0.25F);
					if (drain10 > 0.0F && !data.consumeForceEnergy(drain10)) {
						dropBlock();
						return;
					}
				}

				Vec3 eye = owner.getEyePosition();
				Vec3 look = owner.getLookAngle();
				float dist = getTargetDistance();
				Vec3 targetPos = eye.add(look.scale(dist)).subtract(0, 0.4D, 0);

				Vec3 diff = targetPos.subtract(this.position());
				double distLen = diff.length();
				double speed = Math.min(distLen, 1.8D);
				Vec3 moveVec = (distLen > 0.05D) ? diff.normalize().scale(speed * 0.65D) : Vec3.ZERO;

				this.setDeltaMovement(moveVec);
				this.move(MoverType.SELF, this.getDeltaMovement());
			}

			if (this.level().isClientSide) {
				boolean sith = isSith();
				double px = getX() + (this.random.nextDouble() - 0.5D) * 1.1D;
				double py = getY() + this.random.nextDouble() * 1.1D;
				double pz = getZ() + (this.random.nextDouble() - 0.5D) * 1.1D;

				if (sith) {
					this.level().addParticle(ParticleTypes.SMOKE, px, py, pz, 0, 0.02, 0);
				} else {
					this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0.02, 0);
				}

				if (getBlockState().is(Blocks.TNT) && getTier() >= 3) {
					this.level().addParticle(ParticleTypes.FLAME, px, py + 0.5D, pz, 0, 0.05, 0);
				}
			}
		}

		else {
			Vec3 currentPos = this.position();
			Vec3 movement = this.getDeltaMovement();
			Vec3 nextPos = currentPos.add(movement);

			BlockHitResult blockHit = this.level().clip(
					new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
			if (blockHit.getType() != HitResult.Type.MISS) {
				nextPos = blockHit.getLocation();
			}

			AABB sweepBox = this.getBoundingBox().expandTowards(movement).inflate(0.8D);
			List<Entity> hitEntities = this.level().getEntities(this, sweepBox,
					e -> e.isAlive() && e.getId() != this.getOwnerId() && !(e instanceof TelekineticBlockEntity));

			Entity targetHit = null;
			double closestDist = Double.MAX_VALUE;
			for (Entity target : hitEntities) {
				AABB targetBox = target.getBoundingBox().inflate(0.3D);
				Optional<Vec3> optClip = targetBox.clip(currentPos, nextPos);
				if (optClip.isPresent()) {
					double d = currentPos.distanceToSqr(optClip.get());
					if (d < closestDist) {
						closestDist = d;
						targetHit = target;
					}
				}
			}

			if (targetHit != null) {
				onHitEntity(targetHit);
				return;
			}

			if (blockHit.getType() != HitResult.Type.MISS) {
				onHitBlock(blockHit);
				return;
			}

			this.setPos(nextPos);

			this.setDeltaMovement(movement.scale(0.985D).add(0, -0.038D, 0));
		}
	}

	public void throwBlock(Vec3 direction, double power) {
		this.setThrown(true);
		this.setDeltaMovement(direction.scale(power));
		this.hasImpulse = true;

		if (getBlockState().is(Blocks.TNT) && getTier() >= 3) {
			this.level().playSound(null, blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	public void adjustDistance(float delta) {
		float cur = getTargetDistance();
		float maxReach = (getTier() >= 3) ? 18.0F : ((getTier() >= 2) ? 12.0F : 6.0F);
		float next = Mth.clamp(cur + (delta * 0.85F), 2.0F, maxReach);
		setTargetDistance(next);
	}

	private void onHitEntity(Entity target) {
		if (!this.level().isClientSide) {

			if (getBlockState().is(Blocks.TNT)) {
				float blastPower = (getTier() >= 3) ? 4.5F : 3.0F;
				this.level().explode(getOwner(), getX(), getY(), getZ(), blastPower, Level.ExplosionInteraction.BLOCK);
				this.discard();
				return;
			}

			float baseDamage = 6.0F + (getTier() * 4.0F);
			BlockState state = getBlockState();
			if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.ANCIENT_DEBRIS)) {
				baseDamage += 10.0F;
			}

			target.hurt(this.level().damageSources().fallingBlock(this), baseDamage);
			target.setDeltaMovement(this.getDeltaMovement().scale(0.4D).add(0, 0.25D, 0));
			target.hurtMarked = true;

			breakAndDrop();
		}
	}

	private void onHitBlock(BlockHitResult hit) {
		if (!this.level().isClientSide) {
			if (getBlockState().is(Blocks.TNT)) {
				float blastPower = (getTier() >= 3) ? 4.5F : 3.0F;
				this.level().explode(getOwner(), getX(), getY(), getZ(), blastPower, Level.ExplosionInteraction.BLOCK);
				this.discard();
				return;
			}

			BlockPos placePos = hit.getBlockPos().relative(hit.getDirection());
			if (this.level().getBlockState(placePos).canBeReplaced()) {
				this.level().setBlock(placePos, getBlockState(), 3);
				this.level().playSound(null, placePos, getBlockState().getSoundType().getPlaceSound(),
						SoundSource.BLOCKS, 1.0F, 1.0F);
				this.discard();
			} else {
				breakAndDrop();
			}
		}
	}

	public void dropBlock() {
		if (this.level().isClientSide)
			return;
		BlockPos pos = blockPosition();
		if (this.level().getBlockState(pos).canBeReplaced()) {
			this.level().setBlock(pos, getBlockState(), 3);
			this.level().playSound(null, pos, getBlockState().getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F,
					1.0F);
		} else if (this.level().getBlockState(pos.below()).canBeReplaced()) {
			this.level().setBlock(pos.below(), getBlockState(), 3);
			this.level().playSound(null, pos.below(), getBlockState().getSoundType().getPlaceSound(),
					SoundSource.BLOCKS, 1.0F, 1.0F);
		} else {
			breakAndDrop();
		}
		this.discard();
	}

	private void breakAndDrop() {
		if (this.level() instanceof ServerLevel sl) {
			sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, getBlockState()), getX(), getY() + 0.5D,
					getZ(), 45, 0.35D, 0.35D, 0.35D, 0.15D);
			Block.dropResources(getBlockState(), sl, blockPosition());
		}
		this.level().playSound(null, blockPosition(), getBlockState().getSoundType().getBreakSound(),
				SoundSource.BLOCKS, 1.0F, 1.0F);
		this.discard();
	}

	public BlockState getBlockState() {
		return this.entityData.get(DATA_BLOCK_STATE);
	}

	public void setBlockState(BlockState state) {
		this.entityData.set(DATA_BLOCK_STATE, state);
	}

	public float getTargetDistance() {
		return this.entityData.get(DATA_DISTANCE);
	}

	public void setTargetDistance(float dist) {
		this.entityData.set(DATA_DISTANCE, dist);
	}

	public int getOwnerId() {
		return this.entityData.get(DATA_OWNER_ID);
	}

	public void setOwnerId(int id) {
		this.entityData.set(DATA_OWNER_ID, id);
	}

	public Player getOwner() {
		Entity e = this.level().getEntity(getOwnerId());
		return e instanceof Player p ? p : null;
	}

	public boolean isThrown() {
		return this.entityData.get(DATA_THROWN);
	}

	public void setThrown(boolean thrown) {
		this.entityData.set(DATA_THROWN, thrown);
	}

	public int getTier() {
		return this.entityData.get(DATA_TIER);
	}

	public void setTier(int tier) {
		this.entityData.set(DATA_TIER, tier);
	}

	public boolean isSith() {
		return this.entityData.get(DATA_SITH);
	}

	public void setSith(boolean sith) {
		this.entityData.set(DATA_SITH, sith);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("Block")) {
			this.setBlockState(
					BuiltInRegistries.BLOCK.get(ResourceLocation.parse(tag.getString("Block"))).defaultBlockState());
		}
		this.setTargetDistance(tag.getFloat("Distance"));
		this.setThrown(tag.getBoolean("Thrown"));
		this.setTier(tag.getInt("Tier"));
		this.setSith(tag.getBoolean("Sith"));
	}

	@Override
	public Component getName() {
		Component custom = this.getCustomName();
		if (custom != null) {
			return custom;
		}
		BlockState state = getBlockState();
		if (state != null && !state.isAir()) {
			return Component.translatable("entity.lightsabersfork.telekinetic_block_named", state.getBlock().getName());
		}
		return super.getName();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putString("Block", BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).toString());
		tag.putFloat("Distance", getTargetDistance());
		tag.putBoolean("Thrown", isThrown());
		tag.putInt("Tier", getTier());
		tag.putBoolean("Sith", isSith());
	}
}