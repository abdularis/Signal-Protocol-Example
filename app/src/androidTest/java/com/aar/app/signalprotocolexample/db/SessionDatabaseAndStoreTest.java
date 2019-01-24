package com.aar.app.signalprotocolexample.db;

import android.content.Context;

import com.aar.app.signalprotocolexample.crypto.db.CryptoKeysDatabase;
import com.aar.app.signalprotocolexample.crypto.db.LocalSessionStore;
import com.aar.app.signalprotocolexample.crypto.db.LocalSignedPreKeyStore;
import com.aar.app.signalprotocolexample.crypto.db.SessionEntity;
import com.aar.app.signalprotocolexample.crypto.db.dao.SessionDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SessionDatabaseAndStoreTest {

    private CryptoKeysDatabase db;
    private SessionDao mSessionDao;
    private LocalSessionStore mLocalSessionStore;

    @Before
    public void start() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, CryptoKeysDatabase.class).build();
        mSessionDao = db.getSessionDao();
        mLocalSessionStore = new LocalSessionStore(mSessionDao);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void sessionDaoTest() {
        SessionRecord sessEmpty = new SessionRecord();
        SessionEntity sessEmptyEntity = new SessionEntity(
                "alice", 2,
                sessEmpty.serialize()
        );

        mSessionDao.insert(sessEmptyEntity);

        int count = mSessionDao.queryCountByAddress("alice", 2);

        assertEquals(1, count);

        count = mSessionDao.queryDeviceIds("alice", 1).size();

        assertEquals(1, count);

        count = mSessionDao.queryDeviceIds("alice", 2).size();

        assertEquals(0, count);

        SessionEntity queryEntity = mSessionDao.queryByAddress("alice", 1);

        assertNull(queryEntity);

        queryEntity = mSessionDao.queryByAddress("alice", 2);

        assertNotNull(queryEntity);
        assertEquals(sessEmptyEntity.getProtocolAddressName(), queryEntity.getProtocolAddressName());
        assertEquals(sessEmptyEntity.getDeviceId(), queryEntity.getDeviceId());
        assertArrayEquals(sessEmptyEntity.getSession(), queryEntity.getSession());

        mSessionDao.deleteByAddress("alice", 2);

        count = mSessionDao.queryCountByAddress("alice", 2);

        assertEquals(0, count);

        mSessionDao.insert(sessEmptyEntity);
        mSessionDao.deleteByAddressName("alice");

        count = mSessionDao.queryCountByAddress("alice", 2);

        assertEquals(0, count);
    }

    @Test
    public void localSessionStoreTest() {
        SessionRecord sessEmpty = new SessionRecord();
        SignalProtocolAddress bobAddr = new SignalProtocolAddress("bob", 2);

        boolean exist = mLocalSessionStore.containsSession(bobAddr);

        assertFalse(exist);

        mLocalSessionStore.storeSession(bobAddr, sessEmpty);
        mLocalSessionStore.storeSession(bobAddr, sessEmpty);

        exist = mLocalSessionStore.containsSession(bobAddr);

        assertTrue(exist);

        SessionRecord querySess = mLocalSessionStore.loadSession(bobAddr);

        assertArrayEquals(sessEmpty.serialize(), querySess.serialize());

        int count = mLocalSessionStore.getSubDeviceSessions(bobAddr.getName()).size();

        assertEquals(1, count);

        mLocalSessionStore.deleteSession(bobAddr);

        exist = mLocalSessionStore.containsSession(bobAddr);

        assertFalse(exist);

        mLocalSessionStore.storeSession(bobAddr, sessEmpty);
        mLocalSessionStore.deleteAllSessions(bobAddr.getName());

        count = mLocalSessionStore.getSubDeviceSessions(bobAddr.getName()).size();

        assertEquals(0, count);
    }

}
