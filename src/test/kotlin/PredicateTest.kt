import io.github.solid.resourcepack.api.builder.AdvancedResourcePack
import io.github.solid.resourcepack.api.builder.config.PredicateFeature
import io.github.solid.resourcepack.api.builder.config.SolidModel
import io.github.solid.resourcepack.api.predicate.EnumPredicateArgument
import io.github.solid.resourcepack.api.predicate.PredicateIncrementor
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack

val BOOST_ARROW = Key.key("test", "item/boost_arrow")
fun main() {
    val pack = AdvancedResourcePack(ResourcePack.resourcePack())
    pack.apply(SolidModel.itemModel(Key.key(Key.MINECRAFT_NAMESPACE, "item/paper"), BOOST_ARROW).build())

    val mapper = TestMapper()
    pack.map(
        mapper,
        AdvancedResourcePack.Filters.MINECRAFT_FILTER,
        { model -> model.key().value().startsWith("item/") })
    println(mapper.get(BOOST_ARROW))
}