package com.aar.app.signalprotocolexample.crypto.db;

import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;

import java.util.List;

public class LocalSessionStore implements SessionStore {
    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        return null;
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        return null;
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {

    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        return false;
    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {

    }

    @Override
    public void deleteAllSessions(String name) {

    }
}
