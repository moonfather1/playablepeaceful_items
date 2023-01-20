package moonfather.playablepeaceful_items.cotton;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class CottonBushBlock extends CropBlock
{// novi plan://0==tek unapredjen ili obran; 1,2== rastemo; 3==spremno za branje
	private static final VoxelShape box = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 24.0D, 14.0D);
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { box, box, box, box };


	public CottonBushBlock()
	{
		super(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().sound(SoundType.CROP).strength(2.5f, 1.1f));
	}



	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
	{
		if (entity.getBbHeight() >= 0.75f)
		{
			entity.makeStuckInBlock(state, new Vec3(0.80D, 0.65D, 0.80D));
		}
	}



	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult bhr)
	{
		int age = state.getValue(this.getAgeProperty());
		if (age < 3 && player.getItemInHand(hand).getItem() == Items.BONE_MEAL)
		{
			return InteractionResult.PASS;
		}
		else if (age == 3)
		{
			if (world.getDifficulty() == Difficulty.PEACEFUL || OptionsHolder.COMMON.CottonBushActivatableOnlyOnPeacefulDifficulty.get() == false)
			{
				int amount = (int) Math.floor(Math.sqrt(world.random.nextInt(9) + 1)); // 1-3
				popResource(world, pos, new ItemStack(PeacefulMod.Items.CottonBoll.get(), amount));
				world.playSound((Player) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
				world.setBlock(pos, state.setValue(this.getAgeProperty(), Integer.valueOf(0)), 2);
				if (world.random.nextInt(100) < 20)
				{
					popResource(world, pos, new ItemStack(PeacefulMod.Items.CottonSeeds.get(), 1));
				}
				return InteractionResult.sidedSuccess(world.isClientSide);
			}
			else
			{
				player.displayClientMessage(new TranslatableComponent("block.pp.cotton.notharvestable"), true);
				return InteractionResult.FAIL;
			}
		}
		else
		{
			return super.use(state, world, pos, player, hand, bhr);
		}
	}



	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.AGE_3);
	}


	@Override
	public int getMaxAge()
	{
		return 3;
	}



	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random)
	{
		if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		int age = this.getAge(state);
		if (age < this.getMaxAge())
		{
			if (world.getRawBrightness(pos, 0) >= 9)
			{
				double valueToCheckPercentageAgainst = 35.0d; // we start at 35% - every 3 ticks on farmland
				if (!(world.getBlockState(pos.below()).getBlock() instanceof FarmBlock))
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
	protected int getBonemealAgeIncrease(Level p_185529_1_)
	{
		return 1;
	}



	@Override
	protected ItemLike getBaseSeedId()
	{
		return PeacefulMod.Items.CottonSeeds.get();
	}



	@Override
	public IntegerProperty getAgeProperty()
	{
		return BlockStateProperties.AGE_3;
	}



	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
	{
		return (world.getRawBrightness(pos, 0) >= 8 || world.canSeeSky(pos)) && superCanSurvive(state, world, pos);
	}



	private boolean superCanSurvive(BlockState state, LevelReader world, BlockPos pos)
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
	protected boolean mayPlaceOn(BlockState ground, BlockGetter world, BlockPos pos)
	{
		return CottonSeedlingBlock.locationIsValidDirt(ground) && CottonSeedlingBlock.locationHasAirClearance(world, pos.above(), false);
	}



	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext something)
	{
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}
}
