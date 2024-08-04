package io.github.solid.resourcepack.api.material

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.block.data.Orientable

/**
 * Represents a material that provides all data necessary to be used in a resource pack
 */
data class SolidMaterial(
    val material: Material,
    val minecraftKey: Key,
    val minecraftParent: Key?,
    val minecraftTextures: Map<String, Key>
) {

    /**
     *  Used to fine tune wrong results from the from(material: Material) method
     *  @return A new [SolidMaterial] with updated [SolidMaterial.minecraftParent]
     *  */
    fun minecraftParent(key: Key?): SolidMaterial {
        return SolidMaterial(material, minecraftKey, key, minecraftTextures)
    }

    /**
     *  Used to fine tune wrong results from the from(material: Material) method
     *  @return A new [SolidMaterial] with updated [SolidMaterial.minecraftTextures]
     *  */
    fun minecraftTextures(textures: Map<String, Key>): SolidMaterial {
        return SolidMaterial(material, minecraftKey, minecraftParent, textures)
    }

    companion object {

        /**
         *  Used to create very specific resource pack materials from scratch
         *  @return A [SolidMaterial]
         *  */
        fun from(minecraftKey: Key, minecraftParent: Key?, textureVariables: Map<String, Key>): SolidMaterial {
            val material = Material.matchMaterial(minecraftKey.value().substringAfterLast("/").uppercase())
                ?: throw IllegalArgumentException("Material not found for key: $minecraftKey")
            return SolidMaterial(material, minecraftKey, minecraftParent, textureVariables)
        }

        /**
         * Used to create a resource pack material from a Bukkit material
         * @return A [SolidMaterial] or null if the material is not supported
         */
        fun from(material: Material): SolidMaterial? {
            return try {
                fromUnsafe(material)
            } catch (e: Exception) {
                null
            }
        }

        private fun fromUnsafe(material: Material): SolidMaterial {
            var prefix: String? = null
            var parent: Key? = null
            val textures = mutableMapOf<String, Key>()
            if (material.isBlock) {
                prefix = "block"
                parent = parseBlockParent(material)
                textures.putAll(parseBlockTexture(material))
            } else if (material.isItem) {
                prefix = "item"
                parent = parseItemParent(material)
                textures.putAll(parseItemTexture(material))
            }
            if (prefix == null) {
                throw IllegalArgumentException("Material is not a block or item")
            }

            return SolidMaterial(
                material,
                Key.key(Key.MINECRAFT_NAMESPACE, "$prefix/${material.name.lowercase()}"),
                parent,
                textures
            )
        }

        private fun parseBlockTexture(material: Material): Map<String, Key> {
            val key = Key.key(Key.MINECRAFT_NAMESPACE, "block/${material.name.lowercase()}")
            if (material.isSolid && material.asBlockType()!!.createBlockData() !is Orientable) {
                return mapOf(
                    "all" to key,
                    "particle" to key
                )
            }
            throw IllegalArgumentException("Textures not found for material: $material! This material is not yet supported, please use SolidMaterial#from(Material, Key, Map<String, Key>)")
        }

        private fun parseBlockParent(material: Material): Key? {
            if (material.isSolid && material.asBlockType()!!.createBlockData() !is Orientable) {
                return Key.key(Key.MINECRAFT_NAMESPACE, "block/cube_all")
            }

            throw IllegalArgumentException("Parent not found for material: $material! This material is not yet supported, please use SolidMaterial#from(Material, Key, Map<String, Key>)")
        }

        private fun parseItemTexture(material: Material): Map<String, Key> {
            val key = Key.key(Key.MINECRAFT_NAMESPACE, "block/${material.name.lowercase()}")
            return mapOf("layer0" to key)
        }

        private fun parseItemParent(material: Material): Key {
            if (material.maxDurability > 1) {
                return Key.key(Key.MINECRAFT_NAMESPACE, "item/handheld")
            }
            return Key.key(Key.MINECRAFT_NAMESPACE, "item/generated")
        }

    }
}