package moonfather.playablepeaceful_items.others;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SimpleRecipeSerializer<T extends Recipe<?>>
		extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>>
		implements RecipeSerializer<T>
{
	private final Function<ResourceLocation, T> constructor;

	public SimpleRecipeSerializer(Function<ResourceLocation, T> p_44399_) {
		this.constructor = p_44399_;
	}

	public T fromJson(ResourceLocation p_44404_, JsonObject p_44405_)
	{
		return this.constructor.apply(p_44404_);
	}


	@Nullable
	@Override
	public T fromNetwork(ResourceLocation p_199426_1_, FriendlyByteBuf p_199426_2_)
	{
		return this.constructor.apply(p_199426_1_);
	}

	@Override
	public void toNetwork(FriendlyByteBuf p_199427_1_, T p_199427_2_)
	{

	}
}
