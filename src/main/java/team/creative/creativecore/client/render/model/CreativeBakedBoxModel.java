package team.creative.creativecore.client.render.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import team.creative.creativecore.client.render.box.RenderBox;
import team.creative.creativecore.common.util.math.base.Facing;
import team.creative.creativecore.common.util.mc.ColorUtils;

public class CreativeBakedBoxModel extends CreativeBakedModel {
    
    public static Minecraft mc = Minecraft.getInstance();
    
    public static List<BakedQuad> compileBoxes(List<? extends RenderBox> boxes, Facing side, RenderType layer, RandomSource rand, boolean item) {
        if (side == null)
            return Collections.EMPTY_LIST;
        
        List<BakedQuad> baked = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            RenderBox box = boxes.get(i);
            
            if (!box.shouldRenderFace(side))
                continue;
            
            BlockState state = Blocks.AIR.defaultBlockState();
            if (box.state != null)
                state = box.state;
            
            BakedModel blockModel = mc.getBlockRenderer().getBlockModel(state);
            
            int defaultColor = ColorUtils.WHITE;
            if (item)
                defaultColor = mc.getItemColors().getColor(new ItemStack(state.getBlock()), defaultColor);
            
            baked.addAll(box.getBakedQuad(null, null, box.getOffset(), state, blockModel, side, layer, rand, true, defaultColor));
        }
        return baked;
    }
    
    public CreativeBlockModel block;
    
    public CreativeBakedBoxModel(ModelResourceLocation location, CreativeItemBoxModel item, CreativeBlockModel block) {
        super(location, item);
        this.block = block;
    }
    
    public ItemOverrides customOverride = new ItemOverrides() {
        
        @Override
        public BakedModel resolve(BakedModel original, ItemStack stack, ClientLevel level, LivingEntity entity, int p_173469_) {
            renderedStack = stack;
            return super.resolve(original, stack, level, entity, p_173469_);
        }
    };
    
    @Override
    public boolean isLayered() {
        return ((CreativeItemBoxModel) item).hasTranslucentLayer(renderedStack);
    }
    
    @Override
    public List<com.mojang.datafixers.util.Pair<BakedModel, RenderType>> getLayerModels(ItemStack itemStack, boolean fabulous) {
        RenderType[] itemLayers = ((CreativeItemBoxModel) item).getLayers(itemStack, fabulous);
        List<com.mojang.datafixers.util.Pair<BakedModel, RenderType>> layers = new ArrayList<>(itemLayers.length);
        for (int i = 0; i < itemLayers.length; i++)
            layers.add(new Pair<BakedModel, RenderType>(this, itemLayers[i]));
        return layers;
    }
    
    @Override
    public @NotNull IModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData modelData) {
        if (block != null)
            return block.getModelData(level, pos, state, modelData);
        return modelData;
    }
    
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, @NotNull RandomSource rand, @NotNull IModelData extraData) {
        Facing facing = Facing.get(direction);
        if (state != null) {
            if (block != null)
                return compileBoxes(block.getBoxes(state, extraData, rand), facing, MinecraftForgeClient.getRenderType(), rand, false);
            return Collections.EMPTY_LIST;
        }
        
        if (renderedStack == null || renderedStack.isEmpty())
            return Collections.EMPTY_LIST;
        
        RenderType layer = MinecraftForgeClient.getRenderType();
        List<BakedQuad> cached = ((CreativeItemBoxModel) item).getCachedModel(facing, layer, renderedStack, false);
        if (cached != null)
            return cached;
        List<? extends RenderBox> boxes = ((CreativeItemBoxModel) item).getBoxes(renderedStack, layer);
        if (boxes != null) {
            cached = compileBoxes(boxes, facing, layer, rand, true);
            ((CreativeItemBoxModel) item).saveCachedModel(facing, layer, cached, renderedStack, false);
            return cached;
        }
        
        return Collections.EMPTY_LIST;
    }
    
    @Override
    @Deprecated
    public List<BakedQuad> getQuads(BlockState state, Direction direction, RandomSource rand) {
        return getQuads(state, direction, rand, EmptyModelData.INSTANCE);
    }
    
}
