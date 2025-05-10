package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.util.formatDate
import com.dev_bayan_ibrahim.brc_shifting.util.monthYearFormat

@Composable
fun VerticalSalaryCard(
    salary: EmployeeSalary,
    refreshable: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        headlineContent = {
            Text(salary.netProvidedRounded.toString())
        },
        overlineContent = {
            Text(salary.atStartOfMonth.monthYearFormat())
        },
        supportingContent = {
            // TODO, string res
            Text("Last Update: ${salary.updatedAt.formatDate()}")
        },
        trailingContent = if (refreshable) {
            {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(12.dp)
                    )
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