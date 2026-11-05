package com.hisaabcalculator.app;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Keep the existing nature sound autoplay setting.
    getBridge().getWebView().getSettings().setMediaPlaybackRequiresUserGesture(false);

    getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        if (getBridge() == null || getBridge().getWebView() == null) {
          passToAndroid();
          return;
        }
        // Reuse the existing Menu button, preserving all calculator values.
        getBridge().getWebView().evaluateJavascript(
          "(function(){if(window.hisaabCloseSettings && window.hisaabCloseSettings()){return true;}"
          + "var app=document.getElementById('appView');"
          + "var back=document.getElementById('backBtn');"
          + "if(app && !app.hidden && back){back.click();return true;}"
          + "return false;})()",
          result -> {
            if (!"true".equals(result)) {
              passToAndroid();
            }
          }
        );
      }

      private void passToAndroid() {
        // Already on the menu: preserve the normal Android Back action.
        setEnabled(false);
        try {
          getOnBackPressedDispatcher().onBackPressed();
        } finally {
          setEnabled(true);
        }
      }
    });
  }
}
