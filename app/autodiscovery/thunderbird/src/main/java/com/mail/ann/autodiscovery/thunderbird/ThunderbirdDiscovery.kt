package com.mail.ann.autodiscovery.thunderbird

import com.mail.ann.autodiscovery.api.ConnectionSettingsDiscovery
import com.mail.ann.autodiscovery.api.DiscoveryResults
import com.mail.ann.autodiscovery.api.DiscoveryTarget

class ThunderbirdDiscovery(
    private val fetcher: ThunderbirdAutoconfigFetcher,
    private val parser: ThunderbirdAutoconfigParser
) : ConnectionSettingsDiscovery {

    override fun discover(email: String, target: DiscoveryTarget): DiscoveryResults? {
        val autoconfigInputStream = fetcher.fetchAutoconfigFile(email) ?: return null

        return autoconfigInputStream.use {
            parser.parseSettings(it, email)
        }
    }

    override fun toString(): String = "Thunderbird autoconfig"
}
