package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.Constants;
import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CuteSlimeRenderer extends SlimeRenderer
{
	private static final ResourceLocation TEXTURE2 = new ResourceLocation(Constants.MODID, "textures/entity/slime_yellow.png");
	private static final ResourceLocation TEXTURE3 = new ResourceLocation(Constants.MODID, "textures/entity/slime_orange.png");
	private static final ResourceLocation TEXTURE4 = new ResourceLocation(Constants.MODID, "textures/entity/slime_dark.png");
	private static final ResourceLocation TEXTURE5 = new ResourceLocation("minecraft", "textures/entity/slime/slime.png");

	public CuteSlimeRenderer(EntityRendererProvider.Context context)
	{
		super(context);
	}

	public ResourceLocation getTextureLocation(Slime entity)
	{
		int color = ((CuteSlimeEntity) entity).getColor();
		if (color == 2) return TEXTURE2;
		if (color == 3) return TEXTURE3;
		if (color == 4) return TEXTURE4;
		return TEXTURE5;
	}
}