package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class AttendanceStatus(val labelAr: String, val colorHex: Long) {
    PRESENT("حاضر", 0xFF2E7D32),
    LATE("متأخر", 0xFFF57C00),
    EXCUSED("مستأذن", 0xFF0288D1),
    ABSENT("غائب", 0xFFD32F2F)
}

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("dateStr"), Index(value = ["studentId", "dateStr"], unique = true)]
)
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val dateStr: String, // YYYY-MM-DD
    val status: AttendanceStatus = AttendanceStatus.PRESENT,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
