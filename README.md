## 📱 **Secure Login with Fingerprint Authentication**

Unlock the power of **fingerprint authentication** in your mobile applications with ease. This library offers seamless integration of biometric authentication to ensure a smooth and secure login experience for users. Whether you're adding, enabling, or authenticating with fingerprints, this solution streamlines the process and keeps things simple

---

![This is a Preview.](https://i.imgur.com/99TuIrS.gif 'This is a Preview.')

### 🚀 **Key Features:**

#### 🔐 **Fingerprint Authentication**

Leverage the device's built-in fingerprint sensor to authenticate users quickly and securely. It's fast, easy, and convenient!

#### 🛠️ **Add Fingerprint Authentication**

If the fingerprint option isn't set up yet, users can add their fingerprint with just a tap. The app will guide them directly to the device's security settings to enable it.

#### 🛡️ **Enable Fingerprint Authentication**

If fingerprint authentication is available but not yet enabled, users are prompted to enable it by verifying their device credentials (PIN, pattern, or password). This ensures a layer of security before enabling the feature.

#### 📲 **Real-Time Availability Check**

The app dynamically checks if the device supports fingerprint authentication. If it's available, it provides the option to authenticate via fingerprint, ensuring a hassle-free experience for users.

#### ✅ **Authenticate with Fingerprint**

Once fingerprint authentication is enabled, users can quickly authenticate using their fingerprint, making it easy to log in securely and instantly.

#### 🔄 **Device Credentials Fallback**

In case the fingerprint sensor fails or isn't available, users can always fall back on their device credentials for authentication, ensuring that the login process remains uninterrupted.

#### ⚙️ **Automatic State Updates**

The library continuously monitors changes in the app's state (e.g., the user might add or remove fingerprints). It updates the UI accordingly, providing real-time feedback and a seamless experience.

---

## Installation

```sh
npm install react-native-fingerprint-auth-lib
```

## Usage

```js
import { useEffect, useState } from 'react';
import {
  AppState,
  type AppStateStatus,
  Alert,
  Image,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

import {
  authenticateFingerprint,
  authenticateDeviceCredentials,
  openSecuritySettings,
  isFingerprintAvailable as checkFingerprintNative,
} from 'react-native-fingerprint-auth-lib';

export default function App() {
  const [isFingerprintAvailable, setIsFingerprintAvailable] = useState<
    boolean | null
  >(null);
  const [fingerprintEnabled, setFingerprintEnabled] = useState(false);
  const [checking, setChecking] = useState(false);

  useEffect(() => {
    const initialize = async () => {
      await checkFingerprint();
    };

    const handleAppStateChange = (nextAppState: AppStateStatus) => {
      if (nextAppState === 'active') {
        checkFingerprint();
      }
    };

    initialize();

    const subscription = AppState.addEventListener(
      'change',
      handleAppStateChange
    );
    return () => {
      subscription.remove();
    };
  }, []);

  const checkFingerprint = async () => {
    try {
      setChecking(true);
      const available = await checkFingerprintNative();
      setIsFingerprintAvailable(available);

      // If fingerprint removed manually, disable inside app too
      if (!available) {
        setFingerprintEnabled(false);
      }
    } catch (error) {
      console.error('Fingerprint check error:', error);
      setIsFingerprintAvailable(false);
      setFingerprintEnabled(false);
    } finally {
      setChecking(false);
    }
  };

  const handleAddFingerprint = async () => {
    try {
      await openSecuritySettings();
    } catch (error) {
      console.log('Failed to open settings:', error);
    }
  };

  const handleEnableFingerprint = async () => {
    try {
      await authenticateDeviceCredentials('Please verify your device lock');
      setFingerprintEnabled(true);
      Alert.alert('Success', 'Fingerprint enabled successfully!');
    } catch (error) {
      console.log('Device auth failed or cancelled:', error);
    }
  };

  const handleAuthenticate = async () => {
    try {
      const result = await authenticateFingerprint('Authenticate to continue');
      Alert.alert('Authenticated!', result);
    } catch (error) {
      console.log('Fingerprint auth failed:', error);
      Alert.alert(
        'Failed',
        error instanceof Error ? error.message : 'Authentication failed'
      );
    }
  };

  if (isFingerprintAvailable === null || checking) {
    return (
      <View style={styles.container}>
        <Text style={styles.title}>Checking fingerprint availability...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Secure Login</Text>

      {!isFingerprintAvailable ? (
        <TouchableOpacity
          style={styles.enableButton}
          onPress={handleAddFingerprint}
        >
          <Image
            source={{
              uri: 'https://static.thenounproject.com/png/1238269-200.png',
            }}
            style={styles.fingerprintIcon}
            resizeMode="contain"
          />
          <Text style={styles.enableButtonText}>Add Fingerprint</Text>
        </TouchableOpacity>
      ) : !fingerprintEnabled ? (
        <TouchableOpacity
          style={styles.enableButton}
          onPress={handleEnableFingerprint}
        >
          <Image
            source={{
              uri: 'https://static.thenounproject.com/png/1238269-200.png',
            }}
            style={styles.fingerprintIcon}
            resizeMode="contain"
          />
          <Text style={styles.enableButtonText}>Enable Fingerprint</Text>
        </TouchableOpacity>
      ) : (
        <TouchableOpacity
          style={styles.enableButton}
          onPress={handleAuthenticate}
        >
          <Image
            source={{
              uri: 'https://static.thenounproject.com/png/1238269-200.png',
            }}
            style={styles.fingerprintIcon}
            resizeMode="contain"
          />
          <Text style={styles.enableButtonText}>Login with Fingerprint</Text>
        </TouchableOpacity>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 40,
  },
  enableButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 12,
    paddingHorizontal: 20,
    borderWidth: 2,
    borderColor: '#007AFF',
    borderRadius: 8,
    backgroundColor: 'transparent',
  },
  enableButtonText: {
    color: '#007AFF',
    fontSize: 16,
    fontWeight: '600',
    marginLeft: 10,
  },
  fingerprintIcon: {
    width: 24,
    height: 24,
  },
});
```

### Key Library Functions:

- **`authenticateFingerprint`**\
  Securely authenticate users via their fingerprint with a simple prompt.

  ```
  const result = await authenticateFingerprint('Authenticate to continue');

  ```

- **`authenticateDeviceCredentials`**\
  Use device credentials (PIN, pattern, password) as a fallback if fingerprint auth is unavailable.

  ```
  await authenticateDeviceCredentials('Please verify your device lock');

  ```

- **`openSecuritySettings`**\
  Open device security settings to allow users to add or modify their fingerprint.

  ```
  await openSecuritySettings();

  ```

- **`isFingerprintAvailable`**\
  Check if the device has a fingerprint sensor and if it's enabled for authentication.

  ```
  const available = await isFingerprintAvailable();

  ```

### How to Use:

1.  **Check Fingerprint Availability**:\
    Use `isFingerprintAvailable` to ensure fingerprint authentication is supported.

2.  **Add Fingerprint**:\
    If not added, guide users to the security settings with `openSecuritySettings`.

3.  **Enable Fingerprint Authentication**:\
    Use `authenticateDeviceCredentials` to enable fingerprint authentication on the device.

4.  **Authenticate via Fingerprint**:\
    Once enabled, authenticate users using `authenticateFingerprint`.

This library ensures seamless, secure login via fingerprint authentication while providing a fallback to device credentials when needed.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---
