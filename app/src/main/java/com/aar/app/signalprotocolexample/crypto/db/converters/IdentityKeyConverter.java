package com.aar.app.signalprotocolexample.crypto.db.converters;

import org.whispersystems.libsignal.IdentityKey;

public class IdentityKeyConverter {

    public static byte[] toByte(IdentityKey identityKey) {
        return identityKey.serialize();
    }

}
