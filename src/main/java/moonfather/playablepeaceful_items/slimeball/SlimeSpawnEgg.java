package moonfather.playablepeaceful_items.slimeball;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class SlimeSpawnEgg extends Item
{
	public SlimeSpawnEgg()
	{
		super(new Item.Properties()
			.stacksTo(64)
			.tab(ItemGroup.TAB_MISC)
		);
	}


	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		if (context.getLevel().isClientSide)
		{
			return ActionResultType.SUCCESS;
		}
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.getBlock() == Blocks.SPAWNER)
		{
			TileEntity te = context.getLevel().getBlockEntity(context.getClickedPos());
			if (te instanceof MobSpawnerTileEntity)
			{
				((MobSpawnerTileEntity)te).getSpawner().setEntityId(RegistrationManager.SLIME_HOLDER);
				te.setChanged();
				context.getLevel().sendBlockUpdated(context.getClickedPos(), state, state, 3);
				if (!context.getPlayer().isCreative())
				{
					stack.shrink(1);
				}
				return ActionResultType.SUCCESS;
			}
		}

		BlockPos position = context.getClickedPos();
		boolean move = false;
		if (!state.getCollisionShape(context.getLevel(), position).isEmpty())
		{
			position = position.relative(context.getClickedFace());
			move = true;
		}

		CuteSlimeEntity slime = (CuteSlimeEntity) RegistrationManager.SLIME_HOLDER.spawn((ServerWorld) context.getLevel(), stack, context.getPlayer(), position, SpawnReason.SPAWN_EGG, true, move);
		if (!context.getPlayer().isCreative())
		{
			stack.shrink(1);
		}
		return ActionResultType.SUCCESS;
	}
}
