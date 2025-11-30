package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.R
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.actions.EmployeeLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.actions.EmployeeNavActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeFilterState
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeMutateState
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeSortBy
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeSortState
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(
    mutateState: EmployeeMutateState,
    filterState: EmployeeFilterState,
    sortState: EmployeeSortState,
    allEmployees: List<Employee>,
    scheduleOfToday: Map<WorkGroup, Shift>,
    logicActions: EmployeeLogicActions,
    navActions: EmployeeNavActions,
    modifier: Modifier = Modifier,
) {
    val selectedEmployeesCount by remember(mutateState.selectedEmployeesNumbers) {
        derivedStateOf {
            mutateState.selectedEmployeesNumbers.count()
        }
    }

    val allEmployeesCount by remember(allEmployees) {
        derivedStateOf {
            allEmployees.count()
        }
    }
    val gridState = rememberLazyGridState()
    val showUpScrollItems by remember(gridState.lastScrolledForward) {
        derivedStateOf {
            !gridState.lastScrolledForward
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (mutateState.isSelectionMode) {
                            stringResource(R.string.selected_employees_x_of_y, selectedEmployeesCount, allEmployees.count())
                        } else {
                            stringResource(R.string.employees)
                        }
                    )
                },
                actions = {
                    var showMoreMenu by remember {
                        mutableStateOf(false)
                    }
                    var showDeleteAllConfirmDialog by remember {
                        mutableStateOf(false)
                    }
                    if (mutateState.isSelectionMode) {
                        IconButton(onClick = {
                            logicActions.onCancelSelectEmployees()
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
                                Text(if (mutateState.isSelectionMode) stringResource(R.string.delete_selected) else stringResource(R.string.delete_all))
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, null) },
                            onClick = {
                                showDeleteAllConfirmDialog = true
                                showMoreMenu = false
                            }
                        )
                        if (mutateState.isSelectionMode && selectedEmployeesCount < allEmployeesCount) {
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(R.string.select_all))
                                },
                                leadingIcon = { Icon(Icons.Default.Check, null) },
                                onClick = {
                                    showMoreMenu = false
                                    logicActions.onSetSelectedEmployees(allEmployees)
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
                                    if (mutateState.isSelectionMode) stringResource(
                                        R.string.delete_selected_employees_hint,
                                        selectedEmployeesCount
                                    ) else stringResource(
                                        R.string.delete_all_employees_hint
                                    ),
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        if (mutateState.isSelectionMode) {
                                            logicActions.onDeleteSelectedEmployees()
                                        } else {
                                            logicActions.onDeleteAllEmployees()
                                        }
                                        showDeleteAllConfirmDialog = false
                                    },
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text(stringResource(R.string.delete))
                                }
                            },
                            text = {
                                Text(stringResource(R.string.no_undone_hint))
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showUpScrollItems,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            logicActions.onAddRemoteEmployee()
                        }
                    ) {
                        Icon(Icons.Default.Person, null)
                    }
                    FloatingActionButton(
                        onClick = {
                            logicActions.onAddLocalEmployee()
                        }
                    ) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            }
        },
    ) { padding ->
        if (allEmployees.isEmpty()) {
            Text(
                text = stringResource(R.string.no_employees),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showUpScrollItems,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .widthIn(max = 200.dp),
                        value = filterState.query,
                        onValueChange = {
                            logicActions.onUpdateQuery(it)
                        },
                        label = {
                            Text(stringResource(R.string.search))
                        },
                        trailingIcon = {
                            var showSortMenu by remember {
                                mutableStateOf(false)
                            }
                            IconButton(
                                onClick = {
                                    showSortMenu = true

                                }
                            ) {
                                Icon(Icons.Default.MoreVert, null)
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                EmployeeSortBy.entries.forEach {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(it.nameRes)) },
                                        onClick = {
                                            logicActions.onToggleSortBy(it)
                                            showSortMenu = false
                                        },
                                        trailingIcon = if (sortState.sortBy == it) {
                                            { Icon(Icons.Default.Check, null) }
                                        } else null
                                    )
                                }
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(R.string.employee_toggle_sort))
                                    },
                                    onClick = {
                                        logicActions.onToggleSortOrder(!sortState.asc)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(
                            items = WorkGroup.entries,
                            key = WorkGroup::key
                        ) { group ->
                            val selected by remember(filterState.groups) {
                                derivedStateOf {
                                    group in filterState.groups
                                }
                            }
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    logicActions.onToggleSelectFilterGroup(group)
                                },
                                label = { Text(stringResource(group.nameRes)) }
                            )
                        }
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp),
                state = gridState,
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(
                    items = allEmployees,
                    key = Employee::id
                ) { employee ->
                    val isSelected by remember(
                        key1 = employee.employeeNumber,
                        key2 = mutateState.selectedEmployeesNumbers
                    ) {
                        derivedStateOf {
                            employee.employeeNumber in mutateState.selectedEmployeesNumbers
                        }
                    }
                    EmployeeCard(
                        modifier = Modifier.animateItem(),
                        employee = employee,
                        todayShift = scheduleOfToday[employee.group],
                        isSelected = isSelected,
                        isSelectionMode=  mutateState.isSelectionMode,
                        navigateToSalary = {
                            navActions.navigateToSalary(employee)
                        },
                        navigateToBonus = {
                            navActions.navigateToBonus(employee)
                        },
                        navigateToDayOffs = {
                            navActions.navigateToDayOffs(employee)
                        },
                        navigateToDeductions = {
                            navActions.navigateToDeduction(employee)
                        },
                        onUpdate = {
                            logicActions.onLocalUpdateEmployee(employee)
                        },
                        onDelete = {
                            logicActions.onDeleteEmployee(employee)
                        },
                        onToggleSelect = {
                            logicActions.onToggleSelectEmployee(employee)
                        }
                    )
                }
            }
        }
    }
    if (mutateState.showLocalEmployeeDialog) {
        AddLocalEmployeeDialog(
            mutateState = mutateState,
            logicActions = logicActions
        )
    }
    if (mutateState.showRemoteEmployeeDialog) {
        AddRemoteEmployeeDialog(
            mutateState = mutateState,
            logicActions = logicActions
        )
    }

}

