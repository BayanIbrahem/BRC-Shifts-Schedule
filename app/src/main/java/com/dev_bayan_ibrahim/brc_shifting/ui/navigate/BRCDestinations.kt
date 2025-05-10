package com.dev_bayan_ibrahim.brc_shifting.ui.navigate

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.dev_bayan_ibrahim.brc_shifting.R
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

private val destinationsList = mutableListOf<KClass<out BRCDestination>>()
private val topLevelDestinationsList = mutableListOf<KClass<out BRCDestination.TopLevel>>()
private val lockDrawerGestureDestinationsList = mutableListOf<KClass<out BRCDestination>>()

@Serializable
sealed class BRCDestination {
    init {
        destinationsList.add(this::class)
    }

    @Serializable
    sealed class TopLevel : BRCDestination() {
        init {
            topLevelDestinationsList.add(this::class)
        }

        @Serializable
        data object Employees : TopLevel()
        @Serializable
        data object Schedule : TopLevel()

        enum class Enum(
            @StringRes val labelRes: Int,
        ) {
            Employees(R.string.employees),
            Schedule(R.string.schedule);

            val route
                get() = when (this) {
                    Employees -> TopLevel.Employees
                    Schedule -> TopLevel.Schedule
                }
        }
    }

    @Serializable
    data class Salaries(
        val employeeNumber: Int,
    ) : BRCDestination()

    @Serializable
    data class Bonus(
        val employeeNumber: Int,
    ) : BRCDestination()

    @Serializable
    data class DayOffs(
        val employeeNumber: Int,
    ) : BRCDestination()

    @Serializable
    data class Deductions(
        val employeeNumber: Int,
    ) : BRCDestination()
}

/**
 * return what route is it or null can not be specified
 */
fun NavDestination.getCurrentDestination(): KClass<out BRCDestination>? {
    val d = destinationsList.firstNotNullOfOrNull {
        if (hasRoute(it)) it else null
    }
    return d
}

fun KClass<out BRCDestination>.allowDrawerGestures(): Boolean = this !in lockDrawerGestureDestinationsList
fun KClass<out BRCDestination>.isTopLevel(): Boolean = this in topLevelDestinationsList
fun KClass<out BRCDestination>.getTopLevelEnum(): BRCDestination.TopLevel.Enum? = BRCDestination.TopLevel.Enum.entries.firstOrNull {
    this == it.route::class
}

