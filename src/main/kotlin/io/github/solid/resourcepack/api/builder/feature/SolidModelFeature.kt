package io.github.solid.resourcepack.api.builder.feature

import io.github.solid.resourcepack.api.builder.ResourcePackFeature
import io.github.solid.resourcepack.api.builder.config.ModelModifier
import io.github.solid.resourcepack.api.builder.config.ModelVariant
import io.github.solid.resourcepack.api.builder.config.Predicate
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import io.github.solid.resourcepack.material.SolidMaterial
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.base.Writable
import team.unnamed.creative.texture.Texture

class SolidModelFeature : ResourcePackFeature<SolidModelConfig, Unit> {
    override fun apply(config: SolidModelConfig, pack: ResourcePack) {
        var parentKey = config.key
        val models = pack.model(config.key)?.let { mutableListOf(it.key()) } ?: mutableListOf()

        if (config.mapper != null && config.writable != null) {
            models.clear()
            parentKey = Key.key(config.key.namespace(), config.key.value() + "_abstract")
            ModelModifier.builder().key(parentKey).data(config.writable).mapper(config.mapper).build().let {
                ModelModifierFeature().apply(it, pack)
            }
        } else if (config.writable != null) {
            pack.unknownFile(
                "assets/${config.key.namespace()}/models/${config.key.value()}.json", config.writable
            )
        }

        if (config.variants.isNotEmpty()) {
            models.clear()
            config.variants.forEach { (key, variant) ->
                ModelVariant.builder().key(key).target(parentKey).texturesWithData(variant).build().let {
                    ModelVariantFeature().apply(it, pack).let { model -> models.add(model.key()) }
                }
            }
        }
        val predicateBuilder =
            Predicate.builder().target(config.target).incrementor(config.incrementor).models(models)
        PredicateFeature().apply(predicateBuilder.build(), pack)
    }
}

data class SolidModelConfig(

    /**
     * Data in the model to possibly add
     */
    val writable: Writable?,

    /**
     * The target minecraft model to apply the changes to
     */
    val target: SolidMaterial,

    /**
     * The key of this model
     */
    val key: Key,

    /**
     * The mapper mapping the textures of
     */
    val mapper: ((String) -> String)?,

    /**
     * The incrementor to use for the predicate of the target model
     */
    val incrementor: PredicateIncrementor,

    /**
     * The texture variants of this model, also applied on the target model.
     * If not empty, the key will be treated as an abstract model and not be added to the target model
     */
    val variants: Map<Key, Map<String, Texture>>
)