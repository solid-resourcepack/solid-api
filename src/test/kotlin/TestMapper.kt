import de.dayeeet.solid.mappings.ModelMapper
import net.kyori.adventure.key.Key
import team.unnamed.creative.model.ItemPredicate

class TestMapper: ModelMapper<String> {

    private val registry = mutableMapOf<Key, String>()
    override fun register(key: Key, value: List<ItemPredicate>) {
        registry[key] = value.toString()
    }

    override fun get(key: Key): String? {
        return registry.getOrDefault(key, null)
    }
}