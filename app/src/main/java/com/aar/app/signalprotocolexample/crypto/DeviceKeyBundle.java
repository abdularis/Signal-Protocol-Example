package com.aar.app.signalprotocolexample.crypto;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.util.List;

public class DeviceKeyBundle {

    private int mRegistrationId;
    private SignalProtocolAddress mAddress;
    private IdentityKeyPair mIdentityKeyPair;
    private List<PreKeyRecord> mPreKeys;
    private SignedPreKeyRecord mSignedPreKey;

    public DeviceKeyBundle(int registrationId,
                           SignalProtocolAddress address,
                           IdentityKeyPair identityKeyPair,
                           List<PreKeyRecord> preKeys,
                           SignedPreKeyRecord signedPreKey) {
        mRegistrationId = registrationId;
        mAddress = address;
        mIdentityKeyPair = identityKeyPair;
        mPreKeys = preKeys;
        mSignedPreKey = signedPreKey;
    }

    public int getRegistrationId() {
        return mRegistrationId;
    }

    public SignalProtocolAddress getAddress() {
        return mAddress;
    }

    public IdentityKeyPair getIdentityKeyPair() {
        return mIdentityKeyPair;
    }

    public List<PreKeyRecord> getPreKeys() {
        return mPreKeys;
    }

    public SignedPreKeyRecord getSignedPreKey() {
        return mSignedPreKey;
    }
}
