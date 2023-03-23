package app.k9mail.dev

import app.k9mail.backend.demo.DemoBackend
import com.mail.ann.Account
import com.mail.ann.backend.BackendFactory
import com.mail.ann.backend.api.Backend
import com.mail.ann.mailstore.K9BackendStorageFactory

class DemoBackendFactory(private val backendStorageFactory: K9BackendStorageFactory) : BackendFactory {
    override fun createBackend(account: Account): Backend {
        val backendStorage = backendStorageFactory.createBackendStorage(account)
        return DemoBackend(backendStorage)
    }
}
