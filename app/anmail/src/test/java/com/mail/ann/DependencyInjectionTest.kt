package com.mail.ann

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.mail.ann.ui.changelog.ChangeLogMode
import com.mail.ann.ui.changelog.ChangelogViewModel
import com.mail.ann.ui.endtoend.AutocryptKeyTransferActivity
import com.mail.ann.ui.endtoend.AutocryptKeyTransferPresenter
import com.mail.ann.ui.folders.FolderNameFormatter
import com.mail.ann.ui.helper.SizeFormatter
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.logger.PrintLogger
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.openintents.openpgp.OpenPgpApiManager
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = com.mail.ann.App::class)
class DependencyInjectionTest : AutoCloseKoinTest() {
    val lifecycleOwner = mock<LifecycleOwner> {
        on { lifecycle } doReturn mock<Lifecycle>()
    }
    val autocryptTransferView = mock<AutocryptKeyTransferActivity>()

    @KoinInternalApi
    @Test
    fun testDependencyTree() {
        KoinJavaComponent.getKoin().setupLogger(PrintLogger())

        getKoin().checkModules {
            withParameter<OpenPgpApiManager> { lifecycleOwner }
            create<AutocryptKeyTransferPresenter> { parametersOf(lifecycleOwner, autocryptTransferView) }
            withParameter<FolderNameFormatter> { RuntimeEnvironment.application }
            withParameter<SizeFormatter> { RuntimeEnvironment.application }
            withParameter<ChangelogViewModel> { ChangeLogMode.CHANGE_LOG }
        }
    }
}
