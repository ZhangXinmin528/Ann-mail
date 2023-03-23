package com.mail.ann.storage.messages

import com.mail.ann.mailstore.LockableDatabase

internal class CheckFolderOperations(private val lockableDatabase: LockableDatabase) {
    fun areAllIncludedInUnifiedInbox(folderIds: Collection<Long>): Boolean {
        return lockableDatabase.execute(false) { database ->
            var allIncludedInUnifiedInbox = true

            performChunkedOperation(
                arguments = folderIds,
                argumentTransformation = Long::toString
            ) { selectionSet, selectionArguments ->
                if (allIncludedInUnifiedInbox) {
                    database.rawQuery(
                        "SELECT COUNT(id) FROM folders WHERE integrate = 1 AND id $selectionSet", selectionArguments
                    ).use { cursor ->
                        if (cursor.moveToFirst()) {
                            val count = cursor.getInt(0)
                            if (count != selectionArguments.size) {
                                allIncludedInUnifiedInbox = false
                            }
                        } else {
                            allIncludedInUnifiedInbox = false
                        }
                    }
                }
            }

            allIncludedInUnifiedInbox
        }
    }
}
