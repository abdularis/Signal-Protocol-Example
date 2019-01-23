package com.aar.app.signalprotocolexample;

import android.util.Log;

import com.aar.app.signalprotocolexample.crypto.Kryptonium;
import com.aar.app.signalprotocolexample.crypto.DeviceKeyBundle;
import com.aar.app.signalprotocolexample.crypto.DeviceKeyBundleUtils;
import com.aar.app.signalprotocolexample.crypto.RemoteDeviceKeys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.state.impl.InMemorySignalProtocolStore;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EncryptTest {

    @Test
    public void encryptTest() throws InvalidKeyException, UntrustedIdentityException, InvalidMessageException, DuplicateMessageException, LegacyMessageException, InvalidKeyIdException, NoSessionException {
        DeviceKeyBundle ALICE = DeviceKeyBundleUtils.generateDeviceKeyBundle("+621111_alc", 1, 1, 2);
        DeviceKeyBundle BOB = DeviceKeyBundleUtils.generateDeviceKeyBundle("+622222_bob", 1, 22, 2);

        InMemorySignalProtocolStore ALICE_STORE = new InMemorySignalProtocolStore(ALICE.getIdentityKeyPair(), ALICE.getRegistrationId());
        ALICE_STORE.storePreKey(ALICE.getPreKeys().get(0).getId(), ALICE.getPreKeys().get(0));
        ALICE_STORE.storeSignedPreKey(ALICE.getSignedPreKey().getId(), ALICE.getSignedPreKey());

        InMemorySignalProtocolStore BOB_STORE = new InMemorySignalProtocolStore(BOB.getIdentityKeyPair(), BOB.getRegistrationId());
        BOB_STORE.storePreKey(BOB.getPreKeys().get(0).getId(), BOB.getPreKeys().get(0));
        BOB_STORE.storeSignedPreKey(BOB.getSignedPreKey().getId(), BOB.getSignedPreKey());

        //
        RemoteDeviceKeys ALICE_AS_REMOTE = new RemoteDeviceKeys(
                ALICE.getAddress(),
                ALICE.getRegistrationId(),
                ALICE.getPreKeys().get(0).getId(),
                ALICE.getPreKeys().get(0).getKeyPair().getPublicKey(),
                ALICE.getSignedPreKey().getId(),
                ALICE.getSignedPreKey().getKeyPair().getPublicKey(),
                ALICE.getSignedPreKey().getSignature(),
                ALICE.getIdentityKeyPair().getPublicKey()
        );

        RemoteDeviceKeys BOB_AS_REMOTE = new RemoteDeviceKeys(
                BOB.getAddress(),
                BOB.getRegistrationId(),
                BOB.getPreKeys().get(0).getId(),
                BOB.getPreKeys().get(0).getKeyPair().getPublicKey(),
                BOB.getSignedPreKey().getId(),
                BOB.getSignedPreKey().getKeyPair().getPublicKey(),
                BOB.getSignedPreKey().getSignature(),
                BOB.getIdentityKeyPair().getPublicKey()
        );

        Kryptonium ALICE_CRYPT = new Kryptonium(ALICE_STORE);
        Kryptonium BOB_CRYPT = new Kryptonium(BOB_STORE);

        ALICE_CRYPT.storeRemoteDeviceKeys(BOB_AS_REMOTE);
        BOB_CRYPT.storeRemoteDeviceKeys(ALICE_AS_REMOTE);

        //
        String ALICE_ORIG_MSG = "Hello I'm Alice";
        CiphertextMessage msg = ALICE_CRYPT.encryptFor(ALICE_ORIG_MSG.getBytes(), new SignalProtocolAddress("+622222_bob", 1));
        Log.d("TestEncrypt", "ALICE --to--> BOB: type: " + msg.getType());

        byte[] decryptMsg = BOB_CRYPT.decryptFrom(msg, new SignalProtocolAddress("+621111_alc", 1));
        assertEquals(ALICE_ORIG_MSG, new String(decryptMsg));
        Log.d("TestEncrypt", new String(decryptMsg));

        //
        String BOB_ORIG_MSG = "Hi I'm Bob";
        CiphertextMessage msg2 = BOB_CRYPT.encryptFor(BOB_ORIG_MSG.getBytes(), new SignalProtocolAddress("+621111_alc", 1));
        Log.d("TestEncrypt", "BOB --to--> ALICE: type: " + msg2.getType());

        byte[] decryptMsg2 = ALICE_CRYPT.decryptFrom(msg2, new SignalProtocolAddress("+622222_bob", 1));
        assertEquals(BOB_ORIG_MSG, new String(decryptMsg2));
        Log.d("TestEncrypt", new String(decryptMsg2));

        //
        msg = BOB_CRYPT.encryptFor("teeest".getBytes(), new SignalProtocolAddress("+621111_alc", 1));
        Log.d("TestEncrypt", "ALICE --to--> BOB: type: " + msg.getType());

        decryptMsg = ALICE_CRYPT.decryptFrom(msg, new SignalProtocolAddress("+622222_bob", 1));
        assertEquals("teeest", new String(decryptMsg));
        Log.d("TestEncrypt", new String(decryptMsg));

        //
        msg = ALICE_CRYPT.encryptFor("I'm alice!!!!".getBytes(), new SignalProtocolAddress("+622222_bob", 1));
        Log.d("TestEncrypt", "ALICE --to--> BOB: type: " + msg.getType());

        decryptMsg = BOB_CRYPT.decryptFrom(msg, new SignalProtocolAddress("+621111_alc", 1));
        assertEquals("I'm alice!!!!", new String(decryptMsg));
        Log.d("TestEncrypt", new String(decryptMsg));
    }

}
