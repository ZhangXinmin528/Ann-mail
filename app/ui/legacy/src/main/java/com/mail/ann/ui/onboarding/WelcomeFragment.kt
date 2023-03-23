package com.mail.ann.ui.onboarding

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mail.ann.ui.BuildConfig
import com.mail.ann.ui.R
import com.mail.ann.ui.helper.HtmlToSpanned
import com.mail.ann.ui.observeNotNull
import com.mail.ann.ui.settings.import.SettingsImportResultViewModel
import com.mail.ann.ui.settings.import.SettingsImportSuccess
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * 首次进入
 */
class WelcomeFragment : Fragment() {
    private val htmlToSpanned: HtmlToSpanned by inject()
    private val importResultViewModel: SettingsImportResultViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcome: TextView = view.findViewById(R.id.welcome_message)
        welcome.text = htmlToSpanned.convert(getString(R.string.accounts_welcome))
        welcome.movementMethod = LinkMovementMethod.getInstance()

        //创建账户
        view.findViewById<View>(R.id.next).setOnClickListener { launchAccountSetup() }
        //导入设置
        view.findViewById<View>(R.id.import_settings).setOnClickListener { launchImportSettings() }

        importResultViewModel.settingsImportResult.observeNotNull(this) {
            if (it == SettingsImportSuccess) {
                launchMessageList()
            }
        }
    }

    private fun launchAccountSetup() {
        if (BuildConfig.USE_NEW_SETUP_UI_FOR_ONBOARDING) {
            findNavController().navigate(R.id.action_welcomeScreen_to_newAddAccountScreen)
        } else {
            findNavController().navigate(R.id.action_welcomeScreen_to_addAccountScreen)
            requireActivity().finish()
        }
    }

    private fun launchImportSettings() {
        findNavController().navigate(R.id.action_welcomeScreen_to_settingsImportScreen)
    }

    private fun launchMessageList() {
        findNavController().navigate(R.id.action_welcomeScreen_to_messageListScreen)
        requireActivity().finish()
    }
}
