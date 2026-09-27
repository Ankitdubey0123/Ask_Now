package com.example.ask_now_a.features.auth.view

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ask_now_a.core.theme.*
import com.example.ask_now_a.features.auth.viewmodel.AuthUiState
import com.example.ask_now_a.features.auth.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onAuthSuccess: (route: String) -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()

    var isLoginMode by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("STUDENT") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedIn, uiState.role) {
        if (uiState.isLoggedIn && !uiState.role.isNullOrEmpty()) {
            val route = when (uiState.role?.uppercase()) {
                "ADMIN" -> "admin_dashboard"
                "TEACHER" -> "teacher_dashboard"
                else -> "student_dashboard"
            }
            onAuthSuccess(route)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, Color(0xFF131C31), CardSurface)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .border(1.dp, BorderSlate, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isLoginMode) "Welcome Back" else "Create Account",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Text(
                    text = if (isLoginMode) "Sign in to continue learning" else "Join AskNow educational platform",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = !isLoginMode) {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = PrimaryPurple) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = BorderSlate,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedContainerColor = DarkBackground,
                                unfocusedContainerColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Role Selector
                        Text("Select Account Type", style = MaterialTheme.typography.bodyMedium, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { selectedRole = "STUDENT" },
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedRole == "STUDENT") PrimaryPurple else DarkBackground
                                )
                            ) {
                                Text("Student", color = TextWhite, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { selectedRole = "TEACHER" },
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedRole == "TEACHER") PrimaryPurple else DarkBackground
                                )
                            ) {
                                Text("Teacher", color = TextWhite, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (selectedRole == "TEACHER") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Note: Teacher accounts require admin verification and a scheduled orientation interview.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AccentAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = PrimaryPurple) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = BorderSlate,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = DarkBackground,
                        unfocusedContainerColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = PrimaryPurple) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility",
                                tint = TextMuted
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = BorderSlate,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = DarkBackground,
                        unfocusedContainerColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isLoginMode) {
                            authViewModel.login(email, password)
                        } else {
                            authViewModel.register(name, email, password, selectedRole)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = if (isLoginMode) "Login" else "Register",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    isLoginMode = !isLoginMode
                }) {
                    Text(
                        text = if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login",
                        color = SecondaryCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                uiState.error?.let { errorMsg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMsg,
                        color = AccentRose,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
