package at.specure.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import at.specure.data.Tables
import at.specure.data.entity.TacRecord

@Dao
abstract class TacDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveTermsAndConditions(tac: TacRecord)

    @Query("SELECT * FROM ${Tables.TAC} WHERE language == :language")
    abstract fun loadTermsAndConditions(language: String): TacRecord?

    @Query("DELETE FROM ${Tables.TAC} WHERE language == :language")
    abstract fun deleteTermsAndCondition(language: String)

    @Transaction
    open fun clearInsertItems(tac: TacRecord) {
        deleteTermsAndCondition(tac.language)
        saveTermsAndConditions(tac)
    }
}