package com.pgeon.honeywell_scanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.device.ScanDevice;
import android.content.Intent;
import android.util.Log;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;

class ScanBroadcastReceiver extends BroadcastReceiver {
    private final static String SCAN_ACTION="scan.rcv.message";
    public static final String TAG = "HoneywellScanner";
    private EventChannel.EventSink events;
    private ScanDevice sm;
    public ScanBroadcastReceiver(EventChannel.EventSink events, ScanDevice sm) {
        this.events = events;
        this.sm = sm;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String barcodeStr;
        String action = intent.getAction ();
        if (action.equals (SCAN_ACTION)){
            byte[] barocode=intent.getByteArrayExtra ("barocode");
            int barocodelen=intent.getIntExtra ("length", 0);
            barcodeStr=new String (barocode, 0, barocodelen);
            sm.stopScan ();
            events.success(barcodeStr);
           
        }

    }
}