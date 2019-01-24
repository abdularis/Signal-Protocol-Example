package com.aar.app.signalprotocolexample.db;

import android.content.Context;

import com.aar.app.signalprotocolexample.crypto.db.CryptoKeysDatabase;
import com.aar.app.signalprotocolexample.crypto.db.LocalPreKeyStore;
import com.aar.app.signalprotocolexample.crypto.db.dao.PreKeyDao;
import com.aar.app.signalprotocolexample.crypto.db.PreKeyEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;


import java.io.IOException;
import java.util.List;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreKeyDatabaseAndStoreTest {

    private CryptoKeysDatabase db;
    private PreKeyDao prekeyDao;
    private LocalPreKeyStore prekeyStore;

    @Before
    public void start() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, CryptoKeysDatabase.class).build();
        prekeyDao = db.getPreKeyDao();
        prekeyStore = new LocalPreKeyStore(prekeyDao);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void insertMoreThanOne() {
        ECKeyPair keyPair = Curve.generateKeyPair();
        PreKeyRecord preKeyRecord = new PreKeyRecord(1, keyPair);

        ECKeyPair keyPair1 = Curve.generateKeyPair();
        PreKeyRecord preKeyRecord1 = new PreKeyRecord(2, keyPair1);

        PreKeyEntity entity = new PreKeyEntity(preKeyRecord.getId(), preKeyRecord.serialize());
        PreKeyEntity entity1 = new PreKeyEntity(preKeyRecord1.getId(), preKeyRecord1.serialize());

        prekeyDao.insertPreKey(entity);
        prekeyDao.insertPreKey(entity);

        int count = prekeyDao.countByPreKeyId(entity.getPreKeyId());

        assertEquals(1, count);

        prekeyDao.insertPreKey(entity1);

        count = prekeyDao.countByPreKeyId(entity1.getPreKeyId());

        assertEquals(1, count);

        prekeyDao.deleteByPreKeyId(entity.getPreKeyId());
        prekeyDao.deleteByPreKeyId(entity1.getPreKeyId());

        count = prekeyDao.countByPreKeyId(entity.getPreKeyId());

        assertEquals(0, count);

        count = prekeyDao.countByPreKeyId(entity1.getPreKeyId());

        assertEquals(0, count);
    }

    @Test
    public void allOperationTest() throws IOException {
        ECKeyPair keyPair = Curve.generateKeyPair();
        PreKeyRecord preKeyRecord = new PreKeyRecord(1, keyPair);

        PreKeyEntity entity = new PreKeyEntity(preKeyRecord.getId(), preKeyRecord.serialize());
        prekeyDao.insertPreKey(entity);
        PreKeyEntity queryEntity = prekeyDao.queryByPreKeyId(preKeyRecord.getId());

        PreKeyRecord queryPreKeyRecord = new PreKeyRecord(queryEntity.getPreKeyRecord());

        assertEquals(preKeyRecord.getId(), queryEntity.getPreKeyId());
        assertEquals(preKeyRecord.getId(), queryPreKeyRecord.getId());
        assertEquals(preKeyRecord.getKeyPair().getPublicKey(), queryPreKeyRecord.getKeyPair().getPublicKey());
        assertArrayEquals(preKeyRecord.getKeyPair().getPrivateKey().serialize(), queryPreKeyRecord.getKeyPair().getPrivateKey().serialize());

        int count = prekeyDao.countByPreKeyId(preKeyRecord.getId());

        assertEquals(1, count);

        prekeyDao.deleteByPreKeyId(preKeyRecord.getId());

        count = prekeyDao.countByPreKeyId(preKeyRecord.getId());

        assertEquals(0, count);
    }

    @Test
    public void localPreKeyStoreAllTest() throws InvalidKeyIdException {
        List<PreKeyRecord> preKeys = KeyHelper.generatePreKeys(1, 3);

        for (PreKeyRecord preKey : preKeys) {
            prekeyStore.storePreKey(preKey.getId(), preKey);
            boolean exist = prekeyStore.containsPreKey(preKey.getId());

            assertTrue(exist);

            PreKeyRecord preKeyRecord = prekeyStore.loadPreKey(preKey.getId());

            assertEquals(preKey.getId(), preKeyRecord.getId());
            assertEquals(preKey.getKeyPair().getPublicKey(), preKeyRecord.getKeyPair().getPublicKey());
            assertArrayEquals(preKey.getKeyPair().getPrivateKey().serialize(), preKeyRecord.getKeyPair().getPrivateKey().serialize());

            prekeyStore.removePreKey(preKey.getId());

            exist = prekeyStore.containsPreKey(preKey.getId());

            assertFalse(exist);
        }
    }
}
