package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action.SalaryLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action.SalaryNavActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.state.SalaryUiState
import com.dev_bayan_ibrahim.brc_shifting.util.format
import com.dev_bayan_ibrahim.brc_shifting.util.monthYearFormat
import com.dev_bayan_ibrahim.brc_shifting.util.months
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    uiState: SalaryUiState,
    salaries: List<EmployeeSalary>,
    thisMonth: LocalDate?,
    logicActions: SalaryLogicActions,
    navActions: SalaryNavActions,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Salaries") },
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
                }

            )

        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    TextButton(
                        onClick = {
                            logicActions.onFetchThisMonth()
                        },
                        enabled = !uiState.thisMonthRequestLoading,
                    ) {
                        Text("Fetch this month")
                    }
                    TextButton(
                        onClick = {
                            logicActions.onFetchPrevMonth()
                        },
                        enabled = !uiState.prevMonthRequestLoading
                    ) {
                        Text("Fetch prev month")
                    }
                }
            }
            items(salaries, key = {
                it.id!!
            }) { salary ->
                val isThisMonth by remember(thisMonth, salary) {
                    derivedStateOf {
                        thisMonth != null && thisMonth.monthNumber == salary.monthNumber && thisMonth.year == salary.year
                    }
                }

                val isPrevMonth by remember(thisMonth, salary) {
                    derivedStateOf {
                        val prevMonth = thisMonth?.minus(1.months) ?: return@derivedStateOf false
                        prevMonth.monthNumber == salary.monthNumber && prevMonth.year == salary.year
                    }
                }
                SalaryCard(
                    salary = salary,
                    refreshable = isThisMonth || isPrevMonth,
                    isLoading = (isThisMonth && uiState.thisMonthRequestLoading) || (isPrevMonth && uiState.prevMonthRequestLoading),
                    onRefresh = {
                        if (isThisMonth) {
                            logicActions.onFetchThisMonth()
                        } else if (isPrevMonth) {
                            logicActions.onFetchPrevMonth
                        }
                    }
                )
            }

        }
    }
}

@Composable
private fun SalaryCard(
    salary: EmployeeSalary,
    refreshable: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
        headlineContent = {
            Text(salary.netProvidedRounded.toString())
        },
        overlineContent = {
            Text(salary.atStartOfMonth.monthYearFormat())
        },
        supportingContent = {
            // TODO, string res
            Text("Last Update: ${salary.updatedAt.format()}")
        },
        trailingContent = if (refreshable) {
            {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(12.dp))
                } else {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, null)
                    }
                }
            }

        } else {
            null
        }
    )

}
