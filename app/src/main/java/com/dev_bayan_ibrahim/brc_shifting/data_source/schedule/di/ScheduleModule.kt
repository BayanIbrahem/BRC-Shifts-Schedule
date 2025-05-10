package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.di

import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.CompositeScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic.CompositeCyclicShiftScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic.CyclicShiftScheduleManager2
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic.CyclicShiftScheduleManager3
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.standard.StandardScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ScheduleModule {
    @Provides
    @Singleton
    fun provideScheduleManager(): ScheduleManager<Shift> {
        return CompositeScheduleManager(
            managers = setOf(
                StandardScheduleManager,
                CompositeCyclicShiftScheduleManager(
                    managers = setOf(
                        CyclicShiftScheduleManager3,
                        CyclicShiftScheduleManager2
                    )
                )
            ),
        )
    }
}