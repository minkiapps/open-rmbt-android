package at.specure.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import at.specure.database.Tables.CELL_LOCATION

@Entity(tableName = CELL_LOCATION)
data class CellLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ForeignKey(entity = Test::class, parentColumns = ["testUUID"], childColumns = ["testUUID"], onDelete = ForeignKey.CASCADE)
    val testUUID: String?,
    val primaryScramblingCode: Int?,
    val areaCode: Int?,
    val locationId: String?,
    val time: Long?,
    val timeNs: Long?
)