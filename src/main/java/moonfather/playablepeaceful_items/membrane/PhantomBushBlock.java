package moonfather.playablepeaceful_items.membrane;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.cotton.CottonBushBlock;
import moonfather.playablepeaceful_items.cotton.CottonSeedlingBlock;
import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class PhantomBushBlock extends CropsBlock
{
	private static final VoxelShape box_narrow = Block.box( 2.0D,  0.0D,  2.0D, 14.0D,  8.0D, 14.0D);
	private static final VoxelShape box_wider1 = Block.box(-4.0D,  8.0D, -4.0D, 20.0D, 24.0D, 20.0D);

	private static final VoxelShape box_widerS = Block.box(-4.0D, -8.0D, -4.0D, 20.0D, 16.0D, 20.0D);

	private static final VoxelShape level0_aabb = VoxelShapes.or(box_narrow, box_wider1);

	public PhantomBushBlock()
	{
		super(Properties.of(Material.PLANT).randomTicks().sound(SoundType.CROP).strength(-1.0F, 3600000.0F).noDrops());
		AnvilBlock a;
	}



	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rtr)
	{
		if (state.getValue(this.getAgeProperty()) == 3)
		{
			if (world.getDifficulty() == Difficulty.PEACEFUL || OptionsHolder.COMMON.CottonBushActivatableOnlyOnPeacefulDifficulty.get() == false)
			{
				if (!world.isClientSide)
				{
					int amount = 1 + world.random.nextInt(4) / 3; // 1-2
					popResource(world, pos, new ItemStack(Items.PHANTOM_MEMBRANE, amount));
					world.setBlock(pos, state.setValue(this.getAgeProperty(), Integer.valueOf(0)), 2);
					int otherPartLevel = state.getValue(PhantomBushBlock.LEVEL) == 0 ? 1 : 0;
					int offset = -2 + 4 * otherPartLevel;
					world.setBlock(pos.above(offset), state.setValue(this.getAgeProperty(), Integer.valueOf(0)).setValue(PhantomBushBlock.LEVEL, otherPartLevel), 2);
				}
				world.playSound((PlayerEntity) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
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
	}/////////////////////!!!!!!!!!!!!!!!!//////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 1);



	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
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
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (state.getValue(LEVEL) > 0)  return; // only bottom part ticks
		int age = this.getAge(state);
		if (age < this.getMaxAge())
		{
			boolean growThisTick = random.nextInt(100) < 20;
			if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, growThisTick))
			{
				world.setBlock(pos.above(2), this.getStateForAge(age + 1).setValue(PhantomBushBlock.LEVEL, 1), 2);
				world.setBlock(pos, this.getStateForAge(age + 1), 2);
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
			}
		}
		if (world.getDifficulty() != Difficulty.PEACEFUL && world.random.nextInt(10) == 4)
		{
			List<EndermiteEntity> list = world.getEntitiesOfClass(EndermiteEntity.class, new AxisAlignedBB(pos).inflate(40.0D, 8.0D, 40.0D));
			if (list.size() == 0)
			{
				//EntityType.ENDERMITE.spawn(world, null, null, pos.east(2), SpawnReason.NATURAL,false, false);
				EndermiteEntity endermiteentity = EntityType.ENDERMITE.create(world);
				endermiteentity.setPlayerSpawned(true);
				endermiteentity.moveTo(pos.east(), 0, 0);
				world.addFreshEntity(endermiteentity);
			}
		}
	}



	@Override
	protected int getBonemealAgeIncrease(World p_185529_1_)
	{
		return 0;
	}



	@Override
	public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_)
	{
		return false;
	}



	@Override
	protected IItemProvider getBaseSeedId()
	{
		return Items.PHANTOM_MEMBRANE;
	}



	@Override
	public IntegerProperty getAgeProperty()
	{
		return BlockStateProperties.AGE_3;
	}



	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
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
	protected boolean mayPlaceOn(BlockState ground, IBlockReader world, BlockPos pos)
	{
	//	return ground.getBlock().equals(Blocks.END_STONE) && PhantomBushBlock.locationHasAirClearance(world, pos.above(), false);
		return true;
	}



	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos otherPos, boolean something)
	{
		if (state.getValue(LEVEL) == 0)
		{
			if (!this.isValidBaseBlock(world.getBlockState(pos.below()).getBlock()) || !world.getBlockState(pos.above()).isAir())
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(pos.above(2), Blocks.AIR.defaultBlockState());
				return;
			}
		}
		else
		{
			if (!world.getBlockState(pos.below()).isAir() || !world.getBlockState(pos.above()).isAir())
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(pos.below(2), Blocks.AIR.defaultBlockState());
			}
		}
		super.neighborChanged(state, world, pos, block, otherPos, something);

	}



	private boolean isValidBaseBlock(Block block)
	{
		return SimpleCropPatchGenerationForPhantomBush.getWhitelist().contains(block);
	}



	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext something)
	{
		return state.getValue(PhantomBushBlock.LEVEL) == 0 ? level0_aabb : box_widerS;
	}



	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
	{
		if (state.getValue(LEVEL) == 0)
		{
			if (!(world.getBlockState(pos.above(2)).getBlock() instanceof PhantomBushBlock))
			{
				world.setBlock(pos.above(1), Blocks.AIR.defaultBlockState(), 2);
				world.setBlock(pos.above(2), this.getStateForAge(state.getValue(this.getAgeProperty())).setValue(PhantomBushBlock.LEVEL, 1), 2);
				world.setBlock(pos.above(3), Blocks.AIR.defaultBlockState(), 2);
			}
		}
		super.setPlacedBy(world, pos, state, entity, stack);
	}
}
