package pl.wsiz.przypominajka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NoticeSummaryActivity extends AppCompatActivity {

    DatabaseHelperNotice dbNotice;
    EditText etBeforeSum, etLoopsSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_summary);

        dbNotice = new DatabaseHelperNotice(this);

        Bundle extrasNotice = getIntent().getExtras();
        assert extrasNotice != null;
        final String titleNotice = extrasNotice.getString("titleNot");
        final String dateNotice = extrasNotice.getString("dateNot");
        final String timeNotice = extrasNotice.getString("timeNot");
        int beforeNotice = extrasNotice.getInt("beforeNot");
        int loopsNotice = extrasNotice.getInt("loopsNot");
        final int indexOfThisNotice = extrasNotice.getInt("thisIndexNotice");

        EditText etTitleSum = (EditText) findViewById(R.id.titleNoticeSum);
        etTitleSum.setText(titleNotice);
        EditText etDateSum = (EditText) findViewById(R.id.dateNoticeSum);
        etDateSum.setText(dateNotice);
        EditText etTimeSum = (EditText) findViewById(R.id.timeNoticeSum);
        etTimeSum.setText(timeNotice);
        etBeforeSum = (EditText) findViewById(R.id.beforeNoticeSum);
        switchBefore(beforeNotice);
        etLoopsSum = (EditText) findViewById(R.id.loopsNoticeSum);
        switchLoops(loopsNotice);

        ImageButton noticeShareBtn = (ImageButton) findViewById(R.id.exportBtnNSum);
        noticeShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Tytuł: " + titleNotice + "\n\nData: " + dateNotice + "\n\nGodzina: " + timeNotice);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        Button btnDelete = (Button) findViewById(R.id.delBtnNSum);
        btnDelete.setBackground(ContextCompat.getDrawable(this, R.drawable.delete_ico));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Usunięto", Toast.LENGTH_LONG).show();
                dbNotice.deleteNoticeData(indexOfThisNotice);
                Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                startActivity(intent);
                finish();
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
