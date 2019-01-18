package com.aar.app.signalprotocolexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private static void init() {
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
    }
}
