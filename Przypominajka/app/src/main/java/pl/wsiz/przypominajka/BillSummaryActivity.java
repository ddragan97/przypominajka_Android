package pl.wsiz.przypominajka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class BillSummaryActivity extends AppCompatActivity {

    DatabaseHelperBills dbBills;
    EditText etBeforeSum, etLoopsSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_summary);
        dbBills = new DatabaseHelperBills(this);

        Bundle extrasBill = getIntent().getExtras();
        assert extrasBill != null;
        final String titleBills = extrasBill.getString("titleBill");
        final String dateBills = extrasBill.getString("dateBill");
        String timeBills = extrasBill.getString("timeBill");
        int beforeBills = extrasBill.getInt("beforeBill");
        int loopsBills = extrasBill.getInt("loopsBill");
        final String receiverBills = extrasBill.getString("receiverBill");
        final String accountBills = extrasBill.getString("accountBill");
        final String valueBills = extrasBill.getString("valueBill");
        final int indexOfThisBills = extrasBill.getInt("thisIndexBill");

        EditText etTitleSum = (EditText) findViewById(R.id.titleBillSum);
        etTitleSum.setText(titleBills);
        EditText etDateSum = (EditText) findViewById(R.id.dateBillSum);
        etDateSum.setText(dateBills);
        EditText etTimeSum = (EditText) findViewById(R.id.timeBillSum);
        etTimeSum.setText(timeBills);
        etBeforeSum = (EditText) findViewById(R.id.beforeBillSum);
        switchBefore(beforeBills);
        etLoopsSum = (EditText) findViewById(R.id.loopsBillSum);
        switchLoops(loopsBills);
        final EditText etReceiverSum = (EditText) findViewById(R.id.receiverBillSum);
        etReceiverSum.setText(receiverBills);
        final EditText etAccountSum = (EditText) findViewById(R.id.accountBillSum);
        etAccountSum.setText(accountBills);
        final EditText etValueSum = (EditText) findViewById(R.id.valueBillSum);
        etValueSum.setText(valueBills);

        Button btnDelete = (Button) findViewById(R.id.delBtnBSum);
        btnDelete.setBackground(ContextCompat.getDrawable(this, R.drawable.delete_ico));
        Button btnCopyReceier = (Button) findViewById(R.id.btnCopyReceiverBS);
        Button btnCopyAcc = (Button) findViewById(R.id.btnCopyAccBS);
        Button btnCopyValue = (Button) findViewById(R.id.btnCopyValueBS);
        btnCopyReceier.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_copy_c));
        btnCopyAcc.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_copy_c));
        btnCopyValue.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_copy_c));

        ImageButton BillShareBtn = (ImageButton) findViewById(R.id.exportBtnBSum);
        BillShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Tytuł: " + titleBills + "\n\nTermin płatności: " + dateBills + "\n\nNazwa odbiorcy: " + receiverBills + "\n\nNumer konta bankowego: " + accountBills + "\n\nNależność: " + valueBills);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        Button btnPay = (Button) findViewById(R.id.payBtnBSum);
        btnPay.setBackground(ContextCompat.getDrawable(this, R.drawable.dollar_ico));

        btnCopyReceier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", etReceiverSum.getText());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copyToast, Toast.LENGTH_SHORT).show();
            }
        });

        btnCopyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", etAccountSum.getText());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copyToast, Toast.LENGTH_SHORT).show();
            }
        });

        btnCopyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", etValueSum.getText());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copyToast, Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Usunięto", Toast.LENGTH_LONG).show();
                dbBills.deleteBillsData(indexOfThisBills);
                Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("receiverP", receiverBills);
                intent.putExtra("accountP", accountBills);
                intent.putExtra("valueP", valueBills);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchBefore(int index) {
        switch (index) {
            case 0:
                etBeforeSum.setText(R.string.spinnerOpt1);
                break;
            case 1:
                etBeforeSum.setText(R.string.spinnerOpt2);
                break;
            case 2:
                etBeforeSum.setText(R.string.spinnerOpt3);
                break;
            case 3:
                etBeforeSum.setText(R.string.spinnerOpt4);
                break;
            case 4:
                etBeforeSum.setText(R.string.spinnerOpt5);
                break;
            case 5:
                etBeforeSum.setText(R.string.spinnerOpt6);
                break;
            case 6:
                etBeforeSum.setText(R.string.spinnerOpt7);
                break;
            case 7:
                etBeforeSum.setText(R.string.spinnerOpt8);
                break;
        }
    }

    private void switchLoops(int index) {
        switch (index) {
            case 0:
                etLoopsSum.setText(R.string.onceAddNew);
                break;
            case 1:
                etLoopsSum.setText(R.string.dailyAddNew);
                break;
            case 2:
                etLoopsSum.setText(R.string.weeklyAddNew);
                break;
            case 3:
                etLoopsSum.setText(R.string.monthlyAddNew);
                break;
            case 4:
                etLoopsSum.setText(R.string.yearlyAddNew);
                break;
        }
    }
}