package moonfather.playablepeaceful_items.items;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LavaPoopItemEntity extends ItemEntity
{
	public LavaPoopItemEntity(EntityType<? extends ItemEntity> entityType, World world)
	{
		super(entityType, world);
		this.lifespan = 60 * 20; // 1 min instead of 5
		this.setPickUpDelay(30);
	}

	public LavaPoopItemEntity(World world)
	{
		super(EntityType.ITEM, world);
		this.lifespan = 60 * 20; // 1 min instead of 5
		this.setPickUpDelay(30);
	}

	public LavaPoopItemEntity(World world, double x, double y, double z, ItemStack itemStack)
	{
		super(world, x, y, z, itemStack);
		this.lifespan = 60 * 20; // 1 min instead of 5
		this.setPickUpDelay(30);
	}

	@Override
	public boolean fireImmune()
	{
		return true;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source)
	{
		if (source.equals(DamageSource.IN_FIRE) || source.equals(DamageSource.ON_FIRE) || source.equals(DamageSource.LAVA))
		{
			return true;
		}
		return super.isInvulnerableTo(source);
	}

	@Override
	public void tick()
	{
		super.tick();
		this.clearFire();
		this.checkExpiration();
	}



	@Override
	public boolean isOnFire()
	{
		return false;
	}

	@Override
	public void setSecondsOnFire(int seconds)
	{
		this.setRemainingFireTicks(0);
	}



	private void checkExpiration()
	{
		ItemStack item = this.getItem();
		if (!this.level.isClientSide && this.lifespan - this.getAge() <= 5)
		{
			int ticksToAdd = net.minecraftforge.event.ForgeEventFactory.onItemExpire(this, item);
			if (ticksToAdd <= 0)
			{
				if (item.getCount() <= 32)
				{
					int count = item.getCount();
					if (count > PeacefulMod.LilypadBlock.getMaxLevel() + 1)
					{
						count = PeacefulMod.LilypadBlock.getMaxLevel() + 1;
					}
					this.tryAddToLilypad(this.blockPosition(), count);
					this.remove();
				}
				else
				{
					int count = PeacefulMod.LilypadBlock.getMaxLevel() + 1;
					this.tryAddToLilypad(this.blockPosition(), count);
					item.shrink(32);
					this.lifespan += 2;
				}
			}
			else
			{
				this.lifespan += ticksToAdd;
			}
		}
	}

	private boolean tryAddToLilypad(BlockPos blockPosition, int stackSize)
	{
		BlockPos.Mutable current = new BlockPos.Mutable(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
		int countExisting = 0; int tx, ty, tz, level;
		for (int dx = 0; dx <= 2; dx++)
		{
			for (int dz = 0; dz <= 2; dz++)
			{
				for (int dy = 0; dy <= 2; dy++)
				{
					tx = dx != 2 ? dx : -1; // step 1: count existing lilypads; increase level of first one found.
					ty = dy != 2 ? dy : -1;
					tz = dz != 2 ? dz : -1;
					current.set(blockPosition.getX() + tx, blockPosition.getY() + ty, blockPosition.getZ() + tz);
					BlockState stateCurrent = this.level.getBlockState(current);
					if (stateCurrent.getBlock().equals(PeacefulMod.LilypadBlock))
					{
						countExisting++;
						level = stateCurrent.getValue(BlockStateProperties.LEVEL);
						if (level < PeacefulMod.LilypadBlock.getMaxLevel())
						{
							level = Math.min(level + stackSize, PeacefulMod.LilypadBlock.getMaxLevel());
							stateCurrent = stateCurrent.setValue(BlockStateProperties.LEVEL, level);
							this.level.setBlockAndUpdate(current, stateCurrent);
							return true;
						}
					}
				}
			}
		}
		if (countExisting > 1)
		{
			return false; // step 2: if we found 2+ max level lilypads, just quit.
		}
		BlockPos.Mutable below = new BlockPos.Mutable(), above = new BlockPos.Mutable();
		for (int dx = 0; dx <= 2; dx++)
		{
			for (int dz = 0; dz <= 2; dz++)
			{
				for (int dy = 0; dy <= 2; dy++)
				{
					tx = dx != 2 ? dx : -1; // step 3: make a new one
					ty = dy != 2 ? dy : -1;
					tz = dz != 2 ? dz : -1;
					current.set(blockPosition.getX() + tx, blockPosition.getY() + ty, blockPosition.getZ() + tz);
					below.set(blockPosition.getX() + tx, blockPosition.getY() + ty - 1, blockPosition.getZ() + tz);
					above.set(blockPosition.getX() + tx, blockPosition.getY() + ty + 1, blockPosition.getZ() + tz);

					BlockState stateCurrent = this.level.getBlockState(current);
					if (stateCurrent.getBlock() == Blocks.LAVA && stateCurrent.getValue(BlockStateProperties.LEVEL) == 0)
					{
						BlockState stateAbove = this.level.getBlockState(above);
						if (stateAbove.isAir())
						{
							this.level.setBlockAndUpdate(above, PeacefulMod.LilypadBlock.defaultBlockState().setValue(BlockStateProperties.LEVEL, stackSize - 1));
							return true;
						}
					}
					else if (stateCurrent.isAir())
					{
						BlockState stateBelow = this.level.getBlockState(below);
						if (stateBelow.isFaceSturdy(this.level, below, Direction.UP))
						{
							this.level.setBlockAndUpdate(current, PeacefulMod.LilypadBlock.defaultBlockState().setValue(BlockStateProperties.LEVEL, stackSize - 1));
							return true;
						}
					}
				}
			}
		}
		return  false;
	}
}
