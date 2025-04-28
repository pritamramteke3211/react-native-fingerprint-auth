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
import { StyleSheet, View, Text, Alert, Button } from 'react-native';
import {
  authenticateFingerprint,
  isFingerprintAvailable,
  authenticateDeviceCredentials,
} from 'react-native-fingerprint-auth-lib';

export default function App() {
  const [isAvailable, setIsAvailable] = (useState < boolean) | (null > null);
  const [isAuthenticating, setIsAuthenticating] = useState(false);

  const handleAuthenticate = async () => {
    if (!isAvailable) return;

    setIsAuthenticating(true);
    try {
      // First authenticate device credentials (PIN/Pattern)
      const deviceResult = await authenticateDeviceCredentials(
        'Please verify your device to continue'
      );
      console.log('Device auth result:', deviceResult);

      // Then authenticate fingerprint
      const fingerprintResult = await authenticateFingerprint(
        'Verify your fingerprint to proceed'
      );

      Alert.alert('Success', fingerprintResult);
    } catch (error) {
      console.log('Authentication error:', error);
      if (
        error &&
        error instanceof Error &&
        error.message !== 'Error: Cancel (13)'
      ) {
        console.log('Error message:', error.message);
        Alert.alert(
          'Error',
          error instanceof Error ? error.message : String(error)
        );
      }
    } finally {
      setIsAuthenticating(false);
    }
  };

  const checkFingerprintAvailability = async () => {
    try {
      const available = await isFingerprintAvailable();
      setIsAvailable(available);
      console.log('available', available);
    } catch (error) {
      console.error('Error checking fingerprint availability:', error);
      setIsAvailable(false);
    }
  };

  useEffect(() => {
    checkFingerprintAvailability();
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Fingerprint Authentication</Text>

      <View style={styles.statusContainer}>
        <Text style={styles.statusText}>
          Fingerprint available:{' '}
          {isAvailable === null
            ? 'Checking...'
            : isAvailable
              ? '✅ Yes'
              : '❌ No'}
        </Text>
      </View>

      <View style={styles.buttonContainer}>
        <Button
          title="Authenticate"
          onPress={handleAuthenticate}
          disabled={!isAvailable || isAuthenticating}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 30,
  },
  statusContainer: {
    marginBottom: 30,
    alignItems: 'center',
  },
  statusText: {
    fontSize: 18,
  },
  buttonContainer: {
    gap: 10,
  },
});
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
