package org

import AppViewModel
import org.koin.dsl.module

val appModule = module {
    single { AppViewModel() }
}