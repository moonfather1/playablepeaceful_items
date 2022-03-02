package moonfather.playablepeaceful_items.shared;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BasicItem extends Item
{
	public BasicItem()
	{
		this(false);
	}

	public BasicItem(boolean fireImmune)
	{
		this(new Item.Properties().tab(ItemGroup.TAB_MATERIALS), fireImmune);
	}

	public BasicItem(Properties properties, boolean fireImmune)
	{
		super(fireImmune ? properties.fireResistant() : properties);
	}
}
