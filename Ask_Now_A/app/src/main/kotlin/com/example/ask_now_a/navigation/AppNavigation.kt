package com.example.ask_now_a.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ask_now_a.features.admin.view.AdminAnalyticsScreen
import com.example.ask_now_a.features.admin.view.AdminDashboardScreen
import com.example.ask_now_a.features.admin.viewmodel.AdminViewModel
import com.example.ask_now_a.features.ai_tutor.view.AiTutorScreen
import com.example.ask_now_a.features.ai_tutor.viewmodel.AiViewModel
import com.example.ask_now_a.features.auth.view.AuthScreen
import com.example.ask_now_a.features.auth.viewmodel.AuthViewModel
import com.example.ask_now_a.features.chat.model.UserModel
import com.example.ask_now_a.features.chat.view.ChatListScreen
import com.example.ask_now_a.features.chat.view.ChatRequestScreen
import com.example.ask_now_a.features.chat.view.ChatScreen
import com.example.ask_now_a.features.chat.viewmodel.ChatViewModel
import com.example.ask_now_a.features.reels.view.ReelsFeedScreen
import com.example.ask_now_a.features.reels.view.UploadReelScreen
import com.example.ask_now_a.features.reels.viewmodel.ReelViewModel
import com.example.ask_now_a.features.session.model.SessionModel
import com.example.ask_now_a.features.session.view.CreateSessionScreen
import com.example.ask_now_a.features.session.view.LiveSessionCallScreen
import com.example.ask_now_a.features.session.viewmodel.SessionViewModel
import com.example.ask_now_a.features.splash.SplashScreen
import com.example.ask_now_a.features.student.view.StudentDashboardScreen
import com.example.ask_now_a.features.teacher.view.TeacherDashboardScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel(),
    aiViewModel: AiViewModel = viewModel(),
    reelViewModel: ReelViewModel = viewModel(),
    sessionViewModel: SessionViewModel = viewModel(),
    chatViewModel: ChatViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val sessionState by sessionViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                isLoggedIn = authState.isLoggedIn,
                role = authState.role,
                onNavigateNext = { route ->
                    navController.navigate(route) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("auth") {
            AuthScreen(
                authViewModel = authViewModel,
                onAuthSuccess = { route ->
                    navController.navigate(route) {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("student_dashboard") {
            StudentDashboardScreen(
                name = authState.name ?: "Student",
                email = authState.email ?: "student@email.com",
                sessions = sessionState.liveSessions,
                onOpenChat = { navController.navigate("chat_list") },
                onNavigateToAiTutor = { navController.navigate("ai_tutor") },
                onNavigateToReels = { navController.navigate("reels_feed") },
                onNavigateToLiveClass = { session ->
                    navController.navigate("live_call/${session.title}")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth") { popUpTo(0) }
                }
            )
        }

        composable("teacher_dashboard") {
            TeacherDashboardScreen(
                name = authState.name ?: "Teacher",
                email = authState.email ?: "teacher@email.com",
                sessions = sessionState.myCreatedSessions,
                onOpenChat = { navController.navigate("chat_list") },
                onScheduleClass = { navController.navigate("create_session") },
                onUploadReel = { navController.navigate("upload_reel") },
                onViewReelsFeed = { navController.navigate("reels_feed") },
                onNavigateToAiAssistant = { navController.navigate("ai_tutor") },
                onStartSession = { session ->
                    sessionViewModel.startSession(session.id)
                    navController.navigate("live_call/${session.title}")
                },
                onEndSession = { session ->
                    sessionViewModel.endSession(session.id)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth") { popUpTo(0) }
                }
            )
        }

        composable("admin_dashboard") {
            AdminDashboardScreen(
                adminViewModel = adminViewModel,
                name = authState.name ?: "Admin Governance",
                email = authState.email ?: "admin@email.com",
                onOpenChat = { navController.navigate("chat_list") },
                onOpenAnalytics = { navController.navigate("admin_analytics") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth") { popUpTo(0) }
                }
            )
        }

        composable("admin_analytics") {
            AdminAnalyticsScreen(
                adminViewModel = adminViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("ai_tutor") {
            AiTutorScreen(
                aiViewModel = aiViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("reels_feed") {
            ReelsFeedScreen(
                reelViewModel = reelViewModel,
                isTeacher = authState.role?.uppercase() == "TEACHER",
                onNavigateUpload = { navController.navigate("upload_reel") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("upload_reel") {
            UploadReelScreen(
                reelViewModel = reelViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("create_session") {
            CreateSessionScreen(
                sessionViewModel = sessionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("live_call/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Live Class Session"
            LiveSessionCallScreen(
                sessionTitle = title,
                isTeacher = authState.role?.uppercase() == "TEACHER",
                onEndCall = { navController.popBackStack() }
            )
        }

        composable("chat_list") {
            ChatListScreen(
                chatViewModel = chatViewModel,
                userId = authState.userId ?: 0,
                onNavigateRequests = { navController.navigate("chat_requests") },
                onOpenChat = { user ->
                    navController.navigate("chat_screen/${user.id}/${user.name}")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("chat_requests") {
            ChatRequestScreen(
                chatViewModel = chatViewModel,
                myUserId = authState.userId ?: 0,
                onBack = { navController.popBackStack() }
            )
        }

        composable("chat_screen/{recipientId}/{recipientName}") { backStackEntry ->
            val recipientId = backStackEntry.arguments?.getString("recipientId") ?: "0"
            val recipientName = backStackEntry.arguments?.getString("recipientName") ?: "User"
            ChatScreen(
                chatViewModel = chatViewModel,
                recipientId = recipientId,
                recipientName = recipientName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
