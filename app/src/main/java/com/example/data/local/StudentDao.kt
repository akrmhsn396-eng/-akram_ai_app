package com.example.data.local

import androidx.room.*
import com.example.data.model.Gender
import com.example.data.model.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE active = 1 ORDER BY name ASC")
    fun getAllStudentsFlow(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Long): Student?

    @Query("SELECT * FROM students WHERE parentCode = :code LIMIT 1")
    suspend fun getStudentByParentCode(code: String): Student?

    @Query("SELECT * FROM students WHERE gender = :gender AND active = 1 ORDER BY name ASC")
    fun getStudentsByGenderFlow(gender: Gender): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE circleName = :circleName AND active = 1 ORDER BY name ASC")
    fun getStudentsByCircleFlow(circleName: String): Flow<List<Student>>

    @Query("SELECT DISTINCT circleName FROM students WHERE active = 1")
    fun getCirclesFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("UPDATE students SET totalPoints = totalPoints + :points, streakDays = streakDays + 1 WHERE id = :studentId")
    suspend fun awardPoints(studentId: Long, points: Int)

    @Query("SELECT COUNT(*) FROM students")
    suspend fun count(): Int

    @Query("DELETE FROM students")
    suspend fun deleteAll()
}
