package com.example.ask_now_a.features.student.view

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
fun StudentDashboardScreen(
    name: String,
    email: String,
    sessions: List<SessionModel>,
    onOpenChat: () -> Unit,
    onNavigateToAiTutor: () -> Unit,
    onNavigateToReels: () -> Unit,
    onNavigateToLiveClass: (SessionModel) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navTabs = listOf(
        NavigationTabItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationTabItem("Sessions", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary),
        NavigationTabItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    val drawerItems = listOf(
        DrawerMenuItem("Joined Sessions", Icons.Default.VideoLibrary) {},
        DrawerMenuItem("Certificates", Icons.Default.WorkspacePremium) {}
    )

    AppScaffold(
        title = "Student Dashboard",
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
                0 -> StudentHomeTab(
                    onNavigateToAiTutor = onNavigateToAiTutor,
                    onNavigateToReels = onNavigateToReels,
                    onNavigateToLiveClass = {
                        val dummySession = SessionModel(1, "Live Physics Class", "Interactive problem walkthrough", "", "LIVE", "John Doe")
                        onNavigateToLiveClass(dummySession)
                    }
                )
                1 -> StudentSessionsTab(
                    sessions = sessions,
                    onJoinSession = onNavigateToLiveClass
                )
                2 -> StudentProfileTab(
                    name = name,
                    email = email,
                    onLogout = onLogout
                )
            }
        }
    }
}
