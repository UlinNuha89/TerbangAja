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
import com.andc4.terbangaja.data.datasource.SeatDataSource
import com.andc4.terbangaja.data.datasource.SeatDataSourceImpl
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
import com.andc4.terbangaja.data.repository.SeatRepository
import com.andc4.terbangaja.data.repository.SeatRepositoryImpl
import com.andc4.terbangaja.data.source.local.database.AppDatabase
import com.andc4.terbangaja.data.source.local.database.dao.SearchDao
import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import com.andc4.terbangaja.data.source.local.pref.AuthPreferenceImpl
import com.andc4.terbangaja.data.source.network.service.AuthInterceptor
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.andc4.terbangaja.presentation.account.AccountViewModel
import com.andc4.terbangaja.presentation.checkout.CheckoutViewModel
import com.andc4.terbangaja.presentation.detailticket.DetailTicketViewModel
import com.andc4.terbangaja.presentation.history.HistoryViewModel
import com.andc4.terbangaja.presentation.home.HomeViewModel
import com.andc4.terbangaja.presentation.login.LoginViewModel
import com.andc4.terbangaja.presentation.notification.NotificationViewModel
import com.andc4.terbangaja.presentation.otp.OtpViewModel
import com.andc4.terbangaja.presentation.register.RegisterViewModel
import com.andc4.terbangaja.presentation.resetpassword.ResetPasswordViewModel
import com.andc4.terbangaja.presentation.seat.SeatViewModel
import com.andc4.terbangaja.presentation.ticketorder.TicketOrderViewModel
import com.andc4.terbangaja.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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

 /*   private val paging =
        module {
            single<FlightsPagingSource> { FlightsPagingSource(get()) }

        }*/

    private val datasource =
        module {
            single<AuthDataSource> { AuthDataSourceImpl(get(), get()) }
            single<FlightDataSource> { FlightDataSourceImpl(get()) }
            single<AirlineDataSource> { AirlineDataSourceImpl(get()) }
            single<AirportDataSource> { AirportDataSourceImpl(get()) }
            single<SearchDataSource> { SearchDataSourceImpl(get()) }
            single<SeatDataSource> { SeatDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<AuthRepository> { AuthRepositoryImpl(get()) }
            single<FlightRepository> { FlightRepositoryImpl(get()) }
            single<AirlineRepository> { AirlineRepositoryImpl(get()) }
            single<AirportRepository> { AirportRepositoryImpl(get()) }
            single<SearchRepository> { SearchRepositoryImpl(get()) }
            single<SeatRepository> { SeatRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::HomeViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::OtpViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::ResetPasswordViewModel)
            viewModelOf(::NotificationViewModel)
            viewModelOf(::HistoryViewModel)
            viewModelOf(::AccountViewModel)
            viewModel { params ->
                TicketOrderViewModel(
                    extras = params.get(),
                    flightRepository = get(),
                    authRepository = get(),
                )
            }
            viewModel { params ->
                DetailTicketViewModel(
                    extras = params.get(),
                    authRepository = get(),
                )
            }
            viewModel { params ->
                SeatViewModel(
                    extras = params.get(),
                    seatRepository = get(),
                )
            }
            viewModel { params ->
                CheckoutViewModel(
                    extras = params.get(),
                )
            }
        }

    val modules =
        listOf<Module>(
            networkModule,
            localModule,
            // paging,
            authInterceptorModule,
            datasource,
            repository,
            viewModelModule,
        )
}
