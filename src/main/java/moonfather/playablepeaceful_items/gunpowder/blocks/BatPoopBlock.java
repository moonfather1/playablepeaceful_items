package moonfather.playablepeaceful_items.gunpowder.blocks;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.List;

public class BatPoopBlock extends SulphureousLilypadBlock
{
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
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
	public VoxelShape getCollisionShape(BlockState state, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_)
	{
		return VoxelShapes.empty();
	}



	protected boolean mayPlaceOn(BlockState state, IBlockReader world, BlockPos pos)
	{
		FluidState fluidstate1 = world.getFluidState(pos.above());
		return state.isFaceSturdy(world, pos, Direction.UP) && fluidstate1.getType() == Fluids.EMPTY;
	}


	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(PeacefulMod.BatPoop);
	}



	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_)
	{
		return super.getDrops(p_220076_1_, p_220076_2_);
	}



	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos pos2, boolean something)
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
