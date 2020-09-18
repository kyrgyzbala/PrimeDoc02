package kg.kyrgyzcoder.primedoc02.util

import android.app.Application
import kg.kyrgyzcoder.primedoc02.data.ApiService
import kg.kyrgyzcoder.primedoc02.data.ConnectivityInterceptor
import kg.kyrgyzcoder.primedoc02.data.ConnectivityInterceptorImpl
import kg.kyrgyzcoder.primedoc02.data.login.repository.RegisterRepository
import kg.kyrgyzcoder.primedoc02.data.login.repository.RegisterRepositoryImpl
import kg.kyrgyzcoder.primedoc02.ui.login.model.RegisterViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class PrimeDocDocApp : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@PrimeDocDocApp))

        bind<ConnectivityInterceptorImpl>() with singleton {
            ConnectivityInterceptorImpl(instance())
        }
        bind() from singleton { ApiService(instance()) }

        bind<RegisterRepository>() with singleton { RegisterRepositoryImpl(instance()) }
        bind() from provider { RegisterViewModelFactory(instance()) }

    }

}