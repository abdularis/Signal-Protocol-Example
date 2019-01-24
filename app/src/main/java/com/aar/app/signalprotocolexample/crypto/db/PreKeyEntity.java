package com.aar.app.signalprotocolexample.crypto.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prekeys")
public class PreKeyEntity {

    @PrimaryKey
    @ColumnInfo(name = "prekey_id")
    private int mPreKeyId;

    @ColumnInfo(name = "prekey")
    private byte[] mPreKeyRecord;

    public PreKeyEntity(int preKeyId, byte[] preKeyRecord) {
        mPreKeyId = preKeyId;
        mPreKeyRecord = preKeyRecord;
    }

    public int getPreKeyId() {
        return mPreKeyId;
    }

    public byte[] getPreKeyRecord() {
        return mPreKeyRecord;
    }
}
