package com.aar.app.signalprotocolexample.crypto.db.dao;

import com.aar.app.signalprotocolexample.crypto.db.SessionEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM sessions WHERE address_name = :addressName AND device_id = :deviceId")
    SessionEntity queryByAddress(String addressName, int deviceId);

    @Query("SELECT device_id FROM sessions WHERE address_name = :addressName AND device_id != :notEqualDeviceId")
    List<Integer> queryDeviceIds(String addressName, int notEqualDeviceId);

    @Insert
    long insert(SessionEntity entity);

    @Query("SELECT COUNT(id) FROM sessions WHERE address_name = :addressName AND device_id = :deviceId")
    int queryCountByAddress(String addressName, int deviceId);

    @Query("DELETE FROM sessions WHERE address_name = :addressName AND device_id = :deviceId")
    void deleteByAddress(String addressName, int deviceId);

    @Query("DELETE FROM sessions WHERE address_name = :addressName")
    void deleteByAddressName(String addressName);

}
