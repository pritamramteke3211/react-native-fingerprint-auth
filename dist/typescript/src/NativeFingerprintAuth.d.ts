import type { TurboModule } from 'react-native';
export interface Spec extends TurboModule {
  isFingerprintAvailable(): Promise<boolean>;
  authenticateFingerprint(reason: string): Promise<string>;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
  authenticateDeviceCredentials(promptMessage: string): Promise<string>;
}
declare const FingerprintAuth: Spec;
export default FingerprintAuth;
//# sourceMappingURL=NativeFingerprintAuth.d.ts.map
