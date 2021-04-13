package pl.wsiz.przypominajka;

import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Objects;

public class ProfileActivity extends Fragment {

    private DatabaseHelperProfile db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileFragmentView = inflater.inflate(R.layout.activity_profile, container, false);

        db = new DatabaseHelperProfile(this.getContext());

        Button save = (Button) profileFragmentView.findViewById(R.id.profileSave);
        final EditText name = (EditText) profileFragmentView.findViewById(R.id.profileName);
        final EditText surname = (EditText) profileFragmentView.findViewById(R.id.profileSurname);
        final EditText email = (EditText) profileFragmentView.findViewById(R.id.profileEmail);
        final EditText phone = (EditText) profileFragmentView.findViewById(R.id.profilePhone);
        final EditText account = (EditText) profileFragmentView.findViewById(R.id.profileBankAccount);

        final Spinner bank = profileFragmentView.findViewById(R.id.profileBank);
        String[] bankNames = new String[]{"PKO Bank Polski", "Bank Pekao SA", "Santander Bank Polska", "ING Bank Śląski", "mBank", "Bank BNP Paribas", "Bank Millennium", "Alior Bank", "Bank Pocztowy", "Inny"};
        bank.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(),R.layout.spinner_item,bankNames));

        Cursor actualData = db.getPersonalData();
        while (actualData.moveToNext()) {
            name.setText(actualData.getString(1));
            surname.setText(actualData.getString(2));
            email.setText(actualData.getString(3));
            phone.setText(actualData.getString(4));
            bank.setSelection(actualData.getInt(5));
            account.setText(actualData.getString(6));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String col2 = name.getText().toString();
                String col3 = surname.getText().toString();
                String col4 = email.getText().toString();
                String col5 = phone.getText().toString();
                int col6 = bank.getSelectedItemPosition();
                String col7 = account.getText().toString();

                AddData(col2, col3, col4, col5, col6, col7);

                SharedPreferences sharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences("sharedBank",0);
                sharedPrefs.edit().putInt("bankId", col6).apply();
            }
        });
        return profileFragmentView;
    }

    private void AddData(String newData2, String newData3, String newData4, String newData5, int newData6, String newData7) {
        boolean insertData = db.addData(newData2, newData3, newData4, newData5, newData6, newData7);
        if(insertData)
            messageShow("Zapisano");
        else
            messageShow("Błąd");
    }

    private void messageShow(String mess) {
        Toast.makeText(this.getContext(), mess, Toast.LENGTH_SHORT).show();
    }
}