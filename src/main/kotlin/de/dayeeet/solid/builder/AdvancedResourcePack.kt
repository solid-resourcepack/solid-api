package de.dayeeet.solid.builder

import de.dayeeet.solid.builder.feature.*
import de.dayeeet.solid.mappings.ModelMapper
import net.kyori.adventure.key.Key
import team.unnamed.creative.BuiltResourcePack
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.model.Model
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter

class AdvancedResourcePack(
    val resourcePack: ResourcePack
) {
    inline fun <Config, reified Feature : ResourcePackFeature<Config>> apply(config: Config) {
        Feature::class.java.getDeclaredConstructor().newInstance().apply(config, resourcePack, this)
    }

    fun modelLink(config: PredicateConfig) {
        apply<PredicateConfig, PredicateFeature>(config)
    }

    fun noteBlockModelLink(vararg keys: Key) {
        apply<PredicateConfig, PredicateFeature>(NoteBlockPredicateConfig(keys.toList()))
    }

    fun mushroomBlockModelLink(vararg keys: Key) {
        apply<PredicateConfig, PredicateFeature>(MushroomBlockPredicateConfig(keys.toList()))
    }

    fun itemModelLink(target: Key, parent: Key?, vararg keys: Key) {
        apply<PredicateConfig, PredicateFeature>(CustomModelDataPredicateConfig(target = target, parent = parent, models = keys.toList()))
    }

    fun itemModelLink(target: Key, vararg keys: Key) {
        apply<PredicateConfig, PredicateFeature>(CustomModelDataPredicateConfig(target = target, parent = Key.key("minecraft", "item/generated"), models = keys.toList()))
    }

    fun map(mapper: ModelMapper<*>) {
        map(mapper, Filters.MINECRAFT_FILTER)
    }

    fun map(mapper: ModelMapper<*>, vararg filters: (Model) -> Boolean) {
        resourcePack.models().filter { model -> filters.all { it(model) } }.forEach { model ->
            model.overrides().forEach {
                mapper.register(it.model(), it.predicate())
            }
        }
    }


    fun build(): BuiltResourcePack {
        return MinecraftResourcePackWriter.builder().prettyPrinting(true).build().build(resourcePack)
    }

    object Filters {
        val MINECRAFT_FILTER: (Model) -> Boolean = { model -> model.key().namespace() == Key.MINECRAFT_NAMESPACE }
    }
}