package com.andc4.terbangaja.di

import android.content.SharedPreferences
import com.andc4.terbangaja.data.datasource.AirlineDataSource
import com.andc4.terbangaja.data.datasource.AirlineDataSourceImpl
import com.andc4.terbangaja.data.datasource.AirportDataSource
import com.andc4.terbangaja.data.datasource.AirportDataSourceImpl
import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.data.datasource.AuthDataSourceImpl
import com.andc4.terbangaja.data.datasource.FlightDataSource
import com.andc4.terbangaja.data.datasource.FlightDataSourceImpl
import com.andc4.terbangaja.data.datasource.SearchDataSource
import com.andc4.terbangaja.data.datasource.SearchDataSourceImpl
import com.andc4.terbangaja.data.paging.FlightsPagingSource
import com.andc4.terbangaja.data.repository.AirlineRepository
import com.andc4.terbangaja.data.repository.AirlineRepositoryImpl
import com.andc4.terbangaja.data.repository.AirportRepository
import com.andc4.terbangaja.data.repository.AirportRepositoryImpl
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.AuthRepositoryImpl
import com.andc4.terbangaja.data.repository.FlightRepository
import com.andc4.terbangaja.data.repository.FlightRepositoryImpl
import com.andc4.terbangaja.data.repository.SearchRepository
import com.andc4.terbangaja.data.repository.SearchRepositoryImpl
import com.andc4.terbangaja.data.source.local.database.AppDatabase
import com.andc4.terbangaja.data.source.local.database.dao.SearchDao
import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import com.andc4.terbangaja.data.source.local.pref.AuthPreferenceImpl
import com.andc4.terbangaja.data.source.network.service.AuthInterceptor
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.andc4.terbangaja.presentation.home.HomeViewModel
import com.andc4.terbangaja.presentation.login.LoginViewModel
import com.andc4.terbangaja.presentation.otp.OtpViewModel
import com.andc4.terbangaja.presentation.register.RegisterViewModel
import com.andc4.terbangaja.presentation.resetpassword.ResetPasswordViewModel
import com.andc4.terbangaja.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<TerbangAjaApiService> { TerbangAjaApiService.invoke(androidContext()) }
        }
    private val localModule =
        module {
            single<AppDatabase> { AppDatabase.createInstance(androidContext()) }
            single<SearchDao> { get<AppDatabase>().searchDao() }
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    AuthPreferenceImpl.PREF_NAME,
                )
            }
            single<AuthPreference> { AuthPreferenceImpl(get()) }
        }
    private val authInterceptorModule =
        module {
            single { AuthInterceptor(get()) }
        }

    private val paging =
        module {
            single<FlightsPagingSource> { FlightsPagingSource(get()) }
        }

    private val datasource =
        module {
            single<AuthDataSource> { AuthDataSourceImpl(get(), get()) }
            single<FlightDataSource> { FlightDataSourceImpl(get()) }
            single<AirlineDataSource> { AirlineDataSourceImpl(get()) }
            single<AirportDataSource> { AirportDataSourceImpl(get()) }
            single<SearchDataSource> { SearchDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<AuthRepository> { AuthRepositoryImpl(get()) }
            single<FlightRepository> { FlightRepositoryImpl(get()) }
            single<AirlineRepository> { AirlineRepositoryImpl(get()) }
            single<AirportRepository> { AirportRepositoryImpl(get()) }
            single<SearchRepository> { SearchRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::HomeViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::OtpViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::ResetPasswordViewModel)
        }

    val modules =
        listOf<Module>(
            networkModule,
            localModule,
            paging,
            authInterceptorModule,
            datasource,
            repository,
            viewModelModule,
        )
}
