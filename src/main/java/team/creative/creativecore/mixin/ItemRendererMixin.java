package team.creative.creativecore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import team.creative.creativecore.client.render.model.CreativeBakedQuad;
import team.creative.creativecore.common.util.mc.ColorUtils;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    
    @Redirect(at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFIIZ)V",
            remap = false),
            method = "renderQuadList(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/world/item/ItemStack;II)V")
    public void putBulkData(VertexConsumer consumer, PoseStack.Pose pose, BakedQuad bakedQuad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, boolean readExistingColor) {
        if (bakedQuad instanceof CreativeBakedQuad)
            alpha = ColorUtils.alphaF(bakedQuad.getTintIndex());
        consumer.putBulkData(pose, bakedQuad, red, green, blue, alpha, packedLight, packedOverlay, readExistingColor);
    }
}
