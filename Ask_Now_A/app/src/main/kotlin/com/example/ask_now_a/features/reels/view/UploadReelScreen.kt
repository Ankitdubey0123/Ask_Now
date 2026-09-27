package com.example.ask_now_a.features.reels.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.ask_now_a.core.components.TagChip
import com.example.ask_now_a.core.theme.*
import com.example.ask_now_a.features.reels.viewmodel.ReelViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadReelScreen(
    reelViewModel: ReelViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by reelViewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("#ProblemSolving") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedVideoName by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var capturedFile by remember { mutableStateOf<File?>(null) }

    val categoryTags = listOf("#ProblemSolving", "#CourseAd", "#Solution", "#TipsAndTricks")

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && capturedFile != null) {
            selectedVideoUri = Uri.fromFile(capturedFile)
            selectedVideoName = capturedFile?.name ?: "recorded_video.mp4"
            validationError = null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedVideoUri = it
            selectedVideoName = it.lastPathSegment ?: "video.mp4"
            validationError = null
        }
    }

    LaunchedEffect(uiState.uploadSuccess) {
        if (uiState.uploadSuccess) {
            onBack()
        }
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Select Video Source", color = TextWhite, fontWeight = FontWeight.Bold) },
            text = { Text("Record a new video directly using the device camera or choose an existing video from your gallery.", color = TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSourceDialog = false
                        val file = File(context.cacheDir, "reel_camera_${System.currentTimeMillis()}.mp4")
                        capturedFile = file
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                        cameraLauncher.launch(uri)
                    }
                ) {
                    Text("Record (Camera)", color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSourceDialog = false
                        galleryLauncher.launch("video/*")
                    }
                ) {
                    Text("Choose Gallery", color = TextWhite)
                }
            },
            containerColor = CardSurface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publish Teacher Short Video / Ad", color = TextWhite, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Video Selector Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardSurface)
                    .border(1.dp, PrimaryPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .clickable {
                        showSourceDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.VideoCall, contentDescription = "Video", tint = PrimaryPurple, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (selectedVideoName != null) "Video Selected: $selectedVideoName" else "Tap to Record (Camera) or Choose Gallery",
                        color = if (selectedVideoName != null) AccentEmerald else TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Category Tag", style = MaterialTheme.typography.titleMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categoryTags) { tag ->
                    TagChip(
                        text = tag,
                        isSelected = tag == selectedTag,
                        onClick = { selectedTag = tag }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Reel Title (e.g. Physics Quantum Trick)", color = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderSlate,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Short Walkthrough Description & Ad Details", color = TextMuted) },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderSlate,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        validationError = "Please enter a title for the reel"
                    } else if (selectedVideoUri == null) {
                        validationError = "Please select or record a video file"
                    } else {
                        validationError = null
                        val file = uriToFile(context, selectedVideoUri!!)
                        reelViewModel.uploadReel(title, description, selectedTag, file)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp))
                } else {
                    Text("Publish Short Reel", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            validationError?.let { err ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(err, color = AccentRose, style = MaterialTheme.typography.bodyMedium)
            }

            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(error, color = AccentRose, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun uriToFile(context: Context, uri: Uri): File {
    val file = File(context.cacheDir, "upload_reel_${System.currentTimeMillis()}.mp4")
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return file
}
