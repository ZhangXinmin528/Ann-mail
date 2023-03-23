package com.mail.ann.mailstore

import com.mail.ann.mail.crlf
import com.mail.ann.mail.internet.MimeBodyPart
import com.mail.ann.mail.internet.MimeMessage
import com.mail.ann.mail.internet.MimeMultipart
import java.io.ByteArrayInputStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MimePartStreamParserTest {
    @Test
    fun innerMessage_DispositionInline() {
        val messageContent =
            """
            From: <x@example.org>
            To: <y@example.org>
            Subject: Testmail 1
            Content-Type: multipart/mixed; boundary=1
            
            --1
            Content-Type: text/plain
            
            some text in the first part
            --1
            Content-Type: message/rfc822; name="message"
            
            To: <z@example.org>
            Subject: Hi
            Date: now
            Content-Type: text/plain
            
            inner text
            --1--
            """.trimIndent().crlf()

        val msg = MimePartStreamParser.parse(null, ByteArrayInputStream(messageContent.toByteArray()))

        val body = msg.body as MimeMultipart
        assertEquals(2, body.count.toLong())

        val messagePart = body.getBodyPart(1) as MimeBodyPart
        assertEquals("message/rfc822", messagePart.mimeType)
        assertTrue(messagePart.body is MimeMessage)
    }

    @Test
    fun innerMessage_dispositionAttachment() {
        val messageContent =
            """
            From: <x@example.org>
            To: <y@example.org>
            Subject: Testmail 2
            Content-Type: multipart/mixed; boundary=1
            
            --1
            Content-Type: text/plain
            
            some text in the first part
            --1
            Content-Type: message/rfc822; name="message"
            Content-Disposition: attachment
            
            To: <z@example.org>
            Subject: Hi
            Date: now
            Content-Type: text/plain
            
            inner text
            --1--
            """.trimIndent().crlf()

        val msg = MimePartStreamParser.parse(null, ByteArrayInputStream(messageContent.toByteArray()))

        val body = msg.body as MimeMultipart
        assertEquals(2, body.count)

        val messagePart = body.getBodyPart(1) as MimeBodyPart
        assertEquals("message/rfc822", messagePart.mimeType)
        assertTrue(messagePart.body is DeferredFileBody)
    }
}
