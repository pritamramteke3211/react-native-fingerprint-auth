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

  const isBiometricLockoutError = (error: unknown) => {
    if (!(error instanceof Error)) return false;
    const msg = error.message.toLowerCase();
    return (
      msg.includes('lockout') ||
      msg.includes('face unlock') ||
      msg.includes('biometric')
    );
  };

  const handleAuthenticate = async () => {
    try {
      const result = await authenticateFingerprint('Authenticate to continue');
      Alert.alert('Authenticated!', result);
    } catch (error) {
      console.log('Fingerprint auth failed:', error);

      if (isBiometricLockoutError(error)) {
        Alert.alert(
          'Fingerprint Locked',
          'Fingerprint failed too many times. Please try again after some time.'
        );
      }
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
