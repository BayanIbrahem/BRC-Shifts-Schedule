package com.dev_bayan_ibrahim.brc_shifting.data.di

import com.dev_bayan_ibrahim.brc_shifting.data.repo.EmployeeRepoImpl
import com.dev_bayan_ibrahim.brc_shifting.data.repo.SalaryRepoImpl
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.LocalDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.RemoteDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.BRCUriDirector
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.EmployeeRepo
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.SalaryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        client: HttpClient,
        json: Json,
    ): RemoteDataSource = RemoteDataSource(
        client = client,
        director = BRCUriDirector,
        json = json,
    )

    @Provides
    @Singleton
    fun provideEmployeeRepo(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): EmployeeRepo = EmployeeRepoImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideSalaryRepo(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): SalaryRepo = SalaryRepoImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )
}