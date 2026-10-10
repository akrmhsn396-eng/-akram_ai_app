package com.example.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Gender
import com.example.data.model.QuranData
import com.example.data.model.Surah
import com.example.ui.components.RatingBadge
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@Composable
fun StudentScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedStudent by viewModel.selectedStudent.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val allRecitations by viewModel.allRecitations.collectAsState()

    val currentStudent = selectedStudent ?: allStudents.firstOrNull()

    val recitations = remember(allRecitations, currentStudent) {
        if (currentStudent != null) allRecitations.filter { it.studentId == currentStudent.id } else emptyList()
    }

    var selectedTab by remember { mutableStateOf(0) } // 0: الإنجاز والأوسمة, 1: خريطة الختمة, 2: فهرس القرآن

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        if (currentStudent != null) {
            // Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranEmerald),
                    modifier = Modifier.fillMaxWidth().testTag("student_hero_card")
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(QuranGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentStudent.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${currentStudent.genderLabel} • ${currentStudent.circleName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = QuranEmeraldContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = QuranGold,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = currentStudent.levelRank,
                                color = QuranDarkBackground,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${currentStudent.totalPoints}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranGoldLight
                                )
                                Text(
                                    text = "مجموع النقاط",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(36.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${currentStudent.streakDays} أيام",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFB74D)
                                )
                                Text(
                                    text = "مواظبة متواصلة 🔥",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(36.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "الجزء ${currentStudent.currentJuz}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranEmeraldContainer
                                )
                                Text(
                                    text = "الجزء الحالي",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            // Tabs Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = QuranEmerald
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("سجل التسميع", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.HistoryEdu, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("خريطة الختمة", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("فهرس القرآن", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }

            // Tab 0: Recitation History & Badges
            if (selectedTab == 0) {
                // Badges Banner
                item {
                    Text(
                        text = "الأوسمة والشهادات التقديرية 🏆",
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
                        BadgeItem(
                            title = "حافظ الأسبوع",
                            icon = Icons.Default.MilitaryTech,
                            color = QuranGold,
                            isUnlocked = currentStudent.totalPoints >= 150,
                            modifier = Modifier.weight(1f)
                        )
                        BadgeItem(
                            title = "نجم التجويد",
                            icon = Icons.Default.RecordVoiceOver,
                            color = QuranEmerald,
                            isUnlocked = currentStudent.totalPoints >= 250,
                            modifier = Modifier.weight(1f)
                        )
                        BadgeItem(
                            title = "المواظب الذهبي",
                            icon = Icons.Default.Verified,
                            color = Color(0xFF1976D2),
                            isUnlocked = currentStudent.streakDays >= 7,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Text(
                        text = "سجل التسميعات الأخيرة (${recitations.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = QuranEmerald
                    )
                }

                if (recitations.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "لا توجد تسميعات مسجلة حتى الآن",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(recitations) { rec ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "سورة ${rec.surahName} (الآيات ${rec.fromAyah} - ${rec.toAyah})",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    RatingBadge(rating = rec.rating)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${rec.type.titleAr} • التجويد: ${rec.tajweedScore}/10",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = rec.dateStr,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (rec.teacherNotes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "💬 ملاحظة: ${rec.teacherNotes}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = QuranEmerald
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Tab 1: 30 Juz Khatmah Grid
            if (selectedTab == 1) {
                item {
                    Text(
                        text = "خريطة الأجزاء القرآنية الثلاثين 🗺️",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = QuranEmerald
                    )
                    Text(
                        text = "تتبع مسيرتك في حفظ ومراجعة القرآن الكريم جزءاً بعد جزء",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // 30 Juz interactive grid simulation
                            val completedJuzCount = (31 - currentStudent.currentJuz).coerceIn(0, 30)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("الأجزاء المحفوظة: $completedJuzCount جزء", fontWeight = FontWeight.Bold, color = QuranEmerald)
                                Text("المتبقي: ${30 - completedJuzCount} جزء", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            // 5 columns grid of 30 juz chips
                            for (row in 0 until 6) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    for (col in 1..5) {
                                        val juzNum = row * 5 + col
                                        val isCompleted = juzNum <= completedJuzCount
                                        val isCurrent = juzNum == currentStudent.currentJuz

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    when {
                                                        isCurrent -> QuranGold
                                                        isCompleted -> QuranEmerald
                                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$juzNum",
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isCurrent -> QuranDarkBackground
                                                    isCompleted -> Color.White
                                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                },
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }

            // Tab 2: Complete Quran Index
            if (selectedTab == 2) {
                item {
                    Text(
                        text = "فهرس سور القرآن الكريم (114 سورة) 📖",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = QuranEmerald
                    )
                }

                items(QuranData.surahs) { surah ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(QuranEmeraldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${surah.number}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = QuranEmerald
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "سورة ${surah.name}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "${surah.transliteration} • الجزء ${surah.juzStart}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                color = if (surah.revelationType == "مكية") Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${surah.versesCount} آية • ${surah.revelationType}",
                                    color = if (surah.revelationType == "مكية") Color(0xFFEF6C00) else Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
fun BadgeItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isUnlocked) color else Color.Gray,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) color else Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (isUnlocked) "مكتسب ✨" else "قيد الحفظ 🔒",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = if (isUnlocked) color else Color.Gray
            )
        }
    }
}
