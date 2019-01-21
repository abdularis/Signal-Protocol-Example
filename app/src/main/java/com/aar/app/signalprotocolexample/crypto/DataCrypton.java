package com.aar.app.signalprotocolexample.crypto;

import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.SignalProtocolStore;

import androidx.annotation.NonNull;

public class DataCrypton {

    private SignalProtocolStore mSignalProtocolStore;
    private SignalProtocolAddress mLocalAddress;

    public DataCrypton(@NonNull SignalProtocolStore protocolStore, SignalProtocolAddress localAddress) {
        mSignalProtocolStore = protocolStore;
        mLocalAddress = localAddress;
    }

    public void storeRemoteDeviceKeys(RemoteDeviceKeys remoteDeviceKeys)
            throws UntrustedIdentityException, InvalidKeyException {
        SessionBuilder sessionBuilder = new SessionBuilder(mSignalProtocolStore, remoteDeviceKeys.getAddress());
        PreKeyBundle preKeyBundle = new PreKeyBundle(
                remoteDeviceKeys.getRegistrationId(),
                remoteDeviceKeys.getAddress().getDeviceId(),
                remoteDeviceKeys.getPreKeyId(),
                remoteDeviceKeys.getPreKeyPublic(),
                remoteDeviceKeys.getSignPreKeyId(),
                remoteDeviceKeys.getSignedPreKeyPublic(),
                remoteDeviceKeys.getSignedPreKeySignature(),
                remoteDeviceKeys.getIdentityKey()
        );

        sessionBuilder.process(preKeyBundle);
    }

    public CiphertextMessage encryptFor(byte[] data, SignalProtocolAddress address)
            throws UntrustedIdentityException {
        SessionCipher sessionCipher = new SessionCipher(mSignalProtocolStore, address);
        return sessionCipher.encrypt(data);
    }
}
