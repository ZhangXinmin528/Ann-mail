package com.mail.ann.sasl

import com.google.common.truth.Truth.assertThat
import okio.ByteString.Companion.decodeBase64
import org.junit.Test

class OAuthBearerTest {
    @Test
    fun `username that does not need encoding`() {
        val username = "user@domain.example"
        val token = "token"

        val result = buildOAuthBearerInitialClientResponse(username, token)

        assertThat(result).isEqualTo("bixhPXVzZXJAZG9tYWluLmV4YW1wbGUsAWF1dGg9QmVhcmVyIHRva2VuAQE=")
        assertThat(result.decodeBase64()?.utf8())
            .isEqualTo("n,a=user@domain.example,\u0001auth=Bearer token\u0001\u0001")
    }

    @Test
    fun `username contains equal sign that needs to be encoded`() {
        val username = "user=name@domain.example"
        val token = "token"

        val result = buildOAuthBearerInitialClientResponse(username, token)

        assertThat(result).isEqualTo("bixhPXVzZXI9M0RuYW1lQGRvbWFpbi5leGFtcGxlLAFhdXRoPUJlYXJlciB0b2tlbgEB")
        assertThat(result.decodeBase64()?.utf8())
            .isEqualTo("n,a=user=3Dname@domain.example,\u0001auth=Bearer token\u0001\u0001")
    }
}
