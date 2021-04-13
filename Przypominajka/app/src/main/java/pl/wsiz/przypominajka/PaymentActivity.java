package pl.wsiz.przypominajka;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        SharedPreferences sharedPrefs = getSharedPreferences("sharedBank",0);
        int bankId = sharedPrefs.getInt("bankId",-1);

        final String receiver = Objects.requireNonNull(getIntent().getExtras()).getString("receiverP");
        final String account = Objects.requireNonNull(getIntent().getExtras()).getString("accountP");
        final String value = Objects.requireNonNull(getIntent().getExtras()).getString("valueP");
        assert account != null;
        String lastFourAccount = account.substring(22, 26);

        Button btnReceiver = (Button) findViewById(R.id.btnGetReceiverP);
        Button btnAcc = (Button) findViewById(R.id.btnGetAccP);
        Button btnValue = (Button) findViewById(R.id.btnGetValueP);
        TextView tvLastFour = (TextView) findViewById(R.id.tvLastAccP);
        tvLastFour.setText(lastFourAccount);

        WebView webView = (WebView) findViewById(R.id.webViewPayment);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(urlAddress(bankId));

        btnReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", receiver);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copyToast, Toast.LENGTH_SHORT).show();
            }
        });

        btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", account);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copyToast, Toast.LENGTH_SHORT).show();
            }
        });

        btnValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", value);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copyToast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String urlAddress(int profileBank) {
        String address;
        switch (profileBank) {
            case 0:
                address = "https://www.ipko.pl/";
                break;
            case 1:
                address = "https://www.pekao24.pl/";
                break;
            case 2:
                address = "https://www.santander.pl/klient-indywidualny/bankowosc-internetowa/santander-internet";
                break;
            case 3:
                address = "https://login.ingbank.pl/mojeing/app/#login";
                break;
            case 4:
                address = "https://online.mbank.pl/pl/Login";
                break;
            case 5:
                address = "https://goonline.bnpparibas.pl/#!/login";
                break;
            case 6:
                address = "https://www.bankmillennium.pl/logowanie";
                break;
            case 7:
                address = "https://system.aliorbank.pl/sign-in";
                break;
            case 8:
                address = "https://www.pocztowy24.pl/cbp-webapp/login";
                break;
            default:
                address = "https://www.google.com/";
        }
        return address;
    }
}
