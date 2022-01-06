package moonfather.playablepeaceful_items;

import moonfather.playablepeaceful_items.cleric.WanderingClericEntity;
import moonfather.playablepeaceful_items.cotton.*;
import moonfather.playablepeaceful_items.end.EndCityLootModifier;
import moonfather.playablepeaceful_items.gunpowder.FertilizerItem;
import moonfather.playablepeaceful_items.items.BasicItem;
import moonfather.playablepeaceful_items.membrane.PhantomBushBlock;
import moonfather.playablepeaceful_items.membrane.SimpleCropPatchGenerationForPhantomBush;
import moonfather.playablepeaceful_items.others.OptionalRecipeCondition;
import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import moonfather.playablepeaceful_items.slimeball.RegistrationManager;
import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PeacefulMod.MODID)
public class PeacefulMod
{
    public static final String MODID = "playablepeaceful_items";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public PeacefulMod()
    {
    	//todo: issue: shulker boxes in non-peaceful
	    //todo: try poop scrapper in create and record
	    //todo: charcoal
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OptionsHolder.COMMON_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initFuckingAttributes);
        MinecraftForge.EVENT_BUS.register(this);
	    RegistrationManager.init();

	    ClientInitializationHandler clientInitializationHandlerInstance = new ClientInitializationHandler(FMLJavaModLoadingContext.get().getModEventBus());
	    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientInitializationHandlerInstance::registerClientOnlyEvents);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
	    CraftingHelper.register(new ConfigurableOutputRecipeCondition.Serializer(new ResourceLocation(PeacefulMod.MODID, "configurable")));
	    CraftingHelper.register(new OptionalRecipeCondition.Serializer(new ResourceLocation(PeacefulMod.MODID, "optional")));
		
		event.enqueueWork(()-> EntitySpawnPlacementRegistry.register(RegistrationManager.SLIME.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CuteSlimeEntity::checkCuteSlimeSpawnRules));
	    SimpleCropPatchGeneration.registerConfiguredFeatures();
	    // SimpleCropPatchGenerationForPhantomBush.registerConfiguredFeatures(); -- apparently not necessary - i forgot this line and worldgen works dine
	}

    private void doClientStuff(final FMLClientSetupEvent event)
    {
    	//RenderingRegistry#registerEntityRenderingHandler
    }

    private void initFuckingAttributes(final EntityAttributeCreationEvent event)
    {
        event.put(RegistrationManager.SLIME.get(), CuteSlimeEntity.createAttributes().build());
        event.put(RegistrationManager.CLERIC.get(), WanderingClericEntity.createAttributes().build());
    }
	
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
    }



    public static BasicItem CottonSeeds;
    public static BasicItem CottonBoll;
    public static BasicItem BatPoop;
    public static BasicItem LavaSpritePoop;
	public static BasicItem Fertilizer;

	public static CottonSeedlingBlock CottonSeedling;
	public static CottonBushBlock CottonBush;
	public static PhantomBushBlock PhantomBush;



	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
	{
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
	        CottonBush = new CottonBushBlock();
	        CottonBush.setRegistryName(MODID, "block_cotton_plant");
	        blockRegistryEvent.getRegistry().register(CottonBush);
	        CottonSeedling = new CottonSeedlingBlock();
	        CottonSeedling.setRegistryName(MODID, "block_cotton_seedling");
	        blockRegistryEvent.getRegistry().register(CottonSeedling);

	        PhantomBush = new PhantomBushBlock();
	        PhantomBush.setRegistryName(MODID, "block_phantom_plant");
	        blockRegistryEvent.getRegistry().register(PhantomBush);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            CottonBoll = new BasicItem();
            CottonBoll.setRegistryName(MODID, "item_cotton");
            itemRegistryEvent.getRegistry().register(CottonBoll);

            BatPoop = new BasicItem();
            BatPoop.setRegistryName(MODID, "poop_bat");
            itemRegistryEvent.getRegistry().register(BatPoop);

	        LavaSpritePoop = new BasicItem();
	        LavaSpritePoop.setRegistryName(MODID, "poop_lava_sprite");
	        itemRegistryEvent.getRegistry().register(LavaSpritePoop);

	        Fertilizer = new FertilizerItem();
	        Fertilizer.setRegistryName(MODID, "fertilizer");
	        itemRegistryEvent.getRegistry().register(Fertilizer);

	        CottonSeeds = new CottonSeedsItem();
            CottonSeeds.setRegistryName(MODID, "seeds_cotton");
            itemRegistryEvent.getRegistry().register(CottonSeeds);
	        ComposterBlock.COMPOSTABLES.put(CottonSeeds, 0.3f);
        }

		@SubscribeEvent
		public static void onLootModifierRegistration(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
		{
			event.getRegistry().register(new EndCityLootModifier.Serializer().setRegistryName(new ResourceLocation(PeacefulMod.MODID,"loot_modifier_for_shells")));
		}
    }
}
