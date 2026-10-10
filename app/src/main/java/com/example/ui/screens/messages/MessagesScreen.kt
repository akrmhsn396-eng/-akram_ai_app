package com.example.ui.screens.messages

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
import com.example.ui.theme.*
import com.example.util.ShareHelper
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val allStudents by viewModel.allStudents.collectAsState()
    val allCircles by viewModel.allCircles.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsState()

    var selectedCircleTarget by remember { mutableStateOf("الكل") }
    var messageTitle by remember { mutableStateOf("تنبيه من إدارة حلقات القرآن الكريم") }
    var messageBody by remember { mutableStateOf("السلام عليكم ورحمة الله، نود تذكيركم بموعد مراجعة القرآن الكريم اليوم.") }
    var selectedCategory by remember { mutableStateOf("تنبيه عام") }

    val messageTemplates = remember {
        listOf(
            "تذكير بالمراجعة" to "السلام عليكم ورحمة الله، نرجو من ولي الأمر الكريم متابعة ورد المراجعة اليومي لابنكم لضمان ثبات الحفظ.",
            "تنبيه غياب" to "نحيطكم علماً بأن الطالب تغيب اليوم عن موعد الحلقة القرآنية، نرجو تزويد المعلم بسبب الغياب للاطمئنان عليه.",
            "إشادة وتفوق" to "يسر إدارة الحلقة تهنئتكم بالأداء المتميز والتسميع المتقن الذي قدمه ابنكم اليوم. بارك الله في حفظه وثبته.",
            "موعد اختبار" to "نود إحاطتكم بأن موعد الاختبار التقييمي القادم لحفظ الجزء سيكون في الأسبوع المقبل، نرجو تكثيف المراجعة المنزلية.",
            "دعوة لحفل التكريم" to "نتشرف بدعوتكم لحضور الحفل الختامي لتكريم حفاظ كتاب الله تعالى وتوزيع الأوسمة والجوائز."
        )
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
            // Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranEmeraldDark),
                    modifier = Modifier.fillMaxWidth().testTag("messages_hero_card")
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
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "مركز الرسائل والتواصل 💬",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "التواصل المباشر مع أولياء الأمور عبر التطبيق والواتساب",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = QuranGoldLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Templates Selector
            item {
                Text(
                    text = "نماذج الرسائل الجاهزة 📝",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = QuranEmerald
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    messageTemplates.take(3).forEach { (tTitle, tBody) ->
                        OutlinedButton(
                            onClick = {
                                messageTitle = tTitle
                                messageBody = tBody
                                selectedCategory = tTitle
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(tTitle, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }

            // Message Composer Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "إنشاء وإرسال رسالة جديدة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Target Selector
                        Text("المرسل إليهم:", style = MaterialTheme.typography.labelMedium, color = QuranTextSecondary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedCircleTarget == "الكل",
                                onClick = { selectedCircleTarget = "الكل" },
                                label = { Text("جميع الحلقات") },
                                shape = RoundedCornerShape(10.dp)
                            )
                            allCircles.take(3).forEach { cName ->
                                FilterChip(
                                    selected = selectedCircleTarget == cName,
                                    onClick = { selectedCircleTarget = cName },
                                    label = { Text(cName) },
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = messageTitle,
                            onValueChange = { messageTitle = it },
                            label = { Text("عنوان الرسالة أو الإشعار") },
                            modifier = Modifier.fillMaxWidth().testTag("msg_title_input")
                        )

                        OutlinedTextField(
                            value = messageBody,
                            onValueChange = { messageBody = it },
                            label = { Text("نص الرسالة لأولياء الأمور") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth().testTag("msg_body_input")
                        )

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (messageTitle.isNotBlank() && messageBody.isNotBlank()) {
                                        viewModel.sendMessageToCircle(
                                            circleName = if (selectedCircleTarget == "الكل") null else selectedCircleTarget,
                                            title = messageTitle,
                                            message = messageBody,
                                            category = selectedCategory
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = QuranEmerald),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).testTag("send_app_notif_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("إرسال إشعار بالتطبيق 🔔", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    val fullText = "🌿 *رسالة من حلقات القرآن الكريم* 🌿\n\n📌 *$messageTitle*\n\n$messageBody\n\n━━━━━━━━━━━━━━━\n📲 تطبيق منارة الجيل القرآني"
                                    ShareHelper.shareText(viewModel.getApplication(), fullText, messageTitle)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).testTag("send_whatsapp_btn")
                            ) {
                                Text("مشاركة واتساب 📲", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Notification History Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "سجل الإشعارات والتنبيهات السابقة (${notifications.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = QuranEmerald
                    )
                    if (unreadCount > 0) {
                        TextButton(onClick = { viewModel.markAllNotificationsAsRead() }) {
                            Text("تحديد الكل كمقروء", fontSize = 12.sp)
                        }
                    }
                }
            }

            items(notifications, key = { it.id }) { notif ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!notif.isRead) QuranEmeraldContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.markNotificationAsRead(notif.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (!notif.isRead) QuranEmerald else Color(0xFFBDBDBD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (!notif.isRead) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
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
                            if (notif.studentName.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "الطالب: ${notif.studentName} • التصنيف: ${notif.category}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuranGold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
