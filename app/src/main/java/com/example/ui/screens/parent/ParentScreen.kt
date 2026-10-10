package com.example.ui.screens.parent

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
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.allStudents.collectAsState()
    val selectedStudent by viewModel.selectedStudent.collectAsState()
    val todayAttendance by viewModel.todayAttendance.collectAsState()
    val allRecitations by viewModel.allRecitations.collectAsState()
    val allAttendance by viewModel.allAttendance.collectAsState()
    val allNotifications by viewModel.allNotifications.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    var showSwitchStudentDialog by remember { mutableStateOf(false) }
    var codeInput by remember { mutableStateOf("") }
    val parentLoginError by viewModel.parentLoginError.collectAsState()

    val currentStudent = selectedStudent ?: students.firstOrNull()

    // Student specific records
    val studentRecitations = remember(allRecitations, currentStudent) {
        if (currentStudent != null) allRecitations.filter { it.studentId == currentStudent.id } else emptyList()
    }

    val todayRecitation = remember(studentRecitations, currentDate) {
        studentRecitations.find { it.dateStr == currentDate } ?: studentRecitations.firstOrNull()
    }

    val todayAtt = remember(todayAttendance, currentStudent) {
        if (currentStudent != null) todayAttendance.find { it.studentId == currentStudent.id } else null
    }

    val studentAttendanceHistory = remember(allAttendance, currentStudent) {
        if (currentStudent != null) allAttendance.filter { it.studentId == currentStudent.id } else emptyList()
    }

    val studentNotifications = remember(allNotifications, currentStudent) {
        if (currentStudent != null) allNotifications.filter { it.studentId == currentStudent.id } else emptyList()
    }

    // Attendance stats
    val totalSessions = studentAttendanceHistory.size
    val presentCount = studentAttendanceHistory.count { it.status == AttendanceStatus.PRESENT }
    val attendanceRate = if (totalSessions > 0) (presentCount.toFloat() / totalSessions.toFloat() * 100).toInt() else 100

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Top Switcher Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = QuranEmeraldContainer.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ملف الطالب / الابن الحالي:",
                            style = MaterialTheme.typography.labelMedium,
                            color = QuranEmerald
                        )
                        Text(
                            text = currentStudent?.name ?: "لم يتم اختيار طالب",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = QuranEmeraldDark
                        )
                        currentStudent?.let {
                            Text(
                                text = "${it.circleName} • كود: ${it.parentCode}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = { showSwitchStudentDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = QuranEmerald),
                        modifier = Modifier.testTag("parent_switch_student_btn")
                    ) {
                        Text("تبديل الابن / الكود", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        if (currentStudent == null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("يرجى إدخال كود المتابعة لولي الأمر لربط الطالب")
                    }
                }
            }
        } else {
            // Main Daily Progress Card (بطاقة المتابعة اليومية)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth().testTag("parent_daily_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Card Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(QuranGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = QuranEmerald,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "تقرير الإنجاز اليومي",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranEmerald
                                    )
                                    Text(
                                        text = currentDate,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            AttendanceBadge(status = todayAtt?.status)
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                        // Recitation details
                        if (todayRecitation != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "تسميع: سورة ${todayRecitation.surahName}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    RatingBadge(rating = todayRecitation.rating)
                                }

                                Text(
                                    text = "الآيات المقررة: من آية ${todayRecitation.fromAyah} إلى آية ${todayRecitation.toAyah} • ${todayRecitation.type.titleAr}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Tajweed and Fluency scores
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ScoreProgressBar(
                                        score = todayRecitation.tajweedScore,
                                        label = "درجة التجويد",
                                        modifier = Modifier.weight(1f)
                                    )
                                    ScoreProgressBar(
                                        score = todayRecitation.fluencyScore,
                                        label = "درجة الطلاقة",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                if (todayRecitation.teacherNotes.isNotBlank()) {
                                    Surface(
                                        color = QuranGoldContainer.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Chat,
                                                contentDescription = null,
                                                tint = QuranOnGoldContainer,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = "توجيه المعلم لولي الأمر:",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = QuranOnGoldContainer
                                                )
                                                Text(
                                                    text = todayRecitation.teacherNotes,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "لم يتم تسجيل تسميع لهذا اليوم بعد.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Share Daily Card Button
                        Button(
                            onClick = { viewModel.shareParentReport(currentStudent) },
                            colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("parent_share_report_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("مشاركة التقرير عبر واتساب العائلة", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Stats Overview
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "الرصيد الكلي",
                        value = "${currentStudent.totalPoints} نقطة",
                        icon = Icons.Default.Stars,
                        color = QuranGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "المواظبة",
                        value = "${currentStudent.streakDays} يوم متواصل",
                        icon = Icons.Default.LocalFireDepartment,
                        color = Color(0xFFE65100),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "نسبة الالتزام",
                        value = "$attendanceRate%",
                        icon = Icons.Default.PieChart,
                        color = QuranEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quran Progress Path Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "مسار الختمة القرآنية المباركة",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuranEmerald
                            )
                            Surface(
                                color = QuranEmeraldContainer,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "الجزء ${currentStudent.currentJuz} من 30",
                                    color = QuranEmerald,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "الهدف: ${currentStudent.targetGoal}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        val progress = ((31 - currentStudent.currentJuz).toFloat() / 30f).coerceIn(0.05f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = QuranEmerald,
                            trackColor = QuranEmeraldContainer.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تم إنجاز ${(progress * 100).toInt()}% من خطة الحفظ والمراجعة",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = QuranEmerald
                        )
                    }
                }
            }

            // Notifications sent to parent
            item {
                Text(
                    text = "سجل الإشعارات والتنبيهات الموجهة لولي الأمر (${studentNotifications.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = QuranEmerald
                )
            }

            if (studentNotifications.isEmpty()) {
                item {
                    Text(
                        text = "لا توجد إشعارات مسجلة لهذا الطالب",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(studentNotifications) { notif ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = QuranEmerald
                                )
                                Text(
                                    text = notif.dateStr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notif.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    // Switch Student / Enter Parent Code Dialog
    if (showSwitchStudentDialog) {
        AlertDialog(
            onDismissRequest = { showSwitchStudentDialog = false },
            title = { Text("اختيار الطالب أو إدخال رمز المتابعة", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("أدخل كود المتابعة المعطى من إدارة الحلقة:")
                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = { codeInput = it },
                        placeholder = { Text("مثال: QRN-101") },
                        modifier = Modifier.fillMaxWidth().testTag("parent_code_input"),
                        shape = RoundedCornerShape(12.dp),
                        isError = parentLoginError != null
                    )
                    parentLoginError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    Text("أو اختر مباشرة من قائمة الطلاب المسجلين:")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        students.forEach { s ->
                            OutlinedCard(
                                onClick = {
                                    viewModel.selectStudent(s.id)
                                    showSwitchStudentDialog = false
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(s.name, fontWeight = FontWeight.SemiBold)
                                    Text(s.parentCode, color = QuranEmerald, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (codeInput.isNotBlank()) {
                            viewModel.loginWithParentCode(codeInput)
                            showSwitchStudentDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("دخول بالكود")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSwitchStudentDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}
