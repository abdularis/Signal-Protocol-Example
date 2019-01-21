package com.aar.app.signalprotocolexample;

import java.util.List;

import org.junit.Test;
import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.impl.InMemorySignalProtocolStore;
import org.whispersystems.libsignal.util.KeyHelper;


public class Learn1 {


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
            InvalidMessageException, DuplicateMessageException, InvalidVersionException, InvalidKeyIdException {
        Person ALICE = new Person("+6281111111111", 111, 2);
        Person BOB = new Person("+6282222222222", 112, 5);

        SessionCipher ALICE_CIPHER = createSessionCipher(ALICE, BOB);
        SessionCipher BOB_CIPHER = createSessionCipher(BOB, ALICE);

        CiphertextMessage MSG_TO_BOB = ALICE_CIPHER.encrypt("Hello world!!".getBytes());

        byte[] ss = MSG_TO_BOB.serialize();

        PreKeySignalMessage ms1 = new PreKeySignalMessage(ss);
        PreKeySignalMessage ms2 = new PreKeySignalMessage(ss);

        byte[] msg = BOB_CIPHER.decrypt(ms1);

        System.out.println("-------------- encrypted -------------");
        System.out.println(new String(MSG_TO_BOB.serialize()));

        System.out.println("-------------- decrypted -------------");
        System.out.println(new String(msg));
        // ----------------------------------
    }

    private static SessionCipher createSessionCipher(Person asLocalPerson, Person asRemotePerson)
            throws UntrustedIdentityException, InvalidKeyException {

        InMemorySignalProtocolStore protocolStore = new InMemorySignalProtocolStore(asLocalPerson.identityKeyPair, asLocalPerson.registrationId);
        protocolStore.storePreKey(asLocalPerson.preKeys.get(0).getId(), asLocalPerson.preKeys.get(0));
        protocolStore.storeSignedPreKey(asLocalPerson.signedPreKey.getId(), asLocalPerson.signedPreKey);

        SessionBuilder sessionBuilder = new SessionBuilder(protocolStore, asRemotePerson.address);

        System.out.println(protocolStore.containsSession(asRemotePerson.address));

        PreKeyBundle preKeyBundle = new PreKeyBundle(
                asRemotePerson.registrationId,
                asRemotePerson.address.getDeviceId(),
                asRemotePerson.preKeys.get(0).getId(),
                asRemotePerson.preKeys.get(0).getKeyPair().getPublicKey(),
                asRemotePerson.signedPreKey.getId(),
                asRemotePerson.signedPreKey.getKeyPair().getPublicKey(),
                asRemotePerson.signedPreKey.getSignature(),
                asRemotePerson.identityKeyPair.getPublicKey()
        );

        sessionBuilder.process(preKeyBundle);

        System.out.println(protocolStore.containsSession(asRemotePerson.address));

        return new SessionCipher(protocolStore, asRemotePerson.address);
    }
}