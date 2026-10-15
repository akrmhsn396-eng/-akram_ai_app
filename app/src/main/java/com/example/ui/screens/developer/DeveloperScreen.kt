package com.example.ui.screens.developer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Gender
import com.example.ui.components.AddStudentDialog
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.allStudents.collectAsState()
    val recitations by viewModel.allRecitations.collectAsState()
    val attendance by viewModel.allAttendance.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()

    var showBroadcastDialog by remember { mutableStateOf(false) }
    var broadcastTitle by remember { mutableStateOf("تنبيه هام من إدارة حلقات القرآن") }
    var broadcastMsg by remember { mutableStateOf("نحيطكم علماً بأن موعد الاختبار التقييمي القادم سيكون يوم الأحد القادم. نرجو تشجيع الأبناء على المراجعة.") }

    var showExportDialog by remember { mutableStateOf(false) }
    var exportedJson by remember { mutableStateOf("") }

    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Developer Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = QuranDarkSurfaceVariant),
                modifier = Modifier.fillMaxWidth().testTag("dev_banner_card")
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
                                    .background(QuranEmerald),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "لوحة تحكم المطور والإدارة",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranDarkTextPrimary
                                )
                                Text(
                                    text = "التحكم في النظام • البث والإشعارات • أدوات النشر",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuranEmeraldLight
                                )
                            }
                        }

                        Surface(
                            color = QuranEmerald,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "وضع الإشراف",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "نظام متكامل يتيح للمطور والمشرف متابعة كافة تفاصيل حلقات القرآن، وإرسال تنبيهات جماعية، ومشاركة روابط تثبيت التطبيق للأجهزة الذكية والألواح الرقمية.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuranDarkTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // System Stats Overview
        item {
            Text(
                text = "إحصائيات وقاعدة بيانات النظام 📊",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = QuranEmerald
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "إجمالي الطلاب",
                    value = "${students.size}",
                    icon = Icons.Default.Person,
                    color = QuranEmerald,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "سجلات التسميع",
                    value = "${recitations.size}",
                    icon = Icons.Default.MenuBook,
                    color = QuranGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "سجلات الحضور",
                    value = "${attendance.size}",
                    icon = Icons.Default.Checklist,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section: Share & Install across devices
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Devices, contentDescription = null, tint = QuranEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "نشر وتثبيت التطبيق على كافة الأجهزة الذكية 📱💻",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "التطبيق يدعم العمل المتجاوب على جميع الهواتف المحمولة الذكية (Android / iOS) والأجهزة اللوحية (Tablets) وشاشات العرض.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = QuranEmeraldContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "رابط تثبيت التطبيق الرسمي:",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuranEmerald,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "https://quran-circles.app/install",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.shareAppInvitation() },
                            colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("share_app_invite_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("مشاركة الرابط العام")
                        }

                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString("https://quran-circles.app/install"))
                                viewModel.showSnackbar("تم نسخ رابط التطبيق للحافظة 📋")
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("نسخ الرابط")
                        }
                    }
                }
            }
        }

        // Section: Push Notification Broadcast to Parents
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = QuranGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بث إشعار عام لجميع أولياء الأمور 📢",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "إرسال تنبيه فوري يظهر في هواتف أولياء الأمور وبوابة المتابعة وسجل الإشعارات لكل طالب.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showBroadcastDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = QuranGold),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("open_broadcast_dialog_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = QuranDarkBackground, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("إنشاء وإرسال إشعار عام الآن", color = QuranDarkBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Database & Developer Tools
        item {
            Text(
                text = "أدوات المطور وإدارة البيانات 🛠️",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = QuranEmerald
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Add student shortcut
                OutlinedCard(
                    onClick = { showAddStudentDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("dev_add_student_card")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = QuranEmerald)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("إضافة طالب أو طالبة جديدة لقاعدة البيانات", fontWeight = FontWeight.SemiBold)
                        }
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                    }
                }

                // Export JSON
                OutlinedCard(
                    onClick = {
                        exportedJson = viewModel.exportDatabaseJson()
                        showExportDialog = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("dev_export_json_card")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = QuranEmerald)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("تصدير نسخة احتياطية من البيانات (JSON)", fontWeight = FontWeight.SemiBold)
                        }
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                    }
                }

                // Reset and reseed database
                OutlinedCard(
                    onClick = { showResetConfirmDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("dev_reset_data_card"),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Restore, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("إعادة تهيئة البيانات الرسمية (Reset Data)", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                        }
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }

    // Broadcast Dialog
    if (showBroadcastDialog) {
        AlertDialog(
            onDismissRequest = { showBroadcastDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = QuranGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("بث إشعار لجميع أولياء الأمور", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = broadcastTitle,
                        onValueChange = { broadcastTitle = it },
                        label = { Text("عنوان الإشعار") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = broadcastMsg,
                        onValueChange = { broadcastMsg = it },
                        label = { Text("نص الرسالة / التنبيه") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Text(
                        text = "سيتم إرسال هذا الإشعار إلى هواتف أولياء أمور جميع الطلاب المسجلين (${students.size} طالب/ة).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (broadcastTitle.isNotBlank() && broadcastMsg.isNotBlank()) {
                            viewModel.broadcastNotification(broadcastTitle, broadcastMsg)
                            showBroadcastDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("send_broadcast_btn")
                ) {
                    Text("إرسال الإشعار الآن", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBroadcastDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Export JSON Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("النسخة الاحتياطية المصدرة (JSON)", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = exportedJson,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().heightIn(max = 260.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(exportedJson))
                        viewModel.showSnackbar("تم نسخ بيانات JSON إلى الحافظة 📋")
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("نسخ JSON")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("إغلاق")
                }
            }
        )
    }

    // Reset Confirm Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("تأكيد إعادة التعيين", fontWeight = FontWeight.Bold) },
            text = {
                Text("هل تريد استعادة البيانات الرسمية الافتراضية للحلقات والطلاب والتسميعات؟")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetDatabase()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("نعم، استعادة الضبط")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
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
            onSave = { id, name, gender, circle, phone, code, goal, currentJuz, notes ->
                viewModel.saveStudent(id, name, gender, circle, phone, code, goal, currentJuz, notes)
            }
        )
    }
}
