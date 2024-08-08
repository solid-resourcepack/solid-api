import io.github.solid.resourcepack.api.builder.AdvancedResourcePack
import io.github.solid.resourcepack.api.builder.config.Predicate
import io.github.solid.resourcepack.material.SolidMaterial
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack

val BOOST_ARROW = Key.key("test", "item/boost_arrow")
fun main() {
    val pack = AdvancedResourcePack(ResourcePack.resourcePack())
    pack.apply(
        Predicate.customModelData(SolidMaterial.from(Key.key("minecraft", "item/stick"))).models(BOOST_ARROW).build()
    )

    val mapper = TestMapper()
    pack.map(
        mapper,
        AdvancedResourcePack.Filters.MINECRAFT_FILTER,
        { model -> model.key().value().startsWith("item/") })
    println(mapper.get(BOOST_ARROW))
}