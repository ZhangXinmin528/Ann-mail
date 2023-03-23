package com.mail.ann.crypto.openpgp

import com.mail.ann.crypto.EncryptionExtractor
import com.mail.ann.crypto.EncryptionResult
import com.mail.ann.mail.Message
import com.mail.ann.message.extractors.TextPartFinder

class OpenPgpEncryptionExtractor internal constructor(
    private val encryptionDetector: EncryptionDetector
) : EncryptionExtractor {

    override fun extractEncryption(message: Message): EncryptionResult? {
        return if (encryptionDetector.isEncrypted(message)) {
            EncryptionResult(ENCRYPTION_TYPE, 0)
        } else {
            null
        }
    }

    companion object {
        const val ENCRYPTION_TYPE = "openpgp"

        @JvmStatic
        fun newInstance(): OpenPgpEncryptionExtractor {
            val textPartFinder = TextPartFinder()
            val encryptionDetector = EncryptionDetector(textPartFinder)
            return OpenPgpEncryptionExtractor(encryptionDetector)
        }
    }
}
