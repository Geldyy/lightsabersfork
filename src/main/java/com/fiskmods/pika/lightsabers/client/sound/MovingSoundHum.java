package com.fiskmods.pika.lightsabers.client.sound;

import com.fiskmods.pika.lightsabers.ALSounds;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MovingSoundHum extends AbstractTickableSoundInstance {

	private final LivingEntity entity;
	private final Runnable onStopCallback;

	public MovingSoundHum(LivingEntity entity, Runnable onStopCallback) {
		super(ALSounds.LIGHTSABER_HUM_MEDIUM.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.entity = entity;
		this.onStopCallback = onStopCallback;
		this.looping = true;
		this.delay = 0;
		this.volume = 0.35F;
		this.pitch = 1.0F;
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
	}

	@Override
	public void tick() {
		if (this.entity.isRemoved() || !this.entity.isAlive()) {
			stopHum();
			return;
		}

		ItemStack main = this.entity.getMainHandItem();
		ItemStack off = this.entity.getOffhandItem();
		boolean activeMain = main.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(main);
		boolean activeOff = off.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(off);

		if (!activeMain && !activeOff) {
			stopHum();
			return;
		}

		this.x = this.entity.getX();
		this.y = this.entity.getY();
		this.z = this.entity.getZ();
	}

	private void stopHum() {
		this.stop();
		if (this.onStopCallback != null) {
			this.onStopCallback.run();
		}
	}
}