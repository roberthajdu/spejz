package com.wenovate.spejz.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.wenovate.spejz.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var errorMessage: String? by mutableStateOf(null)

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun login(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.signInWithEmailPassword(email, password)
                onComplete(true)
            } catch (e: Exception) {
                errorMessage = e.message
                onComplete(false)
            }
        }
    }

    fun loginWithGoogle(credential: AuthCredential, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.signInWithCredential(credential)
                onComplete(true)
            } catch (e: Exception) {
                errorMessage = e.message
                onComplete(false)
            }
        }
    }
}