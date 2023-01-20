package moonfather.playablepeaceful_items.gunpowder.blocks;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class BatPoopBlock extends SulphureousLilypadBlock
{
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_)
	{
		int level = state.getValue(BlockStateProperties.LEVEL);
		if (level <= 3)
		{
			return AABB_1;
		}
		else
		{
			return AABB_2;
		}
	}



	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_)
	{
		return Shapes.empty();
	}



	protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos)
	{
		FluidState fluidstate1 = world.getFluidState(pos.above());
		return state.isFaceSturdy(world, pos, Direction.UP) && fluidstate1.getType() == Fluids.EMPTY;
	}



	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
	{
		return new ItemStack(PeacefulMod.Items.BatPoop.get());
	}



	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_)
	{
		return super.getDrops(p_220076_1_, p_220076_2_);
	}



	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos pos2, boolean something)
	{
		// stupid forge, why are canSurvive and onNeighborChange useless?
		boolean breakIt = false;
		if (pos.getY() == pos2.getY() + 1)
		{
			BlockState stateBelow = world.getBlockState(pos2);
			if (stateBelow.isFaceSturdy(world, pos2, Direction.UP))
			{
				//sturdy, leave it
			}
			else
			{
				breakIt = true; // unsupported below, break it
			}
		}
		else if (pos.getY() == pos2.getY() - 1 && world.getFluidState(pos2).getType() != Fluids.EMPTY)
		{
			breakIt = true;  // fluid above, break it
		}
		else
		{
			// unaffected, leave it
		}
		if (breakIt)
		{
			world.destroyBlock(pos, true);
		}
		else
		{
			super.neighborChanged(state, world, pos, block, pos2, something);
		}
	}
}
