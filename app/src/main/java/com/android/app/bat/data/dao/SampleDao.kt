package com.android.app.bat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.android.app.bat.data.SampleEntity

@Dao
interface SampleDao {

    @Insert
    fun save(entity: SampleEntity)

}