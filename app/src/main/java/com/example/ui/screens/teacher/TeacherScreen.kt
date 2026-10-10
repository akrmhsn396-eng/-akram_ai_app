package com.example.ui.screens.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.allStudents.collectAsState()
    val circles by viewModel.allCircles.collectAsState()
    val todayAttendance by viewModel.todayAttendance.collectAsState()
    val allRecitations by viewModel.allRecitations.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedGenderFilter by remember { mutableStateOf<Gender?>(null) }
    var selectedCircleFilter by remember { mutableStateOf<String?>(null) }

    var studentForRecitation by remember { mutableStateOf<Student?>(null) }
    var showRecitationDialog by remember { mutableStateOf(false) }

    var studentForAttendance by remember { mutableStateOf<Student?>(null) }
    var showAttendanceDialog by remember { mutableStateOf(false) }

    var showAddStudentDialog by remember { mutableStateOf(false) }
    var studentToEdit by remember { mutableStateOf<Student?>(null) }

    // Filter students
    val filteredStudents = remember(students, searchQuery, selectedGenderFilter, selectedCircleFilter) {
        students.filter { s ->
            val matchQuery = searchQuery.isBlank() || s.name.contains(searchQuery.trim()) || s.parentCode.contains(searchQuery.trim(), ignoreCase = true)
            val matchGender = selectedGenderFilter == null || s.gender == selectedGenderFilter
            val matchCircle = selectedCircleFilter == null || s.circleName == selectedCircleFilter
            matchQuery && matchGender && matchCircle
        }
    }

    // Today Stats
    val presentCount = todayAttendance.count { it.status == AttendanceStatus.PRESENT }
    val absentCount = todayAttendance.count { it.status == AttendanceStatus.ABSENT }
    val todayRecitationsCount = allRecitations.count { it.dateStr == currentDate }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    studentForRecitation = filteredStudents.firstOrNull()
                    showRecitationDialog = true
                },
                containerColor = QuranEmerald,
                contentColor = Color.White,
                modifier = Modifier.testTag("teacher_fab_recitation")
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تسجيل تسميع", fontWeight = FontWeight.Bold)
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
            contentPadding = PaddingValues(bottom = 90.dp, top = 12.dp)
        ) {
            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "الطلاب",
                        value = "${students.size}",
                        icon = Icons.Default.Groups,
                        color = QuranEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "الحاضرون",
                        value = "$presentCount",
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "تسميعات اليوم",
                        value = "$todayRecitationsCount",
                        icon = Icons.Default.MenuBook,
                        color = QuranGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Search and Add Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("ابحث بالاسم أو كود ولي الأمر...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("teacher_search_field"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledIconButton(
                        onClick = {
                            studentToEdit = null
                            showAddStudentDialog = true
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = QuranEmerald),
                        modifier = Modifier.size(52.dp).testTag("add_student_btn")
                    ) {
                        Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "إضافة طالب", tint = Color.White)
                    }
                }
            }

            // Filters (Gender and Circles)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Gender chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedGenderFilter == null,
                            onClick = { selectedGenderFilter = null },
                            label = { Text("الكل (${students.size})") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        FilterChip(
                            selected = selectedGenderFilter == Gender.MALE,
                            onClick = { selectedGenderFilter = Gender.MALE },
                            label = { Text("بنين 👦 (${students.count { it.gender == Gender.MALE }})") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        FilterChip(
                            selected = selectedGenderFilter == Gender.FEMALE,
                            onClick = { selectedGenderFilter = Gender.FEMALE },
                            label = { Text("بنات 👧 (${students.count { it.gender == Gender.FEMALE }})") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Circle Chips
                    if (circles.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = selectedCircleFilter == null,
                                onClick = { selectedCircleFilter = null },
                                label = { Text("جميع الحلقات") },
                                shape = RoundedCornerShape(10.dp)
                            )
                            circles.forEach { c ->
                                FilterChip(
                                    selected = selectedCircleFilter == c,
                                    onClick = { selectedCircleFilter = if (selectedCircleFilter == c) null else c },
                                    label = { Text(c) },
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "قائمة الطلاب والطالبات (${filteredStudents.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = QuranEmerald
                    )
                    selectedCircleFilter?.let { currentCircle ->
                        TextButton(
                            onClick = {
                                viewModel.markAllCircleAttendance(currentCircle, AttendanceStatus.PRESENT)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تحضير الحلقة كاملة", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Students List
            if (filteredStudents.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.SearchOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("لا توجد نتائج مطابقة للبحث أو التصفية", color = Color.Gray)
                        }
                    }
                }
            } else {
                items(filteredStudents, key = { it.id }) { student ->
                    val todayAtt = todayAttendance.find { it.studentId == student.id }
                    val lastRec = allRecitations.firstOrNull { it.studentId == student.id }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_card_${student.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Top Row: Avatar + Name + Badges
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(if (student.gender == Gender.MALE) Color(0xFF1976D2).copy(alpha = 0.15f) else Color(0xFFC2185B).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = student.name.take(1),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = if (student.gender == Gender.MALE) Color(0xFF1976D2) else Color(0xFFC2185B)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = student.name,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        GenderBadge(gender = student.gender)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = student.circleName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = " • كود: ${student.parentCode}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = QuranEmerald,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                AttendanceBadge(status = todayAtt?.status)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Recent Recitation Preview
                            if (lastRec != null) {
                                Surface(
                                    color = QuranEmeraldContainer.copy(alpha = 0.35f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "آخر تسميع: سورة ${lastRec.surahName} (${lastRec.fromAyah}-${lastRec.toAyah})",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = QuranEmerald
                                        )
                                        RatingBadge(rating = lastRec.rating)
                                    }
                                }
                            } else {
                                Text(
                                    text = "لم يتم تسجيل تسميع لهذا اليوم حتى الآن",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Record recitation
                                Button(
                                    onClick = {
                                        studentForRecitation = student
                                        showRecitationDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("recite_btn_${student.id}"),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تسميع", fontSize = 13.sp)
                                }

                                // Mark attendance
                                OutlinedButton(
                                    onClick = {
                                        studentForAttendance = student
                                        showAttendanceDialog = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("att_btn_${student.id}"),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("الحضور", fontSize = 13.sp)
                                }

                                // Share report via WhatsApp
                                FilledTonalIconButton(
                                    onClick = { viewModel.shareParentReport(student) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("share_parent_btn_${student.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "مشاركة التقرير لولي الأمر عبر واتساب",
                                        tint = QuranEmerald
                                    )
                                }

                                // Edit student
                                IconButton(
                                    onClick = {
                                        studentToEdit = student
                                        showAddStudentDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "تعديل",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showRecitationDialog) {
        RecitationDialog(
            students = students,
            initialStudent = studentForRecitation,
            onDismiss = { showRecitationDialog = false },
            onSave = { student, type, surah, fromAyah, toAyah, rating, tajweed, fluency, notes, teacherName ->
                viewModel.saveRecitation(student, type, surah, fromAyah, toAyah, rating, tajweed, fluency, notes, teacherName)
            }
        )
    }

    if (showAttendanceDialog && studentForAttendance != null) {
        val currentStatus = todayAttendance.find { it.studentId == studentForAttendance!!.id }?.status
        AttendanceDialog(
            student = studentForAttendance!!,
            currentStatus = currentStatus,
            onDismiss = {
                showAttendanceDialog = false
                studentForAttendance = null
            },
            onSave = { studentId, status, note ->
                viewModel.setAttendance(studentId, status, note)
            }
        )
    }

    if (showAddStudentDialog) {
        AddStudentDialog(
            studentToEdit = studentToEdit,
            onDismiss = {
                showAddStudentDialog = false
                studentToEdit = null
            },
            onSave = { id, name, gender, circle, phone, code, goal, currentJuz, notes ->
                viewModel.saveStudent(id, name, gender, circle, phone, code, goal, currentJuz, notes)
            }
        )
    }
}
