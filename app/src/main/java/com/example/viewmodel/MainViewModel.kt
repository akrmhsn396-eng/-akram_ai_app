package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SampleDataProvider
import com.example.data.model.*
import com.example.util.NotificationHelper
import com.example.util.ShareHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

enum class AppRole(val titleAr: String, val iconDesc: String) {
    TEACHER("لوحة المعلم", "إدارة الحفظ والتقييم ومتابعة الطلاب"),
    HALAQAT("الحلقات القرآنية", "إدارة وتوزيع الحلقات والمجموعات"),
    GIRLS_ADMIN("حلقات البنات 👧", "لوحة المشرفات وإدارة المعلمات والطالبات بخصوصية تامة"),
    QUICK_ATTENDANCE("التحضير السريع", "رصد الحضور بنقرة واحدة لجميع الطلاب"),
    PARENT("بوابة ولي الأمر", "متابعة إنجاز الأبناء والتقارير المباشرة"),
    STUDENT("مساحة الطالب", "الختمة والنقاط والأوسمة والتحديات"),
    MESSAGES("الرسائل والتواصل", "إرسال التنبيهات ورسائل أولياء الأمور"),
    TOOLS("الأدوات والشهادات", "الشهادات، التحدي اليومي، تقارير PDF، المساعد الصوتي"),
    DEVELOPER("المطور والنشر", "إدارة النظام والنسخ الاحتياطي وأدوات التثبيت")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val studentDao = db.studentDao()
    private val recitationDao = db.recitationDao()
    private val attendanceDao = db.attendanceDao()
    private val notificationDao = db.notificationDao()

    val currentDate = MutableStateFlow(SampleDataProvider.getTodayDate())

