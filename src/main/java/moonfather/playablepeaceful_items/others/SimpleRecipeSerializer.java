package moonfather.playablepeaceful_items.others;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SimpleRecipeSerializer<T extends IRecipe<?>>
		extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
		implements IRecipeSerializer<T>
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
	public T fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_)
	{
		return this.constructor.apply(p_199426_1_);
	}

	@Override
	public void toNetwork(PacketBuffer p_199427_1_, T p_199427_2_)
	{

	}
}
