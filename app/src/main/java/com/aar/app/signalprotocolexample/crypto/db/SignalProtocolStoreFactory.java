package com.aar.app.signalprotocolexample.crypto.db;

import androidx.annotation.NonNull;

public class SignalProtocolStoreFactory {

    public static LocalSignalProtocolStore createLocalSignalProtocolStore(@NonNull CryptoKeysDatabase db) {
        LocalIdentityKeyStore identityKeyStore = new LocalIdentityKeyStore(db.getTrustedKeyDao(), db.getLocalIdentityDao());
        LocalPreKeyStore preKeyStore = new LocalPreKeyStore(db.getPreKeyDao());
        LocalSignedPreKeyStore signedPreKeyStore = new LocalSignedPreKeyStore(db.getSignedPreKeyDao());
        LocalSessionStore sessionStore = new LocalSessionStore(db.getSessionDao());

        return new LocalSignalProtocolStore(
            identityKeyStore,
            preKeyStore,
            signedPreKeyStore,
            sessionStore
        );
    }

}
