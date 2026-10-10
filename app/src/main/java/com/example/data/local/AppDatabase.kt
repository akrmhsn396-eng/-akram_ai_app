package com.example.data.local

import android.content.Context
import androidx.room.*
import com.example.data.model.*

class Converters {
    @TypeConverter
    fun fromGender(value: Gender): String = value.name

    @TypeConverter
    fun toGender(value: String): Gender = try {
        Gender.valueOf(value)
    } catch (e: Exception) {
        Gender.MALE
    }

    @TypeConverter
    fun fromRecitationType(value: RecitationType): String = value.name

    @TypeConverter
    fun toRecitationType(value: String): RecitationType = try {
        RecitationType.valueOf(value)
    } catch (e: Exception) {
        RecitationType.NEW_MEMORIZATION
    }

    @TypeConverter
    fun fromRecitationRating(value: RecitationRating): String = value.name

    @TypeConverter
    fun toRecitationRating(value: String): RecitationRating = try {
        RecitationRating.valueOf(value)
    } catch (e: Exception) {
        RecitationRating.EXCELLENT
    }

    @TypeConverter
    fun fromAttendanceStatus(value: AttendanceStatus): String = value.name

    @TypeConverter
    fun toAttendanceStatus(value: String): AttendanceStatus = try {
        AttendanceStatus.valueOf(value)
    } catch (e: Exception) {
        AttendanceStatus.PRESENT
    }
}

@Database(
    entities = [
        Student::class,
        RecitationRecord::class,
        AttendanceRecord::class,
        ParentNotification::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun recitationDao(): RecitationDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quran_circles_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