@Composable
private fun AddLocalEmployeeDialog(
    mutateState: EmployeeMutateState,
    logicActions: EmployeeLogicActions,
    modifier: Modifier = Modifier,
) {
    var showGroupMenu by remember {
        mutableStateOf(false)
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            logicActions.onDismissAddMutateEmployee()
        },
        title = { Text(stringResource(R.string.add_local_employee)) },
        confirmButton = {
            TextButton(
                onClick = { logicActions.onConfirmAddLocalEmployee() },
                enabled = !mutateState.isLoading && mutateState.validLocalNewEmployee
            ) {
                Text(if (mutateState.isAdd) stringResource(R.string.add) else stringResource(R.string.edit))
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = mutateState.employeeName ?: "",
                    onValueChange = { name -> logicActions.onEmployeeNameChange(name) },
                    label = {
                        Text(stringResource(R.string.employee_name))
                    }
                )
                OutlinedTextField(
                    value = mutateState.employeeNumber?.toString() ?: "",
                    enabled = mutateState.isAdd,
                    onValueChange = {
                        it.toIntOrNull()?.let { number -> logicActions.onEmployeeNumberChange(number) }
                    },
                    label = {
                        Text(stringResource(R.string.employee_number))
                    },
                )
                InputChip(
                    selected = false,
                    onClick = {
                        showGroupMenu = true
                    },
                    label = {
                        val alpha by animateFloatAsState(if (mutateState.employeeGroup == null) 0.38f else 1f)
                        Text(
                            text = mutateState.employeeGroup?.let { stringResource(it.nameRes) } ?: stringResource(R.string.select_group),
                            modifier = Modifier.graphicsLayer { this.alpha = alpha },
                        )
                        DropdownMenu(
                            expanded = showGroupMenu,
                            onDismissRequest = { showGroupMenu = false }
                        ) {
                            WorkGroup.entries.forEach { g ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(g.nameRes)) },
                                    onClick = {
                                        logicActions.onEmployeeGroupChange(g)
                                        showGroupMenu = false
                                    }
                                )

                            }
                        }
                    },
                )
            }
        }
    )
}

