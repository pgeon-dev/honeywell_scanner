import 'dart:async';

import 'package:flutter/services.dart';

class HoneywellScanner {
  static const MethodChannel _channel = MethodChannel('honeywell_scanner');

  static const EventChannel _eventChannel =
      EventChannel('event_channel/scanned_data');

  static Future<bool?> get openScanner async {
    final bool? success = await _channel.invokeMethod('openScanner');
    return success;
  }

  static Future<bool?> get closeScanner async {
    final bool? success = await _channel.invokeMethod('closeScanner');
    return success;
  }

  static Future<bool?> get startDecode async {
    final bool? success = await _channel.invokeMethod('startDecode');
    return success;
  }

  static Future<bool?> get stopDecode async {
    final bool? success = await _channel.invokeMethod('stopDecode');
    return success;
  }

  static Future<bool?> get startContinuous async {
    final bool? success = await _channel.invokeMethod('startContinuous');
    return success;
  }

  static Future<bool?> get stopContinuous async {
    final bool? success = await _channel.invokeMethod('stopContinuous');
    return success;
  }

  static Stream<String> get receiveDecoded {
    return _eventChannel.receiveBroadcastStream().cast();
  }
}
