package com.aar.app.signalprotocolexample.crypto.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions", primaryKeys = {"address_name", "device_id"})
public class SessionEntity {

    @NonNull
    @ColumnInfo(name = "address_name")
    private String mProtocolAddressName;

    @ColumnInfo(name = "device_id")
    private int mDeviceId;

    @ColumnInfo(name = "session")
    private byte[] mSession;

    public SessionEntity(@NonNull String protocolAddressName, int deviceId, byte[] session) {
        mProtocolAddressName = protocolAddressName;
        mDeviceId = deviceId;
        mSession = session;
    }

    @NonNull
    public String getProtocolAddressName() {
        return mProtocolAddressName;
    }

    public int getDeviceId() {
        return mDeviceId;
    }

    public byte[] getSession() {
        return mSession;
    }
}
