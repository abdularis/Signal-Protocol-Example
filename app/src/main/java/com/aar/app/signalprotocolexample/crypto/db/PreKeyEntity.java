package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.converters.PreKeyRecordConverter;

import org.whispersystems.libsignal.state.PreKeyRecord;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "prekeys")
public class PreKeyEntity {

    @PrimaryKey
    @ColumnInfo(name = "prekey_id")
    private int mPreKeyId;

    @ColumnInfo(name = "prekey")
    @TypeConverters({PreKeyRecordConverter.class})
    private PreKeyRecord mPreKeyRecord;

    public PreKeyEntity(int preKeyId, PreKeyRecord preKeyRecord) {
        mPreKeyId = preKeyId;
        mPreKeyRecord = preKeyRecord;
    }

    public int getPreKeyId() {
        return mPreKeyId;
    }

    public PreKeyRecord getPreKeyRecord() {
        return mPreKeyRecord;
    }
}
