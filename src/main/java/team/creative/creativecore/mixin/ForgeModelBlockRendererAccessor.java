package team.creative.creativecore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraftforge.client.model.lighting.ForgeModelBlockRenderer;
import net.minecraftforge.client.model.lighting.QuadLighter;

@Mixin(value = ForgeModelBlockRenderer.class, remap = false)
public interface ForgeModelBlockRendererAccessor {
    
    @Accessor
    public ThreadLocal<QuadLighter> getFlatLighter();
    
    @Accessor
    public ThreadLocal<QuadLighter> getSmoothLighter();
    
}
