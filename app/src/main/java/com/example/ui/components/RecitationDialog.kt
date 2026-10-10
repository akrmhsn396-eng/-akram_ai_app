package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationDialog(
    students: List<Student>,
    initialStudent: Student?,
    onDismiss: () -> Unit,
    onSave: (
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
    ) -> Unit
) {
    var selectedStudent by remember { mutableStateOf(initialStudent ?: students.firstOrNull()) }
    var selectedType by remember { mutableStateOf(RecitationType.NEW_MEMORIZATION) }

    // Surah selection
    var surahQuery by remember { mutableStateOf(selectedStudent?.currentJuz?.let { "النبأ" } ?: "الملك") }
    var surahExpanded by remember { mutableStateOf(false) }

    var fromAyahStr by remember { mutableStateOf("1") }
    var toAyahStr by remember { mutableStateOf("10") }

    var selectedRating by remember { mutableStateOf(RecitationRating.EXCELLENT) }
    var tajweedScore by remember { mutableStateOf(9f) }
    var fluencyScore by remember { mutableStateOf(9f) }
    var notes by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("الشيخ عبد الرحمن") }

    val currentSurahObj = QuranData.getByName(surahQuery) ?: QuranData.surahs.first()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = QuranEmerald
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تسجيل تسميع وحفظ قرآني",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Student selection
                Text(
                    text = "الطالب / الطالبة:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                var studentDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = studentDropdownExpanded,
                    onExpandedChange = { studentDropdownExpanded = !studentDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStudent?.let { "${it.name} (${it.circleName})" } ?: "اختر الطالب",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("recitation_student_picker"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = studentDropdownExpanded,
                        onDismissRequest = { studentDropdownExpanded = false }
                    ) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = { Text("${s.name} - ${s.genderLabel} (${s.circleName})") },
                                onClick = {
                                    selectedStudent = s
                                    studentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Recitation Type Chips
                Text(
                    text = "نوع التسميع:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecitationType.values().forEach { t ->
                        val isSel = selectedType == t
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedType = t },
                            label = { Text(t.titleAr, style = MaterialTheme.typography.bodySmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuranEmerald,
                                selectedLabelColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Surah Picker
                Text(
                    text = "السورة القرآنية الكريمة:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                ExposedDropdownMenuBox(
                    expanded = surahExpanded,
                    onExpandedChange = { surahExpanded = !surahExpanded }
                ) {
                    OutlinedTextField(
                        value = surahQuery,
                        onValueChange = {
                            surahQuery = it
                            surahExpanded = true
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = surahExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("recitation_surah_picker"),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("ابحث عن السورة...") }
                    )
                    val filteredSurahs = QuranData.surahs.filter {
                        it.name.contains(surahQuery.trim()) || it.transliteration.contains(surahQuery.trim(), ignoreCase = true)
                    }
                    ExposedDropdownMenu(
                        expanded = surahExpanded,
                        onDismissRequest = { surahExpanded = false }
                    ) {
                        filteredSurahs.take(15).forEach { surah ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("سورة ${surah.name}")
                                        Text(
                                            "${surah.versesCount} آية - ${surah.revelationType}",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    surahQuery = surah.name
                                    toAyahStr = surah.versesCount.coerceAtMost(10).toString()
                                    surahExpanded = false
                                }
                            )
                        }
                    }
                }

                // Verses Range
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = fromAyahStr,
                        onValueChange = { fromAyahStr = it.filter { char -> char.isDigit() } },
                        label = { Text("من آية") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = toAyahStr,
                        onValueChange = { toAyahStr = it.filter { char -> char.isDigit() } },
                        label = { Text("إلى آية") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Rating
                Text(
                    text = "التقدير والتقييم العام:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RecitationRating.values().forEach { r ->
                        val isSel = selectedRating == r
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedRating = r },
                            label = { Text(r.titleAr.take(7), style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuranGold,
                                selectedLabelColor = QuranDarkBackground
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                // Tajweed Score Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "إتقان أحكام التجويد ومخارج الحروف:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${tajweedScore.toInt()} / 10",
                            fontWeight = FontWeight.Bold,
                            color = QuranEmerald
                        )
                    }
                    Slider(
                        value = tajweedScore,
                        onValueChange = { tajweedScore = it },
                        valueRange = 1f..10f,
                        steps = 8,
                        colors = SliderDefaults.colors(thumbColor = QuranEmerald, activeTrackColor = QuranEmerald)
                    )
                }

                // Fluency Score Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "طلاقة القراءة والاسترسال:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${fluencyScore.toInt()} / 10",
                            fontWeight = FontWeight.Bold,
                            color = QuranEmerald
                        )
                    }
                    Slider(
                        value = fluencyScore,
                        onValueChange = { fluencyScore = it },
                        valueRange = 1f..10f,
                        steps = 8,
                        colors = SliderDefaults.colors(thumbColor = QuranGold, activeTrackColor = QuranGold)
                    )
                }

                // Teacher Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات المعلم / توجيهات لولي الأمر") },
                    placeholder = { Text("مثال: ما شاء الله، حفظ متقن، نوصي بمراجعة أحكام الإخفاء...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = selectedStudent ?: return@Button
                    val fromAyah = fromAyahStr.toIntOrNull() ?: 1
                    val toAyah = toAyahStr.toIntOrNull() ?: currentSurahObj.versesCount
                    onSave(
                        target,
                        selectedType,
                        surahQuery,
                        fromAyah,
                        toAyah,
                        selectedRating,
                        tajweedScore.toInt(),
                        fluencyScore.toInt(),
                        notes,
                        teacherName
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_recitation_btn")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("حفظ وإشعار ولي الأمر", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
