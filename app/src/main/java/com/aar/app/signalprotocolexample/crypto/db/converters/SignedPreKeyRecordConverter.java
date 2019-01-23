package com.aar.app.signalprotocolexample.crypto.db.converters;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;

import androidx.room.TypeConverter;

public class SignedPreKeyRecordConverter {

    @TypeConverter
    public static byte[] toByte(SignedPreKeyRecord signedPreKeyRecord) {
        return signedPreKeyRecord.serialize();
    }

    @TypeConverter
    public static SignedPreKeyRecord fromByte(byte[] signedPreKeyRecord) {
        try {
            return new SignedPreKeyRecord(signedPreKeyRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
