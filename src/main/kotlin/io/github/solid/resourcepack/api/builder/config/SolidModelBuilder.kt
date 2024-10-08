package io.github.solid.resourcepack.api.builder.config

import io.github.solid.resourcepack.api.builder.feature.SolidModelConfig
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import io.github.solid.resourcepack.api.predicate.PredicateIncrementorType
import io.github.solid.resourcepack.material.SolidItemMaterial
import io.github.solid.resourcepack.material.SolidMaterial
import net.kyori.adventure.key.Key
import team.unnamed.creative.base.Writable
import team.unnamed.creative.texture.Texture

object SolidModel {

    fun itemModel(target: SolidItemMaterial, model: Key): SolidModelBuilder {
        return SolidModelBuilder().target(target.toGeneric()).key(model).incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun itemModel(target: SolidItemMaterial, model: Key, writable: Writable): SolidModelBuilder {
        return SolidModelBuilder().target(target.toGeneric()).key(model).data(writable)
            .incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun itemModel(target: SolidItemMaterial, vararg textures: Texture): SolidModelBuilder {
        return SolidModelBuilder().target(target.toGeneric()).variants(*textures)
            .incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun builder(): SolidModelBuilder {
        return SolidModelBuilder()
    }

}

class SolidModelBuilder : ConfigBuilder<SolidModelConfig> {

    private lateinit var key: Key
    private var writable: Writable? = null

    private var target: SolidMaterial? = null
    private var targetKey: Key? = null
    private var targetParent: Key? = null
    private val targetTextures: MutableMap<String, Key> = mutableMapOf()

    private val variants = mutableMapOf<Key, Map<String, Texture>>()
    private var mapper: ((String) -> String)? = null
    private lateinit var incrementor: PredicateIncrementor

    fun data(data: Writable): SolidModelBuilder {
        this.writable = data
        return this
    }

    fun key(key: Key): SolidModelBuilder {
        this.key = key
        return this
    }

    fun target(target: SolidMaterial): SolidModelBuilder {
        this.target = target
        return this
    }

    fun targetKey(target: Key): SolidModelBuilder {
        this.targetKey = target
        return this
    }

    fun targetParent(parent: Key): SolidModelBuilder {
        this.targetParent = parent
        return this
    }

    fun targetTexture(layer: String, texture: Key): SolidModelBuilder {
        this.targetTextures[layer] = texture
        return this
    }

    fun targetTextures(textures: Map<String, Key>): SolidModelBuilder {
        this.targetTextures.putAll(textures)
        return this
    }

    fun variants(vararg textures: Texture): SolidModelBuilder {

        this.variants.putAll(textures.map { Key.key(it.key().namespace(), it.key().value().replace(".png", "")) to ModelVariant.Textures.generic(it) })
        return this
    }

    fun variant(key: Key, textures: Map<String, Texture>): SolidModelBuilder {
        this.variants[key] = textures
        return this
    }

    fun variants(variants: Map<Key, Map<String, Texture>>): SolidModelBuilder {
        this.variants.putAll(variants)
        return this
    }

    fun incrementor(incrementor: PredicateIncrementor): SolidModelBuilder {
        this.incrementor = incrementor
        return this
    }

    fun incrementor(type: PredicateIncrementorType): SolidModelBuilder {
        this.incrementor = PredicateIncrementor(type.predicates)
        return this
    }

    fun abstract(): SolidModelBuilder {
        this.mapper = ModelModifier.Mapper.ABSTRACT_WRAPPER
        return this
    }

    fun generic(): SolidModelBuilder {
        this.mapper = ModelModifier.Mapper.GENERIC_WRAPPER
        return this
    }

    fun mapper(mapper: (String) -> String): SolidModelBuilder {
        this.mapper = mapper
        return this
    }

    override fun build(): SolidModelConfig {
        if (this.target == null) {
            this.target = SolidMaterial(this.key, this.targetParent, this.targetTextures)
        }
        return SolidModelConfig(
            writable = writable,
            target = target!!,
            key = key,
            incrementor = incrementor,
            mapper = mapper,
            variants = variants
        )
    }
}