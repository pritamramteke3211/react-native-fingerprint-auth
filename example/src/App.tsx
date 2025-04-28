import { useState } from 'react';
import { StyleSheet, View, Text, Alert, Button } from 'react-native';
import { authenticateFingerprint } from 'react-native-fingerprint-auth';

export default function App() {
  const [isAvailable] = useState<boolean | null>(null);
  const [isAuthenticating, setIsAuthenticating] = useState(false);

  const handleAuthenticate = async () => {
    if (!isAvailable) return;

    setIsAuthenticating(true);
    try {
      const result = await authenticateFingerprint(
        'Verify your identity to proceed'
      );
      Alert.alert('Success', result);
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

  // const checkFingerprintAvailability = async () => {
  //   try {
  //     const available = await isFingerprintAvailable();
  //     setIsAvailable(available);
  //     console.log('available', available);
  //   } catch (error) {
  //     console.error('Error checking fingerprint availability:', error);
  //     setIsAvailable(false);
  //   }
  // };

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

        {/* {isAuthenticating && (
          <Button title="Cancel" onPress={handleCancel} color="#ff4444" />
        )} */}
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
