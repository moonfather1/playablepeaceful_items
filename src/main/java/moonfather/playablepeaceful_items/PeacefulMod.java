package moonfather.playablepeaceful_items;

import moonfather.playablepeaceful_items.cleric.WanderingClericEntity;
import moonfather.playablepeaceful_items.cleric.WanderingClericInterModComSupport;
import moonfather.playablepeaceful_items.cotton.*;
import moonfather.playablepeaceful_items.end.EndCityLootModifier;
import moonfather.playablepeaceful_items.end.ShulkerBoxInfoItem;
import moonfather.playablepeaceful_items.gunpowder.FertilizerItem;
import moonfather.playablepeaceful_items.gunpowder.blocks.BatPoopBlock;
import moonfather.playablepeaceful_items.gunpowder.blocks.SulphureousLilypadBlock;
import moonfather.playablepeaceful_items.gunpowder.items.BatPoopItem;
import moonfather.playablepeaceful_items.gunpowder.sprite.SpriteEntity;
import moonfather.playablepeaceful_items.end.ShulkerBoxRecipe;
import moonfather.playablepeaceful_items.shared.BasicItem;
import moonfather.playablepeaceful_items.gunpowder.items.LavaPoopItem;
import moonfather.playablepeaceful_items.membrane.PhantomBushBlock;
import moonfather.playablepeaceful_items.others.OptionalRecipeCondition;
import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
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
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OptionsHolder.COMMON_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initFuckingAttributes);
	    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInterModProcess);
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
	    event.put(RegistrationManager.SPRITE.get(), SpriteEntity.createAttributes().build());
    }
	
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
    }



	private void onInterModProcess(InterModProcessEvent event)
	{
		WanderingClericInterModComSupport.onInterModProcess(event);
	}



	public static BasicItem CottonSeeds;
    public static BasicItem CottonBoll;
    public static BasicItem BatPoop;
    public static BasicItem LavaSpritePoop;
	public static BasicItem Fertilizer;

	public static CottonSeedlingBlock CottonSeedling;
	public static CottonBushBlock CottonBush;
	public static PhantomBushBlock PhantomBush;
	public static SulphureousLilypadBlock LilypadBlock;
	public static BatPoopBlock BatPoopBlock;



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

	        LilypadBlock = new SulphureousLilypadBlock();
	        LilypadBlock.setRegistryName(MODID, "block_sulphureous_lilypad");
	        blockRegistryEvent.getRegistry().register(LilypadBlock);

	        BatPoopBlock = new BatPoopBlock();
	        BatPoopBlock.setRegistryName(MODID, "block_bat_droppings");
	        blockRegistryEvent.getRegistry().register(BatPoopBlock);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            CottonBoll = new BasicItem();
            CottonBoll.setRegistryName(MODID, "item_cotton");
            itemRegistryEvent.getRegistry().register(CottonBoll);

            BatPoop = new BatPoopItem();
            BatPoop.setRegistryName(MODID, "poop_bat");
            itemRegistryEvent.getRegistry().register(BatPoop);

	        LavaSpritePoop = new LavaPoopItem();
	        LavaSpritePoop.setRegistryName(MODID, "poop_lava_sprite");
	        itemRegistryEvent.getRegistry().register(LavaSpritePoop);

	        Fertilizer = new FertilizerItem();
	        Fertilizer.setRegistryName(MODID, "fertilizer");
	        itemRegistryEvent.getRegistry().register(Fertilizer);

	        CottonSeeds = new CottonSeedsItem();
            CottonSeeds.setRegistryName(MODID, "seeds_cotton");
            itemRegistryEvent.getRegistry().register(CottonSeeds);
	        ComposterBlock.COMPOSTABLES.put(CottonSeeds, 0.3f);

	        Item info = new ShulkerBoxInfoItem();
	        info.setRegistryName(MODID, "info_item");
	        itemRegistryEvent.getRegistry().register(info);
        }

		@SubscribeEvent
		public static void onLootModifierRegistration(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
		{
			event.getRegistry().register(new EndCityLootModifier.Serializer().setRegistryName(new ResourceLocation(PeacefulMod.MODID,"loot_modifier_for_shells")));
		}



		@SubscribeEvent
		public static void onRecipeSerializerRegistration(RegistryEvent.Register<IRecipeSerializer<?>> event)
		{
			//ShulkerBoxRecipe.StupidSerializer = new SpecialRecipeSerializer<>(ShulkerBoxRecipe::new);
			//ShulkerBoxRecipe.StupidSerializer.setRegistryName(PeacefulMod.MODID, "shulker_box_conditional");
			event.getRegistry().register(ShulkerBoxRecipe.StupidSerializer);
		}
    }
}
