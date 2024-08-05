package io.github.solid.resourcepack.api.builder.config

interface ConfigBuilder<Config> {
    fun build(): Config
}