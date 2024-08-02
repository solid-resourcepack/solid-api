package io.github.solid.resourcepack.api.builder

import team.unnamed.creative.ResourcePack

interface ResourcePackFeature<Config, Return> {
    fun apply(config: Config, pack: ResourcePack): Return
}