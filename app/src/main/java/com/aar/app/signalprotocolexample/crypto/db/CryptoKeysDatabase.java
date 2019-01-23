package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.dao.LocalIdentityDao;
import com.aar.app.signalprotocolexample.crypto.db.dao.PreKeyDao;
import com.aar.app.signalprotocolexample.crypto.db.dao.SignedPreKeyDao;
import com.aar.app.signalprotocolexample.crypto.db.dao.TrustedKeyDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { PreKeyEntity.class, LocalIdentityEntity.class, TrustedKeyEntity.class, SignedPreKeyEntity.class }, version = 1)
public abstract class CryptoKeysDatabase extends RoomDatabase {

    public abstract PreKeyDao getPreKeyDao();
    public abstract LocalIdentityDao getLocalIdentityDao();
    public abstract TrustedKeyDao getTrustedKeyDao();
    public abstract SignedPreKeyDao getSignedPreKeyDao();

}
