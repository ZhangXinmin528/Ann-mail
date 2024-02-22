
package com.mail.ann.activity.setup;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mail.ann.Account;
import com.mail.ann.Preferences;
import com.mail.ann.activity.MessageList;
import com.mail.ann.helper.Utility;
import com.mail.ann.ui.R;
import com.mail.ann.ui.base.AnnActivity;


/**
 * 快要完成了
 * 设置账户名称；
 * 设置邮件签名；
 */
public class AccountSetupNames extends AnnActivity implements OnClickListener {
    private static final String EXTRA_ACCOUNT = "account";

    private EditText mDescription;

    private EditText mName;

    private Account mAccount;

    private Button mDoneButton;

    public static void actionSetNames(Context context, Account account) {
        Intent i = new Intent(context, AccountSetupNames.class);
        i.putExtra(EXTRA_ACCOUNT, account.getUuid());
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.account_setup_names);
        setTitle(R.string.account_setup_names_title);

        mDescription = findViewById(R.id.account_description);
        mName = findViewById(R.id.account_name);
        mDoneButton = findViewById(R.id.done);
        mDoneButton.setOnClickListener(this);

        TextWatcher validationTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                validateFields();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        mName.addTextChangedListener(validationTextWatcher);

        mName.setKeyListener(TextKeyListener.getInstance(false, Capitalize.WORDS));

        String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
        mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

        String senderName = mAccount.getSenderName();
        if (senderName != null) {
            mName.setText(senderName);
        }

        if (!Utility.requiredFieldValid(mName)) {
            mDoneButton.setEnabled(false);
        }
    }

    private void validateFields() {
        mDoneButton.setEnabled(Utility.requiredFieldValid(mName));
        Utility.setCompoundDrawablesAlpha(mDoneButton, mDoneButton.isEnabled() ? 255 : 128);
    }

    protected void onNext() {
        if (Utility.requiredFieldValid(mDescription)) {
            mAccount.setName(mDescription.getText().toString());
        }
        mAccount.setSenderName(mName.getText().toString());
        mAccount.markSetupFinished();
        Preferences.getPreferences(getApplicationContext()).saveAccount(mAccount);
        finishAffinity();
        //进入邮件列表
        MessageList.launch(this, mAccount);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.done) {
            onNext();
        }
    }
}
