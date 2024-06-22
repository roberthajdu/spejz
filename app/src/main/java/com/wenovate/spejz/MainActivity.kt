@file:Suppress("DEPRECATION")

package com.wenovate.spejz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wenovate.spejz.ui.home.HomeScreen
import com.wenovate.spejz.ui.login.GoogleAuthUiClient
import com.wenovate.spejz.ui.login.LoginScreen
import com.wenovate.spejz.ui.login.LoginViewModel
import com.wenovate.spejz.ui.login.SignInState
import com.wenovate.spejz.ui.theme.SpejzTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneTapClient = Identity.getSignInClient(applicationContext)
        signInRequest = BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()


        val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            try {
                Log.d("MainActivity", "Google Sign-In result: ${result.data}")
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    loginViewModel.loginWithGoogle(firebaseCredential) { success ->
                        if (success) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, loginViewModel.errorMessage ?: "Google Sign-In failed", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Google Sign-In failed: No ID token", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        enableEdgeToEdge()
        setContent {
            SpejzTheme {
//                if (FirebaseAuth.getInstance().currentUser == null) {
//                    LoginScreen(onGoogleSignInClick = {
//                        Log.d("MainActivity", "Google Sign-In clicked")
//                        oneTapClient.beginSignIn(signInRequest)
//                            .addOnSuccessListener(this) { result ->
//                                googleSignInLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
//                            }
//                            .addOnFailureListener(this) { e ->
//                                Log.e("MainActivity", "Google Sign-In failed: ${e.message}")
//                                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
//                            }
//                    })
//                } else {
//                    HomeScreen()
//                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val state by loginViewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if(googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("home")
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            loginViewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("home")
                                    loginViewModel.resetState()
                                }
                            }

                            LoginScreen(
                                state = state,
                                navController = navController,
                                onGoogleSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
//                            ProfileScreen(
//                                userData = googleAuthUiClient.getSignedInUser(),
//                                onSignOut = {
//                                    lifecycleScope.launch {
//                                        googleAuthUiClient.signOut()
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "Signed out",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//
//                                        navController.popBackStack()
//                                    }
//                                }
//                            )
                    }
                }
            }
        }
    }

}