package com.example.ui.screens.girls

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.RecitationDialog
import com.example.ui.theme.*
import com.example.util.ShareHelper
import com.example.viewmodel.MainViewModel
import org.json.JSONArray
import org.json.JSONObject

data class GirlsCircle(
    val name: String,
    val teacherName: String,
    val location: String,
    val targetGoal: String,
    val scheduleTime: String = "العصر (4:00 - 5:30 م)",
    val teacherPhone: String = "0550000000"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GirlsAdminScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("girls_circles_prefs", Context.MODE_PRIVATE) }

    val allStudents by viewModel.allStudents.collectAsState()
    val todayAttendance by viewModel.todayAttendance.collectAsState()
    val allRecitations by viewModel.allRecitations.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    // فصل البيانات تماماً: حصر الطالبات على الإناث فقط
    val girlsStudents = remember(allStudents) {
        allStudents.filter { it.gender == Gender.FEMALE }
    }

    var activeSubSection by remember { mutableIntStateOf(0) } // 0: Circles, 1: Students, 2: Attendance
    var searchQuery by remember { mutableStateOf("") }
    var selectedCircleFilter by remember { mutableStateOf<String?>("الكل") }

    // Dialogs state
    var showAddCircleDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var circleToAssignTeacher by remember { mutableStateOf<GirlsCircle?>(null) }
    var studentToEdit by remember { mutableStateOf<Student?>(null) }
    var studentToDelete by remember { mutableStateOf<Student?>(null) }
    var circleToDelete by remember { mutableStateOf<GirlsCircle?>(null) }
    var studentForRecitation by remember { mutableStateOf<Student?>(null) }
    var showRecitationDialog by remember { mutableStateOf(false) }

    // Persistent Girls Circles List
    var girlsCirclesList by remember {
        mutableStateOf(
            runCatching {
                val raw = prefs.getString("saved_girls_circles", null)
                if (!raw.isNullOrBlank()) {
                    val arr = JSONArray(raw)
                    val list = mutableListOf<GirlsCircle>()
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        list.add(
                            GirlsCircle(
                                name = obj.getString("name"),
                                teacherName = obj.optString("teacherName", "المحفظة المشرفة"),
                                location = obj.optString("location", "معهد البيان - قاعة الفردوس"),
                                targetGoal = obj.optString("targetGoal", "حفظ وتجويد القرآن الكريم"),
                                scheduleTime = obj.optString("scheduleTime", "العصر (4:00 - 5:30 م)"),
                                teacherPhone = obj.optString("teacherPhone", "0550000000")
                            )
                        )
                    }
                    if (list.isNotEmpty()) list else null
                } else null
            }.getOrNull() ?: listOf(
                GirlsCircle("حلقة أم المؤمنين خديجة", "الأستاذة مريم السعيد", "معهد البيان - قاعة الفردوس", "حفظ الأجزاء الخمسة الأخيرة برواية حفص", "العصر (4:00 - 5:30 م)", "0551122334"),
                GirlsCircle("حلقة الزهراء للفتيات", "الأستاذة هدى العمري", "معهد البيان - قاعة الهدى", "ختم القرآن الكريم كاملاً وتجويده", "المساء (6:00 - 7:30 م)", "0559988776"),
                GirlsCircle("حلقة عائشة رضي الله عنها", "الأستاذة نورة القحطاني", "مجمع الفتيات القرآني - قاعة النور", "حفظ جزء عم وتبارك والمواظبة اليومية", "الصباح (9:00 - 10:30 ص)", "0554433221")
            )
        )
    }

    fun saveCircles(list: List<GirlsCircle>) {
        girlsCirclesList = list
        try {
            val arr = JSONArray()
            list.forEach { c ->
                val obj = JSONObject()
                obj.put("name", c.name)
                obj.put("teacherName", c.teacherName)
                obj.put("location", c.location)
                obj.put("targetGoal", c.targetGoal)
                obj.put("scheduleTime", c.scheduleTime)
                obj.put("teacherPhone", c.teacherPhone)
                arr.put(obj)
            }
            prefs.edit().putString("saved_girls_circles", arr.toString()).apply()
        } catch (_: Exception) {}
    }

    // Dynamic circles extracted from girls' students DB + configured circles
    val allGirlsCircles = remember(girlsStudents, girlsCirclesList) {
        val names = girlsCirclesList.map { it.name }.toMutableSet()
        girlsStudents.forEach { names.add(it.circleName) }
        names.toList()
    }

    // Filtered Students (Strictly female only)
    val filteredGirls = remember(girlsStudents, searchQuery, selectedCircleFilter) {
        girlsStudents.filter { s ->
            val matchQuery = searchQuery.isBlank() || s.name.contains(searchQuery.trim()) || s.parentCode.contains(searchQuery.trim(), ignoreCase = true)
            val matchCircle = selectedCircleFilter == null || selectedCircleFilter == "الكل" || s.circleName == selectedCircleFilter
            matchQuery && matchCircle
        }
    }

    // Statistics strictly for girls section
    val totalGirlsCount = girlsStudents.size
    val presentGirlsCount = girlsStudents.count { s ->
        todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.PRESENT }
    }
    val attendanceRate = if (totalGirlsCount > 0) (presentGirlsCount * 100) / totalGirlsCount else 0
    val todayRecitationsCount = allRecitations.count { r ->
        r.dateStr == currentDate && girlsStudents.any { it.id == r.studentId }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (activeSubSection == 0) {
                        showAddCircleDialog = true
                    } else {
                        showAddStudentDialog = true
                    }
                },
                containerColor = Color(0xFFC2185B),
                contentColor = Color.White,
                modifier = Modifier.testTag("girls_admin_fab")
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (activeSubSection == 0) "إضافة حلقة فتيات" else "تسجيل طالبة جديدة",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
        ) {
            // Header Hero Banner for Girls Section
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF880E4F)),
                    modifier = Modifier.fillMaxWidth().testTag("girls_admin_banner")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFCE4EC)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Female,
                                        contentDescription = null,
                                        tint = Color(0xFFC2185B),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "الإدارة العامة لحلقات البنات 🌸",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "لوحة المشرفات • إدارة المحفظات • سجلات الطالبات",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFF8BBD0)
                                    )
                                }
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "فصل تام للبيانات 🔒",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GirlsStatBadge("حلقات البنات", "${girlsCirclesList.size} حلقات", Modifier.weight(1f))
                            GirlsStatBadge("إجمالي الطالبات", "$totalGirlsCount طالبة", Modifier.weight(1f))
                            GirlsStatBadge("حضور اليوم", "$presentGirlsCount ($attendanceRate%)", Modifier.weight(1f))
                            GirlsStatBadge("تسميع اليوم", "$todayRecitationsCount تسميع", Modifier.weight(1f))
                        }
                    }
                }
            }

            // Sub-Section Navigation Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = activeSubSection == 0,
                        onClick = { activeSubSection = 0 },
                        label = { Text("الحلقات والمحفظات (${girlsCirclesList.size})") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = activeSubSection == 1,
                        onClick = { activeSubSection = 1 },
                        label = { Text("دليل الطالبات ($totalGirlsCount)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = activeSubSection == 2,
                        onClick = { activeSubSection = 2 },
                        label = { Text("التحضير السريع ⚡") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 1. SECTION: Girls Circles & Female Teachers Management
            if (activeSubSection == 0) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الحلقات القرآنية والمحفظات المعتمدات 🕌",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2185B)
                        )
                        Button(
                            onClick = { showAddCircleDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("إضافة حلقة جديدة", fontSize = 12.sp)
                        }
                    }
                }

                items(girlsCirclesList, key = { it.name }) { circle ->
                    val circleStudents = girlsStudents.filter { it.circleName == circle.name }
                    val circlePresent = circleStudents.count { s ->
                        todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.PRESENT }
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFCE4EC)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🌸", fontSize = 20.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = circle.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "المحفظة المشرفة: ${circle.teacherName} (📞 ${circle.teacherPhone})",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFC2185B)
                                        )
                                        Text(
                                            text = "📍 المقر: ${circle.location} • ⏰ ${circle.scheduleTime}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Surface(
                                    color = Color(0xFFFCE4EC),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "${circleStudents.size} طالبة",
                                        color = Color(0xFFC2185B),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "🎯 الهدف القرآني: ${circle.targetGoal}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "📊 نسبة الحضور اليوم: $circlePresent من ${circleStudents.size} طالبة",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuranGold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action buttons: View students, Assign/Change teacher, Share report, Delete circle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        selectedCircleFilter = circle.name
                                        activeSubSection = 1
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("عرض الطالبات (${circleStudents.size})", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = { circleToAssignTeacher = circle },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC2185B)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تعيين المحفظة ✏️", fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val text = buildString {
                                            appendLine("🌸 *تقرير حلقة ${circle.name} للفتيات* 🌸")
                                            appendLine("المحفظة المشرفة: ${circle.teacherName} (هاتف: ${circle.teacherPhone})")
                                            appendLine("المقر: ${circle.location} • الموعد: ${circle.scheduleTime}")
                                            appendLine("الهدف القرآني: ${circle.targetGoal}")
                                            appendLine("إجمالي الطالبات: ${circleStudents.size} طالبة")
                                            appendLine("حضور اليوم: $circlePresent طالبة")
                                            appendLine("━━━━━━━━━━━━━━━")
                                            circleStudents.forEachIndexed { i, s ->
                                                appendLine("${i+1}. ${s.name} - جزء ${s.currentJuz} (${s.totalPoints} نقطة)")
                                            }
                                        }
                                        ShareHelper.shareText(viewModel.getApplication(), text, "تقرير ${circle.name}")
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("مشاركة التقرير", fontSize = 12.sp)
                                }

                                TextButton(
                                    onClick = { circleToDelete = circle },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("حذف الحلقة", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // 2. SECTION: Female Students Directory & Recitation
            if (activeSubSection == 1) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("ابحثي باسم الطالبة أو كود ولي الأمر...") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )

                        // Filter Circles (Only girls circles)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedCircleFilter == "الكل" || selectedCircleFilter == null,
                                onClick = { selectedCircleFilter = "الكل" },
                                label = { Text("الكل ($totalGirlsCount)") }
                            )
                            allGirlsCircles.forEach { cName ->
                                val count = girlsStudents.count { it.circleName == cName }
                                FilterChip(
                                    selected = selectedCircleFilter == cName,
                                    onClick = { selectedCircleFilter = cName },
                                    label = { Text("$cName ($count)") }
                                )
                            }
                        }
                    }
                }

                items(filteredGirls, key = { it.id }) { student ->
                    val lastRec = allRecitations.firstOrNull { it.studentId == student.id && it.dateStr == currentDate }
                        ?: allRecitations.firstOrNull { it.studentId == student.id }
                    val attRecord = todayAttendance.firstOrNull { it.studentId == student.id }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFCE4EC)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = student.name.take(1),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFC2185B),
                                            fontSize = 18.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = student.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = "${student.circleName} • كود: ${student.parentCode} • هاتف: ${student.parentPhone}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    color = when (attRecord?.status) {
                                        AttendanceStatus.PRESENT -> Color(0xFFE8F5E9)
                                        AttendanceStatus.LATE -> Color(0xFFFFF3E0)
                                        AttendanceStatus.EXCUSED -> Color(0xFFE1F5FE)
                                        AttendanceStatus.ABSENT -> Color(0xFFFFEBEE)
                                        null -> Color(0xFFF5F5F5)
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = attRecord?.status?.labelAr ?: "لم يرصد",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = when (attRecord?.status) {
                                            AttendanceStatus.PRESENT -> Color(0xFF2E7D32)
                                            AttendanceStatus.LATE -> Color(0xFFEF6C00)
                                            AttendanceStatus.EXCUSED -> Color(0xFF0288D1)
                                            AttendanceStatus.ABSENT -> Color(0xFFC62828)
                                            null -> Color(0xFF757575)
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Text(
                                text = "🎯 الهدف: ${student.targetGoal} • الجزء: ${student.currentJuz}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "⭐ النقاط: ${student.totalPoints} | 🔥 المواظبة: ${student.streakDays} يوم",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuranGold
                            )

                            if (lastRec != null) {
                                Surface(
                                    color = Color(0xFFFCE4EC).copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "📖 آخر تسميع: سورة ${lastRec.surahName} (${lastRec.fromAyah}-${lastRec.toAyah}) - ${lastRec.rating.titleAr} (${lastRec.teacherName})",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF880E4F),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        studentForRecitation = student
                                        showRecitationDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تسجيل تسميع", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.toggleAttendanceForStudent(student.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("رصد الحضور", fontSize = 12.sp)
                                }

                                IconButton(
                                    onClick = { studentToEdit = student }
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "تعديل البيانات", tint = Color(0xFFC2185B))
                                }

                                IconButton(
                                    onClick = { viewModel.shareParentReport(student) }
                                ) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "تقرير ولي الأمر", tint = Color(0xFF25D366))
                                }
                            }
                        }
                    }
                }
            }

            // 3. SECTION: Quick Attendance for Girls Only
            if (activeSubSection == 2) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "رصد الحضور السريع لحلقات الفتيات ⚡",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2185B)
                        )
                        Button(
                            onClick = {
                                girlsStudents.forEach { s ->
                                    viewModel.setAttendance(s.id, AttendanceStatus.PRESENT)
                                }
                                viewModel.showSnackbar("تم تحضير جميع طالبات قسم البنات كـ حاضر ✅")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("تحضير كل الطالبات حاضر ✅", fontSize = 12.sp)
                        }
                    }
                }

                items(girlsStudents, key = { it.id }) { student ->
                    val attRecord = todayAttendance.firstOrNull { it.studentId == student.id }
                    val status = attRecord?.status

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFCE4EC)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(student.name.take(1), fontWeight = FontWeight.Bold, color = Color(0xFFC2185B))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(student.name, fontWeight = FontWeight.Bold)
                                    Text(student.circleName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Surface(
                                onClick = { viewModel.toggleAttendanceForStudent(student.id) },
                                shape = RoundedCornerShape(10.dp),
                                color = when (status) {
                                    AttendanceStatus.PRESENT -> Color(0xFFE8F5E9)
                                    AttendanceStatus.LATE -> Color(0xFFFFF3E0)
                                    AttendanceStatus.EXCUSED -> Color(0xFFE1F5FE)
                                    AttendanceStatus.ABSENT -> Color(0xFFFFEBEE)
                                    null -> Color(0xFFF5F5F5)
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    when (status) {
                                        AttendanceStatus.PRESENT -> Color(0xFF2E7D32)
                                        AttendanceStatus.LATE -> Color(0xFFEF6C00)
                                        AttendanceStatus.EXCUSED -> Color(0xFF0288D1)
                                        AttendanceStatus.ABSENT -> Color(0xFFC62828)
                                        null -> Color(0xFFBDBDBD)
                                    }
                                ),
                                modifier = Modifier.heightIn(min = 44.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = status?.labelAr ?: "لم يرصد",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = when (status) {
                                            AttendanceStatus.PRESENT -> Color(0xFF2E7D32)
                                            AttendanceStatus.LATE -> Color(0xFFEF6C00)
                                            AttendanceStatus.EXCUSED -> Color(0xFF0288D1)
                                            AttendanceStatus.ABSENT -> Color(0xFFC62828)
                                            null -> Color(0xFF757575)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog: Add New Girls Circle & Assign Teacher
    var newCircleName by remember { mutableStateOf("") }
    var newTeacherName by remember { mutableStateOf("") }
    var newTeacherPhone by remember { mutableStateOf("0550000000") }
    var newLocation by remember { mutableStateOf("معهد البيان - قاعة الهدى") }
    var newGoal by remember { mutableStateOf("حفظ وتجويد القرآن الكريم") }
    var newSchedule by remember { mutableStateOf("العصر (4:00 - 5:30 م)") }

    if (showAddCircleDialog) {
        AlertDialog(
            onDismissRequest = { showAddCircleDialog = false },
            title = { Text("إضافة حلقة فتيات جديدة وتعيين المحفظة 🕌🌸", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newCircleName,
                        onValueChange = { newCircleName = it },
                        label = { Text("اسم الحلقة (مثال: حلقة حفصة بنت عمر)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTeacherName,
                        onValueChange = { newTeacherName = it },
                        label = { Text("اسم المحفظة / المعلمة المشرفة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTeacherPhone,
                        onValueChange = { newTeacherPhone = it },
                        label = { Text("رقم هاتف المحفظة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newSchedule,
                        onValueChange = { newSchedule = it },
                        label = { Text("موعد وانعقاد الحلقة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newLocation,
                        onValueChange = { newLocation = it },
                        label = { Text("المقر أو القاعة التعليمية") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newGoal,
                        onValueChange = { newGoal = it },
                        label = { Text("الهدف القرآني للحلقة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCircleName.isNotBlank() && newTeacherName.isNotBlank()) {
                            val newCircle = GirlsCircle(
                                name = newCircleName.trim(),
                                teacherName = newTeacherName.trim(),
                                location = newLocation.trim(),
                                targetGoal = newGoal.trim(),
                                scheduleTime = newSchedule.trim(),
                                teacherPhone = newTeacherPhone.trim()
                            )
                            val updated = girlsCirclesList + newCircle
                            saveCircles(updated)

                            // Add initial female student to seed circle in DB
                            viewModel.saveStudent(
                                name = "طالبة جديدة",
                                gender = Gender.FEMALE,
                                circleName = newCircleName.trim(),
                                parentPhone = "0500000000",
                                parentCode = "QRN-${(100..999).random()}",
                                targetGoal = newGoal.trim(),
                                currentJuz = 30,
                                notes = "حلقة فتيات جديدة بإشراف: $newTeacherName"
                            )
                            viewModel.showSnackbar("تم إنشاء حلقة الفتيات وتعيين المحفظة بنجاح 🌸")
                            newCircleName = ""
                            newTeacherName = ""
                            showAddCircleDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B))
                ) {
                    Text("حفظ وتعيين المحفظة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCircleDialog = false }) { Text("إلغاء") }
            }
        )
    }

    // Dialog: Assign / Reassign Teacher for existing circle
    if (circleToAssignTeacher != null) {
        val circle = circleToAssignTeacher!!
        var editTeacherName by remember { mutableStateOf(circle.teacherName) }
        var editTeacherPhone by remember { mutableStateOf(circle.teacherPhone) }
        var editLocation by remember { mutableStateOf(circle.location) }
        var editSchedule by remember { mutableStateOf(circle.scheduleTime) }
        var editGoal by remember { mutableStateOf(circle.targetGoal) }

        AlertDialog(
            onDismissRequest = { circleToAssignTeacher = null },
            title = { Text("تعيين / تغيير المحفظة المشرفة 👩‍🏫", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("حلقة: ${circle.name}", fontWeight = FontWeight.Bold, color = Color(0xFFC2185B))
                    OutlinedTextField(
                        value = editTeacherName,
                        onValueChange = { editTeacherName = it },
                        label = { Text("اسم المحفظة المشرفة الجديدة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editTeacherPhone,
                        onValueChange = { editTeacherPhone = it },
                        label = { Text("رقم هاتف المحفظة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editSchedule,
                        onValueChange = { editSchedule = it },
                        label = { Text("موعد انعقاد الحلقة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("المقر والقاعة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = girlsCirclesList.map {
                            if (it.name == circle.name) {
                                it.copy(
                                    teacherName = editTeacherName.trim(),
                                    teacherPhone = editTeacherPhone.trim(),
                                    location = editLocation.trim(),
                                    scheduleTime = editSchedule.trim(),
                                    targetGoal = editGoal.trim()
                                )
                            } else it
                        }
                        saveCircles(updated)
                        viewModel.showSnackbar("تم تعيين الأستاذة ${editTeacherName} محفظة لحلقة ${circle.name} 🌸")
                        circleToAssignTeacher = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B))
                ) {
                    Text("حفظ التعيين")
                }
            },
            dismissButton = {
                TextButton(onClick = { circleToAssignTeacher = null }) { Text("إلغاء") }
            }
        )
    }

    // Dialog: Delete Circle Confirmation
    if (circleToDelete != null) {
        val circle = circleToDelete!!
        AlertDialog(
            onDismissRequest = { circleToDelete = null },
            title = { Text("تأكيد حذف الحلقة", fontWeight = FontWeight.Bold) },
            text = { Text("هل أنتِ متأكدة من رغبتك في حذف ${circle.name}؟ لن يتم حذف سجلات الطالبات المسجلات.") },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = girlsCirclesList.filter { it.name != circle.name }
                        saveCircles(updated)
                        viewModel.showSnackbar("تم حذف الحلقة ${circle.name}")
                        circleToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("نعم، حذف الحلقة")
                }
            },
            dismissButton = {
                TextButton(onClick = { circleToDelete = null }) { Text("إلغاء") }
            }
        )
    }

    // Dialog: Add New Female Student (Strictly locked to FEMALE)
    var stName by remember { mutableStateOf("") }
    var stCircle by remember { mutableStateOf(girlsCirclesList.firstOrNull()?.name ?: "حلقة أم المؤمنين خديجة") }
    var stPhone by remember { mutableStateOf("0550000000") }
    var stCode by remember { mutableStateOf("QRN-${(100..999).random()}") }
    var stGoal by remember { mutableStateOf("ختم القرآن الكريم كاملاً") }
    var stJuz by remember { mutableStateOf("30") }
    var stNotes by remember { mutableStateOf("") }

    if (showAddStudentDialog) {
        AlertDialog(
            onDismissRequest = { showAddStudentDialog = false },
            title = { Text("تسجيل طالبة جديدة في قسم البنات 👧", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = stName,
                        onValueChange = { stName = it },
                        label = { Text("اسم الطالبة الثلاثي") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stCircle,
                        onValueChange = { stCircle = it },
                        label = { Text("اسم الحلقة التابعة لها") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stPhone,
                        onValueChange = { stPhone = it },
                        label = { Text("رقم هاتف ولية الأمر / ولي الأمر") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stCode,
                        onValueChange = { stCode = it },
                        label = { Text("كود المتابعة (مثال: QRN-201)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stGoal,
                        onValueChange = { stGoal = it },
                        label = { Text("الهدف القرآني") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (stName.isNotBlank()) {
                            viewModel.saveStudent(
                                name = stName.trim(),
                                gender = Gender.FEMALE, // فصل البيانات: دائماً أنثى
                                circleName = stCircle.trim(),
                                parentPhone = stPhone.trim(),
                                parentCode = stCode.trim(),
                                targetGoal = stGoal.trim(),
                                currentJuz = stJuz.toIntOrNull() ?: 30,
                                notes = stNotes.trim()
                            )
                            viewModel.showSnackbar("تم تسجيل الطالبة ${stName} بنجاح في قسم البنات 🌸")
                            stName = ""
                            showAddStudentDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B))
                ) {
                    Text("تسجيل الطالبة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStudentDialog = false }) { Text("إلغاء") }
            }
        )
    }

    // Dialog: Edit Student Data
    if (studentToEdit != null) {
        val student = studentToEdit!!
        var eName by remember { mutableStateOf(student.name) }
        var eCircle by remember { mutableStateOf(student.circleName) }
        var ePhone by remember { mutableStateOf(student.parentPhone) }
        var eGoal by remember { mutableStateOf(student.targetGoal) }
        var eJuz by remember { mutableStateOf(student.currentJuz.toString()) }

        AlertDialog(
            onDismissRequest = { studentToEdit = null },
            title = { Text("تعديل بيانات الطالبة 👧", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = eName,
                        onValueChange = { eName = it },
                        label = { Text("اسم الطالبة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = eCircle,
                        onValueChange = { eCircle = it },
                        label = { Text("اسم الحلقة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ePhone,
                        onValueChange = { ePhone = it },
                        label = { Text("رقم هاتف ولية الأمر") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = eGoal,
                        onValueChange = { eGoal = it },
                        label = { Text("الهدف القرآني") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = eJuz,
                        onValueChange = { eJuz = it },
                        label = { Text("الجزء الحالي") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveStudent(
                            id = student.id,
                            name = eName.trim(),
                            gender = Gender.FEMALE, // دائماً أنثى
                            circleName = eCircle.trim(),
                            parentPhone = ePhone.trim(),
                            parentCode = student.parentCode,
                            targetGoal = eGoal.trim(),
                            currentJuz = eJuz.toIntOrNull() ?: student.currentJuz,
                            notes = student.notes
                        )
                        viewModel.showSnackbar("تم تحديث بيانات الطالبة بنجاح ✅")
                        studentToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B))
                ) {
                    Text("حفظ التعديلات")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            viewModel.deleteStudent(student)
                            studentToEdit = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                    ) {
                        Text("حذف الطالبة")
                    }
                    TextButton(onClick = { studentToEdit = null }) { Text("إلغاء") }
                }
            }
        )
    }

    // Dialog: Recitation Recording
    if (showRecitationDialog && studentForRecitation != null) {
        val student = studentForRecitation!!
        RecitationDialog(
            students = girlsStudents,
            initialStudent = student,
            onDismiss = { showRecitationDialog = false },
            onSave = { st, type, surah, fromAyah, toAyah, rating, tajweed, fluency, notes, teacher ->
                viewModel.saveRecitation(
                    student = st,
                    type = type,
                    surahName = surah,
                    fromAyah = fromAyah,
                    toAyah = toAyah,
                    rating = rating,
                    tajweedScore = tajweed,
                    fluencyScore = fluency,
                    notes = notes,
                    teacherName = teacher
                )
                showRecitationDialog = false
            }
        )
    }
}

@Composable
fun GirlsStatBadge(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
            Text(title, color = Color(0xFFF8BBD0), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
