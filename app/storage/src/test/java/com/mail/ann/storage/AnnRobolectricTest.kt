package com.mail.ann.storage

import android.app.Application
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * A Robolectric test that creates an instance of our [Application] test class [TestApp].
 *
 * See also [RobolectricTest].
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class)
abstract class AnnRobolectricTest : AutoCloseKoinTest()
