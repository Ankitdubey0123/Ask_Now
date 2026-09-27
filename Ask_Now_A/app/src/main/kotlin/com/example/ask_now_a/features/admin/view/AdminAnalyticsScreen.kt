package com.example.ask_now_a.features.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ask_now_a.core.components.LoadingView
import com.example.ask_now_a.core.components.StatCard
import com.example.ask_now_a.core.theme.*
import com.example.ask_now_a.features.admin.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAnalyticsScreen(
    adminViewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val uiState by adminViewModel.uiState.collectAsState()
    val stats = uiState.stats

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Platform Governance & Analytics", color = TextWhite, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (uiState.isLoading && stats == null) {
            LoadingView("Fetching platform analytics...")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(PrimaryPurple, DeepPurple)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = TextWhite, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("AskNow Platform Governance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextWhite)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Total Registered Users: ${stats?.totalUsers ?: 0}", style = MaterialTheme.typography.bodyMedium, color = TextWhite.copy(alpha = 0.8f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("User Analytics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Students", "${stats?.totalStudents ?: 0}", Icons.Default.School, SecondaryCyan)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Teachers", "${stats?.totalTeachers ?: 0}", Icons.Default.AssignmentInd, AccentAmber)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Live Classes Analytics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Live Classes", "${stats?.totalLiveSessions ?: 0}", Icons.Default.LiveTv, AccentRose)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Completed", "${stats?.totalCompletedSessions ?: 0}", Icons.Default.CheckCircleOutline, AccentEmerald)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Teacher Reels Analytics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Total Reels", "${stats?.totalReels ?: 0}", Icons.Default.VideoLibrary, PrimaryPurple)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Total Views", "${stats?.totalReelViews ?: 0}", Icons.Default.RemoveRedEye, SecondaryCyan)
                    }
                }
            }
        }
    }
}
