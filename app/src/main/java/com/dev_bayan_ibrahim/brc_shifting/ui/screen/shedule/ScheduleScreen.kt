package com.dev_bayan_ibrahim.brc_shifting.ui.screen.shedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.R
import com.dev_bayan_ibrahim.brc_shifting.component.date_picker.ShiftDatePicker
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import com.dev_bayan_ibrahim.brc_shifting.util.formatDate
import kotlinx.datetime.LocalDate

@Composable
fun ScheduleScreen(
    modifier: Modifier,
    uiState: ScheduleUiState,
    viewModel: ScheduleViewModel,
    logicActions: ScheduleLogicActions,
    shifts: Map<WorkGroup, Shift>,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Input(
            modifier = Modifier.weight(1f),
            selectedDate = uiState.selectedDate,
            onSelectedMillisChange = viewModel::selectDate,
            onClickToday = logicActions.resetToday,
            onClickPrevDay = logicActions.selectPrevDay,
            onClickNextDay = logicActions.selectNextDay,
        )
        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
        Output(
            modifier = Modifier.weight(1f),
            date = uiState.selectedDate,
            shifts = shifts,
        )
    }
}

@Composable
private fun Input(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onSelectedMillisChange: (LocalDate) -> Unit,
    onClickToday: () -> Unit,
    onClickPrevDay: () -> Unit,
    onClickNextDay: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.date))
            ShiftDatePicker(
                selectedDate = selectedDate,
                isShort = false,
                onChangeDate = onSelectedMillisChange,
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = onClickToday
            ) { Text(text = stringResource(R.string.today)) }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onClickPrevDay
            ) { Text(text = stringResource(R.string.previous_day)) }
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onClickNextDay
            ) { Text(text = stringResource(R.string.next_day)) }
        }
    }
}

@Composable
private fun Output(
    modifier: Modifier = Modifier,
    date: LocalDate,
    shifts: Map<WorkGroup, Shift>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = date.formatDate(),
            style = MaterialTheme.typography.titleLarge
        )
        shifts.forEach { (group, shift) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = stringResource(id = group.nameRes))
                Text(text = stringResource(id = shift.shiftName))
            }
        }
    }
}

