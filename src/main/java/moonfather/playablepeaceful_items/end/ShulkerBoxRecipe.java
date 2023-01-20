package moonfather.playablepeaceful_items.end;

import moonfather.playablepeaceful_items.OptionsHolder;
import moonfather.playablepeaceful_items.RegistrationManager;
import moonfather.playablepeaceful_items.others.SimpleRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class ShulkerBoxRecipe extends CustomRecipe
{
	public static RecipeSerializer<ShulkerBoxRecipe> CreateSerializer()
	{
		//return (RecipeSerializer<ShulkerBoxRecipe>) (new SimpleRecipeSerializer<ShulkerBoxRecipe>(ShulkerBoxRecipe::new).setRegistryName(Constants.MODID, "shulker_box_conditional"));
		return new SimpleRecipeSerializer<ShulkerBoxRecipe>(ShulkerBoxRecipe::new);
	}


	public ShulkerBoxRecipe(ResourceLocation resourceLocation)
	{
		super(resourceLocation);
	}



	@Override
	public boolean matches(CraftingContainer inventory, Level world)
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
						(i * j == 1 && inventory.getItem(j * inventory.getWidth() + i).is(Tags.Items.CHESTS_WOODEN))
						|| (i * j != 1 && inventory.getItem(j * inventory.getWidth() + i).is(Items.CHORUS_FRUIT))
				))
				{
					return false;
				}
			}
		}
		return true;
	}



	@Override
	public ItemStack assemble(CraftingContainer inventory)
	{
		return new ItemStack(Items.SHULKER_BOX);
	}



	@Override
	public boolean canCraftInDimensions(int d1, int d2)
	{
		return d1 >= 3 && d2 >= 2;
	}



	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RegistrationManager.ShulkerBoxRecipeSerializer.get();
	}
}
