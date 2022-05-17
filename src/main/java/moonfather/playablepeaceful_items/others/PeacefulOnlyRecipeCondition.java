package moonfather.playablepeaceful_items.others;

import com.google.gson.JsonObject;
import moonfather.playablepeaceful_items.OptionsHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class PeacefulOnlyRecipeCondition implements ICondition
{
	private final ResourceLocation conditionId;

	private PeacefulOnlyRecipeCondition(ResourceLocation id)
	{
		this.conditionId = id;
	}

	@Override
	public ResourceLocation getID()
	{
		return this.conditionId;
	}

	@Override
	public boolean test()
	{
		if (ForgeHooks.getCraftingPlayer() != null && ForgeHooks.getCraftingPlayer().level.getDifficulty() == Difficulty.PEACEFUL)
		{
			System.out.println("~~~~PeacefulOnlyRecipeCondition: true");
			return true;
		}
		else
		{
			System.out.println("~~~~PeacefulOnlyRecipeCondition: false");
			return false;
		}
	}

	/////////////////////////////////////////////////////

	public static class Serializer implements IConditionSerializer<PeacefulOnlyRecipeCondition>
	{
		private final ResourceLocation conditionId;

		public Serializer(ResourceLocation id)
		{
			this.conditionId = id;
		}

		@Override
		public void write(JsonObject json, PeacefulOnlyRecipeCondition condition)
		{
		}

		@Override
		public PeacefulOnlyRecipeCondition read(JsonObject json)
		{
			return new PeacefulOnlyRecipeCondition(this.conditionId);
		}

		@Override
		public ResourceLocation getID()
		{
			return this.conditionId;
		}
	}
}
