package moonfather.playablepeaceful_items.end;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import moonfather.playablepeaceful_items.OptionsHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import java.util.List;

public class EndCityLootModifier extends LootModifier
{
	private int chanceAsPercentage = 75;
	private int amountOfShellsInStack = 2;


	public EndCityLootModifier(ResourceLocation name, ILootCondition[] conditionsIn, int chanceAsPercentage, int amountOfShellsInStack)
	{
		super(conditionsIn);
		this.chanceAsPercentage = chanceAsPercentage;
		this.amountOfShellsInStack = amountOfShellsInStack;
	}

	@Override
	public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
	{
		if (!context.getQueriedLootTableId().equals(LootTables.END_CITY_TREASURE))
		{
			return generatedLoot;
		}
		if (OptionsHolder.COMMON.ShulkerBoxSimpleSolution.get() == true)
		{
			return generatedLoot;
		}
		if (context.getLevel().getDifficulty() != Difficulty.PEACEFUL)
		{
			return generatedLoot;
		}

		if (context.getLevel().random.nextInt(100) >= this.chanceAsPercentage)
		{
			return generatedLoot;
		}

		if (generatedLoot.size() == 27)
		{
			generatedLoot.set(26, new ItemStack(Items.SHULKER_SHELL, this.amountOfShellsInStack));
		}
		else
		{
			generatedLoot.add(new ItemStack(Items.SHULKER_SHELL, this.amountOfShellsInStack));
		}
		return generatedLoot;
	}



	public static class Serializer extends GlobalLootModifierSerializer<EndCityLootModifier>
	{
		@Override
		public EndCityLootModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn)
		{
			int chanceAsPercentage = 75;
			int amountOfShellsInStack = 2;
			if (json != null && json.has("chanceAsPercentage"))
			{
				chanceAsPercentage = JSONUtils.getAsInt(json, "chanceAsPercentage");
			}
			if (json != null && json.has("amountOfShellsInStack"))
			{
				amountOfShellsInStack = JSONUtils.getAsInt(json, "amountOfShellsInStack");
			}
			return new EndCityLootModifier(name, conditionsIn, chanceAsPercentage, amountOfShellsInStack);
		}

		@Override
		public JsonObject write(EndCityLootModifier luckBlockDropsModifier)
		{
			JsonObject result = new JsonObject();
			result.add("conditions", new JsonArray());
			result.addProperty("chanceAsPercentage", luckBlockDropsModifier.chanceAsPercentage);
			result.addProperty("amountOfShellsInStack", luckBlockDropsModifier.amountOfShellsInStack);
			result.addProperty("hintPt1", "set the chanceAsPercentage to a percent of end city chests containing shulker shells. default is 75, meaning 75% chance. ");
			result.addProperty("hintPt2", "reminder: this only applies if simple mode is set to false in config AND difficulty is peaceful ");
			result.addProperty("hintPt3", "set the amountOfShellsInStack to number of items you wish to get at once.");
			result.addProperty("hintPt5", "oh, and global loot modifiers are annoying.");
			return result;
		}
	}
}
