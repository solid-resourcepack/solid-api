package io.github.solid.resourcepack.api.key

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.jetbrains.annotations.ApiStatus.Experimental


object SolidKey {

    /**
     * ONLY WORKS WHEN USED IN A MINECRAFT SERVER CONTEXT
     */
    @Experimental
    fun from(material: Material): Key {
        val keyType: KeyType = if (material.isBlock) {
            KeyType.BLOCK
        } else if (material.isItem) {
            KeyType.ITEM
        } else {
            throw IllegalArgumentException("Material is not supported")
        }
        return from(material, keyType)
    }

    fun from(material: Material, keyType: KeyType): Key {
        return Key.key(Key.MINECRAFT_NAMESPACE, "${keyType.name.lowercase()}/${material.name.lowercase()}")
    }

    val ITEM_GENERATED = Key.key(Key.MINECRAFT_NAMESPACE, "item/generated")
    val ITEM_HANDHELD = Key.key(Key.MINECRAFT_NAMESPACE, "item/handheld")
    val CUBE_ALL = Key.key(Key.MINECRAFT_NAMESPACE, "block/cube_all")
}


enum class KeyType {
    ITEM,
    BLOCK
}