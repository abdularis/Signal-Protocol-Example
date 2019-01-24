package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.dao.PreKeyDao;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;

import java.io.IOException;

import androidx.annotation.NonNull;

public class LocalPreKeyStore implements PreKeyStore {

    private PreKeyDao mPreKeyDao;

    public LocalPreKeyStore(@NonNull PreKeyDao preKeyDao) {
        mPreKeyDao = preKeyDao;
    }

    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        PreKeyEntity preKeyEntity = mPreKeyDao.queryByPreKeyId(preKeyId);

        if (preKeyEntity != null && preKeyEntity.getPreKeyRecord() != null) {
            try {
                return new PreKeyRecord(preKeyEntity.getPreKeyRecord());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        throw new InvalidKeyIdException("No such prekeyrecord!");
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        mPreKeyDao.insertPreKey(new PreKeyEntity(preKeyId, record.serialize()));
    }

    @Override
    public boolean containsPreKey(int preKeyId) {
        return mPreKeyDao.countByPreKeyId(preKeyId) > 0;
    }

    @Override
    public void removePreKey(int preKeyId) {
        mPreKeyDao.deleteByPreKeyId(preKeyId);
    }
}
