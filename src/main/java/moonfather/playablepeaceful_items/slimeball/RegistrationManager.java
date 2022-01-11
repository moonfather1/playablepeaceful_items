package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.cleric.WanderingClericEntity;
import moonfather.playablepeaceful_items.cleric.WanderingClericSpawnEgg;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class RegistrationManager
{
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, PeacefulMod.MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PeacefulMod.MODID);

	public static void init()
	{
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<SlimeSpawnEgg> SLIME_EGG = ITEMS.register("slime_egg", SlimeSpawnEgg::new);
	public static final RegistryObject<EntityType<CuteSlimeEntity>> SLIME = ENTITIES.register("slime_mf", () -> EntityType.Builder.<CuteSlimeEntity>of(CuteSlimeEntity::new, EntityClassification.CREATURE)
			.sized(2.0f, 1.7f)
			.setShouldReceiveVelocityUpdates(false)
			//.canSpawnFarFromPlayer()
			.build("slime_mf"));

	public static final RegistryObject<WanderingClericSpawnEgg> CLERIC_EGG = ITEMS.register("cleric_egg", WanderingClericSpawnEgg::new);
	public static final RegistryObject<EntityType<WanderingClericEntity>> CLERIC = ENTITIES.register("cleric_mf", () -> EntityType.Builder.<WanderingClericEntity>of(WanderingClericEntity::new, EntityClassification.CREATURE)
			.sized(0.6F, 1.95F)
			.clientTrackingRange(10)
			.setShouldReceiveVelocityUpdates(false)
			.build("cleric_mf"));


	@ObjectHolder(PeacefulMod.MODID + ":" + "slime_egg")
	public static Item SLIME_EGG_HOLDER;

	@ObjectHolder(PeacefulMod.MODID + ":" + "slime_mf")
	public static EntityType<CuteSlimeEntity> SLIME_HOLDER;

	@ObjectHolder(PeacefulMod.MODID + ":" + "cleric_mf")
	public static EntityType<WanderingClericEntity> CLERIC_HOLDER;

	@ObjectHolder(PeacefulMod.MODID + ":" + "cleric_egg")
	public static Item CLERIC_EGG_HOLDER;
}
