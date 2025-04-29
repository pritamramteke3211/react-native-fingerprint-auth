package com.fingerprintauth

import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class FingerprintAuthModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val biometricManager: BiometricManager = BiometricManager.from(reactContext)
    private val executor: Executor = ContextCompat.getMainExecutor(reactContext)

    override fun getName(): String {
        return "FingerprintAuth"
    }

    // Check if fingerprint authentication is available on the device
    @ReactMethod
    fun isFingerprintAvailable(promise: Promise) {
        try {
            val result = biometricManager.canAuthenticate()
            when (result) {
                BiometricManager.BIOMETRIC_SUCCESS -> promise.resolve(true)
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> promise.resolve(false)
                else -> promise.resolve(false)
            }
        } catch (e: Exception) {
            promise.reject("ERROR", e)
        }
    }

    // Authenticate user using fingerprint
    @ReactMethod
    fun authenticateFingerprint(reason: String, promise: Promise) {
        try {
            val activity = reactApplicationContext.currentActivity
            if (activity is FragmentActivity) {
                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            promise.reject("AUTH_ERROR", errString.toString())
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            promise.resolve("success")
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            promise.reject("AUTH_FAILED", "Authentication failed.")
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Fingerprint Authentication")
                    .setSubtitle("Authenticate using your fingerprint")
                    .setDescription(reason)
                    .setNegativeButtonText("Cancel")
                    .build()

                activity.runOnUiThread {
                    biometricPrompt.authenticate(promptInfo)
                }

            } else {
                promise.reject("ERROR", "Activity is not a FragmentActivity")
            }
        } catch (e: Exception) {
            promise.reject("ERROR", e)
        }
    }

    // Authenticate user using device credentials (PIN, pattern, password)
    @ReactMethod
    fun authenticateDeviceCredentials(reason: String, promise: Promise) {
        try {
            val activity = reactApplicationContext.currentActivity
            if (activity is FragmentActivity) {
                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            promise.reject("AUTH_ERROR", errString.toString())
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            promise.resolve("success")
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            promise.reject("AUTH_FAILED", "Authentication failed.")
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Device Authentication")
                    .setSubtitle("Authenticate using your device credentials")
                    .setDescription(reason)
                    .setAllowedAuthenticators(
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                    .build()

                activity.runOnUiThread {
                    biometricPrompt.authenticate(promptInfo)
                }

            } else {
                promise.reject("ERROR", "Activity is not a FragmentActivity")
            }
        } catch (e: Exception) {
            promise.reject("ERROR", e)
        }
    }

    // Open security settings to let the user set up biometric authentication
    @ReactMethod
    fun openSecuritySettings(promise: Promise) {
        try {
            val intent = Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS)
            reactApplicationContext.currentActivity?.startActivity(intent)
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERROR", e)
        }
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


    // Send fingerprint event to JS
    private fun sendFingerprintEvent(event: String, params: WritableMap) {
        try {
            val emitter = reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            emitter.emit(event, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
