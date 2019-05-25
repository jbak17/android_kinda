package io.bsconsulting.cosc370

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="trackable_table")
data class Trackable(
    @PrimaryKey
    @ColumnInfo(name="trackable") val type: String)


enum class TrackType(val type: String) {

    FOOD("Food"),
    DRINK("Drink"),
    NAPPY("Nappy"),
    MEDICINE("Medicine")

}