    // Roles and navigation
    private val prefs = application.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE)
    private val _currentRole = MutableStateFlow(
        runCatching {
            val savedRole = prefs.getString("active_role", null)
            if (savedRole != null) AppRole.valueOf(savedRole) else AppRole.TEACHER
        }.getOrDefault(AppRole.TEACHER)
    )
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    fun setRole(role: AppRole) {
        _currentRole.value = role
        prefs.edit().putString("active_role", role.name).apply()
    }

    // Filters
    val searchQuery = MutableStateFlow("")
    val genderFilter = MutableStateFlow<Gender?>(null)
    val circleFilter = MutableStateFlow<String?>(null)

    // Data streams
    val allStudents: StateFlow<List<Student>> = studentDao.getAllStudentsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val femaleStudents: StateFlow<List<Student>> = studentDao.getStudentsByGenderFlow(Gender.FEMALE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCircles: StateFlow<List<String>> = studentDao.getCirclesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecitations: StateFlow<List<RecitationRecord>> = recitationDao.getAllRecitationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayAttendance: StateFlow<List<AttendanceRecord>> = currentDate
        .flatMapLatest { date -> attendanceDao.getAttendanceByDateFlow(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttendance: StateFlow<List<AttendanceRecord>> = attendanceDao.getAllAttendanceFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<ParentNotification>> = notificationDao.getAllNotificationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = notificationDao.getUnreadCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Selected student for detailed views
    private val _selectedStudentId = MutableStateFlow<Long?>(1L)
    val selectedStudentId: StateFlow<Long?> = _selectedStudentId.asStateFlow()

    val selectedStudent: StateFlow<Student?> = combine(allStudents, _selectedStudentId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectStudent(id: Long) {
        _selectedStudentId.value = id
    }

    // Parent login / linked code
    val parentSearchCode = MutableStateFlow("QRN-101")
    val parentLoginError = MutableStateFlow<String?>(null)

    fun loginWithParentCode(code: String) {
        viewModelScope.launch {
            val student = studentDao.getStudentByParentCode(code.trim().uppercase())
            if (student != null) {
                _selectedStudentId.value = student.id
                parentLoginError.value = null
                _currentRole.value = AppRole.PARENT
                showSnackbar("تم الدخول بنجاح إلى ملف الطالب: ${student.name}")
            } else {
                parentLoginError.value = "رمز المتابعة غير صحيح. يرجى التأكد من الرمز المعطى من المعلم."
            }
        }
    }

    // Feedback
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    init {
        // Seed initial data if database is empty
        viewModelScope.launch {
            if (studentDao.count() == 0) {
                seedInitialData()
            }
        }
    }

    fun seedInitialData() {
        viewModelScope.launch {
            val today = currentDate.value
            val yesterday = SampleDataProvider.getYesterdayDate()
            studentDao.insertAll(SampleDataProvider.getInitialStudents())
            recitationDao.insertAll(SampleDataProvider.getInitialRecitations(today, yesterday))
            attendanceDao.insertAll(SampleDataProvider.getInitialAttendance(today, yesterday))
            notificationDao.insertAll(SampleDataProvider.getInitialNotifications(today, yesterday))
            showSnackbar("تم تهيئة بيانات الحلقات والطلاب بنجاح ✨")
        }
    }

    // Record Recitation
    fun saveRecitation(
        student: Student,
        type: RecitationType,
        surahName: String,
        fromAyah: Int,
        toAyah: Int,
        rating: RecitationRating,
        tajweedScore: Int,
        fluencyScore: Int,
        notes: String,
        teacherName: String
    ) {
        viewModelScope.launch {
            val today = currentDate.value
            val record = RecitationRecord(
                studentId = student.id,
                studentName = student.name,
                type = type,
                surahName = surahName,
                fromAyah = fromAyah,
                toAyah = toAyah,
                rating = rating,
                tajweedScore = tajweedScore,
                fluencyScore = fluencyScore,
                teacherNotes = notes,
                pointsAwarded = rating.points,
                teacherName = teacherName,
                dateStr = today
            )
            recitationDao.insertRecitation(record)

            // Award points and streak to student
            studentDao.awardPoints(student.id, rating.points)

            // Generate Parent Notification
            val notifTitle = "إنجاز ${type.titleAr} لـ ${student.name}"
            val notifMsg = "أتم/ت سورة $surahName (الآيات $fromAyah-$toAyah) بتقدير ${rating.titleAr} ودرجة تجويد $tajweedScore/10. (+${rating.points} نقطة)"
            val notif = ParentNotification(
                studentId = student.id,
                studentName = student.name,
                title = notifTitle,
                message = notifMsg,
                dateStr = today,
                category = "تقييم يومي"
            )
            notificationDao.insert(notif)

            // Fire real device notification if possible
            NotificationHelper.showParentNotification(
                getApplication(),
                notificationId = (student.id * 100 + System.currentTimeMillis() % 100).toInt(),
                title = notifTitle,
                message = notifMsg
            )

            showSnackbar("تم حفظ التسميع وإرسال إشعار لولي أمر ${student.name} ⭐")
        }
    }

    // Record Attendance
    fun setAttendance(studentId: Long, status: AttendanceStatus, note: String = "") {
        viewModelScope.launch {
            val today = currentDate.value
            val record = AttendanceRecord(
                studentId = studentId,
                dateStr = today,
                status = status,
                note = note
            )
            attendanceDao.insertOrUpdate(record)

            // If absent or late, notify parent automatically
            if (status == AttendanceStatus.ABSENT || status == AttendanceStatus.LATE) {
                val student = studentDao.getStudentById(studentId)
                if (student != null) {
                    val statusText = if (status == AttendanceStatus.ABSENT) "غياب عن الحلقة" else "تأخر عن موعد الحلقة"
                    val notifTitle = "تنبيه $statusText: ${student.name}"
                    val notifMsg = "نحيطكم علماً بأنه تم تسجيل حالة ($statusText) لـ ${student.name} اليوم $today. $note"
                    notificationDao.insert(
                        ParentNotification(
                            studentId = student.id,
                            studentName = student.name,
                            title = notifTitle,
                            message = notifMsg,
                            dateStr = today,
                            category = "حضور وغياب"
                        )
                    )
                    NotificationHelper.showParentNotification(
                        getApplication(),
                        notificationId = (studentId * 10).toInt(),
                        title = notifTitle,
                        message = notifMsg
                    )
                }
            }
        }
    }

    fun markAllCircleAttendance(circleName: String, status: AttendanceStatus) {
        viewModelScope.launch {
            val studentsInCircle = allStudents.value.filter { it.circleName == circleName }
            val today = currentDate.value
            studentsInCircle.forEach { s ->
                attendanceDao.insertOrUpdate(
                    AttendanceRecord(
                        studentId = s.id,
                        dateStr = today,
                        status = status
                    )
                )
            }
            showSnackbar("تم رصد الحضور لجميع طلاب $circleName كـ ${status.labelAr}")
        }
    }

    fun markAllStudentsPresent() {
        viewModelScope.launch {
            val today = currentDate.value
            allStudents.value.forEach { s ->
                attendanceDao.insertOrUpdate(
                    AttendanceRecord(
                        studentId = s.id,
                        dateStr = today,
                        status = AttendanceStatus.PRESENT
                    )
                )
            }
            showSnackbar("تم تحضير جميع الطلاب كـ حاضر بنجاح ✅")
        }
    }

    fun toggleAttendanceForStudent(studentId: Long) {
        viewModelScope.launch {
            val today = currentDate.value
            val currentRecord = todayAttendance.value.firstOrNull { it.studentId == studentId }
            val nextStatus = when (currentRecord?.status) {
                AttendanceStatus.PRESENT -> AttendanceStatus.LATE
                AttendanceStatus.LATE -> AttendanceStatus.EXCUSED
                AttendanceStatus.EXCUSED -> AttendanceStatus.ABSENT
                AttendanceStatus.ABSENT -> AttendanceStatus.PRESENT
                null -> AttendanceStatus.PRESENT
            }
            setAttendance(studentId, nextStatus)
        }
    }

    fun sendMessageToCircle(circleName: String?, title: String, message: String, category: String = "رسالة عامة") {
        viewModelScope.launch {
            val today = currentDate.value
            val targets = if (circleName == null || circleName == "الكل") allStudents.value else allStudents.value.filter { it.circleName == circleName }
            targets.forEach { s ->
                notificationDao.insert(
                    ParentNotification(
                        studentId = s.id,
                        studentName = s.name,
                        title = title,
                        message = message,
                        dateStr = today,
                        category = category
                    )
                )
            }
            NotificationHelper.showParentNotification(
                getApplication(),
                notificationId = (System.currentTimeMillis() % 10000).toInt(),
                title = title,
                message = message,
                subText = if (circleName != null && circleName != "الكل") circleName else "إشعار عام"
            )
            showSnackbar("تم إرسال الرسالة لـ ${targets.size} من أولياء الأمور بنجاح ✉️")
        }
    }

    fun awardChallengePoints(studentId: Long, points: Int) {
        viewModelScope.launch {
            studentDao.awardPoints(studentId, points)
            showSnackbar("أحسنت! تمت إضافة $points نقطة لرصيد الطالب في التحدي اليومي 🌟")
        }
    }

    // Add or Update Student
    fun saveStudent(
        id: Long = 0,
        name: String,
        gender: Gender,
        circleName: String,
        parentPhone: String,
        parentCode: String,
        targetGoal: String,
        currentJuz: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val student = Student(
                id = id,
                name = name.trim(),
                gender = gender,
                circleName = circleName.trim(),
                parentPhone = parentPhone.trim(),
                parentCode = if (parentCode.isBlank()) "QRN-${(100..999).random()}" else parentCode.trim().uppercase(),
                targetGoal = targetGoal.ifBlank { "ختم القرآن الكريم كاملاً" },
                currentJuz = currentJuz,
                notes = notes.trim()
            )
            if (id == 0L) {
                val newId = studentDao.insertStudent(student)
                _selectedStudentId.value = newId
                showSnackbar("تمت إضافة ${student.genderLabel} ${student.name} بنجاح ✅")
            } else {
                studentDao.updateStudent(student)
                showSnackbar("تم تحديث بيانات الطالب ${student.name} بنجاح ✅")
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentDao.deleteStudent(student)
            showSnackbar("تم حذف الطالب ${student.name}")
        }
    }

    // Developer / Admin Broadcast
    fun broadcastNotification(title: String, message: String) {
        viewModelScope.launch {
            val today = currentDate.value
            allStudents.value.forEach { s ->
                notificationDao.insert(
                    ParentNotification(
                        studentId = s.id,
                        studentName = s.name,
                        title = title,
                        message = message,
                        dateStr = today,
                        category = "تنبيه عام"
                    )
                )
            }
            NotificationHelper.showParentNotification(
                getApplication(),
                notificationId = 9999,
                title = title,
                message = message,
                subText = "بث عام لجميع أولياء الأمور"
            )
            showSnackbar("تم إرسال الإشعار لجميع أولياء الأمور بنجاح 📢")
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            notificationDao.markAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            notificationDao.markAllAsRead()
            showSnackbar("تم تعيين جميع الإشعارات كمقروءة")
        }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            studentDao.deleteAll()
            recitationDao.deleteAll()
            attendanceDao.deleteAll()
            notificationDao.deleteAll()
            seedInitialData()
            showSnackbar("تمت استعادة ضبط المصنع للبيانات الرسمية")
        }
    }

    fun exportDatabaseJson(): String {
        val root = JSONObject()
        val studentsArr = JSONArray()
        allStudents.value.forEach { s ->
            val obj = JSONObject().apply {
                put("id", s.id)
                put("name", s.name)
                put("gender", s.gender.name)
                put("circleName", s.circleName)
                put("parentPhone", s.parentPhone)
                put("parentCode", s.parentCode)
                put("targetGoal", s.targetGoal)
                put("currentJuz", s.currentJuz)
                put("totalPoints", s.totalPoints)
                put("streakDays", s.streakDays)
            }
            studentsArr.put(obj)
        }
        root.put("students", studentsArr)
        root.put("exportTime", System.currentTimeMillis())
        root.put("app", "QuranCircles")
        return root.toString(2)
    }

    fun shareParentReport(student: Student) {
        val today = currentDate.value
        val recentRec = allRecitations.value.firstOrNull { it.studentId == student.id && it.dateStr == today }
            ?: allRecitations.value.firstOrNull { it.studentId == student.id }
        val att = todayAttendance.value.firstOrNull { it.studentId == student.id }
        val report = ShareHelper.generateParentDailyReport(student, recentRec, att, today)
        ShareHelper.shareText(getApplication(), report, "تقرير متابعة ${student.name}")
    }

    fun shareAppInvitation() {
        val invite = ShareHelper.generateAppInstallShareText()
        ShareHelper.shareText(getApplication(), invite, "مشاركة رابط وتثبيت تطبيق حلقات القرآن")
    }
}
