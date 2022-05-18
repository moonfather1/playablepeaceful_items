package moonfather.playablepeaceful_items;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class OptionsHolder
{
	public static class Common
	{
		private static final boolean defaultGunpowderRelatedBatsDropPoop = true;
		private static final boolean defaultGunpowderRelatedExtraBatsAppear = true;
		private static final boolean defaultGunpowderRelatedLavaSpritesEnabled = true;
		private static final double defaultGunpowderRelatedBatPoopFreqMultiplier = 1.0f;
		private static final double defaultGunpowderRelatedLavaSpritePoopFreqMultiplier = 1.0f;
		private static final boolean defaultGunpowderModuleEnabled = true;
		private static final boolean defaultGunpowderModuleOnlyOnPeacefulDifficulty = true;

		private static final boolean defaultShulkerBoxSimpleSolution = true;

		private static final boolean defaultCottonSeedsFromVillager = true;
		private static final int defaultCottonStringAmount = 3;
		private static final int defaultCottonWorldGenOdds = 8;
		private static final double defaultCottonGrowthMultiplier = 1.0;
		private static final boolean defaultCottonBushActivatableOnlyOnPeacefulDifficulty = false;

		private static final int defaultHedgeWorldGenOdds = 4;
		private static final boolean defaultHedgeBushActivatableOnlyOnPeacefulDifficulty = false;

		private static final String defaultClericCurrencyItem = "minecraft:gold_ingot";
		private static final double defaultClericCurrencyMultiplier = 1.0d;
		private static final double defaultClericAppearanceDelayMultiplier = 1.0d;

		public final ConfigValue<Boolean> GunpowderModuleEnabled;
		public final ConfigValue<Boolean> GunpowderModuleOnlyOnPeacefulDifficulty;
		public final ConfigValue<Boolean> GunpowderRelatedBatsDropPoop;
		public final ConfigValue<Boolean> GunpowderRelatedExtraBatsAppear;
		public final ConfigValue<Boolean> GunpowderRelatedLavaSpritesEnabled;
		public final ConfigValue<Double> GunpowderRelatedBatPoopFreqMultiplier;
		public final ConfigValue<Double> GunpowderRelatedLavaSpritePoopFreqMultiplier;

		public final ConfigValue<Boolean> ShulkerBoxSimpleSolution;

		public final ConfigValue<Boolean> CottonSeedsFromVillager;
		public final ConfigValue<Integer> CottonStringAmount;
		public final ConfigValue<Integer> CottonWorldGenOdds;
		public final ConfigValue<Double> CottonGrowthMultiplier;
		public final ConfigValue<Boolean> CottonBushActivatableOnlyOnPeacefulDifficulty;

		public final ConfigValue<Integer> HedgeWorldGenOdds;
		public final ConfigValue<Boolean> HedgeBushActivatableOnlyOnPeacefulDifficulty;

		public final ConfigValue<String> ClericCurrencyItem;
		public final ConfigValue<Double> ClericCurrencyMultiplier;
		public final ConfigValue<Double> ClericAppearanceDelayMultiplier;


		public Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("main");

			builder.push("Gunpowder");
			this.GunpowderModuleEnabled = builder.comment("This is a master switch to disable the whole gunpowder module. If you have other ways of acquiring gunpowder and just don't want to bother reading descriptions below, just flip this switch. You could also turn off things below individually.")
					.define("Module enabled", defaultGunpowderModuleEnabled);
			this.GunpowderModuleOnlyOnPeacefulDifficulty = builder.comment("You can use this to let gunpowder module run in peaceful worlds, but have it disabled in normal difficulty worlds.")
					.define("Module Only works in peaceful", defaultGunpowderModuleOnlyOnPeacefulDifficulty);
			this.GunpowderRelatedBatsDropPoop = builder.comment("If this is enabled, bats will poop. Scoop it up to craft gunpowder or fertilize your fields.")
					.define("Bats drop poop", defaultGunpowderRelatedBatsDropPoop);
			this.GunpowderRelatedExtraBatsAppear = builder.comment("This controls whether bats procreate. If enabled, then if there are 2+ but not too many bats in a cave, they will eventually make babies.")
					.define("Extra bats appear over time", defaultGunpowderRelatedExtraBatsAppear);
			this.GunpowderRelatedLavaSpritesEnabled = builder.comment("This controls whether lava sprites even spawn. Their hot sulfury poop can be used to craft gunpowder.")
					.define("Lava sprites enabled", defaultGunpowderRelatedLavaSpritesEnabled);
			this.GunpowderRelatedBatPoopFreqMultiplier = builder.comment("This controls how often the bat will poop. 1.0 is default, 0.2 is 5x less often, 5.0 is 5x more often.")
					.defineInRange("Bat poop frequency multiplier", defaultGunpowderRelatedBatPoopFreqMultiplier, 0f, 10d);
			this.GunpowderRelatedLavaSpritePoopFreqMultiplier = builder.comment("This controls how often the lava sprite will poop put sulfur dust. 1.0 is default, 0.2 is 5x less often, 5.0 is 5x more often.")
					.defineInRange("Lava sprite poop frequency multiplier", defaultGunpowderRelatedLavaSpritePoopFreqMultiplier, 0f, 10d);
			builder.pop();

			builder.push("String");
			this.CottonSeedsFromVillager = builder.comment("You can turn this off to prevent players from buying seeds from village farmer.")
					.define("Cotton - seeds from village farmer", defaultCottonSeedsFromVillager);
			this.CottonStringAmount = builder.comment("This controls how many string pieces you get in a crafting table from 8 cotton bolls. Values of 2 or 3 are likely fine; set to 0 to disable (if you have some kind of loom thing).")
					.defineInRange("Cotton - string recipe amount", defaultCottonStringAmount, 0, 8);
			this.CottonWorldGenOdds = builder.comment("This controls how common cotton bushes are in the wild. Lower values mean higher frequency (biome generator rolls a random number with 1/thisvalue chance). For example 3 means very common in savannas, 13 is reasonable, 43 means you may need to search more than one biome to find any cotton. This setting is for savannas. Cotton is also found in forests and plains, but 3x less commonly compared to savannas. Zero means no world generation.")
					.defineInRange("Cotton - world generation odds", defaultCottonWorldGenOdds, 0, 100);
			this.CottonGrowthMultiplier = builder.comment("This controls fast cotton bush grows. 1.0 is default (as author intended), 0.5 is 2x slower, 2.0 is 2x faster.")
					.defineInRange("Cotton growth speed multiplier", defaultCottonGrowthMultiplier, 0.01d, 3.00d);
			this.CottonBushActivatableOnlyOnPeacefulDifficulty = builder.comment("Can you pick cotton bolls from cotton bush in any difficulty or just in peaceful?. One of those pointless settings.")
					.define("Cotton - harvestable only in peaceful difficulty", defaultCottonBushActivatableOnlyOnPeacefulDifficulty);
			builder.pop();

			builder.push("Shulker boxes");
			this.ShulkerBoxSimpleSolution = builder.comment("Author couldn't decide what to do so you have two options: simple mode means you can craft a shulker box from one chest and 8 chorus fruits. Simpler, but you still have to get to the end and find a chorus forest. If you turn this off, player needs to find shulker shells in end city chests.")
					.define("Shulker box - simple solution", defaultShulkerBoxSimpleSolution);
			builder.pop();

			builder.push("Phantom membranes");
			this.HedgeWorldGenOdds = builder.comment("This controls how common phantom hedges are in the end highlands. Lower values mean higher frequency (biome generator rolls a random number with 1/thisvalue chance). For example 3 means very common, 13 is somewhat rare, 43 means you may need to search more than one end island to find phantom hedges. Zero means no world generation.")
					.defineInRange("Phantom hedge - world generation odds", defaultHedgeWorldGenOdds, 0, 100);
			this.HedgeBushActivatableOnlyOnPeacefulDifficulty = builder.comment("Can you pick phantom membranes from phantom hedge bush in any difficulty or just in peaceful?. One of those pointless settings.")
					.define("Phantom hedge - harvestable only in peaceful difficulty", defaultHedgeBushActivatableOnlyOnPeacefulDifficulty);
			builder.pop();

			builder.push("Wandering cleric");
			this.ClericCurrencyItem = builder.comment("Wandering cleric sells stuff like potions and blaze rods. In exchange he wants this. Default is gold ingot on 1.16 and amethyst on 1.18 and up.")
					.define("Wandering cleric - currency item", defaultClericCurrencyItem);
			this.ClericCurrencyMultiplier = builder.comment("If you changed the currency item (see above) or you just feel that the author set costs badly, use this. This multiplies default values.")
					.defineInRange("Wandering cleric - currency multiplier", defaultClericCurrencyMultiplier, 0.25, 2);
			this.ClericAppearanceDelayMultiplier = builder.comment("Wandering cleric sells stuff like potions and blaze rods. This value multiplies the delay between two appearances; for example 0.5 would cut delay in half which may cause two clerics around you at the same time (you want this on a server with many players that live far apart); similarly higher values mean several days between two appearances. Default (1.0 multiplier) is five in-game days in singleplayer and 2.5 in-game days if there are 2+ players on the server.")
					.defineInRange("Wandering cleric - appearance delay multiplier", defaultClericAppearanceDelayMultiplier, 0.25, 10);
			builder.pop();

			builder.pop();
		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static //constructor
	{
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = commonSpecPair.getLeft();
		COMMON_SPEC = commonSpecPair.getRight();
	}
}
