package moonfather.playablepeaceful_items.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BasicItem extends Item
{
	public BasicItem()
	{
		super(new Item.Properties().tab(ItemGroup.TAB_MATERIALS));
	}

	public BasicItem(Properties properties)
	{
		super(properties);
	}
}
