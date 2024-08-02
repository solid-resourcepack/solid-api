package io.github.solid.resourcepack.api.builder.config

import io.github.solid.resourcepack.api.builder.feature.PredicateConfig
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import io.github.solid.resourcepack.api.predicate.PredicateIncrementorType
import net.kyori.adventure.key.Key


object PredicateFeature {
    fun builder(): PredicateFeatureBuilder {
        return PredicateFeatureBuilder()
    }
    fun mushroomBlock(): PredicateFeatureBuilder {
        return builder().key(Key.key(Key.MINECRAFT_NAMESPACE, "block/mushroom_block"))
            .type(PredicateIncrementorType.MUSHROOM_BLOCK).parent(Key.key(Key.MINECRAFT_NAMESPACE, "block/cube_all"))
    }

    fun chorusFlower(): PredicateFeatureBuilder {
        return builder().key(Key.key(Key.MINECRAFT_NAMESPACE, "block/chorus_flower"))
            .type(PredicateIncrementorType.CHORUS_FLOWER).parent(Key.key(Key.MINECRAFT_NAMESPACE, "block/cube_all"))
    }

    fun customModelData(): PredicateFeatureBuilder {
        return builder().parent(Key.key(Key.MINECRAFT_NAMESPACE, "item/generated")).type(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun noteBlock(): PredicateFeatureBuilder {
        return builder().key(Key.key(Key.MINECRAFT_NAMESPACE, "block/note_block"))
            .type(PredicateIncrementorType.NOTE_BLOCK).parent(Key.key(Key.MINECRAFT_NAMESPACE, "block/cube_all"))
    }
}

class PredicateFeatureBuilder : ConfigBuilder<PredicateConfig> {

    private lateinit var key: Key
    private var parent: Key? = null
    private lateinit var incrementor: PredicateIncrementor
    private val models: MutableList<Key> = mutableListOf()

    fun key(key: Key): PredicateFeatureBuilder {
        this.key = key
        return this
    }

    fun parent(parent: Key): PredicateFeatureBuilder {
        this.parent = parent
        return this
    }

    fun type(type: PredicateIncrementorType): PredicateFeatureBuilder {
        this.incrementor = PredicateIncrementor(type.predicates)
        return this
    }

    fun incrementor(incrementor: PredicateIncrementor): PredicateFeatureBuilder {
        this.incrementor = incrementor
        return this
    }

    fun models(vararg models: Key): PredicateFeatureBuilder {
        this.models.addAll(models)
        return this
    }

    fun models(models: Collection<Key>): PredicateFeatureBuilder {
        this.models.addAll(models)
        return this
    }

    override fun build(): PredicateConfig {
        return PredicateConfig(
            target = key,
            parent = parent,
            models = models,
            incrementor = incrementor
        )
    }
}