package com.mail.ann.storage

import com.mail.ann.mailstore.LockableDatabase
import com.mail.ann.mailstore.MigrationsHelper
import com.mail.ann.mailstore.SchemaDefinitionFactory

class K9SchemaDefinitionFactory : SchemaDefinitionFactory {
    override val databaseVersion = StoreSchemaDefinition.DB_VERSION

    override fun createSchemaDefinition(migrationsHelper: MigrationsHelper): LockableDatabase.SchemaDefinition {
        return StoreSchemaDefinition(migrationsHelper)
    }
}
