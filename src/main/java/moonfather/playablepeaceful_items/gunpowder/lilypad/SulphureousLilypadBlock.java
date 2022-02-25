package moonfather.playablepeaceful_items.gunpowder.lilypad;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SulphureousLilypadBlock extends Block
{
	protected static final VoxelShape AABB_1 = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.2D, 13.0D);
	protected static final VoxelShape AABB_2 = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.2D, 14.0D);
	protected static final VoxelShape AABB_3 = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.2D, 15.0D);

	public SulphureousLilypadBlock()
	{
		super(Properties.of(Material.PLANT).sound(SoundType.CROP).strength(0.2f, 2.2f).noOcclusion());
		this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.LEVEL, 0));
	}



	public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
	{
		super.entityInside(state, world, pos, entity);
		if (world instanceof ServerWorld && entity instanceof BoatEntity)
		{
			world.destroyBlock(new BlockPos(pos), true, entity);
		}
	}



	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
	{
		int level = state.getValue(BlockStateProperties.LEVEL);
		if (level <= 3)
		{
			return AABB_1;
		}
		else if (level <= 9)
		{
			return AABB_2;
		}
		else
		{
			return AABB_3;
		}
	}



	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_)
	{
		int level = state.getValue(BlockStateProperties.LEVEL);
		if (level >= 10)
		{
			return AABB_3;
		}
		else
		{
			return VoxelShapes.empty();
		}
	}



	@Override
	public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_)
	{
		return this.getShape(p_230322_1_, p_230322_2_, p_230322_3_, p_230322_4_);
	}



	protected boolean mayPlaceOn(BlockState state, IBlockReader world, BlockPos pos)
	{
		FluidState fluidstate = world.getFluidState(pos);
		FluidState fluidstate1 = world.getFluidState(pos.above());
		return (fluidstate.getType() == Fluids.LAVA || state.isFaceSturdy(world, pos, Direction.UP)) && fluidstate1.getType() == Fluids.EMPTY;
	}



	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.LEVEL);
	}



	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(PeacefulMod.LavaSpritePoop);
	}



	public int getMaxLevel()
	{
		return 15;
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
			else if (stateBelow.getBlock() == Blocks.LAVA && stateBelow.getValue(BlockStateProperties.LEVEL) == 0)
			{
				//lava, leave it
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
			int level = state.getValue(BlockStateProperties.LEVEL);
			Block.popResource(world, pos, new ItemStack(PeacefulMod.LavaSpritePoop, level + 1)); // loot tables return nothing. no idea.
			world.destroyBlock(pos, true);
		}
		else
		{
			super.neighborChanged(state, world, pos, block, pos2, something);
		}
	}
}
