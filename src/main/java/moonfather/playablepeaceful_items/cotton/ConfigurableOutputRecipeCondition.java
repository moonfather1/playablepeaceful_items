package moonfather.playablepeaceful_items.cotton;

import com.google.gson.JsonObject;
import moonfather.playablepeaceful_items.OptionsHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ConfigurableOutputRecipeCondition implements ICondition
{
	private final String value;
	private final ResourceLocation conditionId;

	private ConfigurableOutputRecipeCondition(ResourceLocation id, String value)
	{
		this.conditionId = id;
		this.value = value;
	}

	@Override
	public ResourceLocation getID()
	{
		return this.conditionId;
	}

	@Override
	public boolean test()
	{
		return OptionsHolder.COMMON.CottonStringAmount.get().toString().equals(this.value);
	}

	/////////////////////////////////////////////////////

	public static class Serializer implements IConditionSerializer<ConfigurableOutputRecipeCondition>
	{
		private final ResourceLocation conditionId;

		public Serializer(ResourceLocation id)
		{
			this.conditionId = id;
		}

		@Override
		public void write(JsonObject json, ConfigurableOutputRecipeCondition condition)
		{
			json.addProperty("amount", condition.value);
		}

		@Override
		public ConfigurableOutputRecipeCondition read(JsonObject json)
		{
			return new ConfigurableOutputRecipeCondition(this.conditionId, json.getAsJsonPrimitive("amount").getAsString());
		}

		@Override
		public ResourceLocation getID()
		{
			return this.conditionId;
		}
	}
}
