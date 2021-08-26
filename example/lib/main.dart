import 'package:flutter/material.dart';

import 'package:platform_event_plugin/platform_event_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: StreamBuilder<String?>(
              stream: PlatformEventPlugin.streamLocation,
              builder: (context, snapshot) {
                return Text('Running on: ${snapshot.data}\n');
              }),
        ),
      ),
    );
  }
}
