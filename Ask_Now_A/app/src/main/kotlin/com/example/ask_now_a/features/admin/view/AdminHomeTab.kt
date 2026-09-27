package com.example.ask_now_a.features.admin.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ask_now_a.core.components.ActionCard
import com.example.ask_now_a.core.components.GradientBanner
import com.example.ask_now_a.core.theme.*

@Composable
fun AdminHomeTab(
    onOpenAnalytics: () -> Unit,
    onManageUsers: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Analytics Banner
        GradientBanner(
            title = "Platform Analytics & Controls",
            subtitle = "View total users, live sessions, reels, and views",
            icon = Icons.Default.Analytics,
            gradientColors = listOf(PrimaryPurple, DeepPurple),
            onClick = onOpenAnalytics
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Administrative Controls", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(14.dp))

        ActionCard(
            title = "Governance & User Accounts",
            subtitle = "Manage roles, approve or block users",
            icon = Icons.Default.ManageAccounts,
            accentColor = SecondaryCyan,
            onClick = onManageUsers
        )

        Spacer(modifier = Modifier.height(12.dp))

        ActionCard(
            title = "Session Moderation",
            subtitle = "Audit & force end active live classes",
            icon = Icons.Default.VideoCameraBack,
            accentColor = AccentRose,
            onClick = onOpenAnalytics
        )

        Spacer(modifier = Modifier.height(12.dp))

        ActionCard(
            title = "Reel Content Moderation",
            subtitle = "Audit teacher short problem solving videos & ads",
            icon = Icons.Default.VideoLibrary,
            accentColor = PrimaryPurple,
            onClick = onOpenAnalytics
        )
    }
}
