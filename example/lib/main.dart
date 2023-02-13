import 'dart:async';

import 'package:flutter/material.dart';
import 'package:honeywell_scanner/honeywell_scanner.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _messangerKey = GlobalKey<ScaffoldMessengerState>();
  StreamSubscription? _scannerSubscription;

  @override
  void initState() {
    super.initState();
    initializeScanner();
  }

  initializeScanner() async {
    _scannerSubscription = HoneywellScanner.receiveDecoded.listen((data) {
      _messangerKey.currentState?.showSnackBar(SnackBar(content: Text(data)));
    });
  }

  @override
  void dispose() {
    _scannerSubscription!.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      scaffoldMessengerKey: _messangerKey,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Honeywell Scanner Example'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                  onPressed: () async {
                    await HoneywellScanner.openScanner;
                  },
                  child: const Text('Open Scanner')),
              ElevatedButton(
                  onPressed: () async {
                    await HoneywellScanner.closeScanner;
                  },
                  child: const Text('Close Scanner')),
              ElevatedButton(
                  onPressed: () async {
                    await HoneywellScanner.startDecode;
                  },
                  child: const Text('Start Decoode')),
              ElevatedButton(
                  onPressed: () async {
                    await HoneywellScanner.stopDecode;
                  },
                  child: const Text('Stop Decode')),
              ElevatedButton(
                  onPressed: () async {
                    await HoneywellScanner.startContinuous;
                  },
                  child: const Text('Start Continuous')),
              ElevatedButton(
                  onPressed: () async {
                    await HoneywellScanner.stopContinuous;
                  },
                  child: const Text('Stop Continuous')),
            ],
          ),
        ),
      ),
    );
  }
}
