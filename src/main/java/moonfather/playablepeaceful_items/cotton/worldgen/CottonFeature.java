package moonfather.playablepeaceful_items.cotton.worldgen;

import com.mojang.serialization.Codec;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CottonFeature extends SimpleFeature
{
    public CottonFeature(Codec<SimpleFeatureConfiguration> codec)
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
        if (! state.is(Blocks.GRASS_BLOCK) && ! state.is(BlockTags.DIRT))
        {
            return false;
        }
        state = level.getBlockState(pos.above());
        if (! state.isAir() && ! state.getMaterial().isReplaceable())
        {
            return false;
        }
        if (! state.isAir())
        {
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 0);
        }
        int age = level.getRandom().nextInt(8);
        if (age > 3) age = 1; // half the time 1, half the time other values.
        level.setBlock(pos, PeacefulMod.Blocks.CottonBush.get().getStateForAge(age), 3);
        return true;
    }
}
