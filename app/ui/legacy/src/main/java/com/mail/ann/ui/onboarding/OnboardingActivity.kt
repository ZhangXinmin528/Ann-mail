package com.mail.ann.ui.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.mail.ann.ui.R
import com.mail.ann.ui.base.AnnActivity
import com.mail.ann.ui.base.extensions.findNavController

/**
 * 设置新账户
 */
class OnboardingActivity : AnnActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.activity_onboarding)
        setTitle(R.string.account_setup_basics_title)

        initializeActionBar()
    }

    private fun initializeActionBar() {
        val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = setOf(R.id.welcomeScreen))

        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        @JvmStatic
        fun launch(activity: Activity) {
            val intent = Intent(activity, OnboardingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            activity.startActivity(intent)
        }
    }
}
