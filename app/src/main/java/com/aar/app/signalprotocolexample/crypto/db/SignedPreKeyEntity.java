package com.aar.app.signalprotocolexample.crypto.db;

import com.aar.app.signalprotocolexample.crypto.db.converters.SignedPreKeyRecordConverter;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "signed_prekeys")
public class SignedPreKeyEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "signed_prekey")
    @TypeConverters({SignedPreKeyRecordConverter.class})
    private SignedPreKeyRecord mSignedPreKeyRecord;

    public SignedPreKeyEntity(int id, SignedPreKeyRecord signedPreKeyRecord) {
        mId = id;
        mSignedPreKeyRecord = signedPreKeyRecord;
    }

    public int getId() {
        return mId;
    }

    public SignedPreKeyRecord getSignedPreKeyRecord() {
        return mSignedPreKeyRecord;
    }
}
