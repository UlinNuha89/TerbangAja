package com.andc4.terbangaja.di

import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.data.datasource.AuthDataSourceImpl
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.AuthRepositoryImpl
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.andc4.terbangaja.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<TerbangAjaApiService> { TerbangAjaApiService.invoke() }
        }
    private val localModule =
        module {
        }

    private val datasource =
        module {
            single<AuthDataSource> { AuthDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<AuthRepository> { AuthRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::RegisterViewModel)
        }

    val modules =
        listOf<Module>(
            networkModule,
            localModule,
            datasource,
            repository,
            viewModelModule,
        )
}
