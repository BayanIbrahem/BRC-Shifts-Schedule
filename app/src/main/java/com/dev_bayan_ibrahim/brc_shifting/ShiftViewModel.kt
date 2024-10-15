package com.dev_bayan_ibrahim.brc_shifting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.brc_shifting.data.data_source.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.data.data_source.ScheduleManager3
import com.dev_bayan_ibrahim.brc_shifting.data.data_source.ScheduleManagerFactory
import com.dev_bayan_ibrahim.brc_shifting.data.data_source.ShiftSchedule
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import com.dev_bayan_ibrahim.brc_shifting.util.days
import com.dev_bayan_ibrahim.brc_shifting.util.now
import com.dev_bayan_ibrahim.brc_shifting.util.shiftOn_Dec_12_2023
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class ShiftViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShiftUiState())
    val uiState = _uiState.asStateFlow()

    init {
        selectDate(LocalDateTime.now().date)
    }
    fun selectDate(date: LocalDate) {
        viewModelScope.launch {
            val shiftManager = shiftManagerOfDate(date)
            _uiState.value = ShiftUiState(
                selectedDate = date,
                schedule = shiftManager,
                groupsShifts = WorkGroup.entries.associateWith { workGroup ->
                    shiftManager.calculateShiftForDate(
                        date = date,
                        group = workGroup
                    )
                }
            )
        }
    }

    private fun shiftManagerOfDate(
        date: LocalDate,
    ): ScheduleManager<out Shift> = ScheduleManagerFactory.getShiftManagerForDate(date)

    fun resetToday() {
        viewModelScope.launch {
            selectDate(LocalDateTime.now().date)
        }
    }

    fun selectPrevDay() {
        selectDate(uiState.value.selectedDate - 1.days)
    }

    fun selectNextDay() {
        selectDate(uiState.value.selectedDate + 1.days)
    }
}

data class ShiftUiState(
    val selectedDate: LocalDate = LocalDateTime.now().date,
    val schedule: ShiftSchedule<out Shift> = ScheduleManager3,
    val groupsShifts: Map<WorkGroup, Shift> = WorkGroup.entries.associateWith { it.shiftOn_Dec_12_2023 },
) {
    val isScheduleDeprecated: Boolean = schedule.lastValidDate < LocalDateTime.now().date
}

