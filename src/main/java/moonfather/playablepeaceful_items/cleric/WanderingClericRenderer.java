package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.Constants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WanderingClericRenderer extends WanderingTraderRenderer
{
	private static final ResourceLocation TEXTURE2 = new ResourceLocation(Constants.MODID, "textures/entity/wandering_cleric.png");

	public WanderingClericRenderer(EntityRendererProvider.Context context)
	{
		super(context);
	}

	public ResourceLocation getTextureLocation(WanderingTrader entity)
	{
		return TEXTURE2;
	}
}