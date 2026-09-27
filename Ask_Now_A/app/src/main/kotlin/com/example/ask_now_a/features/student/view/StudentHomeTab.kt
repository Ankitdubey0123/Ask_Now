package com.example.ask_now_a.features.student.view

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
fun StudentHomeTab(
    onNavigateToAiTutor: () -> Unit,
    onNavigateToReels: () -> Unit,
    onNavigateToLiveClass: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // AI Smart Tutor Banner
        GradientBanner(
            title = "Gemini AI Smart Tutor",
            subtitle = "Snap homework notes or ask doubts 24/7",
            icon = Icons.Default.AutoAwesome,
            gradientColors = listOf(PrimaryPurple, DeepPurple),
            onClick = onNavigateToAiTutor
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Explore Features",
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
                    title = "Short Videos",
                    subtitle = "Problem Walkthroughs & Ads",
                    icon = Icons.Default.VideoLibrary,
                    accentColor = SecondaryCyan,
                    onClick = onNavigateToReels
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                ActionCard(
                    title = "Live Class",
                    subtitle = "Join Live Session",
                    icon = Icons.Default.VideoCall,
                    accentColor = AccentRose,
                    onClick = onNavigateToLiveClass
                )
            }
        }
    }
}
