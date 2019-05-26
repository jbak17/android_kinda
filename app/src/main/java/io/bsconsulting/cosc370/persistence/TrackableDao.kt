package io.bsconsulting.cosc370.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import io.bsconsulting.cosc370.model.Trackable

@Dao
interface TrackableDao {

    @Query("SELECT * FROM trackable_table")
    fun getAllTrackables(): LiveData<List<Trackable>>

    @Query("Select * from trackable_table ")
    suspend fun getAll(): List<Trackable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trackable: Trackable)

    @Update
    suspend fun update(trackable: Trackable)

    @Query("DELETE FROM trackable_table")
    suspend fun deleteAll()

    @Query("UPDATE trackable_table SET active=:active WHERE trackable=:trackableType")
    suspend fun toggleStatus(trackableType: String, active: Boolean)
}