package com.aar.app.signalprotocolexample.crypto.db.dao;

import com.aar.app.signalprotocolexample.crypto.db.LocalIdentityEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LocalIdentityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLocalIdentity(LocalIdentityEntity entity);

    @Query("SELECT * FROM local_identity")
    LocalIdentityEntity query();

    @Query("SELECT reg_id FROM local_identity")
    int queryRegistrationId();

}
