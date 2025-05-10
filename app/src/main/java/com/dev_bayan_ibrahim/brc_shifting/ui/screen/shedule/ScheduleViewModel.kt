package com.dev_bayan_ibrahim.brc_shifting.ui.screen.shedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import com.dev_bayan_ibrahim.brc_shifting.util.days
import com.dev_bayan_ibrahim.brc_shifting.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import javax.inject.Inject

@HiltViewModel()
class ScheduleViewModel @Inject constructor(
    private val scheduleManager: ScheduleManager<Shift>,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()
    val groupShifts = _uiState.map { state ->
        WorkGroup.entries.mapNotNull { group ->
            val shift = scheduleManager.getShiftOrNull(group, state.selectedDate) ?: return@mapNotNull null
            Pair(group, shift)
        }.toMap()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    init {
        selectDate(LocalDateTime.now().date)
    }

    fun selectDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState(
                selectedDate = date,
            )
        }
    }

    fun buildLogicActions() = ScheduleLogicActions(
        resetToday = {
            viewModelScope.launch {
                selectDate(LocalDateTime.now().date)
            }
        },
        selectPrevDay = {
            selectDate(uiState.value.selectedDate - 1.days)
        },
        selectNextDay = {
            selectDate(uiState.value.selectedDate + 1.days)
        }
    )
}


