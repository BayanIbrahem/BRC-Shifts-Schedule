package com.dev_bayan_ibrahim.brc_shifting.ui.screen.deduction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.R
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Deduction
import com.dev_bayan_ibrahim.brc_shifting.util.formatAsCurrency
import com.dev_bayan_ibrahim.brc_shifting.util.formatDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeductionsScreen(
    state: DeductionsUiState,
    actions: DeductionsLogicActions,
    navActions: DeductionsNavActions,
    modifier: Modifier = Modifier,
) {
    if (state.employee == null) {
        Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_employee_selected))
        }
        return
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DeductionsTopBar(
                state = state,
                onRefresh = actions.onRefresh,
                onNavigateUp = navActions.onPop
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            MonthYearSelector(
                month = state.month,
                year = state.year,
                onMonthChange = actions.onSelectMonth,
                onYearChange = actions.onSelectyear
            )

            if (state.isLoading && state.deductions.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp),
                    columns = GridCells.Adaptive(minSize = 200.dp)
                ) {
                    item(
                        span = {
                            GridItemSpan(this.maxLineSpan)
                        }
                    ) {
                        TotalDeductionsCard(state.deductionsSum)
                    }
                    items(state.deductions) { deduction ->
                        DeductionCard(deduction = deduction)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeductionsTopBar(
    state: DeductionsUiState,
    onRefresh: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            Column {
                Text(stringResource(R.string.deductions))
                Text(
                    "${state.employee!!.name} - ${state.employee.employeeNumber}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        actions = {
            if (state.refreshable) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun MonthYearSelector(
    month: Int,
    year: Int,
    onMonthChange: (Int?) -> Unit,
    onYearChange: (Int?) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = month.toString(),
            onValueChange = {
                it.toIntOrNull().let(onMonthChange)
            },
            label = { Text(stringResource(R.string.month)) },
            modifier = Modifier.weight(1f),
        )
        OutlinedTextField(
            value = year.toString(),
            onValueChange = { it.toIntOrNull().let(onYearChange) },
            label = { Text(stringResource(R.string.year)) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DeductionCard(
    deduction: Deduction,
) {
    var expanded by remember { mutableStateOf(false) }

    val paid = deduction.total - deduction.remaining
    val monthsLeft = if (deduction.monthlyInstallment > 0)
        deduction.remaining / deduction.monthlyInstallment
    else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(deduction.name, style = MaterialTheme.typography.titleMedium)
                Icon(
                    if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null
                )
            }

            Text(
                text = stringResource(
                    R.string.remaining_x,
                    deduction.remaining.formatAsCurrency()
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.installment_x,
                    deduction.monthlyInstallment.formatAsCurrency()
                ), style = MaterialTheme.typography.bodyMedium
            )

            AnimatedVisibility(expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))


                    Text(stringResource(
                        R.string.total_x,
                        deduction.total.formatAsCurrency()
                    ))
                    Text(stringResource(R.string.total_paid_x, paid.formatAsCurrency()))
                    if (monthsLeft > 0) {
                        Text(stringResource(R.string.estimated_months_left_x, monthsLeft.formatAsCurrency()))
                    }
                    Text(stringResource(R.string.employee_recipe_number_x, deduction.employRecipeNumber))

                    Text(stringResource(R.string.fetched_at_x, deduction.updatedAt.formatDateTime()))
//                    Text(stringResource(R.string.updated_at_x, deduction.createdAt.formatDateTime()))
                }
            }
        }
    }
}

@Composable
fun TotalDeductionsCard(
    totalDeductionsSum: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                "Total Deductions",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                totalDeductionsSum.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

