'use strict';

import { NativeEventEmitter } from 'react-native';
const FingerprintAuth = require('./NativeFingerprintAuth').default;
export function isFingerprintAvailable() {
  return FingerprintAuth.isFingerprintAvailable();
}
export function authenticateFingerprint(reason) {
  return FingerprintAuth.authenticateFingerprint(reason);
}
export function authenticateDeviceCredentials(promptMessage) {
  return FingerprintAuth.authenticateDeviceCredentials(promptMessage);
}
export const fingerprintEmitter = new NativeEventEmitter(FingerprintAuth);
//# sourceMappingURL=index.js.map
