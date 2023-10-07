package com.mail.ann.activity.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mail.ann.Account
import com.mail.ann.Preferences
import com.mail.ann.helper.EmailHelper.getDomainFromEmailAddress
import com.mail.ann.mail.ConnectionSecurity
import com.mail.ann.mail.ServerSettings
import com.mail.ann.mailstore.SpecialLocalFoldersCreator
import com.mail.ann.preferences.Protocols
import com.mail.ann.setup.ServerNameSuggester
import com.mail.ann.ui.R
import com.mail.ann.ui.base.AnnActivity
import org.koin.android.ext.android.inject

/**
 * Prompts the user to select an account type. The account type, along with the
 * passed in email address, password and makeDefault are then passed on to the
 * AccountSetupIncoming activity.
 * 设置账户类型
 */
class AccountSetupAccountType : AnnActivity() {
    private val preferences: Preferences by inject()
    private val serverNameSuggester: ServerNameSuggester by inject()
    private val localFoldersCreator: SpecialLocalFoldersCreator by inject()

    private lateinit var account: Account
    private var makeDefault = false
    private lateinit var initialAccountSettings: InitialAccountSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.account_setup_account_type)
        setTitle(R.string.account_setup_account_type_title)

        decodeArguments()

        findViewById<View>(R.id.pop).setOnClickListener { setupPop3Account() }
        findViewById<View>(R.id.imap).setOnClickListener { setupImapAccount() }
    }

    private fun decodeArguments() {
        val accountUuid = intent.getStringExtra(EXTRA_ACCOUNT) ?: error("No account UUID provided")
        account = preferences.getAccount(accountUuid) ?: error("No account with given UUID found")
        makeDefault = intent.getBooleanExtra(EXTRA_MAKE_DEFAULT, false)
        initialAccountSettings = intent.getParcelableExtra(EXTRA_INITIAL_ACCOUNT_SETTINGS)
            ?: error("Initial account settings are missing")
    }

    private fun setupPop3Account() {
        setupAccount(Protocols.POP3)
    }

    private fun setupImapAccount() {
        setupAccount(Protocols.IMAP)
    }

    private fun setupAccount(serverType: String) {
        setupStoreAndSmtpTransport(serverType)
        createSpecialLocalFolders()
        //收件服务器设置
        returnAccountTypeSelectionResult()
    }

    private fun setupStoreAndSmtpTransport(serverType: String) {
        val domainPart = getDomainFromEmailAddress(account.email) ?: error("Couldn't get domain from email address")

        initializeIncomingServerSettings(serverType, domainPart)
        initializeOutgoingServerSettings(domainPart)
    }

    private fun initializeIncomingServerSettings(serverType: String, domainPart: String) {
        val suggestedStoreServerName = serverNameSuggester.suggestServerName(serverType, domainPart)
        val storeServer = ServerSettings(
            serverType,
            suggestedStoreServerName,
            -1,
            ConnectionSecurity.SSL_TLS_REQUIRED,
            initialAccountSettings.authenticationType,
            initialAccountSettings.email,
            initialAccountSettings.password,
            initialAccountSettings.clientCertificateAlias
        )
        account.incomingServerSettings = storeServer
    }

    private fun initializeOutgoingServerSettings(domainPart: String) {
        val suggestedTransportServerName = serverNameSuggester.suggestServerName(Protocols.SMTP, domainPart)
        val transportServer = ServerSettings(
            Protocols.SMTP,
            suggestedTransportServerName,
            -1,
            ConnectionSecurity.STARTTLS_REQUIRED,
            initialAccountSettings.authenticationType,
            initialAccountSettings.email,
            initialAccountSettings.password,
            initialAccountSettings.clientCertificateAlias
        )
        account.outgoingServerSettings = transportServer
    }

    private fun createSpecialLocalFolders() {
        localFoldersCreator.createSpecialLocalFolders(account)
    }

    //收件服务器设置
    private fun returnAccountTypeSelectionResult() {
        AccountSetupIncoming.actionIncomingSettings(this, account, makeDefault)
    }

    companion object {
        private const val EXTRA_ACCOUNT = "account"
        private const val EXTRA_MAKE_DEFAULT = "makeDefault"
        private const val EXTRA_INITIAL_ACCOUNT_SETTINGS = "initialAccountSettings"

        @JvmStatic
        fun actionSelectAccountType(
            context: Context,
            account: Account,
            makeDefault: Boolean,
            initialAccountSettings: InitialAccountSettings
        ) {
            val intent = Intent(context, AccountSetupAccountType::class.java).apply {
                putExtra(EXTRA_ACCOUNT, account.uuid)
                putExtra(EXTRA_MAKE_DEFAULT, makeDefault)
                putExtra(EXTRA_INITIAL_ACCOUNT_SETTINGS, initialAccountSettings)
            }
            context.startActivity(intent)
        }
    }
}
