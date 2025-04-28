import { NativeEventEmitter } from 'react-native';

const FingerprintAuth = require('./NativeFingerprintAuth').default;

export function isFingerprintAvailable(): Promise<boolean> {
  return FingerprintAuth.isFingerprintAvailable();
}

export function authenticateFingerprint(reason: string): Promise<string> {
  return FingerprintAuth.authenticateFingerprint(reason);
}

export function authenticateDeviceCredentials(
  promptMessage: string
): Promise<string> {
  return FingerprintAuth.authenticateDeviceCredentials(promptMessage);
}

export const fingerprintEmitter = new NativeEventEmitter(FingerprintAuth);
