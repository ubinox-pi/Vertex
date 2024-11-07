package com.vertex.io;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerSupport extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    WebView webView;
    LinearLayout linearLayout;
    String s ="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Vertex</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <script type=\"text/javascript\">\n" +
            "        var Tawk_API=Tawk_API||{}, Tawk_LoadStart=new Date();\n" +
            "        (function(){\n" +
            "        var s1=document.createElement(\"script\"),s0=document.getElementsByTagName(\"script\")[0];\n" +
            "        s1.async=true;\n" +
            "        s1.src='https://embed.tawk.to/6700bb1137379df10df207b4/default';\n" +
            "        s1.charset='UTF-8';\n" +
            "        s1.setAttribute('crossorigin','*');\n" +
            "        s0.parentNode.insertBefore(s1,s0);\n" +
            "        })();\n" +
            "    </script>        \n" +
            "</body>\n" +
            "</html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        toolbar = findViewById(R.id.action_app_bar);
        linearLayout = findViewById(R.id.points_layout);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        linearLayout.setVisibility(View.INVISIBLE);
        webView = findViewById(R.id.webView);
        webView.loadUrl("https://tawk.to/chat/6700bb1137379df10df207b4/default");
        webView.getSettings().setJavaScriptEnabled(true);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                webView.destroy();
            }
        });
    }


}