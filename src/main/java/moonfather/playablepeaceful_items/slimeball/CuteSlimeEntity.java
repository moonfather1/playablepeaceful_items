package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class CuteSlimeEntity extends Slime
{
	// todo: fav data to addAdditionalSaveData
	private static final EntityDataAccessor<Integer> ID_COLOR = SynchedEntityData.defineId(CuteSlimeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ID_LAST_MYTOSIS = SynchedEntityData.defineId(CuteSlimeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ID_FAV_COOLDOWN = SynchedEntityData.defineId(CuteSlimeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> ID_FAV_SITTING = SynchedEntityData.defineId(CuteSlimeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final int COLOR_YELLOW = 2;
	private static final int COLOR_ORANGE = 3;
	private static final int COLOR_DARK = 4;
	private static final int COLOR_USUAL = 5;

	private static final ParticleOptions[] particles = {ParticleTypes.ITEM_SLIME, ParticleTypes.ITEM_SLIME, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.STRIPPED_BIRCH_WOOD)), new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ORANGE_STAINED_GLASS)), new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.GREEN_CONCRETE_POWDER)), ParticleTypes.ITEM_SLIME};


	public CuteSlimeEntity(Level world)
	{
		super(RegistrationManager.SLIME.get(), world);
		this.moveControl = new CopiedMoveHelperController(this);
	}

	public CuteSlimeEntity(EntityType<CuteSlimeEntity> entityType, Level world)
	{
		super(entityType, world);
		this.moveControl = new CopiedMoveHelperController(this);
	}



	public static float GetScaleH() { return 2.0f; }
	public static float GetScaleV() { return 1.7f; }


	//------------------------------------------------

	@Override
	protected ParticleOptions getParticleType()
	{
		return particles[this.getColor()];
	}


	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(ID_COLOR, 1);
		this.entityData.define(ID_LAST_MYTOSIS, 1000000);
		this.entityData.define(ID_FAV_SITTING, false);
		this.entityData.define(ID_FAV_COOLDOWN, 20*20);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, (double)2.0F)
				.add(Attributes.MOVEMENT_SPEED, (double)0.3F)
				.add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	public static <T extends Mob> boolean checkCuteSlimeSpawnRules(EntityType<T> entityType, LevelAccessor world, MobSpawnType reason, BlockPos position, Random random)
	{
		if (world.getDifficulty() == Difficulty.PEACEFUL)
		{
			boolean isSwamp = Biome.getBiomeCategory(world.getBiome(position)).equals(Biome.BiomeCategory.SWAMP);
			if (isSwamp)
			{
				boolean res = checkMobSpawnRules(entityType, world, reason, position, random);
				return res;
			}

			if (!(world instanceof WorldGenLevel))
			{
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(position);
			boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel)world).getSeed(), 987234911L).nextInt(10) == 0;
			if (flag && random.nextInt(10) == 0 && position.getY() < 40)
			{
				return checkMobSpawnRules(entityType, world, reason, position, random);
			}
		}

		return false;
	}



	public int getColor()
	{
		int value = this.entityData.get(ID_COLOR);
		if (value < 2)
		{
			// not set?
			value = this.random.nextInt(4) + 2;
			this.entityData.set(ID_COLOR, value);
		}
		return value;
	}

	public int getTicksSinceLastMytosis()
	{
		int value = this.entityData.get(ID_LAST_MYTOSIS);
		if (value <= 0)
		{
			// not set?
			value = 1000000;
			this.entityData.set(ID_LAST_MYTOSIS, value);
		}
		return value;
	}

	public void resetTicksSinceLastMytosis()
	{
		this.entityData.set(ID_LAST_MYTOSIS, 1);
	}

	public void incTicksSinceLastMytosis()
	{
		this.entityData.set(ID_LAST_MYTOSIS, 1 + this.getTicksSinceLastMytosis());
	}

	public void tick()
	{
		super.tick();
		this.incTicksSinceLastMytosis();
	}

	public boolean isEnoughTicksSinceLastMytosis()
	{
		return this.getTicksSinceLastMytosis() > 24000;
	}


	public void addAdditionalSaveData(CompoundTag nbt)
	{
		super.addAdditionalSaveData(nbt);
		nbt.putInt("SlimeColor", this.getColor());
		nbt.putInt("TicksSinceLastMytosis", this.getTicksSinceLastMytosis());
	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		int value = nbt.getInt("SlimeColor");
		this.entityData.set(ID_COLOR, value);
		value = nbt.getInt("TicksSinceLastMytosis");
		this.entityData.set(ID_LAST_MYTOSIS, value);
		super.readAdditionalSaveData(nbt);
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_213386_1_, DifficultyInstance p_213386_2_, MobSpawnType p_213386_3_, @Nullable SpawnGroupData p_213386_4_, @Nullable CompoundTag p_213386_5_)
	{
		this.setSize(1, true);
		this.setCanPickUpLoot(false);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0d);
		return p_213386_4_;
	}

	@Nullable
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new CopiedFloatGoal(this)); //SlimeFloatGoal
		this.goalSelector.addGoal(3, new CopiedFaceRandomGoal(this)); //SlimeRandomDirectionGoal
		this.goalSelector.addGoal(5, new CopiedHopGoal(this)); //SlimeKeepOnJumpingGoal
		this.goalSelector.addGoal(2, new CopiedTemptGoal(this, 1.0D, Ingredient.of(Items.SNOWBALL, Items.SNOW_BLOCK), false));
		if (this.getColor() == COLOR_YELLOW)
		{
			this.goalSelector.addGoal(2, new CopiedTemptGoal(this, 1.0D, Ingredient.of(Items.POTATO, Items.POISONOUS_POTATO, Items.BAKED_POTATO), false));
		}
		if (this.getColor() == COLOR_ORANGE)
		{
			this.goalSelector.addGoal(2, new CopiedTemptGoal(this, 1.0D, Ingredient.of(Items.PUMPKIN_PIE), false));
		}
		if (this.getColor() == COLOR_DARK)
		{
			this.goalSelector.addGoal(2, new CopiedTemptGoal(this, 1.0D, Ingredient.of(Items.CACTUS), false));
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		float splitChance = this.getSplitChance(itemstack);
		if (splitChance > 0)
		{
			for (int i = 0; i < 10; i++)
			{
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
			}
			if (!player.isCreative())
			{
				itemstack.shrink(1);
			}
			if (this.random.nextFloat() <= splitChance)
			{
				if (!this.level.isClientSide)
				{
					CuteSlimeEntity newSlime = (CuteSlimeEntity) RegistrationManager.SLIME.get().spawn((ServerLevel) this.level, itemstack, player, blockPosition(), MobSpawnType.SPAWN_EGG, true, true);
					newSlime.setPos(this.position().x, this.position().y, this.position().z);
					newSlime.entityData.set(ID_COLOR, this.getColor());
					this.level.addFreshEntity(newSlime);
					this.resetTicksSinceLastMytosis();
					newSlime.resetTicksSinceLastMytosis();
				}
			}
			else
			{
				// just particles
			}
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		if (itemstack.getItem() == Items.SNOWBALL)
		{
			return InteractionResult.SUCCESS;
		}

		return super.mobInteract(player, hand);
	}

	private float getSplitChance(ItemStack itemstack)
	{
		if (!this.isEnoughTicksSinceLastMytosis())
		{
			return  0;
		}
		if (itemstack.getItem() == Items.SNOW_BLOCK)
		{
			return 0.6f;
		}
		if (itemstack.getItem() == Items.SNOWBALL)
		{
			return 0.1f;
		}
		if (this.getColor() == COLOR_YELLOW)
		{
			if (itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.POISONOUS_POTATO)
			{
				return 0.3f;
			}
			if (itemstack.getItem() == Items.BAKED_POTATO)
			{
				return 0.6f;
			}
		}
		if (this.getColor() == COLOR_ORANGE)
		{
			if (itemstack.getItem() == Items.PUMPKIN_PIE)
			{
				return 0.9f;
			}
		}
		if (this.getColor() == COLOR_DARK)
		{
			if (itemstack.getItem() == Items.CACTUS)
			{
				return 0.3f;
			}
		}
		return 0;
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount)
	{
		if (source.equals(DamageSource.CACTUS))
		{
			return; // completely immune to cactus damage
		}
		if (source.equals(DamageSource.FREEZE))
		{
			return; // todo: reconsider.
		}
		if (source.equals(DamageSource.FALL) && amount <= 3)
		{
			return; // minor resistance to fall damage
		}
		if (this.getColor() == COLOR_ORANGE && (source.equals(DamageSource.IN_FIRE) || source.equals(DamageSource.ON_FIRE)))
		{
			return;  // orange immune to fire
		}
		if (this.tickCount < 180 && source.equals(DamageSource.IN_WALL))
		{
			return; // just spawned, give it a chance against walls
		}
		if (source.msgId.equals("player") && amount < 2)
		{
			long offsetX = Math.round(this.position().x - source.getEntity().position().x);
			long offsetZ = Math.round(this.position().z - source.getEntity().position().z);
			this.moveControl.setWantedPosition(this.position().x + 5 * offsetX, this.position().y + 0.5, this.position().z + 5 * offsetZ, 1.5);
			if (this.level instanceof ServerLevel)
			{
				((ServerLevel)this.level).sendParticles(ParticleTypes.LAVA, this.position().x, this.position().y, position().z, 1/*?*/, offsetX, 0.8, offsetZ, 1d/*?*/);
			}
			else
			{
				this.level.addParticle(ParticleTypes.LAVA, this.position().x, this.position().y, position().z, offsetX, 0.8, offsetZ);
			}
			//this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
			System.out.println("~~TEST smack");
			return; // hand slap
		}
		super.actuallyHurt(source, amount);
	}

	@Override
	public ItemStack getPickedResult(HitResult target)
	{
		return new ItemStack(RegistrationManager.SLIME_SPAWN_EGG.get());
	}

	//------------------------------------------------


	@Override
	public int getSize()
	{
		return 1;
	}

	@Override
	protected boolean shouldDespawnInPeaceful()
	{
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_)
	{
		return false;
	}

	@Override
	protected void dealDamage(LivingEntity p_175451_1_)
	{
	}

	@Override
	protected boolean isDealsDamage()
	{
		return false;
	}

	@Override
	public boolean doPlayJumpSound()
	{
		return true;
	}

	@Override
	public SoundEvent getJumpSound()
	{
		return SoundEvents.SLIME_JUMP_SMALL;
	}


	private float getSoundPitch0()
	{
		float f = 1.4F;
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
	}



	private int cooldownFromFavoriteBlock = 0;
	public int getCooldownFromFavoriteBlock() { return cooldownFromFavoriteBlock; }
	public void setCooldownFromFavoriteBlock(int value) { this.cooldownFromFavoriteBlock = value; }
	private boolean sittingOnFavoriteBlock = false;
	public boolean isSittingOnFavoriteBlock() { return sittingOnFavoriteBlock; }
	public void setSittingOnFavoriteBlock(boolean value) { this.sittingOnFavoriteBlock = value; }
	public boolean isDarkGreen() { return this.getColor() == COLOR_DARK; }
	public boolean isYellow() { return this.getColor() == COLOR_YELLOW; }

	//------------------------------------------------

	static class CopiedFaceRandomGoal extends Goal
	{
		private final Slime slime;
		private float chosenDegrees;
		private int nextRandomizeTime;

		public CopiedFaceRandomGoal(Slime p_i45820_1_) {
			this.slime = p_i45820_1_;
			this.setFlags(EnumSet.of(Flag.LOOK));
		}

		public boolean canUse() {
			return this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof CopiedMoveHelperController;
		}

		public void tick() {
			if (--this.nextRandomizeTime <= 0) {
				this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
				this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
			}

			((CopiedMoveHelperController)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
		}
	}

	static class CopiedFloatGoal extends Goal {
		private final Slime slime;

		public CopiedFloatGoal(Slime p_i45823_1_) {
			this.slime = p_i45823_1_;
			this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
			p_i45823_1_.getNavigation().setCanFloat(true);
		}

		public boolean canUse() {
			return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof CopiedMoveHelperController;
		}

		public void tick() {
			if (this.slime.getRandom().nextFloat() < 0.8F) {
				this.slime.getJumpControl().jump();
			}

			((CopiedMoveHelperController)this.slime.getMoveControl()).setWantedMovement(1.2D);
		}
	}

	static class CopiedHopGoal extends Goal
	{
		private final Slime slime;

		public CopiedHopGoal(Slime p_i45822_1_) {
			this.slime = p_i45822_1_;
			this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
		}

		public boolean canUse() {
			return !this.slime.isPassenger();
		}

		public void tick() {
			((CopiedMoveHelperController)this.slime.getMoveControl()).setWantedMovement(1.0D);
		}
	}

	static class CopiedMoveHelperController extends MoveControl
	{
		private float yRot;
		private int jumpDelay;
		private final CuteSlimeEntity slime;
		private boolean isAggressive;

		public CopiedMoveHelperController(CuteSlimeEntity p_i45821_1_) {
			super(p_i45821_1_);
			this.slime = p_i45821_1_;
			this.yRot = 180.0F * p_i45821_1_.getYRot() / (float)Math.PI;
		}

		public void setDirection(float p_179920_1_, boolean p_179920_2_) {
			this.yRot = p_179920_1_;
			this.isAggressive = p_179920_2_;
		}

		public void setWantedMovement(double p_179921_1_) {
			this.speedModifier = p_179921_1_;
			this.operation = Operation.MOVE_TO;
		}

		public void tick() {
			this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
			this.mob.yHeadRot = this.mob.getYRot();
			this.mob.yBodyRot = this.mob.getYRot();
			if (this.operation != Operation.MOVE_TO) {
				this.mob.setZza(0.0F);
			} else {
				this.operation = Operation.WAIT;
				if (this.mob.isOnGround()) {
					this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
					if (this.jumpDelay-- <= 0) {
						this.jumpDelay = this.slime.getJumpDelay();
						if (this.isAggressive) {
							this.jumpDelay /= 3;
						}

						this.slime.getJumpControl().jump();
						if (this.slime.doPlayJumpSound()) {
							this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch0());
						}
					} else {
						this.slime.xxa = 0.0F;
						this.slime.zza = 0.0F;
						this.mob.setSpeed(0.0F);
					}
				} else {
					this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				}

			}
		}
	}
}
