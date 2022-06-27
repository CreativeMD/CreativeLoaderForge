package team.creative.creativecore.client.render.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class CreativeModelLoader implements IModelLoader<CreativeUnbakedModel> {
    
    @Override
    public void onResourceManagerReload(ResourceManager p_10758_) {}
    
    @Override
    public CreativeUnbakedModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        ResourceLocation block = null;
        if (modelContents.has("block"))
            block = new ResourceLocation(modelContents.get("block").getAsString());
        ResourceLocation item = null;
        if (modelContents.has("item"))
            item = new ResourceLocation(modelContents.get("item").getAsString());
        return new CreativeUnbakedModel(item, block);
    }
    
}
