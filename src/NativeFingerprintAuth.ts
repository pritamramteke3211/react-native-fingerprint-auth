import type { TurboModule } from 'react-native';
import { TurboModuleRegistry, NativeEventEmitter } from 'react-native';

// Define the Spec for the FingerprintAuth module
export interface Spec extends TurboModule {
  isFingerprintAvailable(): Promise<boolean>;
  authenticateFingerprint(reason: string): Promise<string>;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
  authenticateDeviceCredentials(promptMessage: string): Promise<string>;
  openSecuritySettings(): Promise<boolean>;
}

// Get the TurboModule (typed)
const FingerprintAuth =
  TurboModuleRegistry.getEnforcing<Spec>('FingerprintAuth');

// Create the event emitter for events emitted by the native module
export const fingerprintEmitter = new NativeEventEmitter(FingerprintAuth);

// Export the module as default
export default FingerprintAuth;
