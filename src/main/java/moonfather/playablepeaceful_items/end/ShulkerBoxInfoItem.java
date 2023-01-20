package moonfather.playablepeaceful_items.end;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ShulkerBoxInfoItem extends Item
{
	public ShulkerBoxInfoItem()
	{
		this(new Properties().tab(null));
	}

	public ShulkerBoxInfoItem(Properties properties)
	{
		super(properties);
	}



	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> text, TooltipFlag noIdea)
	{
		text.add(new TranslatableComponent("item.playablepeaceful_items.info_item.lore1"));
		text.add(new TranslatableComponent("item.playablepeaceful_items.info_item.lore2"));
		text.add(new TranslatableComponent("item.playablepeaceful_items.info_item.lore3"));
		super.appendHoverText(itemStack, world, text, noIdea);
	}
}
