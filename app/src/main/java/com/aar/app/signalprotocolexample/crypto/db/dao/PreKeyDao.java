package com.aar.app.signalprotocolexample.crypto.db.dao;

import com.aar.app.signalprotocolexample.crypto.db.PreKeyEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PreKeyDao {

    @Query("SELECT * FROM prekeys WHERE prekey_id = :preKeyId")
    PreKeyEntity queryByPreKeyId(int preKeyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreKey(PreKeyEntity preKeyEntity);

    @Query("SELECT COUNT(prekey_id) FROM prekeys WHERE prekey_id = :preKeyId")
    int countByPreKeyId(int preKeyId);

    @Query("DELETE FROM prekeys WHERE prekey_id = :preKeyId")
    void deleteByPreKeyId(int preKeyId);

}
