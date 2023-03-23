package com.mail.ann.storage.migrations;

import com.mail.ann.mail.Flag;

import java.util.List;

class LegacyPendingSetFlag extends LegacyPendingCommand {
    public String folder;
    public boolean newState;
    public Flag flag;
    public List<String> uids;
}
