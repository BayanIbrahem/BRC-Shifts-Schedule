package com.dev_bayan_ibrahim.brc_shifting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev_bayan_ibrahim.brc_shifting.component.date_picker.ShiftDatePicker
import com.dev_bayan_ibrahim.brc_shifting.data.data_source.ShiftSchedule
import com.dev_bayan_ibrahim.brc_shifting.ui.theme.BRCShiftsTheme
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.ShiftScreenSize
import com.dev_bayan_ibrahim.brc_shifting.util.ShiftScreenSize.COMPAT
import com.dev_bayan_ibrahim.brc_shifting.util.ShiftScreenSize.SHORT
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import com.dev_bayan_ibrahim.brc_shifting.util.format
import kotlinx.datetime.LocalDate


@Composable
fun ShiftApplication(
    modifier: Modifier = Modifier,
    shiftViewModel: ShiftViewModel = viewModel(),
    screenSize: ShiftScreenSize,
) {
    val uiState by shiftViewModel.uiState.collectAsState()
    if (screenSize == COMPAT) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Input(
                modifier = Modifier.weight(1f),
                selectedDate = uiState.selectedDate,
                onSelectedMillisChange = shiftViewModel::selectDate,
                onClickToday = shiftViewModel::resetToday,
                onClickPrevDay = shiftViewModel::selectPrevDay,
                onClickNextDay = shiftViewModel::selectNextDay,
                isShort = false,
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            Hint(uiState.schedule, uiState.isScheduleDeprecated)
            Output(
                modifier = Modifier.weight(1f),
                date = uiState.selectedDate,
                groupsShifts = uiState.groupsShifts,
            )
        }
    } else {
        Column {
            Hint(
                schedule = uiState.schedule,
                isDeprecated = uiState.isScheduleDeprecated,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            )
            Row(
                modifier = modifier
                    .padding(16.dp)
                    .height(IntrinsicSize.Min)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Input(
                    modifier = Modifier.weight(1f),
                    selectedDate = uiState.selectedDate,
                    isShort = screenSize == SHORT,
                    onSelectedMillisChange = shiftViewModel::selectDate,
                    onClickToday = shiftViewModel::resetToday,
                    onClickNextDay = shiftViewModel::selectNextDay,
                    onClickPrevDay = shiftViewModel::selectPrevDay,
                )
                Box(
                    Modifier
                        .width(DividerDefaults.Thickness)
                        .padding(vertical = 16.dp)
                        .fillMaxHeight()
                        .background(DividerDefaults.color)
                )
                Output(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    date = uiState.selectedDate,
                    groupsShifts = uiState.groupsShifts
                )
            }
        }
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
    isShort: Boolean,
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
                isShort = isShort,
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
    groupsShifts: Map<WorkGroup, Shift>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = date.format(),
            style = MaterialTheme.typography.titleLarge
        )
        groupsShifts.forEach { (group, shift) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = stringResource(id = group.nameRes))
                Text(text = stringResource(id = shift.shiftName))
            }
        }
    }
}

@Composable
private fun Hint(
    schedule: ShiftSchedule<out Shift>,
    isDeprecated: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isDeprecated) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isDeprecated) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isDeprecated) Icons.Default.Warning else Icons.Default.Info,
                contentDescription = null,
                Modifier.size(48.dp)
            )
            Text(
                if (isDeprecated) stringResource(R.string.deprecated_schedule_hint, schedule.lastValidDate)
                else stringResource(R.string.active_schedule_hint, schedule.startDate)
            )

        }
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_7_pro,orientation=portrait")
@Composable
private fun ShiftApplicationPreview() {
    BRCShiftsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

        }
        ShiftApplication(screenSize = SHORT)
    }
}