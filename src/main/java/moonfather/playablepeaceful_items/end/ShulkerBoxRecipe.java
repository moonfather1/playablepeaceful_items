package moonfather.playablepeaceful_items.end;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.others.SimpleRecipeSerializer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class ShulkerBoxRecipe extends SpecialRecipe
{
	public static IRecipeSerializer<ShulkerBoxRecipe> StupidSerializer = (IRecipeSerializer<ShulkerBoxRecipe>) (new SimpleRecipeSerializer<ShulkerBoxRecipe>(ShulkerBoxRecipe::new).setRegistryName(PeacefulMod.MODID, "shulker_box_conditional"));


	public ShulkerBoxRecipe(ResourceLocation resourceLocation)
	{
		super(resourceLocation);
	}



	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		if (OptionsHolder.COMMON.ShulkerBoxSimpleSolution.get().equals(false))
		{
			return false;
		}
		if (world.getDifficulty() != Difficulty.PEACEFUL)
		{
			return false;
		}
		if (inventory.getHeight() < 3 || inventory.getWidth() < 3)
		{
			return false;
		}
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				if (!(
						(i == 1 && j == 1 && inventory.getItem(j * inventory.getWidth() + i).getItem().is(Tags.Items.CHESTS_WOODEN))
						|| (inventory.getItem(j * inventory.getWidth() + i).getItem().equals(Items.CHORUS_FRUIT))
				))
				{
					return false;
				}
			}
		}
		return true;
	}



	@Override
	public ItemStack assemble(CraftingInventory inventory)
	{
		return new ItemStack(Items.SHULKER_BOX);
	}



	@Override
	public boolean canCraftInDimensions(int d1, int d2)
	{
		return d1 >= 3 && d2 >= 2;
	}



	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return StupidSerializer;
	}
}
