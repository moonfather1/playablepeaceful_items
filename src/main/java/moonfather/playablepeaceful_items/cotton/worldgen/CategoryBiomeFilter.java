package moonfather.playablepeaceful_items.cotton.worldgen;

import com.mojang.serialization.Codec;
import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;

public class CategoryBiomeFilter extends PlacementFilter
{
    public static final Codec<CategoryBiomeFilter> CODEC = Codec.unit(CategoryBiomeFilter::new);

    private CategoryBiomeFilter()   {   }

    public static CategoryBiomeFilter create()
    {
        return new CategoryBiomeFilter();
    }

    public CategoryBiomeFilter assign(Biome.BiomeCategory category)
    {
        if (this.cat1 == null)
        {
            this.cat1 = category;
        }
        else if (this.cat2 == null)
        {
            this.cat2 = category;
        }
        return this;
    }
    private Biome.BiomeCategory cat1, cat2;

    @Override
    public PlacementModifierType<?> type()
    {
        return RegistrationManager.BIOME_FILTER3.get();
    }

    ////////////////////////////////////////////////////////////////////

    @Override
    protected boolean shouldPlace(PlacementContext placementContext, Random random, BlockPos pos)
    {
        if (this.cat1 == null)
        {
            throw new IllegalArgumentException();
        }
        Holder<Biome> biome = placementContext.getLevel().getBiome(pos);
        boolean result = Biome.getBiomeCategory(biome).equals(this.cat1);
        if (! result && this.cat2 != null)
        {
            result = Biome.getBiomeCategory(biome).equals(this.cat2);
        }
        return result;
    }
}
