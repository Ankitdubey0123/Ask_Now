package com.example.ask_now_a.features.session.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ask_now_a.core.theme.*

@Composable
fun LiveSessionCallScreen(
    sessionTitle: String,
    isTeacher: Boolean,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoMuted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F19))
    ) {
        // Main Stream View Container
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Stream", tint = PrimaryPurple, modifier = Modifier.size(54.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isTeacher) "Streaming Live Class to Students..." else "Connected to Teacher's Live Video Stream",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextWhite
                )
            }
        }

        // PIP PIP Preview Box (Bottom Right)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 110.dp)
                .size(110.dp, 150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(CardSurface)
                .border(2.dp, PrimaryPurple, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isVideoMuted) {
                Icon(Icons.Default.VideocamOff, contentDescription = "Cam Off", tint = TextMuted)
            } else {
                Icon(Icons.Default.Person, contentDescription = "Self", tint = PrimaryPurple, modifier = Modifier.size(42.dp))
            }
        }

        // Top Header Bar
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentRose)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("LIVE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = sessionTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.weight(1f)
            )
        }

        // Bottom Controls Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(CardSurface.copy(alpha = 0.9f))
                .padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isMuted = !isMuted }) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = TextWhite
                    )
                }

                IconButton(onClick = { isVideoMuted = !isVideoMuted }) {
                    Icon(
                        imageVector = if (isVideoMuted) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Video",
                        tint = TextWhite
                    )
                }

                IconButton(onClick = { /* Switch camera */ }) {
                    Icon(Icons.Default.Cameraswitch, contentDescription = "Camera", tint = TextWhite)
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AccentRose)
                        .clickable { onEndCall() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = TextWhite)
                }
            }
        }
    }
}
