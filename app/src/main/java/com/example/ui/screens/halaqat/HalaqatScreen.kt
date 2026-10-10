package com.example.ui.screens.halaqat

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceStatus
import com.example.data.model.Gender
import com.example.data.model.Student
import com.example.ui.components.AddStudentDialog
import com.example.ui.theme.*
import com.example.viewmodel.AppRole
import com.example.viewmodel.MainViewModel

data class HalaqahInfo(
    val name: String,
    val gender: Gender,
    val teacherName: String,
    val location: String,
    val targetGoal: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalaqatScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val allStudents by viewModel.allStudents.collectAsState()
    val todayAttendance by viewModel.todayAttendance.collectAsState()
    val allCircles by viewModel.allCircles.collectAsState()

    var selectedGenderFilter by remember { mutableStateOf<Gender?>(null) }
    var showAddCircleDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var selectedCircleForStudent by remember { mutableStateOf("") }
    var newCircleName by remember { mutableStateOf("") }
    var newCircleTeacher by remember { mutableStateOf("") }
    var newCircleGender by remember { mutableStateOf(Gender.MALE) }

    // Aggregate known circles and user circles
    val predefinedCircles = remember {
        listOf(
            HalaqahInfo("حلقة الإمام الشاطبي", Gender.MALE, "الشيخ عبد الله القرني", "مسجد النور - المصلى الرئيسي", "حفظ القرآن الكريم كاملاً وتجويده"),
            HalaqahInfo("حلقة أم المؤمنين خديجة", Gender.FEMALE, "الأستاذة مريم الصالح", "معهد البيان النسائي - قاعة الفردوس", "حفظ الأجزاء الخمسة الأخيرة برواية حفص"),
            HalaqahInfo("حلقة الإمام نافع", Gender.MALE, "الشيخ عبد الرحمن الغامدي", "مسجد النور - رواق الحفظ", "حفظ جزء عم وتبارك والمواظبة اليومية"),
            HalaqahInfo("حلقة الزهراء للفتيات", Gender.FEMALE, "الأستاذة هدى العمري", "معهد البيان النسائي - قاعة الهدى", "إتقان التلاوة وحفظ الزهراوين")
        )
    }

    // Merge any circles found in students DB
    val halaqatList = remember(allStudents, allCircles) {
        val list = predefinedCircles.toMutableList()
        allCircles.forEach { cName ->
            if (list.none { it.name == cName }) {
                val sampleStudent = allStudents.firstOrNull { it.circleName == cName }
                val g = sampleStudent?.gender ?: Gender.MALE
                list.add(
                    HalaqahInfo(
                        name = cName,
                        gender = g,
                        teacherName = if (g == Gender.MALE) "معلم الحلقة" else "معلمة الحلقة",
                        location = "القاعة التعليمية",
                        targetGoal = sampleStudent?.targetGoal ?: "حفظ ومراجعة القرآن الكريم"
                    )
                )
            }
        }
        list
    }

    val filteredHalaqat = halaqatList.filter {
        selectedGenderFilter == null || it.gender == selectedGenderFilter
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCircleDialog = true },
                containerColor = QuranEmerald,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_halaqah_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("إضافة حلقة جديدة", fontWeight = FontWeight.Bold)
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
            // Header Hero Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranEmeraldDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(QuranGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "إدارة الحلقات القرآنية",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${halaqatList.size} حلقات نشطة • ${allStudents.size} طالب وطالبة",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = QuranGoldLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedGenderFilter == null,
                        onClick = { selectedGenderFilter = null },
                        label = { Text("جميع الحلقات (${halaqatList.size})") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedGenderFilter == Gender.MALE,
                        onClick = { selectedGenderFilter = Gender.MALE },
                        label = { Text("حلقات البنين 👦") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedGenderFilter == Gender.FEMALE,
                        onClick = { selectedGenderFilter = Gender.FEMALE },
                        label = { Text("حلقات البنات 👧") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Halaqat Cards
            items(filteredHalaqat, key = { it.name }) { halaqah ->
                val studentsInCircle = allStudents.filter { it.circleName == halaqah.name }
                val presentCount = studentsInCircle.count { s ->
                    todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.PRESENT }
                }
                val attendanceRate = if (studentsInCircle.isNotEmpty()) {
                    (presentCount * 100) / studentsInCircle.size
                } else 0

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("halaqah_card_${halaqah.name}")
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
                                        .background(if (halaqah.gender == Gender.MALE) Color(0xFFE3F2FD) else Color(0xFFFCE4EC)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (halaqah.gender == Gender.MALE) "👦" else "👧",
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = halaqah.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "المحفظ: ${halaqah.teacherName} • ${halaqah.location}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Surface(
                                color = QuranEmeraldContainer,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${studentsInCircle.size} طلاب",
                                    color = QuranEmeraldDark,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats Progress Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "نسبة الحضور اليوم:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$presentCount من ${studentsInCircle.size} ($attendanceRate%)",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (attendanceRate >= 80) QuranEmerald else QuranGold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { (attendanceRate / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = QuranEmerald,
                            trackColor = QuranEmeraldContainer
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.setRole(AppRole.QUICK_ATTENDANCE)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("quick_att_circle_btn")
                            ) {
                                Icon(imageVector = Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("التحضير السريع", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    selectedCircleForStudent = halaqah.name
                                    showAddStudentDialog = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("add_student_to_circle_btn")
                            ) {
                                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إضافة طالب", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Circle Dialog
    if (showAddCircleDialog) {
        AlertDialog(
            onDismissRequest = { showAddCircleDialog = false },
            title = { Text("إنشاء حلقة قرآنية جديدة", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newCircleName,
                        onValueChange = { newCircleName = it },
                        label = { Text("اسم الحلقة (مثال: حلقة الإمام ورش)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCircleTeacher,
                        onValueChange = { newCircleTeacher = it },
                        label = { Text("اسم المعلم أو المحفظ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = newCircleGender == Gender.MALE,
                            onClick = { newCircleGender = Gender.MALE },
                            label = { Text("بنين 👦") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = newCircleGender == Gender.FEMALE,
                            onClick = { newCircleGender = Gender.FEMALE },
                            label = { Text("بنات 👧") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCircleName.isNotBlank()) {
                            viewModel.saveStudent(
                                name = if (newCircleGender == Gender.MALE) "طالب جديد" else "طالبة جديدة",
                                gender = newCircleGender,
                                circleName = newCircleName.trim(),
                                parentPhone = "0500000000",
                                parentCode = "QRN-${(100..999).random()}",
                                targetGoal = "حفظ القرآن الكريم",
                                currentJuz = 30,
                                notes = "حلقة جديدة تم إنشاؤها"
                            )
                            viewModel.showSnackbar("تم إنشاء الحلقة: $newCircleName بنجاح 🕌")
                            newCircleName = ""
                            newCircleTeacher = ""
                            showAddCircleDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald)
                ) {
                    Text("حفظ الحلقة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCircleDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Add Student Dialog
    if (showAddStudentDialog) {
        AddStudentDialog(
            studentToEdit = null,
            onDismiss = { showAddStudentDialog = false },
            onSave = { id, name, gender, circle, phone, code, goal, juz, notes ->
                viewModel.saveStudent(
                    id = id,
                    name = name,
                    gender = gender,
                    circleName = if (selectedCircleForStudent.isNotBlank()) selectedCircleForStudent else circle,
                    parentPhone = phone,
                    parentCode = code,
                    targetGoal = goal,
                    currentJuz = juz,
                    notes = notes
                )
                showAddStudentDialog = false
            }
        )
    }
}
