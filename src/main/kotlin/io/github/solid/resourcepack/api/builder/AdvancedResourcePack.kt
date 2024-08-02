package io.github.solid.resourcepack.api.builder

import io.github.solid.resourcepack.api.builder.feature.*
import io.github.solid.resourcepack.api.mappings.ModelMapper
import net.kyori.adventure.key.Key
import team.unnamed.creative.BuiltResourcePack
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.model.Model
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter

class AdvancedResourcePack(
    val resourcePack: ResourcePack
) {
    inline fun <Config, Return, reified Feature : ResourcePackFeature<Config, Return>> apply(config: Config): Return {
        return Feature::class.java.getDeclaredConstructor().newInstance().apply(config, resourcePack)
    }

    fun apply(config: ModelModifierConfig) {
        return apply<ModelModifierConfig, Unit, ModelModifierFeature>(config)
    }

    fun apply(config: ModelVariantConfig): Key {
        return apply<ModelVariantConfig, Key, ModelVariantFeature>(config)
    }

    fun apply(config: PredicateConfig) {
        apply<PredicateConfig, Unit, PredicateFeature>(config)
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