package io.github.dayyeeet.solid.builder

import de.dayeeet.solid.builder.feature.*
import io.github.dayyeeet.solid.mappings.ModelMapper
import io.github.dayyeeet.solid.builder.feature.*
import net.kyori.adventure.key.Key
import team.unnamed.creative.BuiltResourcePack
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.base.Writable
import team.unnamed.creative.model.Model
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter

class AdvancedResourcePack(
    val resourcePack: ResourcePack
) {
    inline fun <Config, Return, reified Feature : ResourcePackFeature<Config, Return>> apply(config: Config): Return {
        return Feature::class.java.getDeclaredConstructor().newInstance().apply(config, resourcePack)
    }

    fun modifiedModel(config: ModelModifierConfig) {
        return apply<ModelModifierConfig, Unit, ModelModifierFeature>(config)
    }

    fun abstractModel(key: Key, data: Writable) {
        return modifiedModel(ModelModifierConfig(key, data, ModelModifierFeature.Mappers.ABSTRACT_WRAPPER))
    }

    fun genericModel(key: Key, data: Writable) {
        return modifiedModel(ModelModifierConfig(key, data, ModelModifierFeature.Mappers.SIMPLE_WRAPPER))
    }

    fun variantModel(config: ModelVariantConfig): Key {
        return apply<ModelVariantConfig, Key, ModelVariantFeature>(config)
    }

    fun variantModel(target: Key, texture: Key): Key {
        return apply<ModelVariantConfig, Key, ModelVariantFeature>(ModelVariantConfig(target, texture, null))
    }

    fun linkModel(config: PredicateConfig) {
        apply<PredicateConfig, Unit, PredicateFeature>(config)
    }

    fun linkNoteBlock(vararg keys: Key) {
        apply<PredicateConfig, Unit, PredicateFeature>(NoteBlockPredicateConfig(keys.toList()))
    }

    fun linkMushroomBlock(vararg keys: Key) {
        apply<PredicateConfig, Unit, PredicateFeature>(MushroomBlockPredicateConfig(keys.toList()))
    }

    fun linkItemModel(target: Key, parent: Key?, vararg keys: Key) {
        apply<PredicateConfig, Unit, PredicateFeature>(CustomModelDataPredicateConfig(target = target, parent = parent, models = keys.toList()))
    }

    fun linkItemModel(target: Key, vararg keys: Key) {
        apply<PredicateConfig, Unit, PredicateFeature>(CustomModelDataPredicateConfig(target = target, parent = Key.key("minecraft", "item/generated"), models = keys.toList()))
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