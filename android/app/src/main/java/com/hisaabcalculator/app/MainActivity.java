package com.hisaabcalculator.app;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.activity.OnBackPressedCallback;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    applySafeWindowInsets();
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

  private void applySafeWindowInsets() {
    // Explicitly own insets on SDK 36 instead of relying on legacy fitting.
    WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    int barColor = Color.rgb(5, 46, 22);
    getWindow().getDecorView().setBackgroundColor(barColor);
    getWindow().setStatusBarColor(barColor);
    getWindow().setNavigationBarColor(barColor);
    WindowInsetsControllerCompat controller =
      WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
    controller.setAppearanceLightStatusBars(false);
    controller.setAppearanceLightNavigationBars(false);

    View content = findViewById(android.R.id.content);
    content.setBackgroundColor(barColor);
    ViewCompat.setOnApplyWindowInsetsListener(content, (view, windowInsets) -> {
      Insets safe = windowInsets.getInsets(
        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
      );
      // Padding the native parent shrinks the entire WebView viewport, so
      // fixed-position controls, the splash and drawer are all kept safe.
      view.setPadding(safe.left, safe.top, safe.right, safe.bottom);
      // We applied these insets once; do not apply them again in descendants.
      return WindowInsetsCompat.CONSUMED;
    });
    ViewCompat.requestApplyInsets(content);
  }

}
