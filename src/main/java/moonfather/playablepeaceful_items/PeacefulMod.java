package moonfather.playablepeaceful_items;

import com.mojang.logging.LogUtils;
import moonfather.playablepeaceful_items.cleric.WanderingClericEntity;
import moonfather.playablepeaceful_items.cleric.WanderingClericInterModComSupport;
import moonfather.playablepeaceful_items.cotton.ConfigurableOutputRecipeCondition;
import moonfather.playablepeaceful_items.cotton.worldgen.SimpleWildCropGeneration;
import moonfather.playablepeaceful_items.gunpowder.sprite.SpriteEntity;
import moonfather.playablepeaceful_items.membrane.worldgen.SimpleCropPatchGenerationForPhantomBush;
import moonfather.playablepeaceful_items.others.OptionalRecipeCondition;
import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MODID)
public class PeacefulMod
{
	//todo: cotton: when wild cotton ticks, let there be a chance for 3->1   (hedge done)
	//todo: PPXP: master multiplier
	//+odo: remove egg
	//todo: 1.16: 3x3 chorus bug?
	//ne-odo: option to stop spamming bonemeal.
	//+odo: cotton biome tags arent good. include Biomes.SAVANNA_PLATEAU manually
	//+odo: cotton provider is unused
	//+odo: cotton only in savanna for now?
	//todo: cotton spread way too far             (hedge is fine. maybe it's x3 too often, increase chance of 2 and 4)
	//+odo: ALL OF THAT FOR MEMBRANE BUSH
	//? cotton sometimes appears in water and is destroyed. i don't really have to fix it.
	//todo: not enough slimes. intercept cows and sheep, halve their spawns in swamps or cut to 1/4 and spawn slimes instead
	//+bug: top membrane bar in hedge model is bad
	//+bug: hedge accepts torch on top
	//--------------------------
	//todo: remove dragon egg
	//todo: WTR compat
	//--------------------------
	//+no light green slimes?
	//todo: slime special properties
	//-------------
	//todo: test sprite teleport and teleport particles
	//todo: egg colors
	//todo: slime spawning diffi check
	//todo: slime spawning multiplier for all methods
	//todo: modded birds derived from Bat may poop
	//todo: sprites - react to section chande in front of lava, not above
	//bug: WC still gold
	//idea: react to bats dying to fire/lava
	//bug!!: sprites don't spawn at all
	//egg not done, way too many hedges. make slime drowning optional, default to false
	//todo: do particles when hedge destroyed
	//smack doesn't work

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public PeacefulMod()
    {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OptionsHolder.COMMON_SPEC);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initFuckingAttributes);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInterModProcess);
		RegistrationManager.init();
		ClientInitializationHandler clientInitializationHandlerInstance = new ClientInitializationHandler(FMLJavaModLoadingContext.get().getModEventBus());
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientInitializationHandlerInstance::registerClientOnlyEvents);
	}

    private void setup(final FMLCommonSetupEvent event)
    {
		CraftingHelper.register(new ConfigurableOutputRecipeCondition.Serializer(new ResourceLocation(Constants.MODID, "configurable")));
		CraftingHelper.register(new OptionalRecipeCondition.Serializer(new ResourceLocation(Constants.MODID, "optional")));
		event.enqueueWork(()-> SpawnPlacements.register(RegistrationManager.SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CuteSlimeEntity::checkCuteSlimeSpawnRules));
		SimpleWildCropGeneration.registerConfiguredFeatures();
		SimpleCropPatchGenerationForPhantomBush.registerConfiguredFeatures();
		ComposterBlock.COMPOSTABLES.put(Items.CottonSeeds.get(), 0.3f);
	}


    private void onInterModProcess(final InterModProcessEvent event)
    {
        WanderingClericInterModComSupport.onInterModProcess(event);
    }
	
	private void initFuckingAttributes(final EntityAttributeCreationEvent event)
	{
		event.put(RegistrationManager.SLIME.get(), CuteSlimeEntity.createAttributes().build());
		event.put(RegistrationManager.CLERIC.get(), WanderingClericEntity.createAttributes().build());
		event.put(RegistrationManager.SPRITE.get(), SpriteEntity.createAttributes().build());
	}

	//////////////////////////

	public static class Items
	{
		public static RegistryObject<Item> CottonSeeds = null;
		public static RegistryObject<Item> CottonBoll = null;
		public static RegistryObject<Item> BatPoop = null;
		public static RegistryObject<Item> LavaSpritePoop = null;
		public static RegistryObject<Item> Fertilizer = null;
	}
	public static class Blocks
	{
		private static CropBlock bushInstance = null, seedlingInstance = null;
		public static Supplier<CropBlock> CottonBush = () ->
		{
			if (bushInstance == null)
			{
				bushInstance = (CropBlock) Registry.CottonBush.get();
			}
			return bushInstance;
		};
		public static Supplier<CropBlock> CottonSeedling = () ->
		{
			if (seedlingInstance == null)
			{
				seedlingInstance = (CropBlock) Registry.CottonSeedling.get();
			}
			return seedlingInstance;
		};
		private static CropBlock hedgeInstance = null;
		public static Supplier<CropBlock> PhantomBush = () ->
		{
			if (hedgeInstance == null)
			{
				hedgeInstance = (CropBlock) Registry.PhantomBush.get();
			}
			return hedgeInstance;
		};
		public static Supplier<Block> BatPoopBlock = () -> Registry.BatPoopBlock.get();
		public static Supplier<Block> LilypadBlock = () -> Registry.LilypadBlock.get();
		public static class Registry
		{
			public static RegistryObject<Block> CottonBush = null;
			public static RegistryObject<Block> CottonSeedling = null;
			public static RegistryObject<Block> PhantomBush = null;
			public static RegistryObject<Block> BatPoopBlock = null;
			public static RegistryObject<Block> LilypadBlock = null;
		}
	}
	public static class Entities
	{
		public static EntityType<? extends LivingEntity> Sprite;
	}
}
