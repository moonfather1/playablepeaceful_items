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

public class DualTagBiomeFilter extends PlacementFilter
{
    /*public static final Codec<DualTagBiomeFilter> CODEC = RecordCodecBuilder.create((builder) ->
            builder.group(
                    TagKey.codec(ForgeRegistries.BIOMES.getRegistryKey()).fieldOf("biome_tag1").forGetter((instance) -> instance.biomeTag1P),
                    TagKey.codec(ForgeRegistries.BIOMES.getRegistryKey()).fieldOf("biome_tag2").forGetter((instance) -> instance.biomeTag2P),
                    TagKey.codec(ForgeRegistries.BIOMES.getRegistryKey()).fieldOf("biome_tag3_neg").forGetter((instance) -> instance.biomeTag3N),
                    TagKey.codec(ForgeRegistries.BIOMES.getRegistryKey()).fieldOf("biome_tag4_neg").forGetter((instance) -> instance.biomeTag4N)
            ).apply(builder, DualTagBiomeFilter::new));
*/
    public static final Codec<DualTagBiomeFilter> CODEC = Codec.unit(() -> {
        return new DualTagBiomeFilter();
    });


    private DualTagBiomeFilter()
    {
        this.biomeTag1P = null;
        this.biomeTag2P = null;
        this.biomeTag3N = null;
        this.biomeTag4N = null;
    }
    private DualTagBiomeFilter(TagKey<Biome> tag1, TagKey<Biome> tag2, TagKey<Biome> tag3, TagKey<Biome> tag4)
    {
        this.biomeTag1P = tag1;
        this.biomeTag2P = tag2;
        this.biomeTag3N = tag3;
        this.biomeTag4N = tag4;
    }

    public DualTagBiomeFilter add(TagKey<Biome> biomeTag)
    {
        if (this.biomeTag1P == null)
        {
            this.biomeTag1P = biomeTag;
        }
        else if (this.biomeTag2P == null)
        {
            this.biomeTag2P = biomeTag;
        }
        else
        {
            throw new IllegalArgumentException();
        }
        return this;
    }
    public DualTagBiomeFilter addNegative(TagKey<Biome> biomeTag)
    {
        if (this.biomeTag3N == null)
        {
            this.biomeTag3N = biomeTag;
        }
        else if (this.biomeTag4N == null)
        {
            this.biomeTag4N = biomeTag;
        }
        else
        {
            throw new IllegalArgumentException();
        }
        return this;
    }
    public static DualTagBiomeFilter create()
    {
        return new DualTagBiomeFilter();
    }

    private TagKey<Biome> biomeTag1P, biomeTag2P, biomeTag3N, biomeTag4N;

    @Override
    public PlacementModifierType<?> type()
    {
        return RegistrationManager.BIOME_FILTER1.get();
    }

    ////////////////////////////////////////////////////////////////////

    @Override
    protected boolean shouldPlace(PlacementContext placementContext, Random random, BlockPos pos)
    {
        if (this.biomeTag1P == null)
        {
            throw new IllegalArgumentException();
        }
        Holder<Biome> biome = placementContext.getLevel().getBiome(pos);
        boolean result = biome.is(biomeTag1P);
        if (this.biomeTag2P != null)
        {
            result = result && biome.is(biomeTag2P);
        }
        if (this.biomeTag3N != null)
        {
            result = result && ! biome.is(biomeTag3N);
        }
        if (this.biomeTag4N != null)
        {
            result = result && ! biome.is(biomeTag4N);
        }
        System.out.println("~~~DualTagBiomeFilter -> " + result);
        return result;
    }
}
