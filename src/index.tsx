const FingerprintAuth = require('./NativeFingerprintAuth').default;

export function multiply(a: number, b: number): number {
  return FingerprintAuth.multiply(a, b);
}
