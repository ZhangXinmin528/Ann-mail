package app.k9mail.backend.demo

import com.mail.ann.mail.FolderType
import com.squareup.moshi.JsonClass

typealias MessageStoreInfo = Map<String, FolderData>

@JsonClass(generateAdapter = true)
data class FolderData(
    val name: String,
    val type: FolderType,
    val messageServerIds: List<String>
)
