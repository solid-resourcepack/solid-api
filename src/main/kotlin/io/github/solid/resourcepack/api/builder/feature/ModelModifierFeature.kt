package io.github.solid.resourcepack.api.builder.feature

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.github.solid.resourcepack.api.builder.ResourcePackFeature
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.base.Writable

class ModelModifierFeature : ResourcePackFeature<ModelModifierConfig, Unit> {

    private val gson = Gson()
    override fun apply(config: ModelModifierConfig, pack: ResourcePack) {
        if(pack.model(config.key) != null) return
        val model = gson.fromJson(config.data.toUTF8String(), JsonObject::class.java)
        val obj = model.get("textures").asJsonObject
        val map = obj.asMap()
        map.keys.forEach { obj.addProperty(it, config.mapper(it)) }
        model.add("textures", obj)
        val modified = Writable.stringUtf8(Gson().toJson(model))
        pack.unknownFile("assets/${config.key.namespace()}/models/${config.key.value()}.json", modified)
    }

    object Mappers {
        val ABSTRACT_WRAPPER: (String) -> String = { key -> "#$key" }
        val SIMPLE_WRAPPER: (String) -> String = { "#layer0" }
    }
}

data class ModelModifierConfig(
    val key: Key,
    val data: Writable,
    val mapper: (String) -> String
)
