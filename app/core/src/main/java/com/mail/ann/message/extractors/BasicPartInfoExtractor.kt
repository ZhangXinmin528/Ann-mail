package com.mail.ann.message.extractors

import com.mail.ann.helper.MimeTypeUtil
import com.mail.ann.mail.Part
import com.mail.ann.mail.internet.MimeParameterDecoder
import com.mail.ann.mail.internet.MimeValue

private const val FALLBACK_NAME = "noname"

/**
 * Extract a display name and the size from the headers of a message part.
 */
class BasicPartInfoExtractor {
    fun extractPartInfo(part: Part): BasicPartInfo {
        val contentDisposition = part.disposition?.toMimeValue()

        return BasicPartInfo(
            displayName = part.getDisplayName(contentDisposition),
            size = contentDisposition?.getParameter("size")?.toLongOrNull()
        )
    }

    fun extractDisplayName(part: Part): String {
        return part.getDisplayName()
    }

    private fun Part.getDisplayName(contentDisposition: MimeValue? = disposition?.toMimeValue()): String {
        return contentDisposition?.getParameter("filename")
            ?: contentType?.getParameter("name")
            ?: mimeType.toDisplayName()
    }

    private fun String?.toDisplayName(): String {
        val extension = this?.let { mimeType -> MimeTypeUtil.getExtensionByMimeType(mimeType) }
        return if (extension.isNullOrEmpty()) FALLBACK_NAME else "$FALLBACK_NAME.$extension"
    }

    private fun String.toMimeValue(): MimeValue = MimeParameterDecoder.decode(this)

    private fun MimeValue.getParameter(name: String): String? = parameters[name.lowercase()]

    private fun String.getParameter(name: String): String? {
        val mimeValue = MimeParameterDecoder.decode(this)
        return mimeValue.parameters[name.lowercase()]
    }
}

data class BasicPartInfo(
    val displayName: String,
    val size: Long?
)
