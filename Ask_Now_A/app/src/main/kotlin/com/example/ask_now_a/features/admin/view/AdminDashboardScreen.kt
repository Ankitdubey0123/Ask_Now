package com.example.ask_now_a.features.admin.view

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
import com.example.ask_now_a.features.admin.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel,
    name: String,
    email: String,
    onOpenChat: () -> Unit,
    onOpenAnalytics: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val uiState by adminViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navTabs = listOf(
        NavigationTabItem("Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
        NavigationTabItem("Users", Icons.Filled.People, Icons.Outlined.People),
        NavigationTabItem("Analytics", Icons.Filled.Analytics, Icons.Outlined.Analytics)
    )

    val drawerItems = listOf(
        DrawerMenuItem("Platform Governance", Icons.Default.AdminPanelSettings) {},
        DrawerMenuItem("System Analytics", Icons.Default.Analytics) { onOpenAnalytics() }
    )

    AppScaffold(
        title = "Admin Portal",
        drawerState = drawerState,
        onOpenChat = onOpenChat,
        selectedTabIndex = selectedTab,
        onTabSelected = { selectedTab = it },
        navTabs = navTabs,
        drawerName = name.ifEmpty { "Admin System" },
        drawerEmail = email.ifEmpty { "admin@email.com" },
        drawerItems = drawerItems,
        onLogout = onLogout
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> AdminHomeTab(
                    onOpenAnalytics = onOpenAnalytics,
                    onManageUsers = { selectedTab = 1 }
                )
                1 -> AdminUsersTab(
                    users = uiState.users,
                    onToggleStatus = { adminViewModel.toggleUserStatus(it) },
                    onDeleteUser = { adminViewModel.deleteUser(it) }
                )
                2 -> AdminHomeTab(
                    onOpenAnalytics = onOpenAnalytics,
                    onManageUsers = { selectedTab = 1 }
                )
            }
        }
    }
}
