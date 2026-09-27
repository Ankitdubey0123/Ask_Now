package com.example.ask_now_a.features.teacher.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ask_now_a.core.theme.*

@Composable
fun TeacherProfileTab(
    name: String,
    email: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(DeepPurple, AccentRose)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = "Teacher Profile", tint = TextWhite, modifier = Modifier.size(54.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = name.ifEmpty { "Teacher Instructor" },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = email.ifEmpty { "teacher@email.com" },
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardSurface)
                .border(1.dp, BorderSlate, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = "Role", tint = PrimaryPurple)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Role: Verified Educator / Teacher", style = MaterialTheme.typography.bodyLarge, color = TextWhite)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = "Email", tint = SecondaryCyan)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(email.ifEmpty { "teacher@email.com" }, style = MaterialTheme.typography.bodyLarge, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = AccentRose),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = TextWhite)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = TextWhite, fontWeight = FontWeight.Bold)
        }
    }
}
