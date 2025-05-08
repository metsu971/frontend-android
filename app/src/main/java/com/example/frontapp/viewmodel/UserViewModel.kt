package com.example.frontapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontapp.model.User
import com.example.frontapp.repository.UserRepository
import com.example.frontapp.repository.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepositoryImpl(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()
    //起動時最初に出す音声入力モーダル
    private val _shouldShowVoiceInputModal = MutableStateFlow(true)
    val shouldShowVoiceInputModal : StateFlow<Boolean> = _shouldShowVoiceInputModal.asStateFlow()
    //生体認証
    private val _shouldShowBiometricsPrompt = MutableSharedFlow<Unit>()
    val shouldShowBiometricsPrompt : SharedFlow<Unit> = _shouldShowBiometricsPrompt.asSharedFlow()

    private val _authState = MutableStateFlow<BiometricAuthState>(BiometricAuthState.Idle)
    val authState: StateFlow<BiometricAuthState> = _authState.asStateFlow()

    private val lastAuthTime = MutableStateFlow<Long?>(null)

    fun onVoiceInputModalDismissed() {
        _shouldShowVoiceInputModal.value = false
    }

    fun shouldReAuth() : Boolean {
        val now = System.currentTimeMillis()
        val last = lastAuthTime.value ?: return true
        return now - last > 5 * 60 * 1000
    }

    //生体認証
    fun triggerBiometricsPrompt() {
        viewModelScope.launch {
            _shouldShowBiometricsPrompt.emit(Unit)
        }
    }

    fun onAuthenticationSuccess() {
        _authState.value = BiometricAuthState.Authenticated
    }

    fun onAuthenticationError(message: String) {
        _authState.value = BiometricAuthState.Error(message)
    }
    //直近10件のテキスト化メモ
      //フリックで削除および音声確認を行えるようにする
    //ボトムバーから他の画面へ遷移
    //トップバーでお知らせを確認できる

    fun fetchUsers() {
        viewModelScope.launch(dispatcher) {
            val result = repository.getUsers()
            _users.value = result
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch(dispatcher) {
            _isSubmitting.value = true
            repository.addUser(user)
            fetchUsers()
            _isSubmitting.value = false
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(dispatcher) {
            user.id?.let {
                repository.updateUser(it, user)
                fetchUsers()
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch(dispatcher) {
            repository.deleteUser(userId)
            fetchUsers()
        }
    }
}

sealed interface BiometricAuthState {
    object Idle : BiometricAuthState
    object Authenticated : BiometricAuthState
    data class Error(val message: String) : BiometricAuthState
}