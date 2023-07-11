package net.kalandoz.runic_sword_art.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.entity.projectile.IceSpellEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class IceSpellRenderer<T extends IceSpellEntity> extends EntityRenderer<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RunicSwordArt.MOD_ID, "icespell");
    protected final EntityModel<T> modelIceSpell = new IceSpellModle<>();

    public IceSpellRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {

        IVertexBuilder ivertexbuilder =
                bufferIn.getBuffer(this.modelIceSpell.getRenderType(this.getEntityTexture(entityIn)));
        modelIceSpell.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return TEXTURE;
    }
}