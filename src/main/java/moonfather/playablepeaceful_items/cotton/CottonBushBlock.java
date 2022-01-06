package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CottonBushBlock extends CropsBlock
{// novi plan://0==tek unapredjen ili obran; 1,2== rastemo; 3==spremno za branje
	private static final VoxelShape box = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 24.0D, 14.0D);
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { box, box, box, box };


	public CottonBushBlock()
	{
		super(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().sound(SoundType.CROP).strength(2.5f, 1.1f));
	}



	@Override
	public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
	{
		if (entity.getBbHeight() >= 0.75f)
		{
			entity.makeStuckInBlock(state, new Vector3d(0.80D, 0.65D, 0.80D));
		}
	}



	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rtr)
	{
		int age = state.getValue(this.getAgeProperty());
		if (age < 3 && player.getItemInHand(hand).getItem() == Items.BONE_MEAL)
		{
			return ActionResultType.PASS;
		}
		else if (age == 3)
		{
			if (world.getDifficulty() == Difficulty.PEACEFUL || OptionsHolder.COMMON.CottonBushActivatableOnlyOnPeacefulDifficulty.get() == false)
			{
				int amount = (int) Math.floor(Math.sqrt(world.random.nextInt(9) + 1)); // 1-3
				popResource(world, pos, new ItemStack(PeacefulMod.CottonBoll, amount));
				world.playSound((PlayerEntity) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
				world.setBlock(pos, state.setValue(this.getAgeProperty(), Integer.valueOf(0)), 2);
				if (world.random.nextInt(100) < 20)
				{
					popResource(world, pos, new ItemStack(PeacefulMod.CottonSeeds, 1));
				}
				return ActionResultType.sidedSuccess(world.isClientSide);
			}
			else
			{
				player.displayClientMessage(new TranslationTextComponent("block.pp.cotton.notharvestable"), true);
				return ActionResultType.FAIL;
			}
		}
		else
		{
			return super.use(state, world, pos, player, hand, rtr);
		}
	}



	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.AGE_3);
	}



	@Override
	public int getMaxAge()
	{
		return 3;
	}



	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		int age = this.getAge(state);
		if (age < this.getMaxAge())
		{
			if (world.getRawBrightness(pos, 0) >= 9)
			{
				double valueToCheckPercentageAgainst = 35.0d; // we start at 35% - every 3 ticks on farmland
				if (!(world.getBlockState(pos.below()).getBlock() instanceof FarmlandBlock))
				{
					valueToCheckPercentageAgainst = valueToCheckPercentageAgainst / 2; // half for wild cotton (17% or 6 ticks)
				}
				valueToCheckPercentageAgainst = valueToCheckPercentageAgainst * OptionsHolder.COMMON.CottonGrowthMultiplier.get();
				valueToCheckPercentageAgainst = Math.min(valueToCheckPercentageAgainst, 100.0); // not needed really
				valueToCheckPercentageAgainst = Math.max(valueToCheckPercentageAgainst, 0.01); // could have done without this too
				boolean growThisTick = random.nextInt(100) < valueToCheckPercentageAgainst;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, growThisTick))
				{
					world.setBlock(pos, this.getStateForAge(age + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
				}
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
	public IntegerProperty getAgeProperty()
	{
		return BlockStateProperties.AGE_3;
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



	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext something)
	{
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}
}
