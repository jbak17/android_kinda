package io.bsconsulting.cosc370.persistence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.bsconsulting.cosc370.model.Trackable
import io.bsconsulting.cosc370.persistence.TrackableRepository
import io.bsconsulting.cosc370.persistence.TrackableRoomDatabase
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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

    fun update(trackable: Trackable) = viewModelScope.launch {
        repository.update(trackable)
    }

    fun toggle(trackableType: String, active: Boolean) = viewModelScope.launch {
        repository.toggleStatus(trackableType, active)
    }

    fun addTime(trackableType: String, active: MutableList<LocalDateTime>) = viewModelScope.launch {
        repository.addTrackableItem(trackableType, active)
    }

    fun initialiseDB() = viewModelScope.launch { repository.getAll() }
}