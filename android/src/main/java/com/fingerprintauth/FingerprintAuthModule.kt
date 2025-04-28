package com.fingerprintauth

import android.content.Intent
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.util.concurrent.Executor

@ReactModule(name = FingerprintAuthModule.NAME)
class FingerprintAuthModule(private val context: ReactApplicationContext) :
    NativeFingerprintAuthSpec(context) {

    private val reactContext = context
    private var biometricPrompt: BiometricPrompt? = null
    private var executor: Executor? = null

    override fun getName(): String = NAME

    override fun isFingerprintAvailable(promise: Promise) {
        val biometricManager = BiometricManager.from(reactContext)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> promise.resolve(true)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> promise.resolve(false)
            else -> promise.resolve(false)
        }
    }

    override fun authenticateFingerprint(reason: String, promise: Promise) {
        val activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "No current activity found")
            return
        }

        (activity as FragmentActivity).runOnUiThread {
            try {
                executor = ContextCompat.getMainExecutor(activity)
                biometricPrompt = BiometricPrompt(
                    activity,
                    executor!!,
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
                    .setTitle("Fingerprint Authentication")
                    .setSubtitle(reason)
                    .setNegativeButtonText("Cancel")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .build()

                biometricPrompt!!.authenticate(promptInfo)
            } catch (e: Exception) {
                promise.reject("AUTH_FAILED", "Failed to start fingerprint auth: ${e.message}")
            }
        }
    }

    override fun authenticateDeviceCredentials(promptMessage: String, promise: Promise) {
        val activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "No current activity found")
            return
        }

        (activity as FragmentActivity).runOnUiThread {
            try {
                executor = ContextCompat.getMainExecutor(activity)

                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor!!,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            if (errorCode == BiometricPrompt.ERROR_CANCELED ||
                                errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                                promise.reject("USER_CANCELLED", "Authentication cancelled by user")
                            } else {
                                promise.reject("AUTH_ERROR", "Error: $errString ($errorCode)")
                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            promise.resolve("Device credentials authentication successful!")
                        }

                        override fun onAuthenticationFailed() {
                            sendEvent("onDeviceCredentialFail", "Device credential authentication failed")
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Device Authentication")
                    .setSubtitle(promptMessage)
                    .setDescription("Confirm your device credentials (PIN, Pattern, or Password)")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build()

                biometricPrompt.authenticate(promptInfo)

            } catch (e: Exception) {
                promise.reject("AUTH_FAILED", "Failed to start device credential auth: ${e.message}")
            }
        }
    }

    override fun openSecuritySettings(promise: Promise) {
        try {
            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            reactContext.startActivity(intent)
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("OPEN_SETTINGS_FAILED", "Failed to open security settings: ${e.message}")
        }
    }

    private fun sendEvent(eventName: String, message: String) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, message)
    }

    override fun addListener(eventName: String) {}
    override fun removeListeners(count: Double) {}

    companion object {
        const val NAME = "FingerprintAuth"
    }
}
