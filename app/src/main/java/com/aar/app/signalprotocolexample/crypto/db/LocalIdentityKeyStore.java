package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.dao.LocalIdentityDao;
import com.aar.app.signalprotocolexample.crypto.db.dao.TrustedKeyDao;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import androidx.annotation.NonNull;

public class LocalIdentityKeyStore implements IdentityKeyStore {

    private LocalIdentityDao mLocalIdentityDao;
    private TrustedKeyDao mTrustedKeyDao;

    public LocalIdentityKeyStore(@NonNull TrustedKeyDao trustedKeyDao, @NonNull LocalIdentityDao localIdentityDao) {
        mLocalIdentityDao = localIdentityDao;
        mTrustedKeyDao = trustedKeyDao;
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        LocalIdentityEntity entity = mLocalIdentityDao.query();
        if (entity != null && entity.getIdentityKeyPair() != null) {
            try {
                return new IdentityKeyPair(entity.getIdentityKeyPair());
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int getLocalRegistrationId() {
        return mLocalIdentityDao.queryRegistrationId();
    }

    @Override
    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        mTrustedKeyDao.insert(new TrustedKeyEntity(
                address.getName(),
                address.getDeviceId(),
                identityKey.serialize()
        ));
        return true;
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
        int count = mTrustedKeyDao.countByNameAndDeviceId(address.getName(), address.getDeviceId());
        return count > 0;
    }

    @Override
    public IdentityKey getIdentity(SignalProtocolAddress address) {
        TrustedKeyEntity entity = mTrustedKeyDao.queryByNameAndDeviceId(address.getName(), address.getDeviceId());
        if (entity != null) {
            try {
                return new IdentityKey(entity.getIdentityKey(), 0);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
