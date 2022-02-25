package moonfather.playablepeaceful_items.gunpowder.sprite;

import moonfather.playablepeaceful_items.RegistrationManager;
import moonfather.playablepeaceful_items.slimeball.CuteSlimeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class SpriteSpawnEgg extends Item
{
	public SpriteSpawnEgg()
	{
		super(new Properties()
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
		BlockPos position = context.getClickedPos();
		boolean move = false;
		if (!state.getCollisionShape(context.getLevel(), position).isEmpty())
		{
			position = position.relative(context.getClickedFace());
			move = true;
		}

		SpriteEntity sprite = (SpriteEntity) RegistrationManager.SPRITE_HOLDER.spawn((ServerWorld) context.getLevel(), stack, context.getPlayer(), position, SpawnReason.SPAWN_EGG, true, move);
		sprite.addEffect(new EffectInstance(Effects.ABSORPTION, 4 * 20, 2));
		if (!context.getPlayer().isCreative())
		{
			stack.shrink(1);
		}
		return ActionResultType.SUCCESS;
	}
}
