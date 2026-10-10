package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class RecitationType(val titleAr: String) {
    NEW_MEMORIZATION("حفظ جديد"),
    REVISION("مراجعة يومية"),
    CONSOLIDATION("مراجعة كبرى / تثبيت")
}

enum class RecitationRating(val titleAr: String, val stars: Int, val points: Int) {
    EXCELLENT("ممتاز 🌟", 5, 20),
    VERY_GOOD("جيد جداً ✨", 4, 15),
    GOOD("جيد 👍", 3, 10),
    ACCEPTABLE("مقبول ⚠️", 2, 5),
    NEEDS_RETRY("يحتاج إعادة وتدريب 🔄", 1, 2)
}

@Entity(
    tableName = "recitations",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("dateStr")]
)
data class RecitationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val type: RecitationType = RecitationType.NEW_MEMORIZATION,
    val surahName: String,
    val fromAyah: Int = 1,
    val toAyah: Int = 10,
    val rating: RecitationRating = RecitationRating.EXCELLENT,
    val tajweedScore: Int = 9, // 1 - 10
    val fluencyScore: Int = 9, // 1 - 10
    val teacherNotes: String = "",
    val pointsAwarded: Int = 20,
    val teacherName: String = "الشيخ عبد الرحمن",
    val dateStr: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
