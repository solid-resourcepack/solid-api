package io.github.solid.resourcepack.api.builder.feature

import io.github.solid.resourcepack.api.builder.ResourcePackFeature
import io.github.solid.resourcepack.api.builder.config.ModelModifier
import io.github.solid.resourcepack.api.builder.config.ModelVariant
import io.github.solid.resourcepack.api.builder.config.PredicateFeature
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.base.Writable
import team.unnamed.creative.texture.Texture

class SolidModelFeature : ResourcePackFeature<SolidModelConfig, Unit> {
    override fun apply(config: SolidModelConfig, pack: ResourcePack) {
        val models = config.key?.let {
            pack.model(it)
                ?.let { if (config.variants.isEmpty()) mutableListOf(it.key()) else mutableListOf() }
        } ?: mutableListOf()
        if (models.isEmpty()) {
            if (config.key != null && config.mapper != null && config.writable != null) {
                val parentKey = Key.key(config.key.namespace(), config.key.value() + "_abstract")
                ModelModifier.builder().key(parentKey).data(config.writable).mapper(config.mapper).build().let {
                    ModelModifierFeature().apply(it, pack)
                }
                config.variants.forEach { (key, variant) ->
                    ModelVariant.builder().key(key).target(parentKey).texturesWithData(variant).build().let {
                        ModelVariantFeature().apply(it, pack).let { model -> models.add(model.key()) }
                    }
                }
            } else if (config.writable != null) {
                pack.unknownFile(
                    "assets/${config.target.namespace()}/models/${config.target.value()}.json",
                    config.writable
                )
            }
        }
        val predicateBuilder = PredicateFeature.customModelData()
            .models(models)
            .key(config.target)
            .incrementor(config.incrementor)
        if (config.parent != null) {
            predicateBuilder.parent(config.parent)
        }
        PredicateFeature().apply(predicateBuilder.build(), pack)
    }
}

data class SolidModelConfig(
    val writable: Writable?,
    val target: Key,
    val key: Key?,
    val parent: Key?,
    val mapper: ((String) -> String)?,
    val incrementor: PredicateIncrementor,
    val variants: Map<Key, Map<String, Texture>>
)