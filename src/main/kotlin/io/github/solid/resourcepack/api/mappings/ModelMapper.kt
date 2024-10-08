package io.github.solid.resourcepack.api.mappings

import net.kyori.adventure.key.Key
import team.unnamed.creative.model.ItemPredicate

interface ModelMapper<T> {
    fun register(key: Key, value: List<ItemPredicate>)
    fun get(key: Key): T?
}