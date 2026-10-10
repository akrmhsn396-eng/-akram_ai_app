package com.example.data.local

import androidx.room.*
import com.example.data.model.RecitationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface RecitationDao {
    @Query("SELECT * FROM recitations ORDER BY timestamp DESC")
    fun getAllRecitationsFlow(): Flow<List<RecitationRecord>>

    @Query("SELECT * FROM recitations WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getRecitationsByStudentFlow(studentId: Long): Flow<List<RecitationRecord>>

    @Query("SELECT * FROM recitations WHERE dateStr = :dateStr ORDER BY timestamp DESC")
    fun getRecitationsByDateFlow(dateStr: String): Flow<List<RecitationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecitation(record: RecitationRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<RecitationRecord>)

    @Query("SELECT COUNT(*) FROM recitations")
    suspend fun count(): Int

    @Query("DELETE FROM recitations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM recitations")
    suspend fun deleteAll()
}
