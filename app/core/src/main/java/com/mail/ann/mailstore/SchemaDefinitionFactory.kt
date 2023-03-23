package com.mail.ann.mailstore

import com.mail.ann.mailstore.LockableDatabase.SchemaDefinition

interface SchemaDefinitionFactory {
    val databaseVersion: Int

    fun createSchemaDefinition(migrationsHelper: MigrationsHelper): SchemaDefinition
}
