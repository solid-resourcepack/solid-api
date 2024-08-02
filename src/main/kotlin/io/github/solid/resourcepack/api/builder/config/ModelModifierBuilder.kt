package io.github.solid.resourcepack.api.builder.config

import io.github.solid.resourcepack.api.builder.feature.ModelModifierConfig
import net.kyori.adventure.key.Key
import team.unnamed.creative.base.Writable

object ModelModifier {
    fun simple(key: Key, data: Writable): ModelModifierConfig {
        return ModelModifierBuilder().key(key).data(data).mapper(Mapper.SIMPLE_WRAPPER).build()
    }
    fun abstract(key: Key, data: Writable): ModelModifierConfig {
        return ModelModifierBuilder().key(key).data(data).mapper(Mapper.ABSTRACT_WRAPPER).build()
    }

    fun builder(): ModelModifierBuilder {
        return ModelModifierBuilder()
    }

    object Mapper {
        val ABSTRACT_WRAPPER: (String) -> String = { key -> "#$key" }
        val SIMPLE_WRAPPER: (String) -> String = { "#layer0" }
    }
}

class ModelModifierBuilder : ConfigBuilder<ModelModifierConfig> {

    private lateinit var key: Key
    private lateinit var data: Writable
    private lateinit var mapper: (String) -> String

    fun key(key: Key): ModelModifierBuilder {
        this.key = key
        return this
    }

    fun data(data: Writable): ModelModifierBuilder {
        this.data = data
        return this
    }

    fun mapper(mapper: (String) -> String): ModelModifierBuilder {
        this.mapper = mapper
        return this
    }

    override fun build(): ModelModifierConfig {
        return ModelModifierConfig(key, data, mapper)
    }
}