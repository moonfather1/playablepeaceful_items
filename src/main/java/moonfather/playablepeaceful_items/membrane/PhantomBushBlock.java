package moonfather.playablepeaceful_items.membrane;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.membrane.worldgen.PhantomHedgeFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class PhantomBushBlock extends CropBlock
{
	private static final VoxelShape box_narrow_bottom = Block.box( 2.0D,  0.0D,  2.0D, 14.0D,  8.0D, 14.0D);
	private static final VoxelShape box_wider_top = Block.box(-4.0D,  8.0D, -4.0D, 20.0D, 16.0D, 20.0D);
	private static final VoxelShape box_widerS = Block.box(-4.0D, 0D, -4.0D, 20.0D, 16.0D, 20.0D);

	private static final VoxelShape level0_aabb = Shapes.or(box_narrow_bottom, box_wider_top);

	private static final VoxelShape box_mini1 = Block.box( -4.0D,  0.0D,  4.0D, 4.0D,  16.0D, 20.0D);
	private static final VoxelShape box_mini2 = Block.box( 4.0D,  0.0D,  12.0D, 20.0D,  16.0D, 20.0D);
	private static final VoxelShape box_mini3 = Block.box( 12.0D,  0.0D,  -4.0D, 20.0D,  16.0D, 12.0D);
	private static final VoxelShape box_mini4 = Block.box( -4.0D,  0.0D,  -4.0D, 12.0D,  16.0D, 4.0D);

	private static final VoxelShape level1_aabb = Shapes.or(box_mini1, box_mini2, box_mini3, box_mini4);
	private static final VoxelShape box_wider_bottom = Block.box(0.0D,  0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	private static final VoxelShape box_narrow_top = Block.box( 2.0D,  12.0D,  2.0D, 14.0D,  16.0D, 14.0D);
	private static final VoxelShape level2_aabb = Shapes.or(box_wider_bottom, box_narrow_top);

	public PhantomBushBlock()
	{
		super(Properties.of(Material.PLANT).randomTicks().sound(SoundType.CROP).strength(-1.0F, 3600000.0F).noDrops());
	}



	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rtr)
	{
		if (state.getValue(this.getAgeProperty()) == 3)
		{
			if (world.getDifficulty() == Difficulty.PEACEFUL || OptionsHolder.COMMON.HedgeBushActivatableOnlyOnPeacefulDifficulty.get() == false)
			{
				if (!world.isClientSide)
				{
					int amount = 1 + world.random.nextInt(4) / 3; // 1-2
					popResource(world, pos, new ItemStack(Items.PHANTOM_MEMBRANE, amount));
					int thisLevel = state.getValue(PhantomBushBlock.LEVEL);
					world.setBlock(pos.above(-thisLevel + 0), state.setValue(this.getAgeProperty(), Integer.valueOf(0)).setValue(PhantomBushBlock.LEVEL, 0), 2);
					world.setBlock(pos.above(-thisLevel + 1), state.setValue(this.getAgeProperty(), Integer.valueOf(0)).setValue(PhantomBushBlock.LEVEL, 1), 2);
					world.setBlock(pos.above(-thisLevel + 2), state.setValue(this.getAgeProperty(), Integer.valueOf(0)).setValue(PhantomBushBlock.LEVEL, 2), 2);
				}
				world.playSound((Player) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
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
			return super.use(state, world, pos, player, hand, rtr);
		}
	}/////////////////////!!!!!!!!!!!!!!!!//////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 2);



	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.AGE_3);
		builder.add(PhantomBushBlock.LEVEL);
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
		if (state.getValue(LEVEL) > 0)  return; // only bottom part ticks
		int age = this.getAge(state);
		if (age < this.getMaxAge())
		{
			boolean growThisTick = random.nextInt(100) < 20;
			if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, growThisTick))
			{
				world.setBlock(pos.above(2), this.getStateForAge(age + 1).setValue(PhantomBushBlock.LEVEL, 2), 3);
				world.setBlock(pos.above(1), this.getStateForAge(age + 1).setValue(PhantomBushBlock.LEVEL, 1), 3);
				world.setBlock(pos, this.getStateForAge(age + 1), 3);
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
			}
		}
		else
		{
			boolean returnToZero = random.nextInt(100) < 10;
			world.setBlock(pos.above(2), this.getStateForAge(0).setValue(PhantomBushBlock.LEVEL, 2), 3);
			world.setBlock(pos.above(1), this.getStateForAge(0).setValue(PhantomBushBlock.LEVEL, 1), 3);
			world.setBlock(pos, this.getStateForAge(0), 3);
		}
		if (world.getDifficulty() != Difficulty.PEACEFUL && world.random.nextInt(20) == 4 && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING))
		{
			List<Endermite> list = world.getEntitiesOfClass(Endermite.class, new AABB(pos).inflate(40.0D, 8.0D, 40.0D));
			if (list.size() == 0)
			{
				//EntityType.ENDERMITE.spawn(world, null, null, pos.east(2), SpawnReason.NATURAL,false, false);
				Endermite endermiteentity = EntityType.ENDERMITE.create(world);
				endermiteentity.setAggressive(true);
				endermiteentity.moveTo(pos.east(), 0, 0);
				world.addFreshEntity(endermiteentity);
			}
		}
	}



	@Override
	protected int getBonemealAgeIncrease(Level p_185529_1_)
	{
		return 0;
	}



	@Override
	public boolean isValidBonemealTarget(BlockGetter p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_)
	{
		return false;
	}



	@Override
	protected ItemLike getBaseSeedId()
	{
		return Items.PHANTOM_MEMBRANE;
	}



	@Override
	public IntegerProperty getAgeProperty()
	{
		return BlockStateProperties.AGE_3;
	}



	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
	{
		return true;
		//return state.getValue(PhantomBushBlock.LEVEL) == 0 && world.getBlockState(pos.below()).getBlock().equals(Blocks.END_STONE) && world.getBlockState(pos.above()).isAir()
		//	|| state.getValue(PhantomBushBlock.LEVEL) == 1 && world.getBlockState(pos.below()).isAir() && world.getBlockState(pos.above()).isAir();
	}



	//private boolean superCanSurvive(BlockState state, IWorldReader world, BlockPos pos)
	//{
	//	BlockPos below = pos.below();
	//	if (state.getBlock() == this)
	//	{
	//		//Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
	//		return world.getBlockState(below).canSustainPlant(world, below, Direction.UP, this) && PhantomBushBlock.locationHasAirClearance(world, pos, false);
	//	}
	//	return this.mayPlaceOn(world.getBlockState(below), world, below);
	//}



	//public static boolean locationHasAirClearance(IBlockReader world, BlockPos pos, boolean insistOnEmptyForSeeds)
	//{
	//	return (world.getBlockState(pos).isAir()
	//			|| (!insistOnEmptyForSeeds && world.getBlockState(pos).getBlock() instanceof PhantomBushBlock))
	//			&& world.getBlockState(pos.above()).isAir()
	//			&& world.getBlockState(pos.above(2)).isAir();
	//}



	@Override
	protected boolean mayPlaceOn(BlockState ground, BlockGetter world, BlockPos pos)
	{
	//	return ground.getBlock().equals(Blocks.END_STONE) && PhantomBushBlock.locationHasAirClearance(world, pos.above(), false);
		return true;
	}



	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos otherPos, boolean something)
	{
		if (state.getValue(LEVEL) == 0)
		{
			if (!this.isValidBaseBlock(world.getBlockState(pos.below()).getBlock()))
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(pos.above(1), Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(pos.above(2), Blocks.AIR.defaultBlockState());
				return;
			}
			if (!world.getBlockState(pos.above()).getBlock().equals(this))
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				if (world.getBlockState(pos.above(2)).getBlock().equals(this))
				{
					world.setBlockAndUpdate(pos.above(2), Blocks.AIR.defaultBlockState());
				}
			}
		}
		else if (state.getValue(LEVEL) == 1)
		{
			if (!world.getBlockState(pos.above()).getBlock().equals(this))
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				if (world.getBlockState(pos.below()).getBlock().equals(this))
				{
					world.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
				}
			}
			if (!world.getBlockState(pos.below()).getBlock().equals(this))
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				if (world.getBlockState(pos.above()).getBlock().equals(this))
				{
					world.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
				}
			}
		}
		else if (state.getValue(LEVEL) == 2)
		{
			if (!world.getBlockState(pos.below()).getBlock().equals(this))
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				if (world.getBlockState(pos.below(2)).getBlock().equals(this))
				{
					world.setBlockAndUpdate(pos.below(2), Blocks.AIR.defaultBlockState());
				}
			}
		}
		super.neighborChanged(state, world, pos, block, otherPos, something);
	}



	private boolean isValidBaseBlock(Block block)
	{
		return PhantomHedgeFeature.getWhitelist().contains(block);
	}



	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext something)
	{
		int level = state.getValue(PhantomBushBlock.LEVEL);
		switch (level)
		{
			case 0: return level0_aabb;
			case 1: return level1_aabb;
			case 2: return level2_aabb;
		}
		return box_widerS;
	}



	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
	{
		if (state.getValue(LEVEL) == 0)
		{
			if (!(world.getBlockState(pos.above(2)).getBlock() instanceof PhantomBushBlock))
			{
				world.setBlock(pos.above(1), this.getStateForAge(state.getValue(this.getAgeProperty())).setValue(PhantomBushBlock.LEVEL, 1), 2);
				world.setBlock(pos.above(2), this.getStateForAge(state.getValue(this.getAgeProperty())).setValue(PhantomBushBlock.LEVEL, 2), 2);
				world.setBlock(pos.above(3), Blocks.AIR.defaultBlockState(), 2);
			}
		}
		super.setPlacedBy(world, pos, state, entity, stack);
	}
}
