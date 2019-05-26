package io.bsconsulting.cosc370.model

import android.os.Parcelable
import androidx.room.*
import io.bsconsulting.cosc370.persistence.DateConverter
import io.bsconsulting.cosc370.persistence.DateListConverter
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
@Entity(tableName="trackable_table")
//@TypeConverters(DateConverter::class)
@TypeConverters(DateListConverter::class)
data class Trackable(
    @PrimaryKey
    @ColumnInfo(name="trackable") val type: String,
    val active: Boolean = true,
    val frequency: Int = 60,

    val lastActivity: List<LocalDateTime> = listOf<LocalDateTime>(LocalDateTime.now())): Parcelable {


}



enum class TrackType(val type: String) {

    FOOD("Food"),
    DRINK("Drink"),
    NAPPY("Nappy"),
    MEDICINE("Medicine")

}