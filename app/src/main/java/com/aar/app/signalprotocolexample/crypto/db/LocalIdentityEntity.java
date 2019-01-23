package com.aar.app.signalprotocolexample.crypto.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "local_identity")
public class LocalIdentityEntity {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "reg_id")
    private int mRegistrationId;

    @ColumnInfo(name = "id_key_pair")
    private byte[] mIdentityKeyPair;

    public LocalIdentityEntity(int id, int registrationId, byte[] identityKeyPair) {
        mId = id;
        mRegistrationId = registrationId;
        mIdentityKeyPair = identityKeyPair;
    }

    public int getId() {
        return mId;
    }

    public int getRegistrationId() {
        return mRegistrationId;
    }

    public byte[] getIdentityKeyPair() {
        return mIdentityKeyPair;
    }
}
