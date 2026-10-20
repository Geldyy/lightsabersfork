package com.fiskmods.pika.lightsabers.loot;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddLootModifier extends LootModifier {

	public static final MapCodec<AddLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier
			.codecStart(inst)
			.and(inst.group(BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("items").forGetter(m -> m.items),
					Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(m -> m.chance),
					Codec.INT.optionalFieldOf("min_count", 1).forGetter(m -> m.minCount),
					Codec.INT.optionalFieldOf("max_count", 1).forGetter(m -> m.maxCount),
					Codec.BOOL.optionalFieldOf("pick_random_one", true).forGetter(m -> m.pickRandomOne)))
			.apply(inst, AddLootModifier::new));

	private final List<Item> items;
	private final float chance;
	private final int minCount;
	private final int maxCount;
	private final boolean pickRandomOne;

	public AddLootModifier(LootItemCondition[] conditionsIn, List<Item> items, float chance, int minCount, int maxCount,
			boolean pickRandomOne) {
		super(conditionsIn);
		this.items = items;
		this.chance = chance;
		this.minCount = minCount;
		this.maxCount = maxCount;
		this.pickRandomOne = pickRandomOne;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (this.items == null || this.items.isEmpty()) {
			return generatedLoot;
		}

		if (context.getRandom().nextFloat() < this.chance) {
			int count = (this.minCount >= this.maxCount) ? this.minCount
					: this.minCount + context.getRandom().nextInt(this.maxCount - this.minCount + 1);

			if (this.pickRandomOne) {
				Item chosen = this.items.get(context.getRandom().nextInt(this.items.size()));
				generatedLoot.add(new ItemStack(chosen, count));
			} else {
				for (Item item : this.items) {
					generatedLoot.add(new ItemStack(item, count));
				}
			}
		}

		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}