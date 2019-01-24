package com.aar.app.signalprotocolexample.crypto.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "signed_prekeys")
public class SignedPreKeyEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "signed_prekey")
    private byte[] mSignedPreKeyRecord;

    public SignedPreKeyEntity(int id, byte[] signedPreKeyRecord) {
        mId = id;
        mSignedPreKeyRecord = signedPreKeyRecord;
    }

    public int getId() {
        return mId;
    }

    public byte[] getSignedPreKeyRecord() {
        return mSignedPreKeyRecord;
    }
}
