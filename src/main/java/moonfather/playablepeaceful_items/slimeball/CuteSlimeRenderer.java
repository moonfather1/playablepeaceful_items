package moonfather.playablepeaceful_items.slimeball;

import moonfather.playablepeaceful_items.PeacefulMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CuteSlimeRenderer extends SlimeRenderer
{
	private static final ResourceLocation TEXTURE2 = new ResourceLocation(PeacefulMod.MODID, "textures/entity/slime_yellow.png");
	private static final ResourceLocation TEXTURE3 = new ResourceLocation(PeacefulMod.MODID, "textures/entity/slime_orange.png");
	private static final ResourceLocation TEXTURE4 = new ResourceLocation(PeacefulMod.MODID, "textures/entity/slime_dark.png");
	private static final ResourceLocation TEXTURE5 = new ResourceLocation("minecraft", "textures/entity/slime/slime.png");

	public CuteSlimeRenderer(EntityRendererManager erm)
	{
		super(erm);
	}

	public ResourceLocation getTextureLocation(SlimeEntity entity)
	{
		int color = ((CuteSlimeEntity) entity).getColor();
		if (color == 2) return TEXTURE2;
		if (color == 3) return TEXTURE3;
		if (color == 4) return TEXTURE4;
		return TEXTURE5;
	}
}