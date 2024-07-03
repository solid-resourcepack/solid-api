package de.dayeeet.solid.builder

import team.unnamed.creative.ResourcePack

interface ResourcePackFeature<Config> {
    fun apply(config: Config, pack: ResourcePack, ref: AdvancedResourcePack)
}