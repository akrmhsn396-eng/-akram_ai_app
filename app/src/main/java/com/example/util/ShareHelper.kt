package com.example.util

import android.content.Context
import android.content.Intent
import com.example.data.model.AttendanceRecord
import com.example.data.model.RecitationRecord
import com.example.data.model.Student

object ShareHelper {

    fun generateParentDailyReport(
        student: Student,
        recitation: RecitationRecord?,
        attendance: AttendanceRecord?,
        dateStr: String
    ): String {
        val sb = StringBuilder()
        sb.appendLine("🌿 *حلقات تحفيظ القرآن الكريم* 🌿")
        sb.appendLine("📋 *تقرير المتابعة اليومي لولي الأمر*")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("👤 *الاسم:* ${student.name} (${student.genderLabel})")
        sb.appendLine("🕌 *الحلقة:* ${student.circleName}")
        sb.appendLine("📅 *التاريخ:* $dateStr")
        sb.appendLine("🔑 *كود المتابعة:* `${student.parentCode}`")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")

        // Attendance
        val attText = when (attendance?.status?.name) {
            "PRESENT" -> "✅ حاضر ومواظب"
            "LATE" -> "⏰ متأخر"
            "EXCUSED" -> "ℹ️ مستأذن بعذر"
            "ABSENT" -> "❌ غائب"
            else -> "▫️ لم يسجل بعد"
        }
        sb.appendLine("📌 *حالة الحضور اليوم:* $attText")

        // Recitation
        if (recitation != null) {
            sb.appendLine("📖 *نوع التسميع:* ${recitation.type.titleAr}")
            sb.appendLine("📜 *السورة والآيات:* سورة ${recitation.surahName} (الآيات ${recitation.fromAyah} - ${recitation.toAyah})")
            sb.appendLine("⭐ *التقدير العام:* ${recitation.rating.titleAr}")
            sb.appendLine("🎙️ *درجة التجويد:* ${recitation.tajweedScore}/10")
            sb.appendLine("🗣️ *درجة الطلاقة والأداء:* ${recitation.fluencyScore}/10")
            if (recitation.teacherNotes.isNotBlank()) {
                sb.appendLine("💬 *ملاحظة المعلم:* ${recitation.teacherNotes}")
            }
            sb.appendLine("🏆 *النقاط المكتسبة:* +${recitation.pointsAwarded} نقطة")
        } else {
            sb.appendLine("📖 *التسميع:* جاري المتابعة أو مخصص للمراجعة الحرة اليوم.")
        }

        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("🌟 *رصيد النقاط الإجمالي:* ${student.totalPoints} نقطة")
        sb.appendLine("🔥 *أيام المواظبة المتتالية:* ${student.streakDays} يوم")
        sb.appendLine("🎯 *الهدف القرآني:* ${student.targetGoal}")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("📲 لمتابعة التفاصيل لحظة بلحظة، افتح تطبيق *حلقات القرآن* واستخدم الكود: ${student.parentCode}")
        sb.appendLine("نسأل الله أن يجعله من أهل القرآن وخاصته 🤲")

        return sb.toString().trim()
    }

    fun generateAppInstallShareText(): String {
        return """
            📖 *تطبيق حلقات القرآن الكريم* 🕌
            منظومة ذكية وشاملة لمتابعة طلاب وطالبات حلقات القرآن الكريم:
            
            ✨ *مميزات التطبيق:*
            • متابعة يومية للحفظ والمراجعة الكبرى والصغرى
            • تسجيل فوري للحضور والغياب مع تقارير وإحصائيات
            • بوابة خاصة لأولياء الأمور لمعرفة مستوى الأبناء يومياً
            • إشعارات دورية ورسائل واتساب لولي الأمر بكل إنجاز
            • تقييم دقيق للتجويد والطلاقة والأداء الصوتي
            • لوحة تحكم متكاملة للإدارة والمشرف والمطور
            • يعمل على جميع الهواتف الذكية والأجهزة اللوحية
            
            📲 *رابط تثبيت ومشاركة التطبيق:*
            https://quran-circles.app/install
            
            شارك الرابط مع أولياء الأمور والمعلمين لإلحاق الطلاب في الحلقات المباركة!
        """.trimIndent()
    }

    fun shareText(context: Context, text: String, title: String = "مشاركة التقرير عبر واتساب / التطبيقات") {
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
