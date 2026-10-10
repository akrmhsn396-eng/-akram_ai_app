package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Gender
import com.example.data.model.Student
import com.example.ui.theme.QuranEmerald

@Composable
fun AddStudentDialog(
    studentToEdit: Student? = null,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        name: String,
        gender: Gender,
        circleName: String,
        parentPhone: String,
        parentCode: String,
        targetGoal: String,
        currentJuz: Int,
        notes: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(studentToEdit?.name ?: "") }
    var gender by remember { mutableStateOf(studentToEdit?.gender ?: Gender.MALE) }
    var circleName by remember { mutableStateOf(studentToEdit?.circleName ?: "حلقة الإمام الشاطبي") }
    var parentPhone by remember { mutableStateOf(studentToEdit?.parentPhone ?: "0501234567") }
    var parentCode by remember { mutableStateOf(studentToEdit?.parentCode ?: "QRN-${(100..999).random()}") }
    var targetGoal by remember { mutableStateOf(studentToEdit?.targetGoal ?: "ختم القرآن الكريم كاملاً") }
    var currentJuzStr by remember { mutableStateOf(studentToEdit?.currentJuz?.toString() ?: "30") }
    var notes by remember { mutableStateOf(studentToEdit?.notes ?: "") }

    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (studentToEdit == null) "إضافة طالب / طالبة جديد" else "تعديل بيانات الطالب",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        isError = false
                    },
                    label = { Text("اسم الطالب الرباعي *") },
                    isError = isError && name.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                // Gender Selection
                Text(
                    text = "الجنس / الفئة:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = gender == Gender.MALE,
                        onClick = { gender = Gender.MALE },
                        label = { Text("بنين (طلاب) 👦") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = gender == Gender.FEMALE,
                        onClick = { gender = Gender.FEMALE },
                        label = { Text("بنات (طالبات) 👧") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = circleName,
                    onValueChange = { circleName = it },
                    label = { Text("اسم الحلقة القرآنية *") },
                    placeholder = { Text("مثال: حلقة الشاطبي، حلقة خديجة") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = parentPhone,
                    onValueChange = { parentPhone = it },
                    label = { Text("رقم هاتف ولي الأمر (واتساب) *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = parentCode,
                    onValueChange = { parentCode = it },
                    label = { Text("رمز دخول ولي الأمر (كود فريد)") },
                    supportingText = { Text("يستخدمه ولي الأمر لمتابعة الطالب بالتطبيق") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = targetGoal,
                    onValueChange = { targetGoal = it },
                    label = { Text("الهدف القرآني للطالب") },
                    placeholder = { Text("مثال: حفظ جزء عم، حفظ 10 أجزاء...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = currentJuzStr,
                    onValueChange = { currentJuzStr = it.filter { c -> c.isDigit() } },
                    label = { Text("الجزء الحالي (1 - 30)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات إضافية") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        isError = true
                        return@Button
                    }
                    val juz = (currentJuzStr.toIntOrNull() ?: 30).coerceIn(1, 30)
                    onSave(
                        studentToEdit?.id ?: 0L,
                        name,
                        gender,
                        circleName,
                        parentPhone,
                        parentCode,
                        targetGoal,
                        juz,
                        notes
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_student_btn")
            ) {
                Text("حفظ البيانات", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
