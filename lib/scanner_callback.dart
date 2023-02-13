
typedef OnScannerDecodeCallback = Function(String? scannedData);

/// Callback of the decoding process
abstract class ScannerCallback {
  /// Called when decoder has successfully decoded the code
  ///
  /// @param scannedData Encapsulates the result of decoding a barcode within an image
  void onDecoded(String? scannedData);

}
