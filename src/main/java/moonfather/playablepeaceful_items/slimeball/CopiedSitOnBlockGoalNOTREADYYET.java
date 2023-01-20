package moonfather.playablepeaceful_items.slimeball;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CopiedSitOnBlockGoalNOTREADYYET extends MoveToBlockGoal {
    private final CuteSlimeEntity slime;

    public CopiedSitOnBlockGoalNOTREADYYET(CuteSlimeEntity p_25149_, double p_25150_) {
        super(new Cat(null, null), p_25150_, 8);         /////////////////////!!!!!!!!!!!!!!!!!!!!!!!11
        this.slime = p_25149_;
    }

    public boolean canUse() {
        return ! this.slime.isSittingOnFavoriteBlock() && this.slime.getCooldownFromFavoriteBlock() == 0 && super.canUse();
    }

    public void start() {
        super.start();
        this.slime.setSittingOnFavoriteBlock(true);
    }

    public void stop() {
        super.stop();
        this.slime.setSittingOnFavoriteBlock(false);
        this.slime.setCooldownFromFavoriteBlock(120*20);
    }

    public void tick() {
        super.tick();
        if (! this.slime.isSittingOnFavoriteBlock() && this.slime.getCooldownFromFavoriteBlock() > 0)
        {
            this.slime.setCooldownFromFavoriteBlock(this.slime.getCooldownFromFavoriteBlock() - 1);
        }
    }

    protected boolean isValidTarget(LevelReader levelReader, BlockPos pos) {
        if (!levelReader.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockstate = levelReader.getBlockState(pos);
            if (blockstate.is(Blocks.LILY_PAD) && this.slime.isDarkGreen()) {
                return true;
            }
            else if (blockstate.is(WORKBENCH) && this.slime.isYellow()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private static final TagKey<Block> WORKBENCH = null;
}
