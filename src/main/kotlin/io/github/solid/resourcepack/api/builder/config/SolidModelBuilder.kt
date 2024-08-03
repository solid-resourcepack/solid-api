package io.github.solid.resourcepack.api.builder.config

import io.github.solid.resourcepack.api.builder.feature.SolidModelConfig
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import io.github.solid.resourcepack.api.predicate.PredicateIncrementorType
import net.kyori.adventure.key.Key
import team.unnamed.creative.base.Writable
import team.unnamed.creative.texture.Texture

object SolidModel {

    fun itemGenerated(target: Key, vararg textures: Texture): SolidModelConfig {
        return SolidModelBuilder().target(target).parent(Key.key(Key.MINECRAFT_NAMESPACE, "item/generated"))
            .variants(textures.toList()).incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA).build()
    }

    fun itemHandheld(target: Key, vararg textures: Texture): SolidModelConfig {
        return SolidModelBuilder().target(target).parent(Key.key(Key.MINECRAFT_NAMESPACE, "item/handheld"))
            .variants(textures.toList()).incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA).build()
    }

    fun itemModel(target: Key, model: Key): SolidModelBuilder {
        return SolidModelBuilder().target(target).key(model).incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun itemModel(target: Key, model: Key, writable: Writable): SolidModelBuilder {
        return SolidModelBuilder().target(target).key(model).data(writable).incrementor(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun builder(): SolidModelBuilder {
        return SolidModelBuilder()
    }

}

class SolidModelBuilder : ConfigBuilder<SolidModelConfig> {

    private var writable: Writable? = null
    private lateinit var target: Key
    private var parent: Key? = null
    private var key: Key? = null
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

    fun target(target: Key): SolidModelBuilder {
        this.target = target
        return this
    }

    fun parent(parent: Key): SolidModelBuilder {
        this.parent = parent
        return this
    }

    fun variant(texture: Texture): SolidModelBuilder {
        this.variants[texture.key()] = ModelVariant.Textures.simple(texture)
        return this
    }

    fun variants(textures: List<Texture>): SolidModelBuilder {
        this.variants.putAll(textures.map { it.key() to ModelVariant.Textures.simple(it) })
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
        this.mapper = ModelModifier.Mapper.SIMPLE_WRAPPER
        return this
    }

    fun mapper(mapper: (String) -> String): SolidModelBuilder {
        this.mapper = mapper
        return this
    }

    override fun build(): SolidModelConfig {
        return SolidModelConfig(
            writable = writable,
            target = target,
            parent = parent,
            incrementor = incrementor,
            mapper = mapper,
            key = key,
            variants = variants
        )
    }
}