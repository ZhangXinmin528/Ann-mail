package com.mail.ann.preferences.migrations

import android.database.sqlite.SQLiteDatabase
import com.mail.ann.ServerSettingsSerializer
import com.mail.ann.preferences.Protocols

/**
 * Rewrite 'folderPushMode' value of non-IMAP accounts to 'NONE'.
 */
class StorageMigrationTo14(
    private val db: SQLiteDatabase,
    private val migrationsHelper: StorageMigrationsHelper
) {
    private val serverSettingsSerializer = ServerSettingsSerializer()

    fun disablePushFoldersForNonImapAccounts() {
        val accountUuidsListValue = migrationsHelper.readValue(db, "accountUuids")
        if (accountUuidsListValue == null || accountUuidsListValue.isEmpty()) {
            return
        }

        val accountUuids = accountUuidsListValue.split(",")
        for (accountUuid in accountUuids) {
            disablePushFolders(accountUuid)
        }
    }

    private fun disablePushFolders(accountUuid: String) {
        val json = migrationsHelper.readValue(db, "$accountUuid.incomingServerSettings") ?: return
        val serverSettings = serverSettingsSerializer.deserialize(json)
        if (serverSettings.type != Protocols.IMAP) {
            migrationsHelper.writeValue(db, "$accountUuid.folderPushMode", "NONE")
        }
    }
}
