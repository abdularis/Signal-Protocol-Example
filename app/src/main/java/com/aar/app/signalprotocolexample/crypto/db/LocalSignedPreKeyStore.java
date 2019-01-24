package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.dao.SignedPreKeyDao;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class LocalSignedPreKeyStore implements SignedPreKeyStore {

    private SignedPreKeyDao mSignedPreKeyDao;

    public LocalSignedPreKeyStore(@NonNull SignedPreKeyDao signedPreKeyDao) {
        mSignedPreKeyDao = signedPreKeyDao;
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        SignedPreKeyEntity entity = mSignedPreKeyDao.queryById(signedPreKeyId);
        if (entity != null) {
            try {
                return new SignedPreKeyRecord(entity.getSignedPreKeyRecord());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        throw new InvalidKeyIdException("No such signedprekey!");
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        List<SignedPreKeyRecord> signedPreKeyRecords = new ArrayList<>();
        for (SignedPreKeyEntity entity : mSignedPreKeyDao.queryAll()) {
            try {
                signedPreKeyRecords.add(new SignedPreKeyRecord(entity.getSignedPreKeyRecord()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return signedPreKeyRecords;
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        mSignedPreKeyDao.insert(new SignedPreKeyEntity(signedPreKeyId, record.serialize()));
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
