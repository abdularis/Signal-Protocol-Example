package com.aar.app.signalprotocolexample.db;

import android.content.Context;

import com.aar.app.signalprotocolexample.crypto.db.CryptoKeysDatabase;
import com.aar.app.signalprotocolexample.crypto.db.LocalSignedPreKeyStore;
import com.aar.app.signalprotocolexample.crypto.db.SignedPreKeyEntity;
import com.aar.app.signalprotocolexample.crypto.db.dao.SignedPreKeyDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.IOException;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SignedPreKeyDatabaseAndStoreTest {

    private CryptoKeysDatabase db;
    private SignedPreKeyDao mSignedPreKeyDao;
    private LocalSignedPreKeyStore mLocalSignedPreKeyStore;

    @Before
    public void start() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, CryptoKeysDatabase.class).build();
        mSignedPreKeyDao = db.getSignedPreKeyDao();
        mLocalSignedPreKeyStore = new LocalSignedPreKeyStore(mSignedPreKeyDao);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void signedPreKeyDaoTest() throws InvalidKeyException, IOException {
        SignedPreKeyRecord spk = KeyHelper.generateSignedPreKey(KeyHelper.generateIdentityKeyPair(), 10);
        SignedPreKeyEntity entity = new SignedPreKeyEntity(spk.getId(), spk.serialize());

        mSignedPreKeyDao.insert(entity);

        SignedPreKeyEntity qEntity = mSignedPreKeyDao.queryById(spk.getId());
        SignedPreKeyRecord qSpk = new SignedPreKeyRecord(qEntity.getSignedPreKeyRecord());

        assertEquals(spk.getId(), qSpk.getId());
        assertArrayEquals(spk.serialize(), qSpk.serialize());

        assertEquals(1, mSignedPreKeyDao.queryAll().size());

        mSignedPreKeyDao.deleteById(entity.getId());

        assertEquals(0, mSignedPreKeyDao.queryCountById(entity.getId()));
        assertEquals(0, mSignedPreKeyDao.queryAll().size());
    }

    @Test
    public void signedPreKeyStoreTest() throws InvalidKeyException, InvalidKeyIdException {
        SignedPreKeyRecord spk = KeyHelper.generateSignedPreKey(KeyHelper.generateIdentityKeyPair(), 10);
        SignedPreKeyEntity entity = new SignedPreKeyEntity(spk.getId(), spk.serialize());

        mLocalSignedPreKeyStore.storeSignedPreKey(entity.getId(), spk);

        boolean exist = mLocalSignedPreKeyStore.containsSignedPreKey(entity.getId());

        assertTrue(exist);

        SignedPreKeyRecord qSpk = mLocalSignedPreKeyStore.loadSignedPreKey(entity.getId());

        assertArrayEquals(spk.serialize(), qSpk.serialize());
        assertEquals(1, mLocalSignedPreKeyStore.loadSignedPreKeys().size());

        mLocalSignedPreKeyStore.removeSignedPreKey(entity.getId());

        assertFalse(mLocalSignedPreKeyStore.containsSignedPreKey(entity.getId()));
        assertEquals(0, mLocalSignedPreKeyStore.loadSignedPreKeys().size());
    }
}
