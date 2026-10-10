package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_notifications")
data class ParentNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val title: String,
    val message: String,
    val dateStr: String,
    val category: String = "تقييم يومي", // تقييم يومي، حضور وغياب، تنبيه عام، تكريم
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
