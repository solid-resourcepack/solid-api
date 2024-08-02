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
        val incrementor = PredicateIncrementor(config.incrementor.predicates)
        val generator = PredicateGenerator(incrementor)
        model.overrides().clear()
        model.overrides().addAll(generator.generate(overrides, config.models))
        pack.model(model)
    }
}

abstract class PredicateConfig {
    abstract val models: List<Key>
    abstract val target: Key
    abstract val parent: Key?
    abstract val incrementor: PredicateIncrementorType
}

class NoteBlockPredicateConfig(
    override val models: List<Key>
) : PredicateConfig() {
    override val parent: Key = Key.key("minecraft", "block/cube_all")
    override val target: Key = Key.key("minecraft", "block/note_block")
    override val incrementor: PredicateIncrementorType = PredicateIncrementorType.NOTE_BLOCK
}

class MushroomBlockPredicateConfig(
    override val models: List<Key>
) : PredicateConfig() {
    override val parent: Key = Key.key("minecraft", "block/cube_all")
    override val target: Key = Key.key("minecraft", "block/mushroom_block")
    override val incrementor: PredicateIncrementorType = PredicateIncrementorType.MUSHROOM_BLOCK
}

class ChorusFlowerPredicateConfig(
    override val models: List<Key>
) : PredicateConfig() {
    override val parent: Key = Key.key("minecraft", "block/cube_all")
    override val target: Key = Key.key("minecraft", "block/chorus_flower")
    override val incrementor: PredicateIncrementorType = PredicateIncrementorType.CHORUS_FLOWER
}

class CustomModelDataPredicateConfig(
    override val models: List<Key>,
    override val target: Key,
    override val parent: Key?
) : PredicateConfig() {
    override val incrementor: PredicateIncrementorType = PredicateIncrementorType.CUSTOM_MODEL_DATA
}