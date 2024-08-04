package io.github.solid.resourcepack.api.builder.config

import io.github.solid.resourcepack.api.builder.feature.PredicateConfig
import io.github.solid.resourcepack.api.material.SolidMaterial
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import io.github.solid.resourcepack.api.predicate.PredicateIncrementorType
import net.kyori.adventure.key.Key
import org.bukkit.Material


object Predicate {
    fun builder(): PredicateBuilder {
        return PredicateBuilder()
    }

    fun mushroomBlock(): PredicateBuilder {
        return builder().key(Key.key(Key.MINECRAFT_NAMESPACE, "block/mushroom_block"))
            .type(PredicateIncrementorType.MUSHROOM_BLOCK).parent(Key.key(Key.MINECRAFT_NAMESPACE, "block/cube_all"))
    }

    fun chorusFlower(): PredicateBuilder {
        return builder().key(Key.key(Key.MINECRAFT_NAMESPACE, "block/chorus_flower"))
            .type(PredicateIncrementorType.CHORUS_FLOWER)
            .parent(Key.key(Key.MINECRAFT_NAMESPACE, "block/template_chorus_flower")).textures(
                mapOf("texture" to Key.key(Key.MINECRAFT_NAMESPACE, "block/chorus_flower"))
            )
    }

    fun customModelData(material: SolidMaterial): PredicateBuilder {
        return builder().type(PredicateIncrementorType.CUSTOM_MODEL_DATA).target(material)
    }

    fun customModelData(): PredicateBuilder {
        return builder().type(PredicateIncrementorType.CUSTOM_MODEL_DATA)
    }

    fun noteBlock(): PredicateBuilder {
        return builder().type(PredicateIncrementorType.NOTE_BLOCK).target(SolidMaterial.from(Material.NOTE_BLOCK)!!)
    }
}

class PredicateBuilder : ConfigBuilder<PredicateConfig> {

    private var target: SolidMaterial? = null
    private val textures = mutableMapOf<String, Key>()
    private var key: Key? = null
    private var parent: Key? = null
    private lateinit var incrementor: PredicateIncrementor
    private val models: MutableList<Key> = mutableListOf()

    fun target(target: SolidMaterial): PredicateBuilder {
        this.target = target
        return this
    }

    fun key(key: Key): PredicateBuilder {
        this.key = key
        return this
    }

    fun textures(textures: Map<String, Key>): PredicateBuilder {
        this.textures.putAll(textures)
        return this
    }

    fun parent(parent: Key): PredicateBuilder {
        this.parent = parent
        return this
    }

    fun type(type: PredicateIncrementorType): PredicateBuilder {
        this.incrementor = PredicateIncrementor(type.predicates)
        return this
    }

    fun incrementor(incrementor: PredicateIncrementor): PredicateBuilder {
        this.incrementor = incrementor
        return this
    }

    fun models(vararg models: Key): PredicateBuilder {
        this.models.addAll(models)
        return this
    }

    fun models(models: Collection<Key>): PredicateBuilder {
        this.models.addAll(models)
        return this
    }

    override fun build(): PredicateConfig {
        if (target == null) {
            target = SolidMaterial.from(key!!, parent, textures)
        }

        return PredicateConfig(
            target = target!!,
            models = models,
            incrementor = incrementor
        )
    }
}