package io.github.solid.resourcepack.api.builder.feature

import io.github.solid.resourcepack.api.builder.ResourcePackFeature
import io.github.solid.resourcepack.api.material.SolidMaterial
import io.github.solid.resourcepack.api.predicate.PredicateGenerator
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.model.Model
import team.unnamed.creative.model.ModelTexture
import team.unnamed.creative.model.ModelTextures

class PredicateFeature : ResourcePackFeature<PredicateConfig, Unit> {
    override fun apply(config: PredicateConfig, pack: ResourcePack) {
        val model = pack.model(config.target.minecraftKey) ?: Model.model().parent(config.target.minecraftParent)
            .key(config.target.minecraftKey).textures(
            ModelTextures.builder().let {
                config.target.minecraftTextures.forEach { texture ->
                    it.addVariable(
                        texture.key,
                        ModelTexture.ofKey(texture.value)
                    )
                }; it.build()
            }
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
    val target: SolidMaterial,
    val incrementor: PredicateIncrementor
)