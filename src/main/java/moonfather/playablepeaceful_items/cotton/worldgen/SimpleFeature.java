package moonfather.playablepeaceful_items.cotton.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SimpleFeature extends Feature<SimpleFeatureConfiguration>
{
    public SimpleFeature(Codec<SimpleFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleFeatureConfiguration> featurePlaceContext)
    {
        SimpleFeatureConfiguration config = featurePlaceContext.config();
        boolean result = false;
        int dx, dy, dz;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int count = 0;
        for (int i = 0; i < config.tries(); i++)
        {
            dx = featurePlaceContext.random().nextInt(config.xzSpread() * 2 + 1) - config.xzSpread(); //-spread to +spread
            dz = featurePlaceContext.random().nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            dy = featurePlaceContext.random().nextInt(config.ySpread() * 2 + 1) - config.ySpread(); //-spread to +spread
            pos.set(featurePlaceContext.origin().getX() + dx, featurePlaceContext.origin().getY() + dy, featurePlaceContext.origin().getZ() + dz);
            if (this.placeCore(featurePlaceContext.level(), pos))
            {
                result = true;
                count += 1;
                if (count >= config.counterUpperBound())
                {
                    break;
                }
                if (count >= config.counterLowerBound())
                {
                    if (featurePlaceContext.random().nextInt(100) < config.percentChanceToStopAtLower())
                    {
                        break;
                    }
                }
            }
        }
        return result;
    }

    protected boolean placeCore(WorldGenLevel level, BlockPos pos)
    {
        return false; // override me
    }
}
