import io.github.solid.resourcepack.api.builder.AdvancedResourcePack
import io.github.solid.resourcepack.api.builder.config.ModelVariant
import io.github.solid.resourcepack.api.builder.config.Predicate
import io.github.solid.resourcepack.api.key.KeyType
import io.github.solid.resourcepack.api.key.SolidKey
import io.github.solid.resourcepack.api.material.SolidMaterial
import net.kyori.adventure.key.Key
import org.bukkit.Material
import team.unnamed.creative.ResourcePack

val BOOST_ARROW = Key.key("test", "item/boost_arrow")
fun main() {
    val pack = AdvancedResourcePack(ResourcePack.resourcePack())
    pack.apply(
        Predicate.customModelData(
            SolidMaterial.from(
                SolidKey.from(Material.POPPED_CHORUS_FRUIT, KeyType.ITEM),
                SolidKey.ITEM_GENERATED,
                ModelVariant.Textures.simple(SolidKey.from(Material.POPPED_CHORUS_FRUIT, KeyType.ITEM))
            )
        ).models(BOOST_ARROW).build()
    )

    val mapper = TestMapper()
    pack.map(
        mapper,
        AdvancedResourcePack.Filters.MINECRAFT_FILTER,
        { model -> model.key().value().startsWith("item/") })
    println(mapper.get(BOOST_ARROW))
}