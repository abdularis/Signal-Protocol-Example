package com.aar.app.signalprotocolexample.crypto.db.converters;

import android.util.Log;

import org.whispersystems.libsignal.state.PreKeyRecord;

import java.io.IOException;

import androidx.room.TypeConverter;

public class PreKeyRecordConverter {

    @TypeConverter
    public static byte[] preKeyToByte(PreKeyRecord preKeyRecord) {
        return preKeyRecord.serialize();
    }

    @TypeConverter
    public static PreKeyRecord preKeyFromByte(byte[] preKey) {
        try {
            return new PreKeyRecord(preKey);
        } catch (IOException e) {
            Log.d("RoomPreKeyConvert", "Failed to convert byte[] to PreKeyRecord");
            e.printStackTrace();
        }
        return null;
    }
}
