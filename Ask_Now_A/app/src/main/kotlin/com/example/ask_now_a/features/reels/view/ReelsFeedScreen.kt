package com.example.ask_now_a.features.reels.view

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.ask_now_a.core.components.TagChip
import com.example.ask_now_a.core.network.ApiEndpoints
import com.example.ask_now_a.core.theme.*
import com.example.ask_now_a.features.reels.model.ReelCommentModel
import com.example.ask_now_a.features.reels.model.ReelModel
import com.example.ask_now_a.features.reels.viewmodel.ReelViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReelsFeedScreen(
    reelViewModel: ReelViewModel,
    isTeacher: Boolean,
    onNavigateUpload: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by reelViewModel.uiState.collectAsState()
    val reels = uiState.reels
    var showCommentsSheet by remember { mutableStateOf(false) }
    var selectedReelForComments by remember { mutableStateOf<ReelModel?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = PrimaryPurple, modifier = Modifier.align(Alignment.Center))
        } else if (reels.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.VideoLibrary, contentDescription = "Reels", tint = TextMuted, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("No problem walkthrough or ad reels available yet", color = TextWhite, fontWeight = FontWeight.Medium)
                if (isTeacher) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateUpload,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload First Reel")
                    }
                }
            }
        } else {
            @OptIn(ExperimentalFoundationApi::class) val pagerState = rememberPagerState(pageCount = { reels.size })

            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ReelPlayerItem(
                    reel = reels[page],
                    onLikeToggle = { reelViewModel.toggleLike(it) },
                    onOpenComments = { reel ->
                        selectedReelForComments = reel
                        reelViewModel.fetchComments(reel.id)
                        showCommentsSheet = true
                    },
                    onViewRegistered = { reelViewModel.registerView(it) }
                )
            }
        }

        // Top Navigation Bar Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }

            Text("Problem Solutions & Ads Feed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)

            if (isTeacher) {
                IconButton(
                    onClick = onNavigateUpload,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PrimaryPurple)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Upload", tint = TextWhite)
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        // Comments Bottom Sheet Modal
        if (showCommentsSheet && selectedReelForComments != null) {
            ModalBottomSheet(
                onDismissRequest = { showCommentsSheet = false },
                containerColor = CardSurface,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                ReelCommentsSheetContent(
                    comments = uiState.activeComments,
                    isLoading = uiState.isCommentsLoading,
                    onPostComment = { text ->
                        reelViewModel.postComment(selectedReelForComments!!.id, text)
                    },
                    onDeleteComment = { commentId ->
                        reelViewModel.deleteComment(selectedReelForComments!!.id, commentId)
                    }
                )
            }
        }
    }
}

@Composable
fun ReelPlayerItem(
    reel: ReelModel,
    onLikeToggle: (Int) -> Unit,
    onOpenComments: (ReelModel) -> Unit,
    onViewRegistered: (Int) -> Unit
) {
    val context = LocalContext.current

    val hostUrl = if (ApiEndpoints.baseUrl.endsWith("/asknow/api")) {
        ApiEndpoints.baseUrl.removeSuffix("/asknow/api")
    } else {
        ApiEndpoints.baseUrl.trimEnd('/')
    }

    val streamUrl = if (reel.videoUrl.startsWith("http")) {
        reel.videoUrl
    } else if (reel.videoUrl.startsWith("/")) {
        "$hostUrl${reel.videoUrl}"
    } else {
        "$hostUrl/${reel.videoUrl}"
    }

    val exoPlayer = remember(streamUrl) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(streamUrl)
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(streamUrl) {
        onViewRegistered(reel.id)
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Shadow Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Bottom Left Information Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 40.dp, end = 90.dp)
        ) {
            TagChip(text = reel.categoryTag.ifEmpty { "#ProblemSolving" }, isSelected = true)

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (reel.teacherName.isNotBlank()) reel.teacherName.take(1).uppercase() else "T",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = reel.teacherName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = reel.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            reel.description?.let { desc ->
                if (desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextWhite.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Right Action Column (Likes, Comments & Views)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { onLikeToggle(reel.id) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = if (reel.likedByCurrentUser) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (reel.likedByCurrentUser) AccentRose else TextWhite,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text("${reel.likesCount}", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(18.dp))

            IconButton(
                onClick = { onOpenComments(reel) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = "Comments",
                    tint = TextWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text("${reel.commentsCount}", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(18.dp))

            Icon(Icons.Default.RemoveRedEye, contentDescription = "Views", tint = TextWhite.copy(alpha = 0.8f), modifier = Modifier.size(28.dp))
            Text("${reel.viewsCount}", color = TextWhite, fontSize = 12.sp)
        }
    }
}

@Composable
fun ReelCommentsSheetContent(
    comments: List<ReelCommentModel>,
    isLoading: Boolean,
    onPostComment: (String) -> Unit,
    onDeleteComment: (Int) -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .padding(16.dp)
    ) {
        Text(
            text = "Comments (${comments.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        HorizontalDivider(color = BorderSlate, thickness = 1.dp)

        if (isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        } else if (comments.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No comments yet. Be the first to start the discussion!", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(comments) { comment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkBackground)
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = comment.userName.take(1).uppercase(),
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(comment.userName, fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(comment.text, color = TextWhite, fontSize = 14.sp)
                        }

                        IconButton(
                            onClick = { onDeleteComment(comment.id) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted)
                        }
                    }
                }
            }
        }

        // Input row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                placeholder = { Text("Add a comment...", color = TextMuted) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderSlate,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = DarkBackground,
                    unfocusedContainerColor = DarkBackground
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (newCommentText.isNotBlank()) {
                        onPostComment(newCommentText)
                        newCommentText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = TextWhite)
            }
        }
    }
}
