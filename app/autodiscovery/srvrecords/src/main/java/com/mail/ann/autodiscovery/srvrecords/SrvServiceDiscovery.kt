package com.mail.ann.autodiscovery.srvrecords

import com.mail.ann.autodiscovery.api.ConnectionSettingsDiscovery
import com.mail.ann.autodiscovery.api.DiscoveredServerSettings
import com.mail.ann.autodiscovery.api.DiscoveryResults
import com.mail.ann.autodiscovery.api.DiscoveryTarget
import com.mail.ann.helper.EmailHelper
import com.mail.ann.mail.AuthType
import com.mail.ann.mail.ConnectionSecurity

class SrvServiceDiscovery(
    private val srvResolver: MiniDnsSrvResolver
) : ConnectionSettingsDiscovery {

    override fun discover(email: String, target: DiscoveryTarget): DiscoveryResults? {
        val domain = EmailHelper.getDomainFromEmailAddress(email) ?: return null
        val mailServicePriority = compareBy<MailService> { it.priority }.thenByDescending { it.security }

        val outgoingSettings = if (target.outgoing)
            listOf(SrvType.SUBMISSIONS, SrvType.SUBMISSION).flatMap { srvResolver.lookup(domain, it) }
                .sortedWith(mailServicePriority).map { newServerSettings(it, email) }
        else listOf()

        val incomingSettings = if (target.incoming)
            listOf(SrvType.IMAPS, SrvType.IMAP).flatMap { srvResolver.lookup(domain, it) }
                .sortedWith(mailServicePriority).map { newServerSettings(it, email) }
        else listOf()

        return DiscoveryResults(incoming = incomingSettings, outgoing = outgoingSettings)
    }
}

fun newServerSettings(service: MailService, email: String): DiscoveredServerSettings {
    return DiscoveredServerSettings(
        service.srvType.protocol,
        service.host,
        service.port,
        service.security,
        AuthType.PLAIN,
        email
    )
}

enum class SrvType(val label: String, val protocol: String, val assumeTls: Boolean) {
    SUBMISSIONS("_submissions", "smtp", true),
    SUBMISSION("_submission", "smtp", false),
    IMAPS("_imaps", "imap", true),
    IMAP("_imap", "imap", false)
}

data class MailService(
    val srvType: SrvType,
    val host: String,
    val port: Int,
    val priority: Int,
    val security: ConnectionSecurity
)
