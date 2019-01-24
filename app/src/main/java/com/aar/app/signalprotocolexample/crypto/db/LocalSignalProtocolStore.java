package com.aar.app.signalprotocolexample.crypto.db;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.util.List;

public class LocalSignalProtocolStore implements SignalProtocolStore {

    private LocalIdentityKeyStore mIdentityKeyStore;
    private LocalPreKeyStore mPreKeyStore;
    private LocalSignedPreKeyStore mSignedPreKeyStore;
    private LocalSessionStore mSessionStore;

    public LocalSignalProtocolStore(LocalIdentityKeyStore identityKeyStore,
                                    LocalPreKeyStore preKeyStore,
                                    LocalSignedPreKeyStore signedPreKeyStore,
                                    LocalSessionStore sessionStore) {
        mIdentityKeyStore = identityKeyStore;
        mPreKeyStore = preKeyStore;
        mSignedPreKeyStore = signedPreKeyStore;
        mSessionStore = sessionStore;
    }

    public void setLocalIdentity(IdentityKeyPair identityKeyPair, int registrationId) {
        mIdentityKeyStore.setLocalIdentity(identityKeyPair, registrationId);
    }

    // IdentityKey
    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        return mIdentityKeyStore.getIdentityKeyPair();
    }

    @Override
    public int getLocalRegistrationId() {
        return mIdentityKeyStore.getLocalRegistrationId();
    }

    @Override
    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        return mIdentityKeyStore.saveIdentity(address, identityKey);
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
        return mIdentityKeyStore.isTrustedIdentity(address, identityKey, direction);
    }

    @Override
    public IdentityKey getIdentity(SignalProtocolAddress address) {
        return mIdentityKeyStore.getIdentity(address);
    }


    // PreKey
    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        return mPreKeyStore.loadPreKey(preKeyId);
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        mPreKeyStore.storePreKey(preKeyId, record);
    }

    @Override
    public boolean containsPreKey(int preKeyId) {
        return mPreKeyStore.containsPreKey(preKeyId);
    }

    @Override
    public void removePreKey(int preKeyId) {
        mPreKeyStore.removePreKey(preKeyId);
    }


    // Signed PreKey
    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        return mSignedPreKeyStore.loadSignedPreKey(signedPreKeyId);
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        return mSignedPreKeyStore.loadSignedPreKeys();
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        mSignedPreKeyStore.storeSignedPreKey(signedPreKeyId, record);
    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return mSignedPreKeyStore.containsSignedPreKey(signedPreKeyId);
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        mSignedPreKeyStore.removeSignedPreKey(signedPreKeyId);
    }


    // SessionStore
    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        return mSessionStore.loadSession(address);
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        return mSessionStore.getSubDeviceSessions(name);
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {
        mSessionStore.storeSession(address, record);
    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        return mSessionStore.containsSession(address);
    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        mSessionStore.deleteSession(address);
    }

    @Override
    public void deleteAllSessions(String name) {
        mSessionStore.deleteAllSessions(name);
    }
}
