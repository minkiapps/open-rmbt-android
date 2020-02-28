package at.specure.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import at.specure.data.Tables
import at.specure.data.entity.History
import at.specure.data.entity.HistoryContainer
import at.specure.data.entity.HistoryReference

@Dao
abstract class HistoryDao {

    @Query("SELECT * from ${Tables.HISTORY_REFERENCE} ORDER BY time DESC")
    abstract fun getHistorySource(): DataSource.Factory<Int, HistoryContainer>

    @Query("SELECT COUNT(*) from ${Tables.HISTORY}")
    abstract fun getItemsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveHistory(history: List<History>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun saveReferences(history: List<HistoryReference>)

    @Transaction
    open fun insert(history: List<History>) {
        val references = history.map {
            HistoryReference(it.referenceUUID, it.time)
        }
        saveHistory(history)
        saveReferences(references)
    }

    @Query("DELETE FROM ${Tables.HISTORY_REFERENCE}")
    abstract fun clear(): Int

    @Transaction
    open fun clearInsert(history: List<History>) {
        clear()
        insert(history)
    }
}