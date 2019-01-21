package com.aar.app.signalprotocolexample;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.junit.Test;
import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;
import org.whispersystems.libsignal.state.impl.InMemoryIdentityKeyStore;
import org.whispersystems.libsignal.state.impl.InMemoryPreKeyStore;
import org.whispersystems.libsignal.state.impl.InMemorySessionStore;
import org.whispersystems.libsignal.state.impl.InMemorySignalProtocolStore;
import org.whispersystems.libsignal.state.impl.InMemorySignedPreKeyStore;
import org.whispersystems.libsignal.util.KeyHelper;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Learn {


    static class Person {
        IdentityKeyPair identityKeyPair;
        int registrationId;
        List<PreKeyRecord> preKeys;
        SignedPreKeyRecord signedPreKey;
        SignalProtocolAddress address;

        Person(String name, int deviceId, int signedPreKeyId) throws InvalidKeyException {
            identityKeyPair = KeyHelper.generateIdentityKeyPair();
            registrationId = KeyHelper.generateRegistrationId(false);
            preKeys = KeyHelper.generatePreKeys(1, 100);
            signedPreKey = KeyHelper.generateSignedPreKey(identityKeyPair, signedPreKeyId);
            address = new SignalProtocolAddress(name, deviceId);
        }
    }


    @Test
    public void test() throws InvalidKeyException, UntrustedIdentityException, LegacyMessageException,
            InvalidMessageException, NoSessionException, DuplicateMessageException, InvalidVersionException, InvalidKeyIdException {
        Person ALICE = new Person("+6281111111111", 111, 2);
        Person BOB = new Person("+6282222222222", 112, 5);

        CiphertextMessage ALICE_MSG = encrypt(ALICE, BOB);

        byte[] msg = decrypt(ALICE, BOB, new PreKeySignalMessage(ALICE_MSG.serialize()));

        System.out.println("-------------- encrypted -------------");
        System.out.println(new String(ALICE_MSG.serialize()));

        System.out.println("-------------- decrypted -------------");
        System.out.println(new String(msg));
        // ----------------------------------
    }

    static CiphertextMessage encrypt(Person sender, Person recipient) throws UntrustedIdentityException, InvalidKeyException {
        InMemorySignalProtocolStore protocolStore = new InMemorySignalProtocolStore(sender.identityKeyPair, sender.registrationId);

        SessionBuilder sessionBuilder = new SessionBuilder(protocolStore, recipient.address);

        PreKeyBundle preKeyBundle = new PreKeyBundle(
                recipient.registrationId,
                recipient.address.getDeviceId(),
                recipient.preKeys.get(3).getId(),
                recipient.preKeys.get(3).getKeyPair().getPublicKey(),
                recipient.signedPreKey.getId(),
                recipient.signedPreKey.getKeyPair().getPublicKey(),
                recipient.signedPreKey.getSignature(),
                recipient.identityKeyPair.getPublicKey()
        );

        sessionBuilder.process(preKeyBundle);

        SessionCipher cipher = new SessionCipher(protocolStore, recipient.address);

        return cipher.encrypt("Hello world!, hello world!, hello world!, hello world!, hello world!, hello world! haha".getBytes(StandardCharsets.UTF_8));
    }

    static byte[] decrypt(Person sender, Person recipient, PreKeySignalMessage message) throws UntrustedIdentityException, InvalidKeyException, NoSessionException, DuplicateMessageException, InvalidMessageException, LegacyMessageException, InvalidKeyIdException {
        InMemorySignalProtocolStore protocolStore = new InMemorySignalProtocolStore(recipient.identityKeyPair, recipient.registrationId);
        protocolStore.storePreKey(recipient.preKeys.get(3).getId(), recipient.preKeys.get(3));
        protocolStore.storeSignedPreKey(recipient.signedPreKey.getId(), recipient.signedPreKey);

        SessionBuilder sessionBuilder = new SessionBuilder(protocolStore, sender.address);

        PreKeyBundle preKeyBundle = new PreKeyBundle(
                sender.registrationId,
                sender.address.getDeviceId(),
                sender.preKeys.get(0).getId(),
                sender.preKeys.get(0).getKeyPair().getPublicKey(),
                sender.signedPreKey.getId(),
                sender.signedPreKey.getKeyPair().getPublicKey(),
                sender.signedPreKey.getSignature(),
                sender.identityKeyPair.getPublicKey()
        );

        sessionBuilder.process(preKeyBundle);

        SessionCipher cipher = new SessionCipher(protocolStore, sender.address);

        CiphertextMessage enc = cipher.encrypt("Hi hi!!".getBytes());

        return cipher.decrypt(message);
    }
}