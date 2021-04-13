package pl.wsiz.przypominajka;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddNewActivity extends Fragment {

    private DatabaseHelperNotice dbNotice;
    private DatabaseHelperBills dbBills;

    private EditText etSetDate, etSetTime, title, billName, billAcc, billValue;
    private int setYear, setMonth, setDay, setHour, setMinute, col5;
    private LinearLayout changedLayoutFragment;
    private String col2, col3, col4, which;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View AddNewFragmentView = inflater.inflate(R.layout.activity_add_new, container, false);

        dbNotice = new DatabaseHelperNotice(this.getContext());
        dbBills = new DatabaseHelperBills(this.getContext());

        title = (EditText) AddNewFragmentView.findViewById(R.id.titleNewPrzyp);

        Button btnSetDate = (Button) AddNewFragmentView.findViewById(R.id.btnSetDateAddNewPrzyp);
        etSetDate = (EditText) AddNewFragmentView.findViewById(R.id.dateAddNewPrzyp);
        Button btnSetTime = (Button) AddNewFragmentView.findViewById(R.id.btnSetTimeAddNewPrzyp);
        etSetTime = (EditText) AddNewFragmentView.findViewById(R.id.timeAddNewPrzyp);
        etSetTime.setEnabled(false);
        etSetDate.setEnabled(false);
        changedLayoutFragment = (LinearLayout) AddNewFragmentView.findViewById(R.id.changedLLAddNew);

        Button helpBtn = (Button) AddNewFragmentView.findViewById(R.id.btnHelpAddNew);
        Button addBtn = (Button) AddNewFragmentView.findViewById(R.id.btnAddAddNew);
        Button clearBtn = (Button) AddNewFragmentView.findViewById(R.id.btnClearAddNew);

        billName = new EditText(getContext());
        billAcc = new EditText(getContext());
        billValue = new EditText(getContext());

        final Spinner timeBefore = AddNewFragmentView.findViewById(R.id.showAtSpinnerAddNew);
        String[] timeBeforeOptions = new String[]{getString(R.string.spinnerOpt1), getString(R.string.spinnerOpt2), getString(R.string.spinnerOpt3), getString(R.string.spinnerOpt4), getString(R.string.spinnerOpt5), getString(R.string.spinnerOpt6), getString(R.string.spinnerOpt7), getString(R.string.spinnerOpt8)};
        timeBefore.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(),R.layout.spinner_item,timeBeforeOptions));

        final RadioButton payment = AddNewFragmentView.findViewById(R.id.rodzajPlatnoscAddNew);
        final RadioButton notice = AddNewFragmentView.findViewById(R.id.rodzajPrzypomnienieAddNew);

        final Spinner loopsCount = new Spinner(getContext());
        final Spinner loopsCountNotice = new Spinner(getContext());

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                setYear = calendar.get(Calendar.YEAR);
                setMonth = calendar.get(Calendar.MONTH);
                setDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etSetDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, setYear, setMonth, setDay);
                datePickerDialog.show();
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                setHour = calendar.get(Calendar.HOUR_OF_DAY);
                setMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etSetTime.setText(hourOfDay + ":" + minute);
                    }
                }, setHour, setMinute, true);
                timePickerDialog.show();
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedLayoutFragment.removeAllViews();
                which = "payment";

                LinearLayout.LayoutParams billParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                billParams.setMargins(0,20,0,0);

                TextView billNameLabel = new TextView(getContext());
                billNameLabel.setLayoutParams(billParams);
                billNameLabel.setText(R.string.odbiorcaAddNew);
                billNameLabel.setTypeface(null, Typeface.BOLD);
                billNameLabel.setGravity(Gravity.CENTER);

                billName.setLayoutParams(billParams);
                billName.setHint(R.string.nazwaOdbiorcyAddNew);
                billName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                billName.setGravity(Gravity.CENTER);
                billName.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_add_fields));

                TextView billAccLabel = new TextView(getContext());
                billAccLabel.setLayoutParams(billParams);
                billAccLabel.setText(R.string.nrKontaAddNew);
                billAccLabel.setTypeface(null, Typeface.BOLD);
                billAccLabel.setGravity(Gravity.CENTER);

                billAcc.setLayoutParams(billParams);
                billAcc.setHint(R.string.billAccAddNew);
                billAcc.setInputType(InputType.TYPE_CLASS_NUMBER);
                billAcc.setGravity(Gravity.CENTER);
                billAcc.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_add_fields));

                TextView billValueLabel = new TextView(getContext());
                billValueLabel.setLayoutParams(billParams);
                billValueLabel.setText(R.string.valueBillAddNew);
                billValueLabel.setTypeface(null, Typeface.BOLD);
                billValueLabel.setGravity(Gravity.CENTER);

                billValue.setLayoutParams(billParams);
                billValue.setHint(R.string.valBAddNew);
                billValue.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                billValue.setGravity(Gravity.CENTER);
                billValue.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_add_fields));

                billParams.setMargins(0, 20, 0, 20);
                TextView loopsCountLabel = new TextView(getContext());
                loopsCountLabel.setLayoutParams(billParams);
                loopsCountLabel.setText(R.string.loopsCAddNew);
                loopsCountLabel.setTypeface(null, Typeface.BOLD);
                loopsCountLabel.setGravity(Gravity.CENTER);

                ArrayList<String> loopsCountArray = new ArrayList<>();
                loopsCountArray.add(getString(R.string.onceAddNew));
                loopsCountArray.add(getString(R.string.dailyAddNew));
                loopsCountArray.add(getString(R.string.weeklyAddNew));
                loopsCountArray.add(getString(R.string.monthlyAddNew));
                loopsCountArray.add(getString(R.string.yearlyAddNew));

                ArrayAdapter<String> loopsCountAdapter  = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, loopsCountArray);
                loopsCount.setAdapter(loopsCountAdapter);
                loopsCount.setBackgroundResource(R.drawable.bg_spinner);

                changedLayoutFragment.addView(billNameLabel);
                changedLayoutFragment.addView(billName);
                changedLayoutFragment.addView(billAccLabel);
                changedLayoutFragment.addView(billAcc);
                changedLayoutFragment.addView(billValueLabel);
                changedLayoutFragment.addView(billValue);
                changedLayoutFragment.addView(loopsCountLabel);
                changedLayoutFragment.addView(loopsCount);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedLayoutFragment.removeAllViews();
                which = "notice";

                TextView loopTimes = new TextView(getContext());
                LinearLayout.LayoutParams noticeParamsTV = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                noticeParamsTV.setMargins(0, 0, 0, 20);
                loopTimes.setLayoutParams(noticeParamsTV);
                loopTimes.setTypeface(null, Typeface.BOLD);
                loopTimes.setText(R.string.iloscPowtAddNew);
                loopTimes.setGravity(Gravity.CENTER);

                ArrayList<String> loopsCountNoticeArray = new ArrayList<>();
                loopsCountNoticeArray.add(getString(R.string.onceAddNew));
                loopsCountNoticeArray.add(getString(R.string.dailyAddNew));
                loopsCountNoticeArray.add(getString(R.string.weeklyAddNew));
                loopsCountNoticeArray.add(getString(R.string.monthlyAddNew));
                loopsCountNoticeArray.add(getString(R.string.yearlyAddNew));

                ArrayAdapter<String> loopsCountNoticeAdapter  = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, loopsCountNoticeArray);
                loopsCountNotice.setAdapter(loopsCountNoticeAdapter);
                loopsCountNotice.setBackgroundResource(R.drawable.bg_spinner);

                changedLayoutFragment.addView(loopTimes);
                changedLayoutFragment.addView(loopsCountNotice);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getContext()).getApplicationContext(),HelpAddingActivity.class);
                startActivity(intent);
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().equals("") && !etSetDate.getText().toString().equals("") && !etSetTime.getText().toString().equals("")) {
                    col3 = etSetDate.getText().toString();
                    col4 = etSetTime.getText().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                    Date dateNotice = null;
                    try {
                        dateNotice = simpleDateFormat.parse(col3);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    assert dateNotice != null;
                    long dateTimestamp = dateNotice.getTime();

                    SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("H:mm", Locale.getDefault());
                    Date dateNoticeTime = null;
                    try {
                        dateNoticeTime = simpleDateFormatTime.parse(col4);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    assert dateNoticeTime != null;
                    long timeTimestamp = dateNoticeTime.getTime();
                    int timezoneLatency = 3660000;
                    int milisecondsBeforeEvent = 1200000;
                    long timestampNotice = dateTimestamp + timeTimestamp + timezoneLatency;
                    long timestampCur = System.currentTimeMillis();
                    timestampCur += milisecondsBeforeEvent;

                    if(timestampCur > timestampNotice) {
                        Toast.makeText(getContext(), R.string.timeSetErrAN, Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (which.equals("payment")) {
                            col2 = title.getText().toString();
                            col3 = etSetDate.getText().toString();
                            col4 = etSetTime.getText().toString();
                            col5 = timeBefore.getSelectedItemPosition();
                            int col6 = loopsCount.getSelectedItemPosition();
                            String col7 = billName.getText().toString();
                            String col8 = billAcc.getText().toString();
                            String col9 = billValue.getText().toString();

                            if(col8.length() != 26) {
                                Toast.makeText(getContext(), R.string.accNumsToastAN, Toast.LENGTH_LONG).show();
                            }
                            else {

                                AddDataBills(col2, col3, col4, col5, col6, col7, col8, col9);
                                createNoticeCh();
                                createNotice();

                                Intent intent = new Intent(getContext(), HelloActivity.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        } else if (which.equals("notice")) {
                            col2 = title.getText().toString();
                            col3 = etSetDate.getText().toString();
                            col4 = etSetTime.getText().toString();
                            col5 = timeBefore.getSelectedItemPosition();
                            int col6 = loopsCountNotice.getSelectedItemPosition();

                            AddDataNotice(col2, col3, col4, col5, col6);
                            createNoticeCh();
                            createNotice();

                            Intent intent = new Intent(getContext(), HelloActivity.class);
                            startActivity(intent);
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    }
                }
                else
                    Toast.makeText(getContext(), R.string.notAllCompleteAN, Toast.LENGTH_LONG).show();
                }
        });
        return AddNewFragmentView;
    }

    private int getBeforeTS(int whichSelected) {
        int whichS = 0;
        switch (whichSelected) {
            case 0:
                whichS = 1000 * 60 * 15;
                break;
            case 1:
                whichS = 1000 * 60 * 60;
                break;
            case 2:
                whichS = 1000 * 60 * 60 * 3;
                break;
            case 3:
                whichS = 1000 * 60 * 60 * 6;
                break;
            case 4:
                whichS = 1000 * 60 * 60 * 12;
                break;
            case 5:
                whichS = 1000 * 60 * 60 * 24;
                break;
            case 6:
                whichS = 1000 * 60 * 60 * 24 * 3;
                break;
            case 7:
                whichS = 1000 * 60 * 60 * 24 * 7;
                break;
        }
        return whichS;
    }

    private void createNotice() {
        Intent intentNoticeBC = new Intent(this.getContext(), NoticeBroadCast.class);
        intentNoticeBC.putExtra("titll", col2);
        intentNoticeBC.putExtra("datee", col3);
        intentNoticeBC.putExtra("timee", col4);
        int idd = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getContext(), idd, intentNoticeBC, 0);
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        Date dateNotice = null;
        try {
            dateNotice = format.parse(col3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateNotice != null;
        long dateTimestamp = dateNotice.getTime();

        SimpleDateFormat format2 = new SimpleDateFormat("H:mm", Locale.getDefault());
        Date timeNotice = null;
        try {
            timeNotice = format2.parse(col4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert timeNotice != null;
        long timeTimestamp = timeNotice.getTime();

        long timestampNotice = dateTimestamp + timeTimestamp + 3600000 - getBeforeTS(col5);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestampNotice, pendingIntent);
    }

    private void createNoticeCh() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = col2;
            String description = col3 + " " + col4;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void AddDataNotice(String newData2, String newData3, String newData4, int newData5, int newData6) {
        boolean insertData = dbNotice.addNoticeData(newData2, newData3, newData4, newData5, newData6);
        if(insertData)
            messageShow(getString(R.string.correctSaveBtnAN));
        else
            messageShow(getString(R.string.notSaveBtnAN));
    }

    private void AddDataBills(String newData2, String newData3, String newData4, int newData5, int newData6, String newData7, String newData8, String newData9) {
        boolean insertData = dbBills.addBillsData(newData2, newData3, newData4, newData5, newData6, newData7, newData8, newData9);
        if(insertData)
            messageShow(getString(R.string.correctSaveBtnAN));
        else
            messageShow(getString(R.string.notSaveBtnAN));
    }

    private void messageShow(String mess) {
        Toast.makeText(this.getContext(), mess, Toast.LENGTH_SHORT).show();
    }

    private void clearFields () {
        title.setText("");
        etSetDate.setText("");
        etSetTime.setText("");
        billName.setText("");
        billAcc.setText("");
        billValue.setText("");
        Toast.makeText(this.getContext(), R.string.clrAddNewBtn, Toast.LENGTH_SHORT).show();
    }
}