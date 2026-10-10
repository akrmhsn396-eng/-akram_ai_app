package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AttendanceStatus
import com.example.data.model.Student
import com.example.ui.theme.QuranEmerald

@Composable
fun AttendanceDialog(
    student: Student,
    currentStatus: AttendanceStatus?,
    onDismiss: () -> Unit,
    onSave: (studentId: Long, status: AttendanceStatus, note: String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus ?: AttendanceStatus.PRESENT) }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "تسجيل الحضور والغياب",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${student.name} - ${student.circleName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "اختر حالة الحضور لليوم:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                AttendanceStatus.values().forEach { status ->
                    val isSelected = selectedStatus == status
                    val (color, label) = when (status) {
                        AttendanceStatus.PRESENT -> Pair(Color(0xFF2E7D32), "حاضر في الموعد ✅")
                        AttendanceStatus.LATE -> Pair(Color(0xFFEF6C00), "متأخر عن الحلقة ⏰")
                        AttendanceStatus.EXCUSED -> Pair(Color(0xFF0288D1), "مستأذن بعذر مسبق ℹ️")
                        AttendanceStatus.ABSENT -> Pair(Color(0xFFC62828), "غائب عن الحلقة ❌")
                    }

                    OutlinedButton(
                        onClick = { selectedStatus = status },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("attendance_btn_${status.name}"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) color.copy(alpha = 0.15f) else Color.Transparent
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) color else Color.LightGray)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = color
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("سبب الغياب / ملاحظة الحضور") },
                    placeholder = { Text("مثال: تأخر لعذر مدرسي، مريض...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(student.id, selectedStatus, note)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_attendance_btn")
            ) {
                Text("حفظ وتسجيل الحضور", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
