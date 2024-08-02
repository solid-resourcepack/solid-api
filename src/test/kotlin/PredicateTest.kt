import io.github.solid.resourcepack.api.builder.AdvancedResourcePack
import io.github.solid.resourcepack.api.builder.config.ModelVariant
import io.github.solid.resourcepack.api.builder.config.PredicateFeature
import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader
import java.io.File
import java.io.FileOutputStream

val BOOST_ARROW = Key.key("test", "item/boost_arrow")
fun main() {
    val pack = AdvancedResourcePack(ResourcePack.resourcePack())
    pack.apply(PredicateFeature.noteBlock().models().build())
    val mapper = TestMapper()
    pack.map(
        mapper,
        AdvancedResourcePack.Filters.MINECRAFT_FILTER,
        { model -> model.key().value().startsWith("item/") })
    println(mapper.get(BOOST_ARROW))
}