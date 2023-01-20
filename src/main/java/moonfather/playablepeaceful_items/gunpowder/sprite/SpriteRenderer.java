package moonfather.playablepeaceful_items.gunpowder.sprite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import moonfather.playablepeaceful_items.Constants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ExperienceOrbRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpriteRenderer extends EntityRenderer<SpriteEntity>
{
	private static final ResourceLocation SPRITE_LOCATION = new ResourceLocation(Constants.MODID, "textures/entity/sprite.png");
	private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(SPRITE_LOCATION);

	public SpriteRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
		ExperienceOrbRenderer r2;
	}



	@Override
	public ResourceLocation getTextureLocation(SpriteEntity p_110775_1_)
	{
		return SPRITE_LOCATION;
	}



	@Override
	protected int getBlockLightLevel(SpriteEntity p_225624_1_, BlockPos p_225624_2_)
	{
		return Mth.clamp(super.getBlockLightLevel(p_225624_1_, p_225624_2_) + 7, 0, 15);
	}



	@Override
	public void render(SpriteEntity p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_)
	{
		/*p_225623_4_.pushPose();
		int lvt_7_1_ = 5;                                          // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_8_1_ = (float)(lvt_7_1_ % 4 * 16 + 0) / 64.0F;   // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_9_1_ = (float)(lvt_7_1_ % 4 * 16 + 16) / 64.0F;  // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_10_1_ = (float)(lvt_7_1_ / 4 * 16 + 0) / 64.0F;  // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_11_1_ = (float)(lvt_7_1_ / 4 * 16 + 16) / 64.0F; // picking one out of 16 sprites. doesn't matter, they're same.

		float lvt_16_1_ = ((float)p_225623_1_.tickCount + p_225623_3_) / 2.0F;                                   // color cycling
		int lvt_17_1_ = (int)((MathHelper.sin(lvt_16_1_ + 0.0F) + 1.0F) * 0.5F * 255.0F);             // color cycling
		int lvt_19_1_ = (int)((MathHelper.sin(lvt_16_1_ + 4.1887903F) + 1.0F) * 0.1F * 255.0F);       // color cycling
		p_225623_4_.translate(0.0D, 0.10000000149011612D, 0.0D);
		p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
		p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		float lvt_20_1_ = 0.3F;
		p_225623_4_.scale(0.3F, 0.3F, 0.3F);
		IVertexBuilder lvt_21_1_ = p_225623_5_.getBuffer(RENDER_TYPE);
		MatrixStack.Entry lvt_22_1_ = p_225623_4_.last();
		Matrix4f lvt_23_1_ = lvt_22_1_.pose();
		Matrix3f lvt_24_1_ = lvt_22_1_.normal();
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, -0.5F, -0.25F, lvt_17_1_, 255, lvt_19_1_, lvt_8_1_, lvt_11_1_, p_225623_6_);
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, 0.5F, -0.25F, lvt_17_1_, 255, lvt_19_1_, lvt_9_1_, lvt_11_1_, p_225623_6_);
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, 0.5F, 0.75F, lvt_17_1_, 255, lvt_19_1_, lvt_9_1_, lvt_10_1_, p_225623_6_);
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, -0.5F, 0.75F, lvt_17_1_, 255, lvt_19_1_, lvt_8_1_, lvt_10_1_, p_225623_6_);
		p_225623_4_.popPose();
		super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);*/

		p_225623_4_.pushPose();
		int lvt_7_1_ = p_225623_1_.hashCode() % 16;                                          // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_8_1_ = (float)(lvt_7_1_ % 4 * 16 + 0) / 64.0F;   // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_9_1_ = (float)(lvt_7_1_ % 4 * 16 + 16) / 64.0F;  // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_10_1_ = (float)(lvt_7_1_ / 4 * 16 + 0) / 64.0F;  // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_11_1_ = (float)(lvt_7_1_ / 4 * 16 + 16) / 64.0F; // picking one out of 16 sprites. doesn't matter, they're same.
		float lvt_16_1_ = ((float)p_225623_1_.tickCount + p_225623_3_) / 2.0F;                                   // color cycling
		int lvt_17_1_ = (int)((Mth.sin(lvt_16_1_ + 0.0F) + 1.0F) * 0.5F * 255.0F);             // color cycling
		int lvt_19_1_ = (int)((Mth.sin(lvt_16_1_ + 4.1887903F) + 1.0F) * 0.1F * 255.0F);       // color cycling
		int cyclingRed = 255, cyclingGreen = 255, cyclingBlue = (lvt_19_1_ + 255) / 2;
		p_225623_4_.translate(0.0D, 0.10000000149011612D, 0.0D);
		p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
		p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		float lvt_20_1_ = 0.3F;
		p_225623_4_.scale(0.3F, 0.3F, 0.3F);
		VertexConsumer lvt_21_1_ = p_225623_5_.getBuffer(RENDER_TYPE);
		PoseStack.Pose lvt_22_1_ = p_225623_4_.last();
		Matrix4f lvt_23_1_ = lvt_22_1_.pose();
		Matrix3f lvt_24_1_ = lvt_22_1_.normal();
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, -0.5F, -0.25F, 255, 255, cyclingBlue, lvt_8_1_, lvt_11_1_, p_225623_6_);
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, 0.5F, -0.25F, 255, 255, cyclingBlue, lvt_9_1_, lvt_11_1_, p_225623_6_);
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, 0.5F, 0.75F, 255, 255, cyclingBlue, lvt_9_1_, lvt_10_1_, p_225623_6_);
		vertex(lvt_21_1_, lvt_23_1_, lvt_24_1_, -0.5F, 0.75F, 255, 255, cyclingBlue, lvt_8_1_, lvt_10_1_, p_225623_6_);
		p_225623_4_.popPose();
		super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
	}



	private static void vertex(VertexConsumer p_229102_0_, Matrix4f p_229102_1_, Matrix3f p_229102_2_, float p_229102_3_, float p_229102_4_, int p_229102_5_, int p_229102_6_, int p_229102_7_, float p_229102_8_, float p_229102_9_, int p_229102_10_)
	{
		p_229102_0_.vertex(p_229102_1_, p_229102_3_, p_229102_4_, 0.0F).color(p_229102_5_, p_229102_6_, p_229102_7_, 128).uv(p_229102_8_, p_229102_9_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229102_10_).normal(p_229102_2_, 0.0F, 1.0F, 0.0F).endVertex();
	}

}
