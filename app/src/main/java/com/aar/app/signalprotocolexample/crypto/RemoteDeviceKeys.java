package com.aar.app.signalprotocolexample.crypto;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;

public class RemoteDeviceKeys {

    private SignalProtocolAddress mAddress;
    private int mRegistrationId;
    private int mPreKeyId;
    private ECPublicKey mPreKeyPublic;
    private int mSignPreKeyId;
    private ECPublicKey mSignedPreKeyPublic;
    private byte[] mSignedPreKeySignature;
    private IdentityKey mIdentityKey;

    public RemoteDeviceKeys(SignalProtocolAddress address,
                            int registrationId,
                            int preKeyId,
                            ECPublicKey preKeyPublic,
                            int signPreKeyId,
                            ECPublicKey signedPreKeyPublic,
                            byte[] signedPreKeySignature,
                            IdentityKey identityKey) {
        mAddress = address;
        mRegistrationId = registrationId;
        mPreKeyId = preKeyId;
        mPreKeyPublic = preKeyPublic;
        mSignPreKeyId = signPreKeyId;
        mSignedPreKeyPublic = signedPreKeyPublic;
        mSignedPreKeySignature = signedPreKeySignature;
        mIdentityKey = identityKey;
    }

    public SignalProtocolAddress getAddress() {
        return mAddress;
    }

    public int getRegistrationId() {
        return mRegistrationId;
    }

    public int getPreKeyId() {
        return mPreKeyId;
    }

    public ECPublicKey getPreKeyPublic() {
        return mPreKeyPublic;
    }

    public int getSignPreKeyId() {
        return mSignPreKeyId;
    }

    public ECPublicKey getSignedPreKeyPublic() {
        return mSignedPreKeyPublic;
    }

    public byte[] getSignedPreKeySignature() {
        return mSignedPreKeySignature;
    }

    public IdentityKey getIdentityKey() {
        return mIdentityKey;
    }

    public String toJson() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("name", mAddress.getName());
        object.put("dev_id", mAddress.getDeviceId());
        object.put("reg_id", mRegistrationId);
        object.put("prekey_id", mPreKeyId);
        object.put("prekey_public", new String(Base64.encode(mPreKeyPublic.serialize(), Base64.DEFAULT)));
        object.put("signed_prekey_id", mSignPreKeyId);
        object.put("signed_prekey_public", new String(Base64.encode(mSignedPreKeyPublic.serialize(), Base64.DEFAULT)));
        object.put("signed_prekey_signature", new String(Base64.encode(mSignedPreKeySignature, Base64.DEFAULT)));
        object.put("id_key", new String(Base64.encode(mIdentityKey.serialize(), Base64.DEFAULT)));
        return object.toString();
    }

    public static RemoteDeviceKeys fromJson(String jsonString)
            throws InvalidKeyException, JSONException {

        JSONObject object = new JSONObject(jsonString);

        String name = object.getString("name");
        int deviceId = object.getInt("dev_id");
        int registrationId = object.getInt("reg_id");
        int preKeyId = object.getInt("prekey_id");
        byte[] preKeyPublic = Base64.decode(object.getString("prekey_public"), Base64.DEFAULT);
        int signedPreKeyId = object.getInt("signed_prekey_id");
        byte[] signedPreKeyPublic = Base64.decode(object.getString("signed_prekey_public"), Base64.DEFAULT);
        byte[] signedPreKeySignature = Base64.decode(object.getString("signed_prekey_signature"), Base64.DEFAULT);
        byte[] identityKey = Base64.decode(object.getString("id_key"), Base64.DEFAULT);

        return new RemoteDeviceKeys(
                new SignalProtocolAddress(name, deviceId),
                registrationId,
                preKeyId,
                Curve.decodePoint(preKeyPublic, 0),
                signedPreKeyId,
                Curve.decodePoint(signedPreKeyPublic, 0),
                signedPreKeySignature,
                new IdentityKey(identityKey, 0)
        );
    }
}
