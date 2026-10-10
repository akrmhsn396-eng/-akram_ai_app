package com.example.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Student
import com.example.ui.theme.*
import com.example.util.ShareHelper
import com.example.viewmodel.MainViewModel

data class QuranQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val allStudents by viewModel.allStudents.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    // Active tool tab: 0: Certificate, 1: Daily Challenge, 2: Voice Evaluator, 3: Calendar & Events
    var activeSubTool by remember { mutableIntStateOf(0) }

    // Certificate Generator State
    var certStudent by remember { mutableStateOf<Student?>(null) }
    var certTitle by remember { mutableStateOf("شهادة إتمام حفظ جزء عم وتبارك") }
    var certReason by remember { mutableStateOf("تقديراً للجهود المباركة في حفظ وتجويد آيات الذكر الحكيم والمواظبة على الحضور.") }
    var showCertificateDialog by remember { mutableStateOf(false) }

    // Daily Challenge State
    val questions = remember {
        listOf(
            QuranQuestion(
                1,
                "ما هي السورة الملقبة بـ (عروس القرآن)؟",
                listOf("سورة يس", "سورة الرحمن", "سورة الملك", "سورة الواقعة"),
                1,
                "سورة الرحمن تلقب بعروس القرآن لجمال نسقها وتكرار آلاء الله ونعمه."
            ),
            QuranQuestion(
                2,
                "كم عدد أحرف الإظهار الحلقي في أحكام النون الساكنة والتنوين؟",
                listOf("4 أحرف", "6 أحرف (ء هـ ع ح غ خ)", "15 حرفاً", "حرفان فقط"),
                1,
                "أحرف الإظهار الحلقي ستة: الهمزة، الهاء، العين، الحاء، الغين، والخاء."
            ),
            QuranQuestion(
                3,
                "ما هي السورة التي تعدل ثلث القرآن الكريم؟",
                listOf("سورة الإخلاص", "سورة الفاتحة", "سورة الكوثر", "سورة الفلق"),
                0,
                "سورة الإخلاص تعدل ثلث القرآن لاشتمالها على توحيد الله وتنزيهه."
            )
        )
    }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var answerChecked by remember { mutableStateOf(false) }

    // Voice Evaluator State
    var isSimulatingMic by remember { mutableStateOf(false) }
    var evaluationResult by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(allStudents) {
        if (certStudent == null && allStudents.isNotEmpty()) {
            certStudent = allStudents.first()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
        ) {
            // Header Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranEmeraldDark),
                    modifier = Modifier.fillMaxWidth().testTag("tools_header_card")
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
                                        imageVector = Icons.Default.Handyman,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "الأدوات الذكية والشهادات 🛠️",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "شهادات التقدير • التحدي اليومي • المساعد الصوتي",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = QuranGoldLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sub-tool Switcher
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = activeSubTool == 0,
                        onClick = { activeSubTool = 0 },
                        label = { Text("شهادات التقدير 📜") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = activeSubTool == 1,
                        onClick = { activeSubTool = 1 },
                        label = { Text("التحدي اليومي 🏆") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = activeSubTool == 2,
                        onClick = { activeSubTool = 2 },
                        label = { Text("المساعد الصوتي 🎙️") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Tool 1: Certificate Generator
            if (activeSubTool == 0) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "إنشاء وتصميم شهادة تقدير للطالب 🎓",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuranEmerald
                            )

                            // Select Student
                            Text("اختر الطالب المكرم:", style = MaterialTheme.typography.labelMedium)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                allStudents.take(4).forEach { s ->
                                    FilterChip(
                                        selected = certStudent?.id == s.id,
                                        onClick = { certStudent = s },
                                        label = { Text(s.name.split(" ").take(2).joinToString(" ")) },
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = certTitle,
                                onValueChange = { certTitle = it },
                                label = { Text("عنوان الشهادة أو الإنجاز") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = certReason,
                                onValueChange = { certReason = it },
                                label = { Text("عبارة التقدير والثناء") },
                                minLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = { showCertificateDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("preview_certificate_btn")
                            ) {
                                Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("معاينة وإصدار الشهادة الرسمية ✨", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Tool 2: Daily Quran Challenge
            if (activeSubTool == 1) {
                val currentQ = questions[currentQuestionIdx % questions.size]
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "تحدي القرآن والتجويد اليومي 🌟",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranEmerald
                                )
                                Text(
                                    text = "سؤال ${(currentQuestionIdx % questions.size) + 1} من ${questions.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuranGold
                                )
                            }

                            Surface(
                                color = QuranEmeraldContainer.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = currentQ.question,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Options
                            currentQ.options.forEachIndexed { idx, opt ->
                                val isSelected = selectedOptionIdx == idx
                                val isCorrect = idx == currentQ.correctIndex
                                val containerCol = when {
                                    answerChecked && isCorrect -> Color(0xFFE8F5E9)
                                    answerChecked && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                                    isSelected -> QuranEmeraldContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                                val borderCol = when {
                                    answerChecked && isCorrect -> Color(0xFF2E7D32)
                                    answerChecked && isSelected && !isCorrect -> Color(0xFFC62828)
                                    isSelected -> QuranEmerald
                                    else -> Color.Transparent
                                }

                                Surface(
                                    onClick = {
                                        if (!answerChecked) {
                                            selectedOptionIdx = idx
                                        }
                                    },
                                    color = containerCol,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderCol),
                                    modifier = Modifier.fillMaxWidth().testTag("challenge_option_$idx")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${idx + 1}. $opt",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            if (answerChecked) {
                                Surface(
                                    color = if (selectedOptionIdx == currentQ.correctIndex) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = if (selectedOptionIdx == currentQ.correctIndex) "إجابة صحيحة وممتازة! 🎉 (+25 نقطة)" else "إجابة خاطئة، حاول مرة أخرى! 💡",
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedOptionIdx == currentQ.correctIndex) Color(0xFF2E7D32) else Color(0xFFE65100)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentQ.explanation,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (!answerChecked) {
                                    Button(
                                        onClick = {
                                            if (selectedOptionIdx != null) {
                                                answerChecked = true
                                                if (selectedOptionIdx == currentQ.correctIndex) {
                                                    allStudents.firstOrNull()?.let {
                                                        viewModel.awardChallengePoints(it.id, 25)
                                                    }
                                                }
                                            }
                                        },
                                        enabled = selectedOptionIdx != null,
                                        colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("تأكيد الإجابة")
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            currentQuestionIdx++
                                            selectedOptionIdx = null
                                            answerChecked = false
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("السؤال التالي ➡️")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tool 3: Speech Evaluator & Voice Assistant
            if (activeSubTool == 2) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "المساعد الصوتي وتقييم التلاوة 🎙️",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuranEmerald
                            )
                            Text(
                                text = "اقرأ الآية الكريمة وسيقوم المساعد بتحليل مخارج الحروف وأحكام التجويد وإعطاء التقييم:",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Ayah Card
                            Surface(
                                color = QuranGoldContainer,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "﴿ اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ ۝ خَلَقَ الْإِنسَانَ مِنْ عَلَقٍ ﴾",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranOnGoldContainer,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            // Mic Button
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(if (isSimulatingMic) Color(0xFFE53935) else QuranEmerald)
                                    .clickable {
                                        isSimulatingMic = !isSimulatingMic
                                        if (isSimulatingMic) {
                                            evaluationResult = null
                                        } else {
                                            evaluationResult = "أداء متقن ما شاء الله! 🌟\n• مخارج الحروف: 10/10\n• القلقلة في (خَلَقْ): ممتازة\n• الإخفاء في (الْإِنسَانَ): مطبق بدقة"
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSimulatingMic) Icons.Default.GraphicEq else Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            Text(
                                text = if (isSimulatingMic) "جاري الاستماع للترتيل... اضغط للإيقاف والتقييم" else "اضغط على الميكروفون لبدء التسميع الصوتي",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSimulatingMic) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (evaluationResult != null) {
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "نتيجة الفحص الصوتي والتجويدي:",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = evaluationResult!!,
                                            style = MaterialTheme.typography.bodyMedium,
                                            lineHeight = 22.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Export Reports & Backup Suite
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "تقارير المتابعة والنسخ الاحتياطي 📊",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تصدير تقرير شامل لكافة طلاب الحلقات وسجلات الحفظ أو نسخ قاعدة البيانات:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val summary = buildString {
                                        appendLine("📋 *تقرير حلقات القرآن الكريم الشامل*")
                                        appendLine("إجمالي الطلاب: ${allStudents.size}")
                                        appendLine("الحلقات النشطة: ${allStudents.map { it.circleName }.distinct().joinToString(", ")}")
                                        appendLine("━━━━━━━━━━━━━━━")
                                        allStudents.forEachIndexed { i, s ->
                                            appendLine("${i+1}. ${s.name} - ${s.circleName} (النقاط: ${s.totalPoints} | جزء: ${s.currentJuz})")
                                        }
                                        appendLine("━━━━━━━━━━━━━━━")
                                        appendLine("منظومة منارة الجيل القرآني 🌿")
                                    }
                                    ShareHelper.shareText(viewModel.getApplication(), summary, "تقرير الحلقات الشامل")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تصدير التقرير", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val json = viewModel.exportDatabaseJson()
                                    clipboardManager.setText(AnnotatedString(json))
                                    viewModel.showSnackbar("تم نسخ بيانات المنظومة كاملة للحافظة 📋")
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("نسخ الاحتياطي", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Certificate Preview Dialog
    if (showCertificateDialog && certStudent != null) {
        val st = certStudent!!
        AlertDialog(
            onDismissRequest = { showCertificateDialog = false },
            title = {
                Text(
                    text = "معاينة شهادة التقدير الرسمية 🏆",
                    fontWeight = FontWeight.Bold,
                    color = QuranEmerald
                )
            },
            text = {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFCFBF7)),
                    border = androidx.compose.foundation.BorderStroke(3.dp, QuranGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("﷽", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = QuranEmeraldDark)
                        Text("حلقات تحفيظ القرآن الكريم", style = MaterialTheme.typography.labelSmall, color = QuranTextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "شهادة تميّز وإنجاز",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = QuranGold
                        )
                        Text(
                            text = "يُشهد بأن ${if (st.gender == com.example.data.model.Gender.MALE) "الطالب" else "الطالبة"}:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = st.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = QuranEmerald
                        )
                        Text(
                            text = "من ${st.circleName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuranTextSecondary
                        )
                        Text(
                            text = "قد أتم/ت بنجاح: $certTitle",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = certReason,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = QuranTextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("المحفظ المشرف", style = MaterialTheme.typography.labelSmall, color = QuranTextSecondary)
                            Text("ختم الإدارة المعتمد", style = MaterialTheme.typography.labelSmall, color = QuranTextSecondary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val certText = buildString {
                            appendLine("📜 *شهادة تقدير وإنجاز قرآني* 📜")
                            appendLine("━━━━━━━━━━━━━━━━━━━")
                            appendLine("تشهد إدارة حلقات القرآن الكريم بأن ${st.name}")
                            appendLine("من: ${st.circleName}")
                            appendLine("قد نال/ت: $certTitle")
                            appendLine("$certReason")
                            appendLine("━━━━━━━━━━━━━━━━━━━")
                            appendLine("مبارك الحفظ والتفوق 🌟")
                        }
                        ShareHelper.shareText(viewModel.getApplication(), certText, "شهادة تقدير ${st.name}")
                        showCertificateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald)
                ) {
                    Text("مشاركة الشهادة 📲")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCertificateDialog = false }) {
                    Text("إغلاق")
                }
            }
        )
    }
}
