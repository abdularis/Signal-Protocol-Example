package com.aar.app.signalprotocolexample.crypto;

import android.util.Base64;

import com.aar.app.signalprotocolexample.crypto.RemoteDeviceKeys;

import org.json.JSONException;
import org.json.JSONObject;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.ecc.Curve;

public class RemoteDeviceKeysUtils {

    private static final String K_NAME = "name";
    private static final String K_DEV_ID = "dev_id";
    private static final String K_REG_ID = "reg_id";
    private static final String K_PREKEY_ID = "pk_id";
    private static final String K_PREKEY_PUBLIC = "pk_pub";
    private static final String K_SIGNED_PREKEY_ID = "spk_id";
    private static final String K_SIGNED_PREKEY_PUBLIC = "spk_pub";
    private static final String K_SIGNED_PREKEY_SIG = "spk_sig";
    private static final String K_ID_KEY = "id_key";

    public static String toJson(RemoteDeviceKeys rdk) throws JSONException {
        JSONObject object = new JSONObject();

        object.put(K_NAME, rdk.getAddress().getName());
        object.put(K_DEV_ID, rdk.getAddress().getDeviceId());
        object.put(K_REG_ID, rdk.getRegistrationId());
        object.put(K_PREKEY_ID, rdk.getPreKeyId());
        object.put(K_PREKEY_PUBLIC, new String(Base64.encode(rdk.getPreKeyPublic().serialize(), Base64.DEFAULT)));
        object.put(K_SIGNED_PREKEY_ID, rdk.getSignPreKeyId());
        object.put(K_SIGNED_PREKEY_PUBLIC, new String(Base64.encode(rdk.getSignedPreKeyPublic().serialize(), Base64.DEFAULT)));
        object.put(K_SIGNED_PREKEY_SIG, new String(Base64.encode(rdk.getSignedPreKeySignature(), Base64.DEFAULT)));
        object.put(K_ID_KEY, new String(Base64.encode(rdk.getIdentityKey().serialize(), Base64.DEFAULT)));
        return object.toString();
    }

    public static RemoteDeviceKeys fromJson(String jsonString)
            throws InvalidKeyException, JSONException {

        JSONObject object = new JSONObject(jsonString);

        String name = object.getString(K_NAME);
        int deviceId = object.getInt(K_DEV_ID);
        int registrationId = object.getInt(K_REG_ID);
        int preKeyId = object.getInt(K_PREKEY_ID);
        byte[] preKeyPublic = Base64.decode(object.getString(K_PREKEY_PUBLIC), Base64.DEFAULT);
        int signedPreKeyId = object.getInt(K_SIGNED_PREKEY_ID);
        byte[] signedPreKeyPublic = Base64.decode(object.getString(K_SIGNED_PREKEY_PUBLIC), Base64.DEFAULT);
        byte[] signedPreKeySignature = Base64.decode(object.getString(K_SIGNED_PREKEY_SIG), Base64.DEFAULT);
        byte[] identityKey = Base64.decode(object.getString(K_ID_KEY), Base64.DEFAULT);

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
