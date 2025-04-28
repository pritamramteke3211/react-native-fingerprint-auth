package com.fingerprintauth

import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.util.concurrent.Executors

class FingerprintAuthModuleCpy(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "FingerprintAuthModuleCpy"

    @ReactMethod
    fun isFingerprintAvailable(promise: Promise) {
        val biometricManager = BiometricManager.from(reactContext)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> promise.resolve(true)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> promise.resolve(false)
            else -> promise.resolve(false)
        }
    }

    @ReactMethod
    fun authenticateFingerprint(reason: String, promise: Promise) {
        val activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "No current activity found")
            return
        }

        (activity as FragmentActivity).runOnUiThread {
            try {
                val executor = Executors.newSingleThreadExecutor()

                val biometricPrompt = BiometricPrompt(
                    activity as FragmentActivity,  // Explicit cast here
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            
                        if (errorCode == BiometricPrompt.ERROR_CANCELED || 
                            errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                            promise.reject("USER_CANCELLED", "Authentication was cancelled by user")
                        } else {
                            promise.reject("AUTH_ERROR", "Error: $errString ($errorCode)")
                        }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            promise.resolve("Authentication successful!")
                        }

                        override fun onAuthenticationFailed() {
                            sendEvent("onFingerprintFail", "Authentication failed")
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authenticate")
                    .setSubtitle(reason)
                    .setNegativeButtonText("Cancel")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .build()

                biometricPrompt.authenticate(promptInfo)
            } catch (e: Exception) {
                promise.reject("AUTH_FAILED", "Failed to start authentication: ${e.message}")
            }
        }
    }

    private fun sendEvent(eventName: String, message: String) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, message)
    }

        // Add this method to support NativeEventEmitter
    @ReactMethod
    fun addListener(eventName: String) {
        // Required for RN 0.65+
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // Required for RN 0.65+
    }
}