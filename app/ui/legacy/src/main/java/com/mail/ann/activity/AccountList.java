package com.mail.ann.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mail.ann.Account;
import com.mail.ann.BaseAccount;
import com.mail.ann.FontSizes;
import com.mail.ann.Ann;
import com.mail.ann.Preferences;
import com.mail.ann.ui.R;
import com.mail.ann.search.SearchAccount;


/**
 * Activity displaying the list of accounts.
 *
 * <p>
 * Classes extending this abstract class have to provide an {@link #onAccountSelected(BaseAccount)}
 * method to perform an action when an account is selected.
 * </p>
 */
public abstract class AccountList extends AnnListActivity implements OnItemClickListener {
    private FontSizes mFontSizes = Ann.getFontSizes();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        setLayout(R.layout.account_list);

        ListView listView = getListView();
        listView.setOnItemClickListener(this);
        listView.setItemsCanFocus(false);
    }

    /**
     * Reload list of accounts when this activity is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        new LoadAccounts().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseAccount account = (BaseAccount) parent.getItemAtPosition(position);
        onAccountSelected(account);
    }

    /**
     * Create a new {@link AccountsAdapter} instance and assign it to the {@link ListView}.
     *
     * @param realAccounts
     *         An array of accounts to display.
     */
    public void populateListView(List<Account> realAccounts) {
        List<BaseAccount> accounts = new ArrayList<>();

        if (Ann.isShowUnifiedInbox()) {
            BaseAccount unifiedInboxAccount = SearchAccount.createUnifiedInboxAccount();
            accounts.add(unifiedInboxAccount);
        }

        accounts.addAll(realAccounts);
        AccountsAdapter adapter = new AccountsAdapter(accounts);
        ListView listView = getListView();
        listView.setAdapter(adapter);
        listView.invalidate();
    }

    /**
     * This method will be called when an account was selected.
     *
     * @param account
     *         The account the user selected.
     */
    protected abstract void onAccountSelected(BaseAccount account);

    class AccountsAdapter extends ArrayAdapter<BaseAccount> {
        public AccountsAdapter(List<BaseAccount> accounts) {
            super(AccountList.this, 0, accounts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BaseAccount account = getItem(position);

            final View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = getLayoutInflater().inflate(R.layout.accounts_item, parent, false);
            }

            AccountViewHolder holder = (AccountViewHolder) view.getTag();
            if (holder == null) {
                holder = new AccountViewHolder();
                holder.description = view.findViewById(R.id.description);
                holder.email = view.findViewById(R.id.email);
                holder.chip = view.findViewById(R.id.chip);

                view.setTag(holder);
            }

            String accountName = account.getName();
            if (accountName != null) {
                holder.description.setText(accountName);
                holder.email.setText(account.getEmail());
                holder.email.setVisibility(View.VISIBLE);
            } else {
                holder.description.setText(account.getEmail());
                holder.email.setVisibility(View.GONE);
            }

            if (account instanceof Account) {
                Account realAccount = (Account) account;
                holder.chip.setBackgroundColor(realAccount.getChipColor());
            } else {
                holder.chip.setBackgroundColor(0xff999999);
            }

            holder.chip.getBackground().setAlpha(255);

            mFontSizes.setViewTextSize(holder.description, mFontSizes.getAccountName());
            mFontSizes.setViewTextSize(holder.email, mFontSizes.getAccountDescription());


            return view;
        }

        class AccountViewHolder {
            public TextView description;
            public TextView email;
            public View chip;
        }
    }

    /**
     * Load accounts in a background thread
     */
    class LoadAccounts extends AsyncTask<Void, Void, List<Account>> {
        @Override
        protected List<Account> doInBackground(Void... params) {
            return Preferences.getPreferences(getApplicationContext()).getAccounts();
        }

        @Override
        protected void onPostExecute(List<Account> accounts) {
            populateListView(accounts);
        }
    }
}
