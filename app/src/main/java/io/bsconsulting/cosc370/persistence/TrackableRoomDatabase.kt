package io.bsconsulting.cosc370.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.bsconsulting.cosc370.model.TrackType
import io.bsconsulting.cosc370.model.Trackable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Trackable::class], version=5)
abstract class TrackableRoomDatabase: RoomDatabase(){

    abstract fun trackableDao(): TrackableDao

    companion object {

        @Volatile
        private var INSTANCE: TrackableRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TrackableRoomDatabase {

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackableRoomDatabase::class.java,
                    "trackable_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(
                        TrackableDatabaseCallback(
                            scope
                        )
                    )
                    .build()

                INSTANCE = instance

                instance
            }
        }

        private class TrackableDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.trackableDao())
                    }
                }
            }

            suspend fun populateDatabase(trackableDao: TrackableDao){
                trackableDao.deleteAll()

                var trackable1 = Trackable(TrackType.FOOD.type);
                var trackable2 = Trackable(TrackType.DRINK.type);
                var trackable3 = Trackable(TrackType.NAPPY.type);
                trackableDao.insert(trackable1)
                trackableDao.insert(trackable2)
                trackableDao.insert(trackable3)
            }


        }
    }
}