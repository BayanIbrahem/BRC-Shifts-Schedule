package com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.BonusType
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.action.BonusLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.action.BonusNavActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.state.BonusUiState
import com.dev_bayan_ibrahim.brc_shifting.util.formatDate
import com.dev_bayan_ibrahim.brc_shifting.util.formatDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonusScreen(
    uiState: BonusUiState,
    bonusList: List<Bonus>,
    bonusTypes: Map<BonusType, Pair<Int, Int>>,
    logicActions: BonusLogicActions,
    navActions: BonusNavActions,
    modifier: Modifier = Modifier,
) {
    if (uiState.employee == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            //TODO, string res
            Text("Invalid Employee")
        }
    } else {
        val pullToRefreshState = rememberPullToRefreshState()
        val selectedBonusesCount by remember(uiState.selectedBonus) {
            derivedStateOf {
                uiState.selectedBonus.count()
            }
        }

        val allBonusCount by remember(bonusList) {
            derivedStateOf {
                bonusList.count()
            }
        }
        Scaffold(
            modifier = Modifier.pullToRefresh(
                isRefreshing = uiState.isLoading,
                state = pullToRefreshState,
                onRefresh = { logicActions.onFetch() }
            ),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = if (uiState.isSelectionMode) {
                                    // TODO, string res
                                    "Selected bonuses $selectedBonusesCount of $allBonusCount"
                                } else {
                                    // TODO, string res
                                    "Bonuses - ${uiState.employee.name}"
                                }
                            )
                            bonusList.firstOrNull()?.updatedAt?.let {
                                Text(
                                    "Last Update - ${it.formatDateTime()}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navActions.onPop()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        var showMoreMenu by remember {
                            mutableStateOf(false)
                        }
                        var showDeleteAllConfirmDialog by remember {
                            mutableStateOf(false)
                        }
                        if (uiState.isSelectionMode) {
                            IconButton(onClick = {
                                logicActions.onCancelSelectBonus()
                            }) {
                                Icon(Icons.Default.Close, null)
                            }
                        }
                        IconButton(
                            onClick = {
                                showMoreMenu = true
                            }
                        ) {
                            Icon(Icons.Default.MoreVert, null)
                        }
                        DropdownMenu(
                            expanded = showMoreMenu,
                            onDismissRequest = { showMoreMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    // TODO, string res
                                    Text(if (uiState.isSelectionMode) "Delete Selected" else "Delete all")
                                },
                                leadingIcon = { Icon(Icons.Default.Delete, null) },
                                onClick = {
                                    showDeleteAllConfirmDialog = true
                                    showMoreMenu = false
                                }
                            )
                            if (uiState.isSelectionMode && selectedBonusesCount < allBonusCount) {
                                DropdownMenuItem(
                                    text = {
                                        // TODO, string res
                                        Text("Select all")
                                    },
                                    leadingIcon = { Icon(Icons.Default.Check, null) },
                                    onClick = {
                                        showMoreMenu = false
                                        logicActions.onSetSelectedBonuses(bonusList)
                                    }
                                )
                            }
                        }
                        if (showDeleteAllConfirmDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    showDeleteAllConfirmDialog = false
                                },
                                title = {
                                    Text(
                                        // TODO, string res
                                        if (uiState.isSelectionMode) "Are you sure you want to delete selected bonus (x${selectedBonusesCount})?" else "Are you sure you want to delete all bonuses?",
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            if (uiState.isSelectionMode) {
                                                logicActions.onDeleteSelectedBonuses()
                                            } else {
                                                logicActions.onDeleteAllBonuses()
                                            }
                                            showDeleteAllConfirmDialog = false
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        // TODO, string res
                                        Text("Delete")
                                    }
                                },
                                text = {
                                    Text(
                                        // TODO, string res
                                        "This action can not be undone",
                                    )
                                }
                            )
                        }
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    bonusTypes.forEach { entry ->
                        item(
                            key = entry.key
                        ) {
                            val selected by remember(entry, uiState.selectedBonusTypes) {
                                derivedStateOf {
                                    entry.key in uiState.selectedBonusTypes
                                }
                            }
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    logicActions.onToggleSelectBonusType(entry.key)
                                },
                                label = {
                                    Column(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                    ) {
                                        Text(stringResource(entry.key.nameRes) + " x${entry.value.first}")
                                        Text("Total: ${entry.value.second}")
                                    }
                                }
                            )
                        }
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(200.dp),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    itemsIndexed(
                        items = bonusList,
                        key = { _, it -> it.id!! },
                    ) { i, bonus ->
                        val isSelected by remember(uiState.selectedBonus, bonus.id) {
                            derivedStateOf {
                                bonus.id in uiState.selectedBonus
                            }
                        }
                        BonusCard(
                            modifier = Modifier.animateItem(),
                            bonus = bonus,
                            isSelected = isSelected,
                            isSelectionMode = uiState.isSelectionMode,
                            onDelete = {
                                logicActions.onDeleteBonus(bonus)
                            },
                            onToggleSelect = {
                                logicActions.onToggleSelectBonus(bonus)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BonusCard(
    bonus: Bonus,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onDelete: () -> Unit,
    onToggleSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropdown by remember {
        mutableStateOf(false)
    }

    val containerColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        }
    )
    val contentColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )

    val contentVarianceColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
    ListItem(
        overlineContent = {
            Text(bonus.date.formatDate())
        },
        supportingContent = {
            Text(stringResource(bonus.type.nameRes))
        },
        headlineContent = {
            Text(buildAnnotatedString {
                // net (total - tax)
                append(bonus.net.toString())

                append(" (")

                pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
                append(bonus.total.toString())

                pop()

                append(" - ")

                pushStyle(SpanStyle(color = MaterialTheme.colorScheme.error))
                append(bonus.tax.toString())

                pop()

                append(" )")
            })
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Default.Delete, null)
                    },
                    text = {
                        // TODO, string res
                        Text("Delete")
                    },
                    onClick = {
                        showDropdown = false
                        onDelete()
                    }
                )

            }
        },
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(onLongClick = onToggleSelect) {
                showDropdown = true
                if (isSelectionMode) {
                    onToggleSelect()
                }
            },

        colors = ListItemDefaults.colors(
            containerColor = containerColor,
            leadingIconColor = contentColor,
            headlineColor = contentColor,
            overlineColor = contentVarianceColor,
            supportingColor = contentVarianceColor,
        ),
    )

}
