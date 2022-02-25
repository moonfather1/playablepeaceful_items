package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.RegistrationManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class CuteSlimeEntity extends SlimeEntity
{
	private static final DataParameter<Integer> ID_COLOR = EntityDataManager.defineId(CuteSlimeEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> ID_LAST_MYTOSIS = EntityDataManager.defineId(CuteSlimeEntity.class, DataSerializers.INT);
	private static final int COLOR_YELLOW = 2;
	private static final int COLOR_ORANGE = 3;
	private static final int COLOR_DARK = 4;
	private static final int COLOR_USUAL = 5;

	private static final IParticleData[] particles = {ParticleTypes.ITEM_SLIME, ParticleTypes.ITEM_SLIME, new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.STRIPPED_BIRCH_WOOD)), new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.PUMPKIN)), new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.GREEN_CONCRETE_POWDER)), ParticleTypes.ITEM_SLIME};


	public CuteSlimeEntity(World world)
	{
		super(RegistrationManager.SLIME_HOLDER, world);
		this.moveControl = new CopiedMoveHelperController(this);
	}

	public CuteSlimeEntity(EntityType<CuteSlimeEntity> entityType, World world)
	{
		super(entityType, world);
		this.moveControl = new CopiedMoveHelperController(this);
	}


    //------------------------------------------------

	@Override
	protected IParticleData getParticleType()
	{
		return particles[this.getColor()];
	}


	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(ID_COLOR, 1);
		this.entityData.define(ID_LAST_MYTOSIS, 1000000);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes()
	{
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, (double)2.0F)
				.add(Attributes.MOVEMENT_SPEED, (double)0.3F)
				.add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	public static <T extends MobEntity> boolean checkCuteSlimeSpawnRules(EntityType<T> entityType, IWorld world, SpawnReason reason, BlockPos position, Random random)
	{
		if (world.getDifficulty() == Difficulty.PEACEFUL)
		{
			boolean isSwamp = world.getBiome(position).getBiomeCategory().equals(Biome.Category.SWAMP);
			if (isSwamp)
			{
				boolean res = checkMobSpawnRules(entityType, world, reason, position, random);
				return res;
			}

			if (!(world instanceof ISeedReader))
			{
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(position);
			boolean flag = SharedSeedRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((ISeedReader)world).getSeed(), 987234911L).nextInt(10) == 0;
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
			value = this.random.nextInt(3) + 2;
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


	public void addAdditionalSaveData(CompoundNBT nbt)
	{
		super.addAdditionalSaveData(nbt);
		nbt.putInt("SlimeColor", this.getColor());
		nbt.putInt("TicksSinceLastMytosis", this.getTicksSinceLastMytosis());
	}

	public void readAdditionalSaveData(CompoundNBT nbt) {
		int value = nbt.getInt("SlimeColor");
		this.entityData.set(ID_COLOR, value);
		value = nbt.getInt("TicksSinceLastMytosis");
		this.entityData.set(ID_LAST_MYTOSIS, value);
		super.readAdditionalSaveData(nbt);
	}

	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_)
	{
		this.setSize(1, true);
		this.setCanPickUpLoot(false);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0d);
		return p_213386_4_;
	}

	@Nullable
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new CopiedFloatGoal(this));
		this.goalSelector.addGoal(3, new CopiedFaceRandomGoal(this));
		this.goalSelector.addGoal(5, new CopiedHopGoal(this));
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
	public ActionResultType mobInteract(PlayerEntity player, Hand hand)
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
					CuteSlimeEntity newSlime = (CuteSlimeEntity) RegistrationManager.SLIME_HOLDER.spawn((ServerWorld) this.level, itemstack, player, blockPosition(), SpawnReason.SPAWN_EGG, true, true);
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
			return ActionResultType.sidedSuccess(this.level.isClientSide);
		}
		if (itemstack.getItem() == Items.SNOWBALL)
		{
			return ActionResultType.SUCCESS;
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
		if (source.equals(DamageSource.FALL) && amount <= 3)
		{
			return; // minor resistance to fall damage
		}
		if (this.getColor() == COLOR_ORANGE && (source.equals(DamageSource.IN_FIRE) || source.equals(DamageSource.ON_FIRE)))
		{
			return;  // orange immune to fire
		}
		if (this.tickCount < 120 && source.equals(DamageSource.IN_WALL))
		{
			return; // just spawned, give it a chance against walls
		}
		if (source.msgId.equals("player") && amount < 2)
		{
			long offsetX = Math.round(this.position().x - source.getEntity().position().x);
			long offsetZ = Math.round(this.position().z - source.getEntity().position().z);
			this.moveControl.setWantedPosition(this.position().x + 5 * offsetX, this.position().y + 0.5, this.position().z + 5 * offsetZ, 1.5);
			return; // hand slap
		}
		super.actuallyHurt(source, amount);
	}


	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(RegistrationManager.SLIME_EGG_HOLDER);
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

	//------------------------------------------------

	static class CopiedFaceRandomGoal extends Goal
	{
		private final SlimeEntity slime;
		private float chosenDegrees;
		private int nextRandomizeTime;

		public CopiedFaceRandomGoal(SlimeEntity p_i45820_1_) {
			this.slime = p_i45820_1_;
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(Effects.LEVITATION)) && this.slime.getMoveControl() instanceof CopiedMoveHelperController;
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
		private final SlimeEntity slime;

		public CopiedFloatGoal(SlimeEntity p_i45823_1_) {
			this.slime = p_i45823_1_;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
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
		private final SlimeEntity slime;

		public CopiedHopGoal(SlimeEntity p_i45822_1_) {
			this.slime = p_i45822_1_;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !this.slime.isPassenger();
		}

		public void tick() {
			((CopiedMoveHelperController)this.slime.getMoveControl()).setWantedMovement(1.0D);
		}
	}

	static class CopiedMoveHelperController extends MovementController
	{
		private float yRot;
		private int jumpDelay;
		private final CuteSlimeEntity slime;
		private boolean isAggressive;

		public CopiedMoveHelperController(CuteSlimeEntity p_i45821_1_) {
			super(p_i45821_1_);
			this.slime = p_i45821_1_;
			this.yRot = 180.0F * p_i45821_1_.yRot / (float)Math.PI;
		}

		public void setDirection(float p_179920_1_, boolean p_179920_2_) {
			this.yRot = p_179920_1_;
			this.isAggressive = p_179920_2_;
		}

		public void setWantedMovement(double p_179921_1_) {
			this.speedModifier = p_179921_1_;
			this.operation = MovementController.Action.MOVE_TO;
		}

		public void tick() {
			this.mob.yRot = this.rotlerp(this.mob.yRot, this.yRot, 90.0F);
			this.mob.yHeadRot = this.mob.yRot;
			this.mob.yBodyRot = this.mob.yRot;
			if (this.operation != MovementController.Action.MOVE_TO) {
				this.mob.setZza(0.0F);
			} else {
				this.operation = MovementController.Action.WAIT;
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
