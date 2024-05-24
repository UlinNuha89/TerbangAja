package com.andc4.terbangaja.di

import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
        }
    private val localModule =
        module {
        }

    private val datasource =
        module {
        }

    private val repository =
        module {
        }

    private val viewModelModule =
        module {
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
