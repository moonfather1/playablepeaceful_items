package moonfather.playablepeaceful_items.cleric;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WanderingClericRenderer extends WanderingTraderRenderer
{
	private static final ResourceLocation TEXTURE2 = new ResourceLocation(PeacefulMod.MODID, "textures/entity/wandering_cleric.png");

	public WanderingClericRenderer(EntityRendererManager erm)
	{
		super(erm);
	}

	public ResourceLocation getTextureLocation(WanderingTraderEntity entity)
	{
		return TEXTURE2;
	}
}