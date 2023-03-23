package com.mail.ann.contacts

import com.mail.ann.mail.Address

class ContactLetterExtractor {
    fun extractContactLetter(address: Address): String {
        val displayName = address.personal ?: address.address

        val matchResult = EXTRACT_LETTER_PATTERN.find(displayName)
        return matchResult?.value?.uppercase() ?: FALLBACK_CONTACT_LETTER
    }

    companion object {
        private val EXTRACT_LETTER_PATTERN = Regex("\\p{L}\\p{M}*")
        private const val FALLBACK_CONTACT_LETTER = "?"
    }
}
