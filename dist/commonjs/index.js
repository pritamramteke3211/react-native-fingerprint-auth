'use strict';

Object.defineProperty(exports, '__esModule', {
  value: true,
});
exports.authenticateDeviceCredentials = authenticateDeviceCredentials;
exports.authenticateFingerprint = authenticateFingerprint;
exports.fingerprintEmitter = void 0;
exports.isFingerprintAvailable = isFingerprintAvailable;
var _reactNative = require('react-native');
const FingerprintAuth = require('./NativeFingerprintAuth').default;
function isFingerprintAvailable() {
  return FingerprintAuth.isFingerprintAvailable();
}
function authenticateFingerprint(reason) {
  return FingerprintAuth.authenticateFingerprint(reason);
}
function authenticateDeviceCredentials(promptMessage) {
  return FingerprintAuth.authenticateDeviceCredentials(promptMessage);
}
const fingerprintEmitter = (exports.fingerprintEmitter =
  new _reactNative.NativeEventEmitter(FingerprintAuth));
//# sourceMappingURL=index.js.map
