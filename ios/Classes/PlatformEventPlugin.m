#import "PlatformEventPlugin.h"
#if __has_include(<platform_event_plugin/platform_event_plugin-Swift.h>)
#import <platform_event_plugin/platform_event_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "platform_event_plugin-Swift.h"
#endif

@implementation PlatformEventPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPlatformEventPlugin registerWithRegistrar:registrar];
}
@end
