package com.pgeon.honeywell_scanner;

import androidx.annotation.NonNull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** HoneywellScannerPlugin */
public class HoneywellScannerPlugin implements FlutterPlugin, MethodCallHandler {
  private EventChannel receiveDecodedEventChannel;
  private Context mContext;
  public static final String TAG = "HoneywellScanner";
  private IntentFilter filter;
  ScanDevice sm;
  private final static String SCAN_ACTION = "scan.rcv.message";
  ScanBroadcastReceiver mScanReceiver;
  private String barcodeStr;
  private int startNum = 0;
  private int endNum = 0;
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private final Handler handler;
  private MethodChannel methodChannel;
  public EventChannel.EventSink mEvents;
  private EventChannel.StreamHandler eventStream;
  private static final String RECEIVE_DECODED_EVENT_CHANNEL = "event_channel/scanned_data";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    init(
        flutterPluginBinding.getApplicationContext(),
        flutterPluginBinding.getBinaryMessenger());
  }

  private void init(Context context, BinaryMessenger messenger) {
    this.mContext = context;
    methodChannel = new MethodChannel(messenger, "honeywell_scanner");
    methodChannel.setMethodCallHandler(this);
    sm = new ScanDevice();

    receiveDecodedEventChannel = new EventChannel(messenger, RECEIVE_DECODED_EVENT_CHANNEL);
    eventStream = new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object args, EventChannel.EventSink events) {
        mEvents = events;
        // mScanReceiver = new ScanBroadcastReceiver(events, sm);

      }

      @Override
      public void onCancel(Object args) {
        Log.w(TAG, "cancelling listener");
      }
    };
    receiveDecodedEventChannel.setStreamHandler(eventStream);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    if (call.method.equals("openScanner")) {
      mScanReceiver = new ScanBroadcastReceiver(mEvents, sm);
      sm.openScan();
      result.success(true);
    } else if (call.method.equals("closeScanner")) {
      sm.closeScan();
      result.success(true);
    } else if (call.method.equals("startDecode")) {
      sm.startScan();
      IntentFilter filter = new IntentFilter();
      filter.addAction(SCAN_ACTION);
      mContext.registerReceiver(mScanReceiver, filter);
      result.success(true);
    } else if (call.method.equals("stopDecode")) {
      sm.stopScan();
      mContext.unregisterReceiver(mScanReceiver);
      result.success(true);
    } else if (call.method.equals("startContinuous")) {
      sm.setScanLaserMode(4);
      result.success(true);
    } else if (call.method.equals("stopContinuous")) {
      sm.setScanLaserMode(8);
      result.success(true);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    methodChannel.setMethodCallHandler(null);
    if (eventStream != null && receiveDecodedEventChannel != null) {
      eventStream.onCancel(null);
      receiveDecodedEventChannel.setStreamHandler(null);
      eventStream = null;
    }
  }

  public HoneywellScannerPlugin() {
    handler = new Handler();
  }
}
