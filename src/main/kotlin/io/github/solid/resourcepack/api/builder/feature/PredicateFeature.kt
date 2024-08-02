package io.github.solid.resourcepack.api.builder.feature

import io.github.solid.resourcepack.api.builder.ResourcePackFeature
import io.github.solid.resourcepack.api.predicate.PredicateGenerator
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import io.github.solid.resourcepack.api.predicate.PredicateIncrementorType
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.model.Model
import team.unnamed.creative.model.ModelTexture
import team.unnamed.creative.model.ModelTextures

class PredicateFeature : ResourcePackFeature<PredicateConfig, Unit> {
    override fun apply(config: PredicateConfig, pack: ResourcePack) {
        val model = pack.model(config.target) ?: Model.model().parent(config.parent).key(config.target).textures(
            ModelTextures.builder().layers(
                ModelTexture.ofKey(config.target)
            ).build()
        ).build()
        val overrides = model.overrides().toList()
        val incrementor = config.incrementor
        val generator = PredicateGenerator(incrementor)
        model.overrides().clear()
        model.overrides().addAll(generator.generate(overrides, config.models))
        pack.model(model)
    }
}

data class PredicateConfig(
    val models: List<Key>,
    val target: Key,
    val parent: Key?,
    val incrementor: PredicateIncrementor
)