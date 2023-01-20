package moonfather.playablepeaceful_items;

import com.mojang.serialization.Codec;
import moonfather.playablepeaceful_items.cleric.WanderingClericEntity;
import moonfather.playablepeaceful_items.cotton.CottonBollItem;
import moonfather.playablepeaceful_items.cotton.CottonBushBlock;
import moonfather.playablepeaceful_items.cotton.CottonSeedlingBlock;
import moonfather.playablepeaceful_items.cotton.CottonSeedsItem;
import moonfather.playablepeaceful_items.cotton.worldgen.CategoryBiomeFilter;
import moonfather.playablepeaceful_items.cotton.worldgen.DualTagBiomeFilter;
import moonfather.playablepeaceful_items.cotton.worldgen.CottonFeature;
import moonfather.playablepeaceful_items.cotton.worldgen.SimpleFeatureConfiguration;
import moonfather.playablepeaceful_items.end.EndCityLootModifier;
import moonfather.playablepeaceful_items.end.ShulkerBoxInfoItem;
import moonfather.playablepeaceful_items.end.ShulkerBoxRecipe;
import moonfather.playablepeaceful_items.gunpowder.blocks.*;
import moonfather.playablepeaceful_items.gunpowder.items.*;
import moonfather.playablepeaceful_items.gunpowder.sprite.SpriteEntity;
import moonfather.playablepeaceful_items.membrane.PhantomBushBlock;
import moonfather.playablepeaceful_items.membrane.worldgen.HedgePlacementFilter;
import moonfather.playablepeaceful_items.membrane.worldgen.PhantomHedgeFeature;
import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistrationManager
{
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
	private static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Constants.MODID);
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);
	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);
	private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Constants.MODID);
	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);

	public static void init()
	{
		RegistrationManager.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistrationManager.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistrationManager.LOOT_MODIFIER_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistrationManager.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistrationManager.FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistrationManager.PLACEMENT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistrationManager.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	static
	{
		PeacefulMod.Items.CottonSeeds = ITEMS.register("seeds_cotton", CottonSeedsItem::new);
		PeacefulMod.Items.CottonBoll = ITEMS.register("item_cotton", CottonBollItem::new);
		PeacefulMod.Items.BatPoop = ITEMS.register("poop_bat", BatPoopItem::new);
		PeacefulMod.Items.LavaSpritePoop = ITEMS.register("poop_lava_sprite", LavaPoopItem::new);
		PeacefulMod.Items.Fertilizer = ITEMS.register("fertilizer", FertilizerItem::new);
		//-------------------------------------------------
		PeacefulMod.Blocks.Registry.CottonBush = BLOCKS.register("block_cotton_plant", CottonBushBlock::new);
		PeacefulMod.Blocks.Registry.CottonSeedling = BLOCKS.register("block_cotton_seedling", CottonSeedlingBlock::new);
		PeacefulMod.Blocks.Registry.PhantomBush = BLOCKS.register("block_phantom_plant", PhantomBushBlock::new);
		PeacefulMod.Blocks.LilypadBlock = BLOCKS.register("block_sulphureous_lilypad", SulphureousLilypadBlock::new);
		PeacefulMod.Blocks.BatPoopBlock = BLOCKS.register("block_bat_droppings", BatPoopBlock::new);
		//-------------------------------------------------

	}

	public static final RegistryObject<Item> SHULKER_BOX_INFO = ITEMS.register("info_item", ShulkerBoxInfoItem::new);
	public static final RegistryObject<RecipeSerializer<ShulkerBoxRecipe>> ShulkerBoxRecipeSerializer = RECIPE_SERIALIZERS.register("shulker_box_conditional", ShulkerBoxRecipe::CreateSerializer);

	public static final RegistryObject<Feature<SimpleFeatureConfiguration>> WILD_COTTON = FEATURES.register("wild_cotton", () -> new CottonFeature(SimpleFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<SimpleFeatureConfiguration>> PHANTOM_BUSH = FEATURES.register("phantom_hedge_feature", () -> new PhantomHedgeFeature(SimpleFeatureConfiguration.CODEC));
	private static final RegistryObject<GlobalLootModifierSerializer<EndCityLootModifier>> GLMSerializer1 = LOOT_MODIFIER_SERIALIZERS.register("loot_modifier_for_shells", EndCityLootModifier.Serializer::new);

	public static final RegistryObject<EntityType<CuteSlimeEntity>> SLIME = ENTITIES.register("slime_mf", () -> EntityType.Builder.<CuteSlimeEntity>of(CuteSlimeEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(false)
			.sized(CuteSlimeEntity.GetScaleH(), CuteSlimeEntity.GetScaleV())
			.build("slime_mf"));
	public static final RegistryObject<Item> SLIME_SPAWN_EGG = ITEMS.register("slime_spawn_egg", () -> new ForgeSpawnEggItem(SLIME::get, 0x44cc22, 0x229900, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<EntityType<WanderingClericEntity>> CLERIC = ENTITIES.register("cleric_mf", () -> EntityType.Builder.<WanderingClericEntity>of(WanderingClericEntity::new, MobCategory.CREATURE)
			.clientTrackingRange(10)
			.setShouldReceiveVelocityUpdates(false)
			.sized(WanderingClericEntity.GetScaleH(), WanderingClericEntity.GetScaleV())
			.build("cleric_mf"));
	public static final RegistryObject<Item> CLERIC_SPAWN_EGG = ITEMS.register("cleric_spawn_egg", () -> new ForgeSpawnEggItem(CLERIC::get, 0x1188ee, 0xeecc33, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<EntityType<SpriteEntity>> SPRITE = ENTITIES.register("sprite_mf", () -> EntityType.Builder.<SpriteEntity>of(SpriteEntity::new, MobCategory.CREATURE)
			.clientTrackingRange(1)
			.setShouldReceiveVelocityUpdates(false)
			.sized(SpriteEntity.GetScale(), SpriteEntity.GetScale())
			.build("sprite_mf"));
	public static final RegistryObject<Item> SPRITE_SPAWN_EGG = ITEMS.register("sprite_spawn_egg", () -> new ForgeSpawnEggItem(SPRITE::get, 0xcc5511, 0xffcc22, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	//******************************************************
	public static final RegistryObject<PlacementModifierType<DualTagBiomeFilter>> BIOME_FILTER1 = PLACEMENT_MODIFIERS.register("biome_filter1", () -> typeConvert(DualTagBiomeFilter.CODEC));
	public static final RegistryObject<PlacementModifierType<HedgePlacementFilter>> BIOME_FILTER2 = PLACEMENT_MODIFIERS.register("biome_filter2", () -> typeConvert(HedgePlacementFilter.CODEC));
	public static final RegistryObject<PlacementModifierType<CategoryBiomeFilter>> BIOME_FILTER3 = PLACEMENT_MODIFIERS.register("biome_filter3", () -> typeConvert(CategoryBiomeFilter.CODEC));
	private static <P extends PlacementModifier> PlacementModifierType<P> typeConvert(Codec<P> codec) {	return () -> codec;	} // copied from farmer's delight
}
