package io.bsconsulting.cosc370.persistence

import androidx.lifecycle.LiveData
import io.bsconsulting.cosc370.model.Trackable
import java.time.LocalDateTime

class TrackableRepository (private val trackableDao: TrackableDao){

    val allTrackable: LiveData<List<Trackable>> = trackableDao.getAllTrackables()

    suspend fun insert(trackable: Trackable){
        trackableDao.insert(trackable)
    }

    suspend fun update(trackable: Trackable){
        trackableDao.update(trackable)
    }

    suspend fun toggleStatus(trackableType: String, active: Boolean){
        trackableDao.toggleStatus(trackableType, active)
    }

    suspend fun addTrackableItem(trackableType: String, times: MutableList<LocalDateTime>){
        trackableDao.addTrackableItem(trackableType, times)
    }

    suspend fun getAll() = trackableDao.getAll()
}