package moonfather.playablepeaceful_items.cotton.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SimpleFeatureConfiguration implements FeatureConfiguration
{
    public static final SimpleFeatureConfiguration INSTANCE = new SimpleFeatureConfiguration();
    public static final Codec<SimpleFeatureConfiguration> CODEC = Codec.unit(() -> {
        return SimpleFeatureConfiguration.INSTANCE;
    });

    public int tries() {
        return 64; //1+
    }

    public int xzSpread() {
        return 4; //0+
    }

    public int ySpread()
    {
        return 3; //0+
    }

    public int counterUpperBound()
    {
        return 3; //2to3
    }
    public int counterLowerBound()
    {
        return 2; //2to3
    }
    public int percentChanceToStopAtLower()
    {
        return 20; //20% to stop each time between lower and upper bound (which is only once now)
    }
}
