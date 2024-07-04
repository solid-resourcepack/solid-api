package de.dayeeet.solid.builder

import team.unnamed.creative.ResourcePack

interface ResourcePackFeature<Config, Return> {
    fun apply(config: Config, pack: ResourcePack): Return
}