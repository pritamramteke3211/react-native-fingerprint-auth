
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNFingerprintAuthSpec.h"

@interface FingerprintAuth : NSObject <NativeFingerprintAuthSpec>
#else
#import <React/RCTBridgeModule.h>

@interface FingerprintAuth : NSObject <RCTBridgeModule>
#endif

@end
