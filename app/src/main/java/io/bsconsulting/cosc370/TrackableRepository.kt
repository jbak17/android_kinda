package io.bsconsulting.cosc370

import androidx.lifecycle.LiveData

class TrackableRepository (private val trackableDao: TrackableDao){

    val allTrackable: LiveData<List<Trackable>> = trackableDao.getAllTrackables()

    suspend fun insert(trackable: Trackable){
        trackableDao.insert(trackable)
    }
}