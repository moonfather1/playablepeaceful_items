package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.Random;

public class CottonSeedlingBlock extends CropsBlock
{// novi plan://0==tek zasadili; 1,2...6== rastemo; 7==spremno za unapredjenje
	private static final VoxelShape box1 = Block.box(5.0D, 0.0D, 5.0D, 11.0D,  5.0D, 11.0D);
	private static final VoxelShape box2 = Block.box(4.0D, 0.0D, 4.0D, 12.0D,  8.0D, 12.0D);
	private static final VoxelShape box3 = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { box1, box2, box2, box2, box3, box3, box3, box3 };



	public CottonSeedlingBlock()
	{
		super(Properties.of(Material.PLANT).noCollission().randomTicks().sound(SoundType.CROP).strength(2.5f, 1.1f));
	}



	@Override
	public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
	{
		if (entity.getBbHeight() >= 0.75f)
		{
			if (state.getValue(CropsBlock.AGE) < 4)
			{
				entity.makeStuckInBlock(state, new Vector3d(0.95D, 0.95D, 0.95D));
			}
			else
			{
				entity.makeStuckInBlock(state, new Vector3d(0.90D, 0.90D, 0.90D));
			}
		}
	}



	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (world.getRawBrightness(pos, 0) >= 9)
		{
			int age = this.getAge(state);
			boolean growThisTick = random.nextInt(100) < 35.0 * OptionsHolder.COMMON.CottonGrowthMultiplier.get();
			if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, growThisTick))
			{
				if (age < this.getMaxAge() - 1)
				{
					// stages 0 to 5
					world.setBlock(pos, this.getStateForAge(age + 1), 2);
				}
				else
				{
					// stages 6 and 7
					world.setBlock(pos, PeacefulMod.CottonBush.getStateForAge(0), 2);
					// six moved here as a compatibility with harvester machines - we don't want them returning us from 7 to 0.
				}

				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
			}
		}
	}



	@Override
	protected int getBonemealAgeIncrease(World p_185529_1_)
	{
		return 1;
	}



	@Override
	protected IItemProvider getBaseSeedId()
	{
		return PeacefulMod.CottonSeeds;
	}



	@Override
	public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState state, boolean deZnam)
	{
		return true;
	}



	@Override
	public void performBonemeal(ServerWorld world, Random random, BlockPos pos, BlockState state)
	{
		if (!this.isMaxAge(state))
		{
			this.growCrops(world, pos, state);
		}
		else
		{
			world.setBlock(pos, PeacefulMod.CottonBush.getStateForAge(0), 2);
		}
	}



	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
	{
		return (world.getRawBrightness(pos, 0) >= 8 || world.canSeeSky(pos)) && superCanSurvive(state, world, pos);
	}



	private boolean superCanSurvive(BlockState state, IWorldReader world, BlockPos pos)
	{
		BlockPos below = pos.below();
		if (state.getBlock() == this)
		{
			//Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return world.getBlockState(below).canSustainPlant(world, below, Direction.UP, this) && CottonSeedlingBlock.locationHasAirClearance(world, pos, false);
		}
		return this.mayPlaceOn(world.getBlockState(below), world, below);
	}



	@Override
	protected boolean mayPlaceOn(BlockState ground, IBlockReader world, BlockPos pos)
	{
		return CottonSeedlingBlock.locationIsValidDirt(ground) && CottonSeedlingBlock.locationHasAirClearance(world, pos.above(), false);
	}



	public static boolean locationIsValidDirt(IBlockReader world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		return locationIsValidDirt(state);
	}



	public static boolean locationIsValidDirt(BlockState state)
	{
		return state.is(Blocks.FARMLAND) || state.getBlock().is(Tags.Blocks.DIRT);
	}



	public static boolean locationHasAirClearance(IBlockReader world, BlockPos pos, boolean insistOnEmptyForSeeds)
	{
		return (world.getBlockState(pos).isAir()
					|| (!insistOnEmptyForSeeds && world.getBlockState(pos).getBlock() instanceof CottonSeedlingBlock)
					|| (!insistOnEmptyForSeeds && world.getBlockState(pos).getBlock() instanceof CottonBushBlock))
				&& world.getBlockState(pos.above()).isAir()
				&& world.getBlockState(pos.above(2)).isAir();
	}



	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext something)
	{
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}
}
