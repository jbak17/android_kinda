package io.bsconsulting.cosc370

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackableDao {

    @Query("SELECT * FROM trackable_table")
    fun getAllTrackables(): LiveData<List<Trackable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trackable: Trackable)

    @Query("DELETE FROM trackable_table")
    suspend fun deleteAll()
}