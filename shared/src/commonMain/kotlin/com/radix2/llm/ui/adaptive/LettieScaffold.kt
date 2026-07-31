package com.radix2.llm.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

/** Primary app destinations shown in NavigationBar / NavigationRail. */
enum class LettieDestination(
    val label: String,
    val emoji: String,
) {
    Play("Play", "\uD83C\uDFAE"),
    Words("Words", "\uD83D\uDCDA"),
    Learn("Learn", "\uD83C\uDCCF"),
    Me("Me", "\uD83D\uDCCA"),
}

/**
 * M3 scaffold: content is always laid out in the Scaffold slot so
 * [PaddingValues] account for top bar + bottom NavigationBar correctly.
 * Landscape / wide uses a NavigationRail beside content; scaffold insets
 * wrap the rail+content row once (no double padding).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LettieScaffold(
    title: String? = null,
    onBack: (() -> Unit)? = null,
    selected: LettieDestination? = null,
    onDestinationSelected: ((LettieDestination) -> Unit)? = null,
    showNav: Boolean = selected != null && onDestinationSelected != null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    /** Optional fully custom top bar (e.g. LargeTopAppBar). When set, [title] is ignored. */
    topBar: (@Composable () -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val window = LocalWindowSize.current
    val useRail = showNav && window.useNavigationRail
    // Labels + 4 destinations overflow phone landscape height — icons only.
    val iconOnlyRail = window.isLandscape ||
        window.heightSizeClass == LettieHeightSizeClass.Compact

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            when {
                topBar != null -> topBar()
                title != null -> {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        },
                        navigationIcon = {
                            if (onBack != null) {
                                IconButton(onClick = onBack) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_arrow_back),
                                        contentDescription = "Back",
                                    )
                                }
                            }
                        },
                        actions = actions,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        scrollBehavior = scrollBehavior,
                    )
                }
            }
        },
        bottomBar = {
            if (showNav && !useRail && onDestinationSelected != null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = NavigationBarDefaults.Elevation,
                ) {
                    LettieDestination.entries.forEach { dest ->
                        NavigationBarItem(
                            selected = selected == dest,
                            onClick = { onDestinationSelected(dest) },
                            icon = {
                                Text(dest.emoji, style = MaterialTheme.typography.titleMedium)
                            },
                            label = {
                                Text(
                                    dest.label,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            },
                        )
                    }
                }
            }
        },
        floatingActionButton = floatingActionButton,
    ) { padding ->
        if (showNav && useRail && onDestinationSelected != null) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    // Parent Row already applied safeDrawing — don't pad the rail again.
                    windowInsets = WindowInsets(0.dp),
                ) {
                    Spacer(Modifier.weight(1f))
                    LettieDestination.entries.forEach { dest ->
                        NavigationRailItem(
                            selected = selected == dest,
                            onClick = { onDestinationSelected(dest) },
                            icon = {
                                Text(dest.emoji, style = MaterialTheme.typography.titleMedium)
                            },
                            label = if (iconOnlyRail) {
                                null
                            } else {
                                {
                                    Text(
                                        dest.label,
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                }
                            },
                        )
                    }
                    Spacer(Modifier.weight(1f))
                }
                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    content(PaddingValues())
                }
            }
        } else {
            content(padding)
        }
    }
}
