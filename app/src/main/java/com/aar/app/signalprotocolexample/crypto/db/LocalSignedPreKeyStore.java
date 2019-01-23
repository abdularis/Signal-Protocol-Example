package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.dao.SignedPreKeyDao;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class LocalSignedPreKeyStore implements SignedPreKeyStore {

    private SignedPreKeyDao mSignedPreKeyDao;

    public LocalSignedPreKeyStore(@NonNull SignedPreKeyDao signedPreKeyDao) {
        mSignedPreKeyDao = signedPreKeyDao;
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) {
        SignedPreKeyEntity entity = mSignedPreKeyDao.queryById(signedPreKeyId);
        if (entity != null) {
            return entity.getSignedPreKeyRecord();
        }
        return null;
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        List<SignedPreKeyRecord> signedPreKeyRecords = new ArrayList<>();
        for (SignedPreKeyEntity entity : mSignedPreKeyDao.queryAll()) {
            signedPreKeyRecords.add(entity.getSignedPreKeyRecord());
        }
        return signedPreKeyRecords;
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        mSignedPreKeyDao.insert(new SignedPreKeyEntity(signedPreKeyId, record));
    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        int count = mSignedPreKeyDao.queryCountById(signedPreKeyId);
        return count > 0;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        mSignedPreKeyDao.deleteById(signedPreKeyId);
    }
}
