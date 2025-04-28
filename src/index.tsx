import { NativeEventEmitter } from 'react-native';

const FingerprintAuth = require('./NativeFingerprintAuth').default;

export function isFingerprintAvailable(): Promise<boolean> {
  return FingerprintAuth.isFingerprintAvailable();
}

export function authenticateFingerprint(reason: string): Promise<string> {
  return FingerprintAuth.authenticateFingerprint(reason);
}

export const fingerprintEmitter = new NativeEventEmitter(FingerprintAuth);
