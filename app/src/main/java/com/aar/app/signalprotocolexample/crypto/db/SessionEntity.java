package com.aar.app.signalprotocolexample.crypto.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class SessionEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "address_name")
    private String mProtocolAddressName;

    @ColumnInfo(name = "device_id")
    private int mDeviceId;

    @ColumnInfo(name = "session")
    private byte[] mSession;

    public SessionEntity(int id, String protocolAddressName, int deviceId, byte[] session) {
        mId = id;
        mProtocolAddressName = protocolAddressName;
        mDeviceId = deviceId;
        mSession = session;
    }

    public int getId() {
        return mId;
    }

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
