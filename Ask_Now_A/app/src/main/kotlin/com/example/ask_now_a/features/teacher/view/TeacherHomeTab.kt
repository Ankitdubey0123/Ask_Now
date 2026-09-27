package com.example.ask_now_a.features.teacher.view

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
fun TeacherHomeTab(
    onScheduleClass: () -> Unit,
    onUploadReel: () -> Unit,
    onViewReelsFeed: () -> Unit,
    onNavigateToAiAssistant: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Schedule Class Banner
        GradientBanner(
            title = "Schedule Live Class",
            subtitle = "Start a WebRTC video class with students",
            icon = Icons.Default.VideoCall,
            gradientColors = listOf(DeepPurple, PrimaryPurple),
            onClick = onScheduleClass
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Teacher Studio Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ActionCard(
                    title = "Upload Reel",
                    subtitle = "Problem Walkthrough or Course Ad",
                    icon = Icons.Default.VideoCall,
                    accentColor = PrimaryPurple,
                    onClick = onUploadReel
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                ActionCard(
                    title = "Reels Feed",
                    subtitle = "Browse Teacher Videos",
                    icon = Icons.Default.VideoLibrary,
                    accentColor = SecondaryCyan,
                    onClick = onViewReelsFeed
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        ActionCard(
            title = "Gemini AI Teacher Assistant",
            subtitle = "Generate quiz questions & summary notes automatically",
            icon = Icons.Default.AutoAwesome,
            accentColor = AccentAmber,
            onClick = onNavigateToAiAssistant
        )
    }
}
