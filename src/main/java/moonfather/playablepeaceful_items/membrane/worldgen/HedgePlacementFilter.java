package moonfather.playablepeaceful_items.membrane.worldgen;

import com.mojang.serialization.Codec;
import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;

public class HedgePlacementFilter extends PlacementFilter
{
    public static final Codec<HedgePlacementFilter> CODEC = Codec.unit(() -> {
        return new HedgePlacementFilter();
    });


    private HedgePlacementFilter()
    {
        super();
    }

    private boolean lessOften = true;
    public static HedgePlacementFilter create(boolean rare)
    {
        HedgePlacementFilter newOne = new HedgePlacementFilter();
        newOne.lessOften = rare;
        return newOne;
    }

    @Override
    public PlacementModifierType<?> type()
    {
        return RegistrationManager.BIOME_FILTER2.get();
    }

    ////////////////////////////////////////////////////////////////////

    @Override
    protected boolean shouldPlace(PlacementContext placementContext, Random random, BlockPos pos)
    {
        Holder<Biome> biome = placementContext.getLevel().getBiome(pos);
        if (this.lessOften)
        {
            //System.out.println("~~shouldPlace lower chance: returning " + biome.is(new ResourceLocation("minecraft:end_highlands")));
            return biome.is(new ResourceLocation("minecraft:end_highlands"));
        }
        else
        {
            //System.out.println("~~shouldPlace higher chance: returning false");
            return biome.is(new ResourceLocation("byg:bulbis_gardens_edge")) || biome.is(new ResourceLocation("byg:ivis_fields"))
                    || biome.is(new ResourceLocation("byg:shattered_desert_isles")) || biome.is(new ResourceLocation("byg:shattered_desert"))
                    || biome.is(new ResourceLocation("byg:purpur_peaks")) || biome.is(new ResourceLocation("byg:nightshade_forest"));
        }
    }
}
