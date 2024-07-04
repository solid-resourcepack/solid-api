import de.dayeeet.solid.builder.AdvancedResourcePack
import net.kyori.adventure.key.Key
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader
import java.io.File
import java.io.FileOutputStream

val BOOST_ARROW = Key.key("test", "item/boost_arrow")
fun main() {
    val pack = AdvancedResourcePack(MinecraftResourcePackReader.minecraft().readFromZipFile(File("test.zip")))
    val mapper = TestMapper()
    val fog_1 = pack.variantModel(Key.key("test", "item/fog"), Key.key("test", "item/fog1"))
    pack.linkItemModel(
        target = Key.key("minecraft", "item/paper"),
        parent = Key.key("minecraft", "item/generated"),
        BOOST_ARROW,
        Key.key("test", "boost/fog"),
        Key.key("test", "boost/fog_alt"),
    )
    pack.map(
        mapper,
        AdvancedResourcePack.Filters.MINECRAFT_FILTER,
        { model -> model.key().value().startsWith("item/") })
    val build = pack.build()
    val stream = FileOutputStream(File("test.zip"))
    build.data().write(stream)
    stream.close()
    println(mapper.get(BOOST_ARROW))
}