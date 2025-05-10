package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action.SalariesLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action.SalaryNavActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.component.DetailsSalaryCard
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.component.VerticalSalaryCard
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.state.SalariesUiState
import com.dev_bayan_ibrahim.brc_shifting.util.months
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalariesScreen(
    uiState: SalariesUiState,
    salaries: List<EmployeeSalary>,
    thisMonth: LocalDate?,
    logicActions: SalariesLogicActions,
    navActions: SalaryNavActions,
    modifier: Modifier = Modifier,
) {
    if (uiState.employee == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Invalid Employee")

        }
    } else {
        val verticalListState = rememberLazyListState()
        val horizontalState = rememberPagerState { salaries.count() }
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Salaries - ${uiState.employee.name}") },
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
                        IconButton(
                            onClick = {
                                if (uiState.verticalView) {
                                    coroutineScope.launch {
                                        verticalListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index?.let {
                                            horizontalState.animateScrollToPage(it)
                                        }
                                    }
                                    logicActions.onToggleVerticalView(false)
                                } else {
                                    coroutineScope.launch {
                                        horizontalState.layoutInfo.visiblePagesInfo.firstOrNull()?.index?.let {
                                            verticalListState.animateScrollToItem(it)
                                        }
                                    }
                                    logicActions.onToggleVerticalView(true)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.verticalView) {
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight
                                } else {
                                    Icons.Default.KeyboardArrowDown
                                },
                                contentDescription = null
                            )
                        }
                    }

                )
            }
        ) { padding ->
            if (uiState.verticalView) {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = verticalListState,
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
                    itemsIndexed(
                        items = salaries,
                        key = { i, it ->
                            it.id!!
                        }
                    ) { i, salary ->
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
                        VerticalSalaryCard(
                            salary = salary,
                            refreshable = isThisMonth || isPrevMonth,
                            isLoading = (isThisMonth && uiState.thisMonthRequestLoading) || (isPrevMonth && uiState.prevMonthRequestLoading),
                            onClick = {
                                coroutineScope.launch {
                                    horizontalState.animateScrollToPage(i)
                                }
                                logicActions.onToggleVerticalView(false)
                            },
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
            } else {
                HorizontalPager(
                    state = horizontalState,
                    pageSize = PageSize.Fill,
                    modifier = Modifier.padding(padding),
                    pageSpacing = 12.dp
                ) {
                    val salary by remember(it) {
                        derivedStateOf {
                            salaries[it]
                        }
                    }
                    DetailsSalaryCard(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        employee = uiState.employee,
                        salary = salary
                    )
                }
            }
        }
    }
}


