import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  isFingerprintAvailable(): Promise<boolean>;
  authenticateFingerprint(reason: string): Promise<string>;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

// export default TurboModuleRegistry.getEnforcing<Spec>('FingerprintAuth');

// Get the TurboModule (typed)
const FingerprintAuth =
  TurboModuleRegistry.getEnforcing<Spec>('FingerprintAuth');

// Create the event emitter
// export const fingerprintEmitter = new NativeEventEmitter(FingerprintAuth);

// Export the module as default
export default FingerprintAuth;
