package com.example.data.local

import androidx.room.*
import com.example.data.model.AttendanceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance ORDER BY timestamp DESC")
    fun getAllAttendanceFlow(): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance WHERE dateStr = :dateStr")
    fun getAttendanceByDateFlow(dateStr: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY dateStr DESC")
    fun getAttendanceByStudentFlow(studentId: Long): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: AttendanceRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<AttendanceRecord>)

    @Query("SELECT COUNT(*) FROM attendance")
    suspend fun count(): Int

    @Query("DELETE FROM attendance WHERE studentId = :studentId AND dateStr = :dateStr")
    suspend fun deleteForStudentAndDate(studentId: Long, dateStr: String)

    @Query("DELETE FROM attendance")
    suspend fun deleteAll()
}
