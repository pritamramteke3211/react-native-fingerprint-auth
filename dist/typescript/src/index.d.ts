import { NativeEventEmitter } from 'react-native';
export declare function isFingerprintAvailable(): Promise<boolean>;
export declare function authenticateFingerprint(
  reason: string
): Promise<string>;
export declare function authenticateDeviceCredentials(
  promptMessage: string
): Promise<string>;
export declare const fingerprintEmitter: NativeEventEmitter;
//# sourceMappingURL=index.d.ts.map
