package com.mail.ann.storage.migrations;

import java.util.Map;

class LegacyPendingMoveOrCopy extends LegacyPendingCommand {
    public String srcFolder;
    public String destFolder;
    public boolean isCopy;
    public Map<String, String> newUidMap;
}
