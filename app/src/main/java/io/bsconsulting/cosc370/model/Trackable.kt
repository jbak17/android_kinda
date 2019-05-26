package io.bsconsulting.cosc370.model

import android.os.Parcelable
import androidx.room.*
import io.bsconsulting.cosc370.persistence.DateListConverter
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName="trackable_table")
//@TypeConverters(DateConverter::class)
@TypeConverters(DateListConverter::class)
data class Trackable(
    @PrimaryKey
    @ColumnInfo(name="trackable") val type: String,
    val active: Boolean = true,
    val frequency: Int = 2,
    var activity: MutableList<LocalDateTime> = mutableListOf<LocalDateTime>(LocalDateTime.now())): Parcelable {

//    init {
//        val now: LocalDateTime = LocalDateTime.now().plusHours(1)
//        activity.add(now)
//        activity.add(now.plusHours(1))
//    }
}



enum class TrackType(val type: String) {

    FOOD("Food"),
    DRINK("Drink"),
    NAPPY("Nappy"),
    MEDICINE("Medicine")

}