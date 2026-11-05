package com.hisaabcalculator.app;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Nature BGM app khulte hi chalu ho, isliye WebView me
    // media ke liye user gesture ki zaroorat band kar rahe hain.
    getBridge().getWebView().getSettings().setMediaPlaybackRequiresUserGesture(false);
  }
}
