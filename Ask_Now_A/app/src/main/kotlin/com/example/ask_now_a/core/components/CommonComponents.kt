package com.example.ask_now_a.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ask_now_a.core.theme.*
import kotlinx.coroutines.launch

data class NavigationTabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

data class DrawerMenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    drawerState: DrawerState,
    onOpenChat: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    navTabs: List<NavigationTabItem>,
    drawerName: String,
    drawerEmail: String,
    drawerItems: List<DrawerMenuItem>,
    onLogout: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CardSurface,
                drawerContentColor = TextWhite
            ) {
                AppDrawer(
                    name = drawerName,
                    email = drawerEmail,
                    items = drawerItems,
                    onLogout = onLogout
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    },
                    navigationIcon = {
                        val scope = rememberCoroutineScope()
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextWhite)
                        }
                    },
                    actions = {
                        IconButton(onClick = onOpenChat) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = AccentPink) {
                                        Text("•", color = TextWhite)
                                    }
                                }
                            ) {
                                Icon(Icons.Outlined.Chat, contentDescription = "Chats", tint = TextWhite)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkBackground
                    )
                )
            },
            bottomBar = {
                if (navTabs.isNotEmpty()) {
                    NavigationBar(
                        containerColor = CardSurface,
                        tonalElevation = 8.dp
                    ) {
                        navTabs.forEachIndexed { index, tab ->
                            val selected = index == selectedTabIndex
                            NavigationBarItem(
                                selected = selected,
                                onClick = { onTabSelected(index) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title,
                                        tint = if (selected) PrimaryPurple else TextMuted
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        color = if (selected) PrimaryPurple else TextMuted,
                                        fontSize = 12.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = PrimaryPurple.copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
            },
            containerColor = DarkBackground
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}

@Composable
fun AppDrawer(
    name: String,
    email: String,
    items: List<DrawerMenuItem>,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(CardSurface)
            .padding(20.dp)
    ) {
        // Drawer Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (name.isNotBlank()) name.take(1).uppercase() else "U",
                    color = TextWhite,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(color = BorderSlate, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Drawer Menu Items
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.title, color = TextWhite, fontWeight = FontWeight.Medium) },
                selected = false,
                onClick = item.onClick,
                icon = { Icon(item.icon, contentDescription = item.title, tint = SecondaryCyan) },
                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(color = BorderSlate, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        NavigationDrawerItem(
            label = { Text("Logout", color = AccentRose, fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = onLogout,
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = AccentRose) },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
        )
    }
}

@Composable
fun LoadingView(message: String = "Loading...") {
    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = PrimaryPurple)
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurface)
            .border(1.dp, BorderSlate, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(2.dp))
                Text(title, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            }
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, icon: ImageVector, accentColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardSurface)
            .border(1.dp, BorderSlate, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = TextMuted)
        }
    }
}

@Composable
fun GradientBanner(title: String, subtitle: String, icon: ImageVector, gradientColors: List<Color>, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(colors = gradientColors))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = TextWhite, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextWhite.copy(alpha = 0.9f))
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Open", tint = TextWhite)
        }
    }
}

@Composable
fun TagChip(text: String, isSelected: Boolean, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryPurple else CardSurface)
            .border(1.dp, if (isSelected) PrimaryPurple else BorderSlate, RoundedCornerShape(20.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) TextWhite else TextMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}
