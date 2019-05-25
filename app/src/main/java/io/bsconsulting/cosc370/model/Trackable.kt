package io.bsconsulting.cosc370.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName="trackable_table")
data class Trackable(
    @PrimaryKey
    @ColumnInfo(name="trackable") val type: String, val active: Boolean = true, val frequency: Int = 60): Parcelable {

}


enum class TrackType(val type: String) {

    FOOD("Food"),
    DRINK("Drink"),
    NAPPY("Nappy"),
    MEDICINE("Medicine")

}