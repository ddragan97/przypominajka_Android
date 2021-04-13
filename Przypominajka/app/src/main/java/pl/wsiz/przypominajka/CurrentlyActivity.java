package pl.wsiz.przypominajka;

import androidx.fragment.app.Fragment;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CurrentlyActivity extends Fragment {

    private DatabaseHelperNotice dbNotice;
    private DatabaseHelperBills dbBills;

    private ListViewAdapter listAdapter = null;
    private int lastIndexOfNotic = 0;
    private boolean toRefresh = false;

    private ArrayList<String> arrayListData = new ArrayList<>();
    private ArrayList<String> arrayIsNotice = new ArrayList<>();
    private ArrayList<Integer> arrayOfIdNotice = new ArrayList<>();
    private ArrayList<Integer> arrayOfIdBills = new ArrayList<>();

    private ArrayList<String> arrayTitleC = new ArrayList<>();
    private ArrayList<String> arrayDateC = new ArrayList<>();
    private ArrayList<String> arrayTimeC = new ArrayList<>();
    private ArrayList<Integer> arrayBeforeC = new ArrayList<>();
    private ArrayList<Integer> arrayLoopsC = new ArrayList<>();

    private ArrayList<String> arrayTitleBill = new ArrayList<>();
    private ArrayList<String> arrayDateBill = new ArrayList<>();
    private ArrayList<String> arrayTimeBill = new ArrayList<>();
    private ArrayList<Integer> arrayBeforeBill = new ArrayList<>();
    private ArrayList<Integer> arrayLoopsBill = new ArrayList<>();
    private ArrayList<String> arrayReceiverBill = new ArrayList<>();
    private ArrayList<String> arrayAccountBill = new ArrayList<>();
    private ArrayList<String> arrayValueBill = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View CurrentlyFragmentView = inflater.inflate(R.layout.activity_currently, container, false);
        ListView listView = (ListView) CurrentlyFragmentView.findViewById(R.id.currentlyListView);
        dbNotice = new DatabaseHelperNotice(this.getContext());
        dbBills = new DatabaseHelperBills(this.getContext());

        listAdapter = new ListViewAdapter();
        listView.setAdapter(listAdapter);

        try {
            setCurrentlyContent();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayIsNotice.get(position).equals("yes"))
                {
                    Intent intent = new Intent(getContext(), NoticeSummaryActivity.class);
                    intent.putExtra("titleNot", arrayTitleC.get(position));
                    intent.putExtra("dateNot", arrayDateC.get(position));
                    intent.putExtra("timeNot", arrayTimeC.get(position));
                    intent.putExtra("beforeNot", arrayBeforeC.get(position));
                    intent.putExtra("loopsNot", arrayLoopsC.get(position));
                    intent.putExtra("thisIndexNotice", arrayOfIdNotice.get(position));
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }
                else if (arrayIsNotice.get(position).equals("no")) {
                    Intent intent = new Intent(getContext(), BillSummaryActivity.class);
                    intent.putExtra("titleBill", arrayTitleBill.get(position-lastIndexOfNotic));
                    intent.putExtra("dateBill", arrayDateBill.get(position-lastIndexOfNotic));
                    intent.putExtra("timeBill", arrayTimeBill.get(position-lastIndexOfNotic));
                    intent.putExtra("beforeBill", arrayBeforeBill.get(position-lastIndexOfNotic));
                    intent.putExtra("loopsBill", arrayLoopsBill.get(position-lastIndexOfNotic));
                    intent.putExtra("receiverBill", arrayReceiverBill.get(position-lastIndexOfNotic));
                    intent.putExtra("accountBill", arrayAccountBill.get(position-lastIndexOfNotic));
                    intent.putExtra("valueBill", arrayValueBill.get(position-lastIndexOfNotic));
                    intent.putExtra("thisIndexBill", arrayOfIdBills.get(position-lastIndexOfNotic));
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });
        checkIfFirstLaunch();
        return  CurrentlyFragmentView;
    }

    private void setCurrentlyContent() throws ParseException {
        Cursor dataNotice = dbNotice.getNoticeData();
        Cursor dataBills = dbBills.getBillsData();
        boolean checked = false;
        while(dataNotice.moveToNext()) {
            int idNoticeToCheck = dataNotice.getInt(0);
            String dateNoticeToCheck = dataNotice.getString(2);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
            Date dateNotice = simpleDateFormat.parse(dateNoticeToCheck);
            assert dateNotice != null;
            long dateTimestamp = dateNotice.getTime();

            String curDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());
            SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
            Date curDateCh = dateFormat.parse(curDate);
            assert curDateCh != null;
            long curDateTimestamp = curDateCh.getTime();

            if(dateTimestamp < curDateTimestamp)
            {
                toRefresh = true;
                int nLoops = dataNotice.getInt(5);
                if(nLoops > 0) {
                    addNextlyNotice(dataNotice.getString(1), dataNotice.getString(2), dataNotice.getString(3), dataNotice.getInt(4), nLoops);
                }
                dbNotice.deleteNoticeData(idNoticeToCheck);
                checked = false;
            }
            else
                checked = true;

            if(checked) {
                arrayIsNotice.add("yes");
                arrayOfIdNotice.add(dataNotice.getInt(0));
                arrayListData.add(dataNotice.getString(1));
                arrayTitleC.add(dataNotice.getString(1));
                arrayDateC.add(dataNotice.getString(2));
                arrayTimeC.add(dataNotice.getString(3));
                arrayBeforeC.add(dataNotice.getInt(4));
                arrayLoopsC.add(dataNotice.getInt(5));
                lastIndexOfNotic++;
            }
        }
        while (dataBills.moveToNext()) {
            int idBillToCheck = dataBills.getInt(0);
            String dateBillToCheck = dataBills.getString(2);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
            Date dateBill = simpleDateFormat.parse(dateBillToCheck);
            assert dateBill != null;
            long dateTimestamp = dateBill.getTime();

            String curDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());
            SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
            Date curDateCh = dateFormat.parse(curDate);
            assert curDateCh != null;
            long curDateTimestamp = curDateCh.getTime();

            if(dateTimestamp < curDateTimestamp)
            {
                toRefresh = true;
                int nLoops = dataBills.getInt(5);
                if(nLoops > 0) {
                    addNextlyBill(dataBills.getString(1), dataBills.getString(2), dataBills.getString(3), dataBills.getInt(4), nLoops, dataBills.getString(6), dataBills.getString(7), dataBills.getString(8));
                }
                dbBills.deleteBillsData(idBillToCheck);
                checked = false;
            }
            else
                checked = true;

            if(checked) {
                arrayIsNotice.add("no");
                arrayOfIdBills.add(dataBills.getInt(0));
                arrayListData.add(dataBills.getString(1));
                arrayTitleBill.add(dataBills.getString(1));
                arrayDateBill.add(dataBills.getString(2));
                arrayTimeBill.add(dataBills.getString(3));
                arrayBeforeBill.add(dataBills.getInt(4));
                arrayLoopsBill.add(dataBills.getInt(5));
                arrayReceiverBill.add(dataBills.getString(6));
                arrayAccountBill.add(dataBills.getString(7));
                arrayValueBill.add(dataBills.getString(8));
            }
        }
    }

    private void addNextlyNotice(String nTitle, String nDate, String nTime, int nBefore, int nLoops) throws ParseException {
        String newDate="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        Date dateNotice = simpleDateFormat.parse(nDate);
        assert dateNotice != null;
        long dateTimestamp = dateNotice.getTime();

        switch (nLoops) {
            case 1:
                dateTimestamp += 86400000;
                newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                break;
            case 2:
                for(int i = 0; i < 7; i++)
                    dateTimestamp += 86400000;
                newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                break;
            case 3:
                int curYear3 = Calendar.getInstance().get(Calendar.YEAR);
                int curMonth3 = Calendar.getInstance().get(Calendar.MONTH);
                if(curMonth3 == 3 || curMonth3 == 5 || curMonth3 == 7 || curMonth3 == 8 || curMonth3 == 10 || curMonth3 == 12 || curMonth3 == 1)
                {
                    for(int i = 0; i < 31; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else if (curMonth3 == 4 || curMonth3 == 6 || curMonth3 == 9 || curMonth3 == 11)
                {
                    for(int i = 0; i < 30; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else if(curMonth3 == 2 && curYear3 % 4 == 0)
                {
                    for(int i = 0; i < 29; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else if(curMonth3 == 2)
                {
                    for(int i = 0; i < 28; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                break;
            case 4:
                int curYear = Calendar.getInstance().get(Calendar.YEAR);
                int curMonth = Calendar.getInstance().get(Calendar.MONTH);
                if((curYear % 4 == 0 && curMonth < 3) || ((curYear+1) % 4 == 0 && curMonth > 2)){
                    for(int i = 0; i < 366; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else {
                    for(int i = 0; i < 365; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                break;
        }
        dbNotice.addNoticeData(nTitle, newDate, nTime, nBefore, nLoops);
        createNoticeCh(nTitle, newDate, nTime);
        createNotice(nTitle, newDate, nTime, nBefore);
    }

    private void addNextlyBill(String nTitle, String nDate, String nTime, int nBefore, int nLoops, String nReceiver, String nAccount, String nValue) throws ParseException {
        String newDate="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        Date dateBill = simpleDateFormat.parse(nDate);
        assert dateBill != null;
        long dateTimestamp = dateBill.getTime();

        switch (nLoops) {
            case 1:
                dateTimestamp += 86400000;
                newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                break;
            case 2:
                for(int i = 0; i < 7; i++)
                    dateTimestamp += 86400000;
                newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                break;
            case 3:
                int curYear3 = Calendar.getInstance().get(Calendar.YEAR);
                int curMonth3 = Calendar.getInstance().get(Calendar.MONTH);
                if(curMonth3 == 3 || curMonth3 == 5 || curMonth3 == 7 || curMonth3 == 8 || curMonth3 == 10 || curMonth3 == 12 || curMonth3 == 1)
                {
                    for(int i = 0; i < 31; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else if (curMonth3 == 4 || curMonth3 == 6 || curMonth3 == 9 || curMonth3 == 11)
                {
                    for(int i = 0; i < 30; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else if(curMonth3 == 2 && curYear3 % 4 == 0)
                {
                    for(int i = 0; i < 29; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else if(curMonth3 == 2)
                {
                    for(int i = 0; i < 28; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                break;
            case 4:
                int curYear = Calendar.getInstance().get(Calendar.YEAR);
                int curMonth = Calendar.getInstance().get(Calendar.MONTH);
                if((curYear % 4 == 0 && curMonth < 3) || ((curYear+1) % 4 == 0 && curMonth > 2)){
                    for(int i = 0; i < 366; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                else {
                    for(int i = 0; i < 365; i++)
                        dateTimestamp += 86400000;
                    newDate = DateFormat.format("d-M-yyyy", dateTimestamp).toString();
                }
                break;
        }
        dbBills.addBillsData(nTitle, newDate, nTime, nBefore, nLoops, nReceiver, nAccount, nValue);
        createNoticeCh(nTitle, newDate, nTime);
        createNotice(nTitle, newDate, nTime, nBefore);
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

    private void createNotice(String nameNoticeS, String dateNoticeS, String timeNoticeS, int beforeNoticeS) {
        Intent intentNoticeBC = new Intent(this.getContext(), NoticeBroadCast.class);
        intentNoticeBC.putExtra("titll", nameNoticeS);
        intentNoticeBC.putExtra("datee", dateNoticeS);
        intentNoticeBC.putExtra("timee", timeNoticeS);
        int idd = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getContext(), idd, intentNoticeBC, 0);
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        Date dateNotice = null;
        try {
            dateNotice = format.parse(dateNoticeS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateNotice != null;
        long dateTimestamp = dateNotice.getTime();

        SimpleDateFormat format2 = new SimpleDateFormat("H:mm", Locale.getDefault());
        Date timeNotice = null;
        try {
            timeNotice = format2.parse(timeNoticeS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert timeNotice != null;
        long timeTimestamp = timeNotice.getTime();

        long timestampNotice = dateTimestamp + timeTimestamp + 3600000 - getBeforeTS(beforeNoticeS);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestampNotice, pendingIntent);
    }

    private void createNoticeCh(String nameNotice, String dateNotice, String timeNotice) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = dateNotice + " " + timeNotice;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyChannel", nameNotice, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        private LayoutInflater listViewInflater;
        ListViewAdapter() {
            listViewInflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayListData.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null)
            {
                convertView = listViewInflater.inflate(R.layout.currently_item, parent, false);
                holder = new ViewHolder();
                holder.titleOfThis = (TextView) convertView.findViewById(R.id.tvItemTitle);
                holder.dateTimeOfThis = (TextView) convertView.findViewById(R.id.tvItemDateTime);
                holder.valueOfThis = (TextView) convertView.findViewById(R.id.tvItemValue);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.titleOfThis.setText(arrayListData.get(position));
            String dateThis="", timeThis="", dateTimeThis, valueThis="", valueBill="";
            if(arrayIsNotice.get(position).equals("yes"))
            {
                dateThis = arrayDateC.get(position);
                timeThis = arrayTimeC.get(position);
            }
            else if(arrayIsNotice.get(position).equals("no"))
            {
                dateThis = arrayDateBill.get(position-lastIndexOfNotic);
                timeThis = arrayTimeBill.get(position-lastIndexOfNotic);
                valueBill = arrayValueBill.get(position-lastIndexOfNotic);
                if(valueBill.equals(""))
                    valueBill = "0";
                valueThis = getString(R.string.kwotaValueThisC) + valueBill + getString(R.string.zlValueThisC);
            }
            dateTimeThis = getString(R.string.dataDateTimeThisC) + dateThis + ", " + timeThis;
            holder.dateTimeOfThis.setText(dateTimeThis);
            holder.valueOfThis.setText(valueThis);
            return convertView;
        }

        private class ViewHolder {
            TextView titleOfThis;
            TextView dateTimeOfThis;
            TextView valueOfThis;
        }
    }

    private void checkIfFirstLaunch() {
        if(toRefresh)
        {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getContext()),HelloActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
            getActivity().overridePendingTransition(0, 0);
        }
    }
}