package com.iogb.sheduletown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class PrefectActivity extends AppCompatActivity {
    private TextView tv_rango;
    private Spinner sp_area;
    private ListView lv_schedule;
    private Session session;
    private boolean trust;
    private Employee employee;
    private RelativeLayout view;
    private ArrayList<String> areas;
    private Employee e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefect);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        session=new Session(this);
        if(!session.loggedIn()) {
            goToLogin();
            trust=true;
        }
        view=(RelativeLayout)findViewById(R.id.rl_view);
        tv_rango=(TextView)findViewById(R.id.tv_rango);
        sp_area=(Spinner)findViewById(R.id.sp_area);
        lv_schedule = (ListView) findViewById(R.id.lv_sh);

        if (!trust)
            start();
    }

    private void start() {
        employee=new Gson().fromJson(session.getFullInfo(),Employee.class);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_down);
        sp_area.setAdapter(adapter);
        tv_rango.setText(e.getFullName());

    }
    private void goToLogin() {
        session.setLoggedIn(false,null);
        session.clear();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK   | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public String countAreas() {
        Calendar now = Calendar.getInstance();
        String[] strDays = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};
        return strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
    }
}
