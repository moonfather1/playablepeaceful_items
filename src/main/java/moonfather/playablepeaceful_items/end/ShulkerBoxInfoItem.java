package moonfather.playablepeaceful_items.end;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
	public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> text, ITooltipFlag noIdea)
	{
		text.add(new TranslationTextComponent("item.playablepeaceful_items.info_item.lore1"));
		text.add(new TranslationTextComponent("item.playablepeaceful_items.info_item.lore2"));
		text.add(new TranslationTextComponent("item.playablepeaceful_items.info_item.lore3"));
		super.appendHoverText(itemStack, world, text, noIdea);
	}
}
