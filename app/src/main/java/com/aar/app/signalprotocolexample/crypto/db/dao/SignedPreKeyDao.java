package com.aar.app.signalprotocolexample.crypto.db.dao;

import com.aar.app.signalprotocolexample.crypto.db.SignedPreKeyEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SignedPreKeyDao {

    @Query("SELECT * FROM signed_prekeys")
    List<SignedPreKeyEntity> queryAll();

    @Query("SELECT * FROM signed_prekeys WHERE id = :signedPreKeyId")
    SignedPreKeyEntity queryById(int signedPreKeyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SignedPreKeyEntity signedPreKeyEntity);

    @Query("DELETE FROM signed_prekeys WHERE id = :signedPreKeyId")
    void deleteById(int signedPreKeyId);

    @Query("SELECT COUNT(*) FROM signed_prekeys WHERE id = :signedPreKeyId")
    int queryCountById(int signedPreKeyId);

}
