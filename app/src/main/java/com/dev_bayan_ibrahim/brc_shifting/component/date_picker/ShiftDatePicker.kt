package com.dev_bayan_ibrahim.brc_shifting.component.date_picker


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.R
import com.dev_bayan_ibrahim.brc_shifting.ui.theme.BRCShiftsTheme
import com.dev_bayan_ibrahim.brc_shifting.util.format
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftDatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onChangeDate: (LocalDate) -> Unit,
    isShort: Boolean,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        yearRange = (1980..2080), // company is created in 1980
        initialDisplayMode = if (isShort) {
            DisplayMode.Input
        } else DisplayMode.Picker
    )
    var show by rememberSaveable { mutableStateOf(false) }

    AssistChip(
        onClick = { show = true },
        label = {
            Text(text = selectedDate.format())
        },
    )
    if (show) {
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onChangeDate(it.asEpochMillisToDate())
                        }
                        show = false
                    },
                    enabled = datePickerState.selectedDateMillis != null,
                ) { Text(text = stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { show = false }
                ) { Text(text = stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(
                headline = {
                    if (datePickerState.displayMode == DisplayMode.Input) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(id = R.string.date_input_hint),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    } else {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = datePickerState
                                .selectedDateMillis
                                ?.asEpochMillisToDate()
                                ?.format()
                                ?: stringResource(R.string.invalid_date),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                },
                state = datePickerState,
                showModeToggle = !isShort,
            )
        }
    }
}

private fun Long.asEpochMillisToDate() = Instant
    .fromEpochMilliseconds(this)
    .toLocalDateTime(
        timeZone = TimeZone.currentSystemDefault()
    ).date

@Preview(showBackground = true)
@Composable
private fun ShiftDatePickerPreviewLight() {
    BRCShiftsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            ShiftDatePicker(
                modifier = Modifier,
                selectedDate = LocalDate.parse("2023-01-01"),
                onChangeDate = {},
                isShort = false,
            )
        }
    }
}
