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
		private static final boolean defaultCottonBushActivatableOnlyOnPeacefulDifficulty = false;

		private static final int defaultHedgeWorldGenOdds = 4;
		private static final boolean defaultHedgeBushActivatableOnlyOnPeacefulDifficulty = false;

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
		public final ConfigValue<Boolean> CottonBushActivatableOnlyOnPeacefulDifficulty;

		public final ConfigValue<Integer> HedgeWorldGenOdds;
		public final ConfigValue<Boolean> HedgeBushActivatableOnlyOnPeacefulDifficulty;


		public Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("main");

			builder.push("Gunpowder");
			this.GunpowderModuleEnabled = builder.comment("This is a master switch to disable the whole gunpowder module. If you have other ways of acquiring gunpowder and just don't want to bother reading descriptions below, just flip this switch. You could also turn off things below individually.")
					.define("Module enabled", defaultGunpowderModuleEnabled);
			this.GunpowderModuleOnlyOnPeacefulDifficulty = builder.comment("You can use this to let gunpowder module run in peaceful worlds, but have it disabled in normal difficulty worlds.")
					.define("Module enabled", defaultGunpowderModuleOnlyOnPeacefulDifficulty);
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
			this.CottonSeedsFromVillager = builder.comment("If you don't want cotton to generate in the wild (or seeds in tall grass), you can turn this on to be able to buy seeds from village farmer.")
					.define("Cotton - seeds from village farmer", defaultCottonSeedsFromVillager);
			this.CottonStringAmount = builder.comment("This controls how many string pieces you get in a crafting table from 8 cotton bolls. Values of 2 or 3 are likely fine; set to 0 to disable (if you have some kind of loom thing).")
					.defineInRange("Cotton - string recipe amount", defaultCottonStringAmount, 0, 8);
			this.CottonWorldGenOdds = builder.comment("This controls how common cotton bushes are in the wild. Lower values mean higher frequency (biome generator rolls a random number with 1/thisvalue chance). For example 3 means very common in savannas, 13 is reasonable, 43 means you may need to search more than one biome to find any cotton. This setting is for savannas. Cotton is also found in forests and plains, but 3x less commonly compared to savannas. Zero means no world generation.")
					.defineInRange("Cotton - world generation odds", defaultCottonWorldGenOdds, 0, 100);
			this.CottonBushActivatableOnlyOnPeacefulDifficulty = builder.comment("Can you pick cotton bolls from cotton bush in any difficulty or just in peaceful?. One of those pointless settings.")
					.define("Cotton - harvestable only in peaceful difficulty", defaultCottonBushActivatableOnlyOnPeacefulDifficulty);
			builder.pop();

			builder.push("Shulker boxes");
			this.ShulkerBoxSimpleSolution = builder.comment("Author couldn't decide what to do so you have two options: simple mode means you can craft a shulker box from one chest and 8 chorus fruits. Simpler, but you still have to get to the end and find a chorus forest. If you turn this off, player needs to find shulker shells in end city chests.")
					.define("Shulker box - simple solution", defaultShulkerBoxSimpleSolution);
			builder.pop();

			builder.push("Phantom membranes");
			this.HedgeWorldGenOdds = builder.comment("This controls how phantom hedges are in the end highlands. Lower values mean higher frequency (biome generator rolls a random number with 1/thisvalue chance). For example 3 means very common, 13 is somewhat rare, 43 means you may need to search more than one end island to find phantom hedges. Zero means no world generation.")
					.defineInRange("Phantom hedge - world generation odds", defaultHedgeWorldGenOdds, 0, 100);
			this.HedgeBushActivatableOnlyOnPeacefulDifficulty = builder.comment("Can you pick phantom membranes from phantom hedge bush in any difficulty or just in peaceful?. One of those pointless settings.")
					.define("Phantom hedge - harvestable only in peaceful difficulty", defaultHedgeBushActivatableOnlyOnPeacefulDifficulty);
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
