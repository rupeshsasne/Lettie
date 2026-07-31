package com.radix2.llm.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.radix2.llm.ui.adaptive.LettieDestination
import com.radix2.llm.ui.adaptive.LettieScaffold

/**
 * Back-compat wrapper around [LettieScaffold] for nested screens with a top app bar.
 */
@Composable
fun AppScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    LettieScaffold(
        title = title,
        onBack = onBack,
        selected = null,
        onDestinationSelected = null,
        showNav = false,
        actions = actions,
        floatingActionButton = floatingActionButton,
        content = content,
    )
}

@Composable
fun MainScaffold(
    selected: LettieDestination,
    onDestinationSelected: (LettieDestination) -> Unit,
    title: String? = null,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    LettieScaffold(
        title = title,
        selected = selected,
        onDestinationSelected = onDestinationSelected,
        showNav = true,
        floatingActionButton = floatingActionButton,
        content = content,
    )
}
