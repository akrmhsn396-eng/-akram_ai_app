package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceStatus
import com.example.data.model.Gender
import com.example.data.model.RecitationRating
import com.example.ui.theme.*

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("stat_card_${title}"),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(color.copy(alpha = 0.3f)))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun GenderBadge(gender: Gender, modifier: Modifier = Modifier) {
    val isMale = gender == Gender.MALE
    val bg = if (isMale) Color(0xFFE3F2FD) else Color(0xFFFCE4EC)
    val fg = if (isMale) Color(0xFF1976D2) else Color(0xFFC2185B)
    val text = if (isMale) "بنين 👦" else "بنات 👧"

    Surface(
        modifier = modifier,
        color = bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AttendanceBadge(status: AttendanceStatus?, modifier: Modifier = Modifier) {
    val (bg, fg, label) = when (status) {
        AttendanceStatus.PRESENT -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "حاضر ✅")
        AttendanceStatus.LATE -> Triple(Color(0xFFFFF3E0), Color(0xFFEF6C00), "متأخر ⏰")
        AttendanceStatus.EXCUSED -> Triple(Color(0xFFE1F5FE), Color(0xFF0288D1), "مستأذن ℹ️")
        AttendanceStatus.ABSENT -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "غائب ❌")
        null -> Triple(Color(0xFFF5F5F5), Color(0xFF757575), "لم يرصد ▫️")
    }

    Surface(
        modifier = modifier,
        color = bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun RatingBadge(rating: RecitationRating, modifier: Modifier = Modifier) {
    val color = when (rating) {
        RecitationRating.EXCELLENT -> Color(0xFF2E7D32)
        RecitationRating.VERY_GOOD -> Color(0xFF1976D2)
        RecitationRating.GOOD -> Color(0xFFF57C00)
        RecitationRating.ACCEPTABLE -> Color(0xFF6D4C41)
        RecitationRating.NEEDS_RETRY -> Color(0xFFD32F2F)
    }

    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = rating.titleAr,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun ScoreProgressBar(
    score: Int,
    maxScore: Int = 10,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$score / $maxScore",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = QuranEmerald
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = QuranEmerald,
            trackColor = QuranEmeraldContainer.copy(alpha = 0.5f),
        )
    }
}
