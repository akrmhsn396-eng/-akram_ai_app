package com.example.data.local

import com.example.data.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SampleDataProvider {
    fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getYesterdayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(System.currentTimeMillis() - 86400000L))
    }

    fun getInitialStudents(): List<Student> = listOf(
        Student(
            id = 1,
            name = "عمر أحمد الشريف",
            gender = Gender.MALE,
            circleName = "حلقة الإمام الشاطبي",
            parentPhone = "0501234567",
            parentCode = "QRN-101",
            targetGoal = "ختم القرآن الكريم كاملاً",
            currentJuz = 29,
            totalMemorizedPages = 35,
            totalPoints = 380,
            streakDays = 12,
            notes = "طالب متميز في التجويد ومخارج الحروف، صوته ندي ما شاء الله"
        ),
        Student(
            id = 2,
            name = "فاطمة محمد السعيد",
            gender = Gender.FEMALE,
            circleName = "حلقة أم المؤمنين خديجة",
            parentPhone = "0559876543",
            parentCode = "QRN-102",
            targetGoal = "حفظ الأجزاء الخمسة الأخيرة",
            currentJuz = 28,
            totalMemorizedPages = 52,
            totalPoints = 420,
            streakDays = 15,
            notes = "حفظ متقن ومراجعة دورية ممتازة"
        ),
        Student(
            id = 3,
            name = "يوسف خالد العتيبي",
            gender = Gender.MALE,
            circleName = "حلقة الإمام نافع",
            parentPhone = "0543219876",
            parentCode = "QRN-103",
            targetGoal = "حفظ جزء عم وتبارك",
            currentJuz = 30,
            totalMemorizedPages = 18,
            totalPoints = 210,
            streakDays = 7,
            notes = "مواظب على الحضور، يحتاج تركيزاً إضافياً في أحكام النون الساكنة"
        ),
        Student(
            id = 4,
            name = "مريم عبد الله الغامدي",
            gender = Gender.FEMALE,
            circleName = "حلقة الزهراء للفتيات",
            parentPhone = "0561122334",
            parentCode = "QRN-104",
            targetGoal = "ختم القرآن الكريم كاملاً",
            currentJuz = 25,
            totalMemorizedPages = 110,
            totalPoints = 650,
            streakDays = 22,
            notes = "نجمة الحلقة، قارئة متقنة وحافظة ماهرة"
        ),
        Student(
            id = 5,
            name = "عبد الرحمن طارق المنصور",
            gender = Gender.MALE,
            circleName = "حلقة الإمام الشاطبي",
            parentPhone = "0533344556",
            parentCode = "QRN-105",
            targetGoal = "حفظ 10 أجزاء",
            currentJuz = 27,
            totalMemorizedPages = 45,
            totalPoints = 290,
            streakDays = 9,
            notes = "تقدم ملحوظ في التسميع وسرعة الاستيعاب"
        ),
        Student(
            id = 6,
            name = "سارة إبراهيم القحطاني",
            gender = Gender.FEMALE,
            circleName = "حلقة أم المؤمنين خديجة",
            parentPhone = "0577788990",
            parentCode = "QRN-106",
            targetGoal = "حفظ جزء عم",
            currentJuz = 30,
            totalMemorizedPages = 15,
            totalPoints = 175,
            streakDays = 6,
            notes = "بداية مباركة، حرص كبير على الحضور المبكر"
        ),
        Student(
            id = 7,
            name = "حمزة فهد الدوسري",
            gender = Gender.MALE,
            circleName = "حلقة البراعم الصغار",
            parentPhone = "0588899112",
            parentCode = "QRN-107",
            targetGoal = "حفظ قصار السور",
            currentJuz = 30,
            totalMemorizedPages = 8,
            totalPoints = 130,
            streakDays = 4,
            notes = "نشيط ومحب للحلقة، يتم تشجيعه بالنقاط"
        ),
        Student(
            id = 8,
            name = "نور الهدى صالح العمري",
            gender = Gender.FEMALE,
            circleName = "حلقة الحافظات",
            parentPhone = "0599900113",
            parentCode = "QRN-108",
            targetGoal = "ختم القرآن الكريم كاملاً",
            currentJuz = 20,
            totalMemorizedPages = 200,
            totalPoints = 850,
            streakDays = 30,
            notes = "قدوة لزميلاتها، تم إتقان ثلثي القرآن الكريم"
        )
    )

    fun getInitialRecitations(today: String, yesterday: String): List<RecitationRecord> = listOf(
        RecitationRecord(
            studentId = 1,
            studentName = "عمر أحمد الشريف",
            type = RecitationType.NEW_MEMORIZATION,
            surahName = "الملك",
            fromAyah = 1,
            toAyah = 15,
            rating = RecitationRating.EXCELLENT,
            tajweedScore = 10,
            fluencyScore = 10,
            teacherNotes = "ما شاء الله، إتقان تام للمدود ومخارج الحروف.",
            pointsAwarded = 25,
            teacherName = "الشيخ عبد الرحمن",
            dateStr = today
        ),
        RecitationRecord(
            studentId = 1,
            studentName = "عمر أحمد الشريف",
            type = RecitationType.REVISION,
            surahName = "النبأ",
            fromAyah = 1,
            toAyah = 40,
            rating = RecitationRating.EXCELLENT,
            tajweedScore = 9,
            fluencyScore = 10,
            teacherNotes = "مراجعة متينة ومتقنة.",
            pointsAwarded = 20,
            teacherName = "الشيخ عبد الرحمن",
            dateStr = yesterday
        ),
        RecitationRecord(
            studentId = 2,
            studentName = "فاطمة محمد السعيد",
            type = RecitationType.NEW_MEMORIZATION,
            surahName = "المجادلة",
            fromAyah = 1,
            toAyah = 10,
            rating = RecitationRating.EXCELLENT,
            tajweedScore = 10,
            fluencyScore = 9,
            teacherNotes = "تلاوة خاشعة وأداء رائع ومتميز.",
            pointsAwarded = 25,
            teacherName = "الأستاذة أمينة",
            dateStr = today
        ),
        RecitationRecord(
            studentId = 3,
            studentName = "يوسف خالد العتيبي",
            type = RecitationType.NEW_MEMORIZATION,
            surahName = "الأعلى",
            fromAyah = 1,
            toAyah = 19,
            rating = RecitationRating.VERY_GOOD,
            tajweedScore = 8,
            fluencyScore = 8,
            teacherNotes = "حفظ جيد جداً، نوصي بمراجعة الغنن.",
            pointsAwarded = 15,
            teacherName = "الشيخ عبد الرحمن",
            dateStr = today
        ),
        RecitationRecord(
            studentId = 4,
            studentName = "مريم عبد الله الغامدي",
            type = RecitationType.CONSOLIDATION,
            surahName = "الفرقان",
            fromAyah = 1,
            toAyah = 77,
            rating = RecitationRating.EXCELLENT,
            tajweedScore = 10,
            fluencyScore = 10,
            teacherNotes = "تثبيت سورة الفرقان كاملة بامتياز فائق.",
            pointsAwarded = 35,
            teacherName = "الأستاذة فاطمة",
            dateStr = today
        )
    )

    fun getInitialAttendance(today: String, yesterday: String): List<AttendanceRecord> = listOf(
        AttendanceRecord(studentId = 1, dateStr = today, status = AttendanceStatus.PRESENT, note = "حضور مبكر ونشاط ممتاز"),
        AttendanceRecord(studentId = 2, dateStr = today, status = AttendanceStatus.PRESENT, note = "حاضرة في الموعد المحدد"),
        AttendanceRecord(studentId = 3, dateStr = today, status = AttendanceStatus.LATE, note = "تأخر 10 دقائق بعذر"),
        AttendanceRecord(studentId = 4, dateStr = today, status = AttendanceStatus.PRESENT, note = "حاضرة ومستعدة"),
        AttendanceRecord(studentId = 5, dateStr = today, status = AttendanceStatus.PRESENT, note = "حاضر"),
        AttendanceRecord(studentId = 6, dateStr = today, status = AttendanceStatus.EXCUSED, note = "استئذان لظرف عائلي"),
        AttendanceRecord(studentId = 7, dateStr = today, status = AttendanceStatus.PRESENT, note = "حاضر بنشاط"),
        AttendanceRecord(studentId = 8, dateStr = today, status = AttendanceStatus.PRESENT, note = "حاضرة"),
        // Yesterday
        AttendanceRecord(studentId = 1, dateStr = yesterday, status = AttendanceStatus.PRESENT),
        AttendanceRecord(studentId = 2, dateStr = yesterday, status = AttendanceStatus.PRESENT),
        AttendanceRecord(studentId = 3, dateStr = yesterday, status = AttendanceStatus.PRESENT),
        AttendanceRecord(studentId = 4, dateStr = yesterday, status = AttendanceStatus.PRESENT)
    )

    fun getInitialNotifications(today: String, yesterday: String): List<ParentNotification> = listOf(
        ParentNotification(
            studentId = 1,
            studentName = "عمر أحمد الشريف",
            title = "تقرير الحفظ اليومي لـ عمر",
            message = "أتم ابنكم عمر تسميع سورة الملك (الآيات 1-15) بتقدير ممتاز 🌟 ودرجة تجويد 10/10. بارك الله في جهوده.",
            dateStr = today,
            category = "تقييم يومي",
            isRead = false
        ),
        ParentNotification(
            studentId = 2,
            studentName = "فاطمة محمد السعيد",
            title = "تقرير إنجاز الحفظ لـ فاطمة",
            message = "أكملت ابنتكم فاطمة حفظ سورة المجادلة (1-10) بتقدير ممتاز وحصلت على 25 نقطة إضافية.",
            dateStr = today,
            category = "تقييم يومي",
            isRead = false
        ),
        ParentNotification(
            studentId = 3,
            studentName = "يوسف خالد العتيبي",
            title = "تنبيه الحضور لـ يوسف",
            message = "تم تسجيل حضور الطالب يوسف مع تأخر خفيف (10 دقائق). نرجو حثه على الحضور في بداية الحلقة.",
            dateStr = today,
            category = "حضور وغياب",
            isRead = true
        ),
        ParentNotification(
            studentId = 4,
            studentName = "مريم عبد الله الغامدي",
            title = "تهنئة وتكريم: وسام القارئ الماهر",
            message = "مبارك لولي أمر الطالبة مريم حصولها على وسام القارئ الماهر بعد إتمام مراجعة سورة الفرقان كاملة بنجاح باهر!",
            dateStr = yesterday,
            category = "تكريم",
            isRead = true
        )
    )
}
