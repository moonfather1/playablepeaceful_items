package moonfather.playablepeaceful_items.gunpowder.sprite;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class SpriteEntity extends Bat
{
	public BlockPos homePosition = null; // lava; we return to it if too far.                /////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private int notAboveLavaCounter = 0;  // how many big-ticks away from lava...
	private BlockPos previousPos1 = null, previousPos2 = null; // checking for sprite being stuck.

	public SpriteEntity(EntityType<? extends Bat> et, Level world)
	{
		super(et, world);
	}

	public SpriteEntity(Level world)
	{
		super(RegistrationManager.SPRITE.get(), world);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
	}

	public static float GetScale() { return 0.2f; }


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
	protected void actuallyHurt(DamageSource source, float damage)
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
				BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
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
								if (this.tickCount <= 200)
								{
									damage = 0;
								}
								super.actuallyHurt(source, damage);
								return;
							}
						}
					}
				}
			}
		}
		System.out.println("!!~~~!!~~~~ lava sprite hurt by " + source.toString() + " HP==" + this.getHealth());
		super.actuallyHurt(source, damage);
	}


	private int ageInSecondsLastDone = -1;
	@Override
	public void tick()
	{
		this.clearFire();
		super.tick();
		int ageInSecondsNew = this.tickCount / 20;
		if (this.ageInSecondsLastDone < 0)
		{
			this.ageInSecondsLastDone = ageInSecondsNew; // we don't store in save file
		}
		if (ageInSecondsNew < 3 && this.tickCount % 5 == 0)
		{
			if (this.homePosition == null)
			{
				this.tryToAssignHomePosition();
			}
		}
		if (ageInSecondsNew >= this.ageInSecondsLastDone + 3)  // 3 seconds between iterations
		{  // main loop
			this.ageInSecondsLastDone = ageInSecondsNew;
			this.validateHomePosition();
			if (this.homePosition == null)
			{
				this.tryToAssignHomePosition();
			}
			else
			{
				if (ageInSecondsNew % 12 == 0)
				{
					this.tryToAssignHomePosition();
				}
				else if (this.notAboveLava())
				{
					this.notAboveLavaCounter += 1;
					System.out.println("-----------notAboveLavaCounter==" + this.notAboveLavaCounter);
				}
				else
				{
					this.notAboveLavaCounter = 0;
					System.out.println("-----------notAboveLavaCounter <- 0");
				}
			}
			if (this.homePosition != null && this.notAboveLavaCounter > 10) // back to home
			{
				if (this.homePosition.distSqr(this.position()) > 100*100)
				{
					this.homePosition = null; // support for bug-nets and other relocation
				}
				else
				{
					this.teleportTo(this.homePosition);
					this.notAboveLavaCounter = 0;
					System.out.println("-----------notAboveLavaCounter==" + this.notAboveLavaCounter + "   TELEPORTING");
				}
			}
			if (ageInSecondsNew > 6) //anti-corner
			{
				if (this.homePosition != null && this.previousPos2 != null && this.previousPos1 != null && this.previousPos2.equals(this.previousPos1) && this.previousPos1.equals(this.blockPosition()) && !this.previousPos2.equals(this.homePosition))
				{
					this.teleportTo(this.homePosition);
					this.notAboveLavaCounter = 0;
					this.previousPos2 = this.previousPos1 = null;
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


	/*public void tick1()
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
					System.out.println("-----------notAboveLavaCounter==" + this.notAboveLavaCounter);
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
				System.out.println("-----------notAboveLavaCounter==" + this.notAboveLavaCounter + "   TELEPORTING");
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
	}*/

	private void tryToAssignHomePosition()
	{
		BlockPos.MutableBlockPos current = new BlockPos.MutableBlockPos(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
		BlockState currentState = this.level.getBlockState(current);
		while (currentState.isAir() && current.getY() > this.level.getMinBuildHeight())
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
		BlockPos.MutableBlockPos current = new BlockPos.MutableBlockPos(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
		BlockState currentState = this.level.getBlockState(current);
		while (currentState.isAir() && current.getY() > this.level.getMinBuildHeight())
		{
			current.setY(current.getY() - 1);
			currentState = this.level.getBlockState(current);
		}
		return currentState.getMaterial() != Material.LAVA;
	}

	public void teleportTo(double x, double y, double z)
	{
		Vec3 target = new Vec3(x, y, z);
		double diag = this.position().distanceTo(target);
		double sx = (target.x - this.position().x) / diag;
		double sy = (target.y - this.position().y) / diag;
		double sz = (target.z - this.position().z) / diag;
		for (int i = 0; i < 12; i++)
		{
			this.level.addParticle(ParticleTypes.SMALL_FLAME, this.getX(), this.getY(), this.getZ(), sx, sy, sz);
		}
		if (this.level instanceof ServerLevel)
		{
			ServerLevel serverworld = (ServerLevel)this.level;
			this.moveTo(x, y, z, this.getYRot(), this.getXRot());
			//serverworld.updateChunkPos(this);
			//this.forceChunkAddition = true;
		}
	}

	public void teleportTo(BlockPos pos)
	{
		this.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}



	@Override
	public void addAdditionalSaveData(CompoundTag nbt)
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
	public void readAdditionalSaveData(CompoundTag nbt)
	{
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("HomeX"))
		{
			this.homePosition = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
		}
	}

	@Override
	public void die(DamageSource p_21014_)
	{
		System.out.println("-----------sprite died to " + p_21014_);
		super.die(p_21014_);
	}
}