@Composable
private fun AddRemoteEmployeeDialog(
    mutateState: EmployeeMutateState,
    logicActions: EmployeeLogicActions,
    modifier: Modifier = Modifier,
) {
    var showGroupMenu by remember {
        mutableStateOf(false)
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            logicActions.onDismissAddMutateEmployee()
        },
        title = { Text(stringResource(R.string.add_remote_employee)) },
        confirmButton = {
            TextButton(
                onClick = {
                    logicActions.onConfirmAddRemoteEmployee()
                },
                enabled = !mutateState.isLoading && mutateState.validRemoteNewEmployee
            ) {
                Text(stringResource(R.string.add))
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = mutateState.employeeNumber?.toString() ?: "",
                    onValueChange = {
                        it.toIntOrNull()?.let { number -> logicActions.onEmployeeNumberChange(number) }
                    },
                    label = {
                        Text(stringResource(R.string.employee_number))
                    },
                )
                OutlinedTextField(
                    value = mutateState.employeePassword ?: "",
                    onValueChange = {
                        logicActions.onEmployeePasswordChange(it)
                    },
                    label = {
                        Text(stringResource(R.string.employee_password))
                    },
                )
                InputChip(
                    selected = false,
                    onClick = {
                        showGroupMenu = true
                    },
                    label = {
                        val alpha by animateFloatAsState(if (mutateState.employeeGroup == null) 0.38f else 1f)
                        Text(
                            text = mutateState.employeeGroup?.let { stringResource(it.nameRes) } ?: stringResource(R.string.select_group),
                            modifier = Modifier.graphicsLayer { this.alpha = alpha },
                        )
                        DropdownMenu(
                            expanded = showGroupMenu,
                            onDismissRequest = { showGroupMenu = false }
                        ) {
                            WorkGroup.entries.forEach { g ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(g.nameRes)) },
                                    onClick = {
                                        logicActions.onEmployeeGroupChange(g)
                                        showGroupMenu = false
                                    }
                                )

                            }
                        }
                    },
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmployeeCard(
    employee: Employee,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    todayShift: Shift? = null,
    navigateToSalary: () -> Unit,
    navigateToBonus: () -> Unit,
    navigateToDayOffs: () -> Unit,
    navigateToDeductions: () -> Unit,
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    onToggleSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember {
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
        colors = ListItemDefaults.colors(
            containerColor = containerColor,
            leadingIconColor = contentColor,
            headlineColor = contentColor,
            overlineColor = contentVarianceColor,
            supportingColor = contentVarianceColor,
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onLongClick = onToggleSelect
            ) {
                showMenu = true
                if(isSelectionMode) {
                    onToggleSelect()
                }
            },
        headlineContent = {
            Text(employee.name)
        },
        overlineContent = todayShift?.let { shift ->
            {
                Text(stringResource(shift.shiftName))
            }
        },
        leadingContent = {
            Text(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                text = employee.group?.let { stringResource(it.shortcutRes) } ?: "-",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = navigateToSalary,
                    text = {
                        Text(stringResource(R.string.see_salary))
                    },
                    leadingIcon = {
                        // TODO, icon res
                        Icon(Icons.Default.AccountCircle, null)
                    }
                )
                DropdownMenuItem(
                    onClick = navigateToBonus,
                    text = {
                        Text(stringResource(R.string.see_bonus))
                    },
                    leadingIcon = {
                        // TODO, icon res
                        Icon(Icons.Default.AccountCircle, null)
                    }
                )
                DropdownMenuItem(
                    onClick = navigateToDayOffs,
                    text = {
                        Text(stringResource(R.string.see_day_offs))
                    },
                    leadingIcon = {
                        // TODO, icon res
                        Icon(Icons.Default.AccountCircle, null)
                    }
                )
                DropdownMenuItem(
                    onClick = navigateToDeductions,
                    text = {
                        Text(stringResource(R.string.see_deductions))
                    },
                    leadingIcon = {
                        // TODO, icon res
                        Icon(Icons.Default.AccountCircle, null)
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        onUpdate()
                        showMenu = false
                    },
                    colors = MenuDefaults.itemColors(
                        leadingIconColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary
                    ),
                    text = {
                        Text(stringResource(R.string.edit))
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, null)
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        onDelete()
                        showMenu = false
                    },
                    colors = MenuDefaults.itemColors(
                        leadingIconColor = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error
                    ),
                    text = {
                        Text(stringResource(R.string.delete))
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Delete, null)
                    }
                )
            }
        },
        supportingContent = {
            Text(employee.employeeNumber.toString())
        },
    )
}
