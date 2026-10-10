package com.example.data.local

import androidx.room.*
import com.example.data.model.ParentNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM parent_notifications ORDER BY timestamp DESC")
    fun getAllNotificationsFlow(): Flow<List<ParentNotification>>

    @Query("SELECT * FROM parent_notifications WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getNotificationsByStudentFlow(studentId: Long): Flow<List<ParentNotification>>

    @Query("SELECT COUNT(*) FROM parent_notifications WHERE isRead = 0")
    fun getUnreadCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: ParentNotification): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<ParentNotification>)

    @Query("UPDATE parent_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE parent_notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM parent_notifications")
    suspend fun deleteAll()
}
