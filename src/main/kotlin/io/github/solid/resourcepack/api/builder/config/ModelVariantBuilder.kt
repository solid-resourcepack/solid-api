package io.github.solid.resourcepack.api.builder.config

import io.github.solid.resourcepack.api.builder.feature.ModelVariantConfig
import net.kyori.adventure.key.Key
import team.unnamed.creative.base.Writable
import team.unnamed.creative.model.ModelTexture
import team.unnamed.creative.texture.Texture


object ModelVariant {
    fun simple(target: Key, texture: Key): ModelVariantConfig {
        return ModelVariantBuilder().target(target).key(texture).textures(Textures.simple(texture)).build()
    }
    fun simple(target: Key, texture: Texture): ModelVariantConfig {
        return ModelVariantBuilder().target(target).key(texture.key()).texturesWithData(Textures.simple(texture)).build()
    }
    fun builder(): ModelVariantBuilder {
        return ModelVariantBuilder()
    }

    object Textures {
        fun simple(texture: Key): Map<String, Key> {
            return mapOf("layer0" to texture)
        }

        fun simple(texture: Texture): Map<String, Texture> {
            return mapOf("layer0" to texture)
        }
    }
}

class ModelVariantBuilder: ConfigBuilder<ModelVariantConfig> {
    private lateinit var target: Key
    private lateinit var key: Key

    private val textures: MutableMap<String, ModelTexture> = mutableMapOf()
    private val textureData: MutableMap<Key, Writable> = mutableMapOf()

    fun target(target: Key): ModelVariantBuilder {
        this.target = target
        return this
    }

    fun key(key: Key): ModelVariantBuilder {
        this.key = key
        return this
    }

    fun texture(layer: String, key: Key): ModelVariantBuilder {
        this.textures[layer] = ModelTexture.ofKey(key)
        return this
    }

    fun textures(textures: Map<String, Key>): ModelVariantBuilder {
        this.textures.putAll(textures.map { it.key to ModelTexture.ofKey(it.value) })
        return this
    }

    fun texturesWithData(textures: Map<String, Texture>): ModelVariantBuilder {
        this.textures.putAll(textures.map { it.key to ModelTexture.ofKey(it.value.key()) })
        this.textureData.putAll(textures.map { it.value.key() to it.value.data() })
        return this
    }
    override fun build(): ModelVariantConfig {
        return ModelVariantConfig(target, key, textures, textureData)
    }
}