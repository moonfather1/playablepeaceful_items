package moonfather.playablepeaceful_items.membrane.worldgen;

import com.mojang.serialization.Codec;
import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.cotton.worldgen.SimpleFeature;
import moonfather.playablepeaceful_items.cotton.worldgen.SimpleFeatureConfiguration;
import moonfather.playablepeaceful_items.membrane.PhantomBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class PhantomHedgeFeature extends SimpleFeature
{
    public PhantomHedgeFeature(Codec<SimpleFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    protected boolean placeCore(WorldGenLevel level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        if (! state.isAir() && ! state.getMaterial().isReplaceable())
        {
            return false;
        }
        state = level.getBlockState(pos.below());
        if (! getWhitelist().contains(state.getBlock()))
        {
            return false;
        }
        BlockPos above1 = pos.above();
        state = level.getBlockState(above1);
        if (! state.isAir() && ! state.getMaterial().isReplaceable())
        {
            return false;
        }
        BlockPos above2 = pos.above(2);
        state = level.getBlockState(above2);
        if (! state.isAir() && ! state.getMaterial().isReplaceable())
        {
            return false;
        }
        int age = ageDistribution[level.getRandom().nextInt(8)];
        state = PeacefulMod.Blocks.PhantomBush.get().defaultBlockState();
        level.setBlock(above2, state.setValue(PhantomBushBlock.LEVEL, 2).setValue(PeacefulMod.Blocks.PhantomBush.get().getAgeProperty(), age), 2);
        level.setBlock(above1, state.setValue(PhantomBushBlock.LEVEL, 1).setValue(PeacefulMod.Blocks.PhantomBush.get().getAgeProperty(), age), 2);
        level.setBlock(pos, state.setValue(PhantomBushBlock.LEVEL, 0).setValue(PeacefulMod.Blocks.PhantomBush.get().getAgeProperty(), age), 2);
        //System.out.println("~~~ placed hedge at " + pos);
        return true;
    }
    private static int[] ageDistribution = {0, 1, 1, 1, 2, 2, 2, 3};


    private static Set<Block> cachedWhitelist = null;


    public static Set<Block> getWhitelist()
    {
        if (cachedWhitelist != null)
        {
            return cachedWhitelist;
        }
        Set<Block> cachedWhitelist = new HashSet<Block>();
        cachedWhitelist.add(Blocks.END_STONE);
        Block tryBlock = Registry.BLOCK.get(new ResourceLocation("byg:bulbis_phycelium"));
        if (tryBlock != Blocks.AIR)
        {
            cachedWhitelist.add(tryBlock);
        }
        tryBlock = Registry.BLOCK.get(new ResourceLocation("byg:nightshade_phylium"));
        if (tryBlock != Blocks.AIR)
        {
            cachedWhitelist.add(tryBlock);
        }
        return cachedWhitelist;
    }
}
