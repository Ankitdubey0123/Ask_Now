package com.example.ask_now_a.features.teacher.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ask_now_a.core.components.AppScaffold
import com.example.ask_now_a.core.components.DrawerMenuItem
import com.example.ask_now_a.core.components.NavigationTabItem
import com.example.ask_now_a.features.session.model.SessionModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    name: String,
    email: String,
    sessions: List<SessionModel>,
    onOpenChat: () -> Unit,
    onScheduleClass: () -> Unit,
    onUploadReel: () -> Unit,
    onViewReelsFeed: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onStartSession: (SessionModel) -> Unit,
    onEndSession: (SessionModel) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navTabs = listOf(
        NavigationTabItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationTabItem("Sessions", Icons.Filled.VideoCameraBack, Icons.Outlined.VideoCameraBack),
        NavigationTabItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    val drawerItems = listOf(
        DrawerMenuItem("My Created Sessions", Icons.Default.VideoCall) {},
        DrawerMenuItem("Enrolled Students", Icons.Default.People) {}
    )

    AppScaffold(
        title = "Teacher Studio",
        drawerState = drawerState,
        onOpenChat = onOpenChat,
        selectedTabIndex = selectedTab,
        onTabSelected = { selectedTab = it },
        navTabs = navTabs,
        drawerName = name,
        drawerEmail = email,
        drawerItems = drawerItems,
        onLogout = onLogout
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> TeacherHomeTab(
                    onScheduleClass = onScheduleClass,
                    onUploadReel = onUploadReel,
                    onViewReelsFeed = onViewReelsFeed,
                    onNavigateToAiAssistant = onNavigateToAiAssistant
                )
                1 -> TeacherSessionsTab(
                    sessions = sessions,
                    onStartSession = onStartSession,
                    onEndSession = onEndSession
                )
                2 -> TeacherProfileTab(
                    name = name,
                    email = email
                )
            }
        }
    }
}
