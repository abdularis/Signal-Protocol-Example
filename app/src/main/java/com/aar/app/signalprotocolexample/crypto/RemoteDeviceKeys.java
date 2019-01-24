package com.aar.app.signalprotocolexample.crypto;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.ecc.ECPublicKey;

public class RemoteDeviceKeys {

    private SignalProtocolAddress mAddress;
    private int mRegistrationId;
    private int mPreKeyId;
    private ECPublicKey mPreKeyPublic;
    private int mSignPreKeyId;
    private ECPublicKey mSignedPreKeyPublic;
    private byte[] mSignedPreKeySignature;
    private IdentityKey mIdentityKey;

    public RemoteDeviceKeys(SignalProtocolAddress address,
                            int registrationId,
                            int preKeyId,
                            ECPublicKey preKeyPublic,
                            int signPreKeyId,
                            ECPublicKey signedPreKeyPublic,
                            byte[] signedPreKeySignature,
                            IdentityKey identityKey) {
        mAddress = address;
        mRegistrationId = registrationId;
        mPreKeyId = preKeyId;
        mPreKeyPublic = preKeyPublic;
        mSignPreKeyId = signPreKeyId;
        mSignedPreKeyPublic = signedPreKeyPublic;
        mSignedPreKeySignature = signedPreKeySignature;
        mIdentityKey = identityKey;
    }

    public SignalProtocolAddress getAddress() {
        return mAddress;
    }

    public int getRegistrationId() {
        return mRegistrationId;
    }

    public int getPreKeyId() {
        return mPreKeyId;
    }

    public ECPublicKey getPreKeyPublic() {
        return mPreKeyPublic;
    }

    public int getSignPreKeyId() {
        return mSignPreKeyId;
    }

    public ECPublicKey getSignedPreKeyPublic() {
        return mSignedPreKeyPublic;
    }

    public byte[] getSignedPreKeySignature() {
        return mSignedPreKeySignature;
    }

    public IdentityKey getIdentityKey() {
        return mIdentityKey;
    }

}
