package com.aar.app.signalprotocolexample.crypto.db;

import org.whispersystems.libsignal.IdentityKey;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "trusted_identities")
public class TrustedKeyEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "address_name")
    private String mAddressName;

    @ColumnInfo(name = "device_id")
    private int mDeviceId;

    @ColumnInfo(name = "identity_key")
    private byte[] mIdentityKey;

    public TrustedKeyEntity(@NonNull String addressName, int deviceId, byte[] identityKey) {
        mAddressName = addressName;
        mDeviceId = deviceId;
        mIdentityKey = identityKey;
    }

    public String getAddressName() {
        return mAddressName;
    }

    public int getDeviceId() {
        return mDeviceId;
    }

    public byte[] getIdentityKey() {
        return mIdentityKey;
    }
}
