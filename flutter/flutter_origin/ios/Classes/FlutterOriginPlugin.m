#import "FlutterOriginPlugin.h"
#if __has_include(<flutter_origin/flutter_origin-Swift.h>)
#import <flutter_origin/flutter_origin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_origin-Swift.h"
#endif

@implementation FlutterOriginPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterOriginPlugin registerWithRegistrar:registrar];
}
@end
