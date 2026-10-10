package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Gender {
    MALE, FEMALE
}

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val gender: Gender = Gender.MALE,
    val circleName: String, // اسم الحلقة
    val parentPhone: String,
    val parentCode: String, // كود ولي الأمر للدخول السريع مثلاً QR-101
    val targetGoal: String = "ختم القرآن الكريم كاملاً",
    val currentJuz: Int = 30, // الجزء الحالي
    val totalMemorizedPages: Int = 20,
    val totalPoints: Int = 100,
    val streakDays: Int = 5,
    val notes: String = "",
    val active: Boolean = true
) {
    val genderLabel: String
        get() = if (gender == Gender.MALE) "طالب" else "طالبة"

    val levelRank: String
        get() = when {
            totalPoints >= 500 -> "القارئ الماهر 🌟"
            totalPoints >= 300 -> "المواظب الذهبي 🥇"
            totalPoints >= 150 -> "المجتهد المتميز 🥈"
            else -> "طالب واعد 🌱"
        }
}
