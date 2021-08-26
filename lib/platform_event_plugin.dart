import 'dart:async';

import 'package:flutter/services.dart';

class PlatformEventPlugin {
  static const EventChannel _channel = const EventChannel('locationStatusStream');

  static Stream<String?> get streamLocation {
    return _channel.receiveBroadcastStream().map((event) => event as String);
  }
}
