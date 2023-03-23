package com.mail.ann.backend

import com.mail.ann.Account
import com.mail.ann.backend.api.Backend

interface BackendFactory {
    fun createBackend(account: Account): Backend
}
