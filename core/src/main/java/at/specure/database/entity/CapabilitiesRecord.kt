package at.specure.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import at.specure.database.Columns
import at.specure.database.Tables

@Entity(tableName = Tables.CAPABILITIES)
data class CapabilitiesRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ForeignKey(
        entity = TestRecord::class,
        parentColumns = [Columns.TEST_UUID_PARENT_COLUMN],
        childColumns = ["testUUID"],
        onDelete = ForeignKey.CASCADE
    )
    val testUUID: String,
    val classificationCount: Int,
    val qosSupportInfo: Boolean,
    val rmbtHttpStatus: Boolean
)