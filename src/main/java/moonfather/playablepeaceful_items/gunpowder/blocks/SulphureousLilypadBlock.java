package moonfather.playablepeaceful_items.gunpowder.blocks;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SulphureousLilypadBlock extends Block
{
	protected static final VoxelShape AABB_1 = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.2D, 13.0D);
	protected static final VoxelShape AABB_2 = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.2D, 14.0D);
	protected static final VoxelShape AABB_3 = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.2D, 15.0D);

	public SulphureousLilypadBlock()
	{
		super(Properties.of(Material.PLANT).sound(SoundType.CROP).strength(0.2f, 2.2f).noOcclusion().instabreak());
		this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.LEVEL, 0));
	}



	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
	{
		super.entityInside(state, world, pos, entity);
		if (world instanceof ServerLevel && entity instanceof Boat)
		{
			world.destroyBlock(new BlockPos(pos), true, entity);
		}
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_)
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
	public VoxelShape getCollisionShape(BlockState state, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_)
	{
		int level = state.getValue(BlockStateProperties.LEVEL);
		if (level >= 10)
		{
			return AABB_3;
		}
		else
		{
			return Shapes.empty();
		}
	}



	@Override
	public VoxelShape getVisualShape(BlockState p_230322_1_, BlockGetter p_230322_2_, BlockPos p_230322_3_, CollisionContext p_230322_4_)
	{
		return this.getShape(p_230322_1_, p_230322_2_, p_230322_3_, p_230322_4_);
	}



	protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos)
	{
		FluidState fluidstate = world.getFluidState(pos);
		FluidState fluidstate1 = world.getFluidState(pos.above());
		return (fluidstate.getType() == Fluids.LAVA || state.isFaceSturdy(world, pos, Direction.UP)) && fluidstate1.getType() == Fluids.EMPTY;
	}



	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.LEVEL);
	}


	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
	{
		return new ItemStack(PeacefulMod.Items.LavaSpritePoop.get());
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
			world.destroyBlock(pos, true);
		}
		else
		{
			super.neighborChanged(state, world, pos, block, pos2, something);
		}
	}
}
