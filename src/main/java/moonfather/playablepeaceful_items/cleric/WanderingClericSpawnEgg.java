package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import moonfather.playablepeaceful_items.slimeball.RegistrationManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

public class WanderingClericSpawnEgg extends Item
{
	public WanderingClericSpawnEgg()
	{
		super(new Item.Properties()
				.stacksTo(64)
				.tab(ItemGroup.TAB_MISC)
		);
	}



	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		if (!(context.getLevel() instanceof ServerWorld))
		{
			return ActionResultType.SUCCESS;
		}
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		BlockPos position = context.getClickedPos();
		boolean move = false;
		if (!state.getCollisionShape(context.getLevel(), position).isEmpty())
		{
			position = position.relative(context.getClickedFace());
			move = true;
		}

		BlockPos pos3 = WanderingClericSpawning.findSpawnPositionNear(context.getLevel(), position, 4, context.getLevel().random);
		if (pos3 != null && WanderingClericSpawning.hasEnoughSpace(context.getLevel(), pos3))
		{
			WanderingClericEntity cleric = (WanderingClericEntity) RegistrationManager.CLERIC_HOLDER.spawn((ServerWorld) context.getLevel(), (CompoundNBT)null, (ITextComponent)null, (PlayerEntity)null, pos3, SpawnReason.EVENT, false, false);
			if (cleric != null)
			{
				DonkeyManagement.tryToSpawnDonkeyFor((ServerWorld) context.getLevel(), cleric, 4);
				cleric.setDespawnDelay(48000);
				cleric.setWanderTarget(position);
				cleric.restrictTo(position, 32); // was 16, reconsider
			}
		}
		else
		{
			return ActionResultType.FAIL;
		}

		if (!context.getPlayer().isCreative())
		{
			stack.shrink(1);
		}
		return ActionResultType.SUCCESS;
	}
}
