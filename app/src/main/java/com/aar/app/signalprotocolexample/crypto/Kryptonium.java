package com.aar.app.signalprotocolexample.crypto;

import android.util.Log;

import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.protocol.SignalMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;

import androidx.annotation.NonNull;

public class Kryptonium {

    private SignalProtocolStore mSignalProtocolStore;

    public Kryptonium(@NonNull SignalProtocolStore protocolStore) {
        mSignalProtocolStore = protocolStore;
    }

    public void generateKeys() throws InvalidKeyException {
        DeviceKeyBundle ALICE = DeviceKeyBundleUtils.generateDeviceKeyBundle("+621111_alc", 1, 1, 1);
        for (PreKeyRecord preKeyRecord : ALICE.getPreKeys()) {
            mSignalProtocolStore.storePreKey(preKeyRecord.getId(), preKeyRecord);
        }
        mSignalProtocolStore.storeSignedPreKey(ALICE.getSignedPreKey().getId(), ALICE.getSignedPreKey());
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

    public byte[] decryptFrom(PreKeySignalMessage data, SignalProtocolAddress remoteAddress)
            throws InvalidKeyException, LegacyMessageException, InvalidMessageException,
            DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException {
        SessionCipher sessionCipher = new SessionCipher(mSignalProtocolStore, remoteAddress);
        return sessionCipher.decrypt(data);
    }

    public byte[] decryptFrom(SignalMessage data, SignalProtocolAddress remoteAddress)
            throws NoSessionException, DuplicateMessageException, InvalidMessageException,
            UntrustedIdentityException, LegacyMessageException {
        SessionCipher sessionCipher = new SessionCipher(mSignalProtocolStore, remoteAddress);
        return sessionCipher.decrypt(data);
    }

    public byte[] decryptFrom(CiphertextMessage data, SignalProtocolAddress remoteAddress)
            throws DuplicateMessageException, InvalidMessageException, UntrustedIdentityException,
            LegacyMessageException, InvalidKeyException, InvalidKeyIdException, NoSessionException {
        if (data.getType() == CiphertextMessage.PREKEY_TYPE) {
            return decryptFrom((PreKeySignalMessage) data, remoteAddress);
        } else if (data.getType() == CiphertextMessage.WHISPER_TYPE) {
            return decryptFrom((SignalMessage) data, remoteAddress);
        }
        return null;
    }
}
