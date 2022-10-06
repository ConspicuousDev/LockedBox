package com.omniscient.lockedbox.Utils;

import com.omniscient.lockedbox.LockedBox;

public class DisableThread extends Thread {
    @Override
    public void run() {
        LockedBox.plugin.onDisable();
    }
}
