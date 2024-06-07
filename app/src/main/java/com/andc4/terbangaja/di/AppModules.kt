package com.andc4.terbangaja.di

import android.content.SharedPreferences
import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.data.datasource.AuthDataSourceImpl
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.AuthRepositoryImpl
import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import com.andc4.terbangaja.data.source.local.pref.AuthPreferenceImpl
import com.andc4.terbangaja.data.source.network.service.AuthInterceptor
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.andc4.terbangaja.presentation.login.LoginViewModel
import com.andc4.terbangaja.presentation.otp.OtpViewModel
import com.andc4.terbangaja.presentation.register.RegisterViewModel
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

    private val datasource =
        module {
            single<AuthDataSource> { AuthDataSourceImpl(get(), get()) }
        }

    private val repository =
        module {
            single<AuthRepository> { AuthRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::RegisterViewModel)
            viewModelOf(::OtpViewModel)
            viewModelOf(::LoginViewModel)
        }

    val modules =
        listOf<Module>(
            networkModule,
            localModule,
            authInterceptorModule,
            datasource,
            repository,
            viewModelModule,
        )
}
