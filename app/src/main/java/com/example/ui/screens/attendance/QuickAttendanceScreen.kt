package com.example.ui.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceStatus
import com.example.data.model.Gender
import com.example.data.model.Student
import com.example.ui.components.AttendanceDialog
import com.example.ui.theme.*
import com.example.util.ShareHelper
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAttendanceScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val allStudents by viewModel.allStudents.collectAsState()
    val todayAttendance by viewModel.todayAttendance.collectAsState()
    val allCircles by viewModel.allCircles.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    var selectedCircle by remember { mutableStateOf<String?>("الكل") }
    var studentForNoteDialog by remember { mutableStateOf<Student?>(null) }
    var showNoteDialog by remember { mutableStateOf(false) }

    val filteredStudents = remember(allStudents, selectedCircle) {
        if (selectedCircle == null || selectedCircle == "الكل") {
            allStudents
        } else {
            allStudents.filter { it.circleName == selectedCircle }
        }
    }

    // Counters
    val totalCount = filteredStudents.size
    val presentCount = filteredStudents.count { s ->
        todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.PRESENT }
    }
    val lateCount = filteredStudents.count { s ->
        todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.LATE }
    }
    val excusedCount = filteredStudents.count { s ->
        todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.EXCUSED }
    }
    val absentCount = filteredStudents.count { s ->
        todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.ABSENT }
    }
    val unrecordedCount = totalCount - (presentCount + lateCount + excusedCount + absentCount)
    val attendancePercent = if (totalCount > 0) ((presentCount + lateCount) * 100) / totalCount else 0

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
        ) {
            // Header Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranEmerald),
                    modifier = Modifier.fillMaxWidth().testTag("quick_att_banner")
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
                                        .background(QuranGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FactCheck,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "التحضير السريع اليومي ⚡",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "تاريخ اليوم: $currentDate",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = QuranGoldLight
                                    )
                                }
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$attendancePercent% إنجاز",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            QuickStatBadge("حاضر ✅", "$presentCount", Color(0xFF2E7D32), Modifier.weight(1f))
                            QuickStatBadge("متأخر ⏰", "$lateCount", Color(0xFFEF6C00), Modifier.weight(1f))
                            QuickStatBadge("مستأذن ℹ️", "$excusedCount", Color(0xFF0288D1), Modifier.weight(1f))
                            QuickStatBadge("غائب ❌", "$absentCount", Color(0xFFC62828), Modifier.weight(1f))
                            QuickStatBadge("لم يرصد", "$unrecordedCount", Color(0xFF757575), Modifier.weight(1f))
                        }
                    }
                }
            }

            // Quick Batch Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (selectedCircle == null || selectedCircle == "الكل") {
                                viewModel.markAllStudentsPresent()
                            } else {
                                viewModel.markAllCircleAttendance(selectedCircle!!, AttendanceStatus.PRESENT)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("mark_all_present_btn")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تحضير الكل حاضر ✅", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val absentStudents = filteredStudents.filter { s ->
                                todayAttendance.any { it.studentId == s.id && it.status == AttendanceStatus.ABSENT }
                            }
                            if (absentStudents.isEmpty()) {
                                viewModel.showSnackbar("لا يوجد طلاب غائبون اليوم في هذه الحلقة 🎉")
                            } else {
                                val text = buildString {
                                    appendLine("📋 *تقرير غياب طلاب حلقة ${selectedCircle ?: "القرآن"} اليوم*")
                                    appendLine("التاريخ: $currentDate")
                                    appendLine("━━━━━━━━━━━━━━━")
                                    absentStudents.forEachIndexed { idx, s ->
                                        appendLine("${idx + 1}. ${s.name} (كود: ${s.parentCode})")
                                    }
                                    appendLine("━━━━━━━━━━━━━━━")
                                    appendLine("يرجى من أولياء الأمور إرسال إشعار العذر عبر التطبيق.")
                                }
                                ShareHelper.shareText(viewModel.getApplication(), text, "تقرير الغياب اليومي")
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("share_absent_report_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("مشاركة الغياب", fontSize = 12.sp)
                    }
                }
            }

            // Circles Horizontal Filter
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCircle == "الكل" || selectedCircle == null,
                        onClick = { selectedCircle = "الكل" },
                        label = { Text("جميع الحلقات (${allStudents.size})") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    allCircles.forEach { cName ->
                        val count = allStudents.count { it.circleName == cName }
                        FilterChip(
                            selected = selectedCircle == cName,
                            onClick = { selectedCircle = cName },
                            label = { Text("$cName ($count)") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Students Attendance List
            items(filteredStudents, key = { it.id }) { student ->
                val record = todayAttendance.firstOrNull { it.studentId == student.id }
                val status = record?.status

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().testTag("quick_att_student_${student.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (student.gender == Gender.MALE) Color(0xFFE3F2FD) else Color(0xFFFCE4EC)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = student.name.take(1),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = if (student.gender == Gender.MALE) Color(0xFF1976D2) else Color(0xFFC2185B)
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
                                    text = "${student.circleName} • كود: ${student.parentCode}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (record?.note?.isNotBlank() == true) {
                                    Text(
                                        text = "💬 ملاحظة: ${record.note}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = QuranGold
                                    )
                                }
                            }
                        }

                        // Attendance Toggle Button (Min Touch Target 48dp)
                        Surface(
                            onClick = { viewModel.toggleAttendanceForStudent(student.id) },
                            shape = RoundedCornerShape(12.dp),
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
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("att_toggle_${student.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (status) {
                                        AttendanceStatus.PRESENT -> "حاضر ✅"
                                        AttendanceStatus.LATE -> "متأخر ⏰"
                                        AttendanceStatus.EXCUSED -> "مستأذن ℹ️"
                                        AttendanceStatus.ABSENT -> "غائب ❌"
                                        null -> "لم يرصد ▫️"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = when (status) {
                                        AttendanceStatus.PRESENT -> Color(0xFF2E7D32)
                                        AttendanceStatus.LATE -> Color(0xFFEF6C00)
                                        AttendanceStatus.EXCUSED -> Color(0xFF0288D1)
                                        AttendanceStatus.ABSENT -> Color(0xFFC62828)
                                        null -> Color(0xFF616161)
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

@Composable
fun QuickStatBadge(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
            Text(label, color = Color.White.copy(alpha = 0.9f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
