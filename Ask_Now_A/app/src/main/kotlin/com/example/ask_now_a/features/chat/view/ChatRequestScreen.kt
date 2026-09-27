package com.example.ask_now_a.features.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ask_now_a.core.theme.*
import com.example.ask_now_a.features.chat.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRequestScreen(
    chatViewModel: ChatViewModel,
    myUserId: Int,
    onBack: () -> Unit
) {
    val uiState by chatViewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(myUserId) {
        chatViewModel.loadRequests(myUserId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Requests & Search", color = TextWhite, fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search users by name or email...", color = TextMuted) },
                trailingIcon = {
                    IconButton(onClick = { chatViewModel.searchUsers(searchQuery) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryPurple)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderSlate,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Results Section
            if (uiState.searchResults.isNotEmpty()) {
                Text("Search Results", style = MaterialTheme.typography.titleMedium, color = TextWhite, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                uiState.searchResults.forEach { user ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CardSurface)
                            .border(1.dp, BorderSlate, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = "User", tint = PrimaryPurple)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(user.name, fontWeight = FontWeight.Bold, color = TextWhite)
                                Text(user.email, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                            }
                            Button(
                                onClick = { chatViewModel.sendRequest(myUserId, user.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Send", color = TextWhite)
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderSlate)
            }

            // Incoming Requests Section
            Text("Incoming Connection Requests", style = MaterialTheme.typography.titleMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))

            if (uiState.incomingRequests.isEmpty()) {
                Text("No pending connection requests", color = TextMuted)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.incomingRequests) { req ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(CardSurface)
                                .border(1.dp, BorderSlate, RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(38.dp).clip(CircleShape).background(PrimaryPurple.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = "Req", tint = PrimaryPurple)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(req.senderName, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text("Sent you a chat request", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                                }
                                IconButton(onClick = { chatViewModel.acceptRequest(req.id, myUserId) }) {
                                    Icon(Icons.Default.Check, contentDescription = "Accept", tint = AccentEmerald)
                                }
                                IconButton(onClick = { chatViewModel.rejectRequest(req.id, myUserId) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Reject", tint = AccentRose)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
