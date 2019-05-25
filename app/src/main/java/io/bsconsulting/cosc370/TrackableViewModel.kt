package io.bsconsulting.cosc370

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TrackableViewModel (application: Application): AndroidViewModel(application){

    private val repository: TrackableRepository

    val allTrackables: LiveData<List<Trackable>>

    init {
        val trackableDao = TrackableRoomDatabase.getDatabase(application, viewModelScope).trackableDao()
        repository = TrackableRepository(trackableDao)

        allTrackables = repository.allTrackable
    }

    fun insert(trackable: Trackable) = viewModelScope.launch {
        repository.insert(trackable)
    }
}