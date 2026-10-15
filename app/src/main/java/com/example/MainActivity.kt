package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppTopBar
import com.example.ui.screens.attendance.QuickAttendanceScreen
import com.example.ui.screens.developer.DeveloperScreen
import com.example.ui.screens.girls.GirlsAdminScreen
import com.example.ui.screens.halaqat.HalaqatScreen
import com.example.ui.screens.messages.MessagesScreen
import com.example.ui.screens.parent.ParentScreen
import com.example.ui.screens.student.StudentScreen
import com.example.ui.screens.teacher.TeacherScreen
import com.example.ui.screens.tools.ToolsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.NotificationHelper
import com.example.viewmodel.AppRole
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.initNotificationChannel(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                QuranCirclesApp()
            }
        }
    }
}

@Composable
fun QuranCirclesApp(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentRole by viewModel.currentRole.collectAsState()
    val unreadNotifCount by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Request notification permission on Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Permission result handled */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Snackbar listener
    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // BackHandler: If in sub-roles, back button returns to Teacher view
    if (currentRole != AppRole.TEACHER) {
        BackHandler {
            viewModel.setRole(AppRole.TEACHER)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                currentRole = currentRole,
                onRoleSelected = { viewModel.setRole(it) },
                unreadCount = unreadNotifCount,
                notifications = notifications,
                onNotificationClick = { viewModel.markNotificationAsRead(it) },
                onMarkAllRead = { viewModel.markAllNotificationsAsRead() },
                onShareAppClick = { viewModel.shareAppInvitation() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(
                targetState = currentRole,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "role_screen_transition"
            ) { role ->
                when (role) {
                    AppRole.TEACHER -> TeacherScreen(viewModel = viewModel)
                    AppRole.HALAQAT -> HalaqatScreen(viewModel = viewModel)
                    AppRole.GIRLS_ADMIN -> GirlsAdminScreen(viewModel = viewModel)
                    AppRole.QUICK_ATTENDANCE -> QuickAttendanceScreen(viewModel = viewModel)
                    AppRole.PARENT -> ParentScreen(viewModel = viewModel)
                    AppRole.STUDENT -> StudentScreen(viewModel = viewModel)
                    AppRole.MESSAGES -> MessagesScreen(viewModel = viewModel)
                    AppRole.TOOLS -> ToolsScreen(viewModel = viewModel)
                    AppRole.DEVELOPER -> DeveloperScreen(viewModel = viewModel)
                }
            }
        }
    }
}
