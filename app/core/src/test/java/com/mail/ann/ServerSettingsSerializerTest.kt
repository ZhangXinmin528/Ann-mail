package com.mail.ann

import com.mail.ann.mail.AuthType
import com.mail.ann.mail.ConnectionSecurity
import com.mail.ann.mail.ServerSettings
import com.mail.ann.mail.store.imap.ImapStoreSettings
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test

class ServerSettingsSerializerTest {
    private val serverSettingsSerializer = ServerSettingsSerializer()

    @Test
    fun `serialize and deserialize IMAP server settings`() {
        val serverSettings = ServerSettings(
            type = "imap",
            host = "imap.domain.example",
            port = 143,
            connectionSecurity = ConnectionSecurity.STARTTLS_REQUIRED,
            authenticationType = AuthType.PLAIN,
            username = "user",
            password = null,
            clientCertificateAlias = "alias",
            extra = ImapStoreSettings.createExtra(autoDetectNamespace = true, pathPrefix = null)
        )

        val json = serverSettingsSerializer.serialize(serverSettings)
        val deserializedServerSettings = serverSettingsSerializer.deserialize(json)

        assertThat(deserializedServerSettings).isEqualTo(serverSettings)
    }

    @Test
    fun `serialize and deserialize POP3 server settings`() {
        val serverSettings = ServerSettings(
            type = "pop3",
            host = "pop3.domain.example",
            port = 995,
            connectionSecurity = ConnectionSecurity.SSL_TLS_REQUIRED,
            authenticationType = AuthType.PLAIN,
            username = "user",
            password = "password",
            clientCertificateAlias = null
        )

        val json = serverSettingsSerializer.serialize(serverSettings)
        val deserializedServerSettings = serverSettingsSerializer.deserialize(json)

        assertThat(deserializedServerSettings).isEqualTo(serverSettings)
    }

    @Test
    fun `deserialize JSON with missing type`() {
        val json = """
            {
                "host": "imap.domain.example",
                "port": 993,
                "connectionSecurity": "SSL_TLS_REQUIRED",
                "authenticationType": "PLAIN",
                "username": "user",
                "password": "pass",
                "clientCertificateAlias": null
            }
        """.trimIndent()

        try {
            serverSettingsSerializer.deserialize(json)
            fail("Expected exception")
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat().isEqualTo("'type' must not be missing")
        }
    }
}
