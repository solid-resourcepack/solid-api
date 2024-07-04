package io.github.dayyeeet.solid.builder.feature

import io.github.dayyeeet.solid.builder.ResourcePackFeature
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.base.Writable
import team.unnamed.creative.model.Model
import team.unnamed.creative.model.ModelTexture
import team.unnamed.creative.model.ModelTextures
import team.unnamed.creative.texture.Texture

class ModelVariantFeature : ResourcePackFeature<ModelVariantConfig, Key> {
    override fun apply(config: ModelVariantConfig, pack: ResourcePack): Key {
        config.textureData?.let { data ->
            pack.texture(Texture.texture(config.texture, data))
        }
        val model = Model.model().key(config.texture).parent(config.target)
            .textures(ModelTextures.builder().layers(ModelTexture.ofKey(config.texture)).build())
        pack.model(model.build())
        return config.texture
    }
}

data class ModelVariantConfig(
    val target: Key,
    val texture: Key,
    val textureData: Writable?
)