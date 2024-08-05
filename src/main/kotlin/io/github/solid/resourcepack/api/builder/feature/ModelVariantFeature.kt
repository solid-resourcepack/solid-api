package io.github.solid.resourcepack.api.builder.feature

import io.github.solid.resourcepack.api.builder.ResourcePackFeature
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.base.Writable
import team.unnamed.creative.model.Model
import team.unnamed.creative.model.ModelTexture
import team.unnamed.creative.model.ModelTextures
import team.unnamed.creative.texture.Texture

class ModelVariantFeature : ResourcePackFeature<ModelVariantConfig, Key> {
    override fun apply(config: ModelVariantConfig, pack: ResourcePack): Key {
        config.textureData.forEach { data ->
            if(pack.texture(data.key) == null)
            pack.texture(Texture.texture(data.key, data.value))
        }
        val model = Model.model().key(config.key).parent(config.target)
            .textures(ModelTextures.builder().variables(config.textures).build())
        pack.model(model.build())
        return config.key
    }
}

data class ModelVariantConfig(
    val target: Key,
    val key: Key,
    val textures: Map<String, ModelTexture>,
    val textureData: Map<Key, Writable>
)