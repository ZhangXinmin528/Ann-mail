package com.mail.ann.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.mail.ann.ui.R
import com.mail.ann.ui.base.AnnActivity
import com.mail.ann.ui.base.extensions.findNavController

class SettingsActivity : AnnActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.activity_settings)

        initializeActionBar()
    }

    private fun initializeActionBar() {
        // Empty set of top level destinations so the app bar's "up" button is also displayed at the start destination
        val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = emptySet())

        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp() || navigateUpBySimulatedBackButtonPress()
    }

    private fun navigateUpBySimulatedBackButtonPress(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        @JvmStatic fun launch(activity: Activity) {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
