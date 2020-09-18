package kg.kyrgyzcoder.primedoc02.ui.login.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kg.kyrgyzcoder.primedoc02.data.login.repository.RegisterRepository

class RegisterViewModelFactory(private val registerRepository: RegisterRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(registerRepository) as T
    }
}