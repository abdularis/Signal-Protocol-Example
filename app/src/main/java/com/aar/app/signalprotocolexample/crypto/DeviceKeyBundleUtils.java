package com.aar.app.signalprotocolexample.crypto;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.util.KeyHelper;

public class DeviceKeyBundleUtils {

    public static DeviceKeyBundle generateDeviceKeyBundle(String name,
                                                          int deviceId,
                                                          int signedPreKeyId,
                                                          int preKeyCount)
            throws InvalidKeyException {

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        return new DeviceKeyBundle(
                KeyHelper.generateRegistrationId(false),
                new SignalProtocolAddress(name, deviceId),
                identityKeyPair,
                KeyHelper.generatePreKeys(1, preKeyCount),
                KeyHelper.generateSignedPreKey(identityKeyPair, signedPreKeyId)
        );
    }

}
