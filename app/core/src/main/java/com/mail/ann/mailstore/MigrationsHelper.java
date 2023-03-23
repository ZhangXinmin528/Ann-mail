package com.mail.ann.mailstore;


import com.mail.ann.Account;


/**
 * Helper to allow accessing classes and methods that aren't visible or accessible to the 'migrations' package
 */
public interface MigrationsHelper {
    Account getAccount();
    void saveAccount();
}
