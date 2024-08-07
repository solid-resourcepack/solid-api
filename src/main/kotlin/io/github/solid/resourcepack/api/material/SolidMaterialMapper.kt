package io.github.solid.resourcepack.api.material

import io.github.solid.resourcepack.material.SolidBlockMaterial
import io.github.solid.resourcepack.material.SolidItemMaterial
import io.github.solid.resourcepack.material.SolidMaterial
import org.bukkit.Material
import org.jetbrains.annotations.ApiStatus.Experimental

object SolidMaterialMapper {

    /**
     * Converts a [Material] to a [SolidMaterial]
     * IMPORTANT: Only works in a server context
     */
    @Experimental
    fun from(material: Material): SolidMaterial {
        if (material.isItem) {
            return SolidItemMaterial.valueOf(material.name).toGeneric()
        }
        if (material.isBlock) {
            return SolidBlockMaterial.valueOf(material.name).toGeneric()
        }
        throw IllegalArgumentException("Material $material is not supported")
    }

    fun from(material: Material, type: MaterialType): SolidMaterial {
        val returned = mutableListOf<SolidMaterial>()
        if (type == MaterialType.ITEM || type == MaterialType.BOTH) {
            try {
                returned.add(SolidItemMaterial.valueOf(material.name).toGeneric())
            } catch (ignored: Exception) {
            }
        }

        if (type == MaterialType.BLOCK || type == MaterialType.BOTH) {
            try {
                returned.add(SolidBlockMaterial.valueOf(material.name).toGeneric())
            } catch (ignored: Exception) {
            }
        }

        if (returned.size != 1) throw IllegalArgumentException("Material $material is not supported")
        return returned.first()
    }
}

enum class MaterialType {
    ITEM,
    BLOCK,
    BOTH;
}