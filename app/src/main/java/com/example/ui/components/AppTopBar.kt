package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import com.example.data.model.ParentNotification
import com.example.ui.theme.*
import com.example.viewmodel.AppRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentRole: AppRole,
    onRoleSelected: (AppRole) -> Unit,
    unreadCount: Int,
    notifications: List<ParentNotification>,
    onNotificationClick: (Long) -> Unit,
    onMarkAllRead: () -> Unit,
    onShareAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showNotificationsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(QuranEmerald)
            .statusBarsPadding()
    ) {
        // Main Top Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(QuranGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "شعار التطبيق",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "منارة الجيل القرآني",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "متابعة الحفظ والحضور وبوابة ولي الأمر",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuranEmeraldContainer,
                        fontSize = 11.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Share App Link Button
                IconButton(
                    onClick = onShareAppClick,
                    modifier = Modifier.testTag("top_bar_share_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة التطبيق وتثبيته",
                        tint = Color.White
                    )
                }

                // Notification Bell with Badge
                IconButton(
                    onClick = { showNotificationsDialog = true },
                    modifier = Modifier.testTag("top_bar_notifications_btn")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFE53935),
                                    contentColor = Color.White
                                ) {
                                    Text(unreadCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "الإشعارات والتنبيهات",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Horizontal Role Switcher Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppRole.values().forEach { role ->
                val isSelected = currentRole == role
                val icon = when (role) {
                    AppRole.TEACHER -> Icons.Default.School
                    AppRole.HALAQAT -> Icons.Default.Groups
                    AppRole.QUICK_ATTENDANCE -> Icons.Default.FactCheck
                    AppRole.PARENT -> Icons.Default.FamilyRestroom
                    AppRole.STUDENT -> Icons.Default.EmojiEvents
                    AppRole.MESSAGES -> Icons.Default.Chat
                    AppRole.TOOLS -> Icons.Default.Handyman
                    AppRole.DEVELOPER -> Icons.Default.AdminPanelSettings
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { onRoleSelected(role) },
                    label = {
                        Text(
                            text = role.titleAr,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = QuranEmeraldDark.copy(alpha = 0.5f),
                        labelColor = Color.White.copy(alpha = 0.85f),
                        iconColor = QuranGoldLight,
                        selectedContainerColor = QuranGold,
                        selectedLabelColor = QuranDarkBackground,
                        selectedLeadingIconColor = QuranDarkBackground
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("role_chip_${role.name}")
                )
            }
        }
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("إشعارات الحلقات وأولياء الأمور", fontWeight = FontWeight.Bold)
                    if (unreadCount > 0) {
                        TextButton(onClick = onMarkAllRead) {
                            Text("تحديد الكل كمقروء", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    if (notifications.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("لا توجد إشعارات حتى الآن", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        androidx.compose.foundation.lazy.LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(notifications.size) { index ->
                                val notif = notifications[index]
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (notif.isRead) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        else QuranEmeraldContainer.copy(alpha = 0.35f)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onNotificationClick(notif.id) }
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
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("إغلاق")
                }
            }
        )
    }
}
