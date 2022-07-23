package moonfather.playablepeaceful_items.gunpowder.sprite;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;

public class SpriteEntity extends BatEntity
{
	private BlockPos homePosition = null; // lava; we return to it if too far.
	private int notAboveLavaCounter = 0;  // how many big-ticks away from lava...
	private BlockPos previousPos1 = null, previousPos2 = null; // checking for sprite being stuck.

	public SpriteEntity(EntityType<? extends BatEntity> et, World world)
	{
		super(et, world);
	}

	public SpriteEntity(World world)
	{
		super(RegistrationManager.SPRITE_HOLDER, world);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes()
	{
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
	}



	public SoundEvent getAmbientSound()
	{
		return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.TURTLE_AMBIENT_LAND;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_)
	{
		return SoundEvents.DOLPHIN_HURT;
	}

	protected SoundEvent getDeathSound()
	{
		return null;
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
	public boolean fireImmune()
	{
		return true;
	}

	@Override
	protected void actuallyHurt(DamageSource source, float p_70665_2_)
	{
		if (source.equals(DamageSource.IN_WALL))
		{
			if (this.homePosition != null)
			{
				this.teleportTo(this.homePosition);
				this.setHealth(this.getMaxHealth());
			}
			else
			{
				BlockPos.Mutable pos = new BlockPos.Mutable();
				for (int dy = -1; dy <= 0; dy++) // below first
				{
					for (int dz = -2; dz <= 2; dz++)
					{
						for (int dx = -2; dx <= 2; dx++)
						{
							pos.set(this.blockPosition().getX() + dx, this.blockPosition().getY() + dy, this.blockPosition().getZ() + dz);
							if (this.level.getBlockState(pos).isAir())
							{
								this.teleportTo(pos);
								this.setHealth(this.getMaxHealth());
								super.actuallyHurt(source, p_70665_2_);
								return;
							}
						}
					}
				}
			}
		}
		//System.out.println("!!~~~!!~~~~ lava sprite hurt by " + source.toString() + " HP==" + this.getHealth());
		super.actuallyHurt(source, p_70665_2_);
	}

	@Override
	public void tick()
	{
		this.clearFire();
		super.tick();
		if (this.tickCount % 60 == 0 || (this.tickCount < 60 && this.tickCount % 5 == 0)) // every three seconds
		{
			if (this.homePosition == null)
			{
				this.tryToAssignHomePosition();
			}
			else
			{
				if (this.tickCount % 12*20 == 0)
				{
					this.tryToAssignHomePosition();
				}
				else if (this.notAboveLava())
				{
					this.notAboveLavaCounter += 1;
				}
				else
				{
					this.notAboveLavaCounter = 0;
				}
			}
			if (this.homePosition != null && this.notAboveLavaCounter > 10)
			{
				this.teleportTo(this.homePosition);
				this.notAboveLavaCounter = 0;
			}
			if (this.tickCount > 120)
			{
				if (this.homePosition != null && this.previousPos2 != null && this.previousPos2.equals(this.previousPos1) && this.previousPos1.equals(this.blockPosition()) && !this.previousPos2.equals(this.homePosition))
				{
					this.teleportTo(this.homePosition);
					this.notAboveLavaCounter = 0;
					//System.out.println("-----------stuck moved");
				}
				else
				{
					this.previousPos2 = this.previousPos1;
					this.previousPos1 = this.blockPosition();
				}
			}
		}
		this.clearFire();
	}

	private void tryToAssignHomePosition()
	{
		BlockPos.Mutable current = new BlockPos.Mutable(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
		BlockState currentState = this.level.getBlockState(current);
		while (currentState.isAir() && current.getY() > 0)
		{
			current.setY(current.getY() - 1);
			currentState = this.level.getBlockState(current);
		}
		if (currentState.getMaterial() == Material.LAVA)
		{
			this.homePosition = new BlockPos(current);
			this.notAboveLavaCounter = 0;
			//System.out.println("-----------tryToAssignHomePosition: succeeded"); return;
		}
		//System.out.println("-----------tryToAssignHomePosition: failed");
	}

	private boolean notAboveLava()
	{
		BlockPos.Mutable current = new BlockPos.Mutable(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
		BlockState currentState = this.level.getBlockState(current);
		while (currentState.isAir() && current.getY() > 0)
		{
			current.setY(current.getY() - 1);
			currentState = this.level.getBlockState(current);
		}
		return currentState.getMaterial() != Material.LAVA;
	}

	public void teleportTo(double x, double y, double z)
	{
		if (this.level instanceof ServerWorld)
		{
			ServerWorld serverworld = (ServerWorld)this.level;
			this.moveTo(x, y, z, this.yRot, this.xRot);
			serverworld.updateChunkPos(this);
			this.forceChunkAddition = true;
		}
	}

	public void teleportTo(BlockPos pos)
	{
		this.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}



	@Override
	public void addAdditionalSaveData(CompoundNBT nbt)
	{
		super.addAdditionalSaveData(nbt);
		if (this.homePosition != null)
		{
			nbt.putInt("HomeX", this.homePosition.getX());
			nbt.putInt("HomeY", this.homePosition.getY());
			nbt.putInt("HomeZ", this.homePosition.getZ());
			TickEvent.PlayerTickEvent e;
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt)
	{
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("HomeX"))
		{
			this.homePosition = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
		}
	}
}
