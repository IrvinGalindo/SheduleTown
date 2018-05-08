package com.iogb.sheduletown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TeacherActivity extends AppCompatActivity {
    private TextView tv_name;
    private Spinner sp_days;
    private ListView lv_schedule;
    private Session session;
    private boolean trust;
    private AsyncHttpClient client;
    private RequestParams parameters;
    private Employee employee;
    private RelativeLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        session=new Session(this);
        if(!session.loggedIn()) {
            goToLogin();
            trust=true;
        }
        view=(RelativeLayout)findViewById(R.id.rl_view);
        tv_name=(TextView)findViewById(R.id.tv_name);
        sp_days=(Spinner)findViewById(R.id.sp_dia);
        lv_schedule = (ListView) findViewById(R.id.lv_horario);

        if (!trust)
        start();
    }



    private void start() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_down);
        sp_days.setAdapter(adapter);
        employee=new Gson().fromJson(session.getFullInfo(),Employee.class);
        tv_name.setText(employee.getFullName());
        parameters=new RequestParams();
        client = new AsyncHttpClient();
        sp_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gtSchedule();
                lv_schedule.setAdapter(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void gtSchedule() {
        String url="http://scheduletown18.x10host.com/gts/gtSchTea.php";
        String day=sp_days.getSelectedItem().toString();
        parameters.put("e",employee.getIdEmployee());
        parameters.put("d",day);
        client.post(url, parameters, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
           if (statusCode==200)
               gtJSONSchedule(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void gtJSONSchedule(String result) {
        ArrayList<TeacherList> arrayList=new ArrayList<TeacherList>();
        if (result.equals(""))
            Snackbar.make(view, R.string.without_hour,Snackbar.LENGTH_SHORT).show();
        else {
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        TeacherList teacherList = new TeacherList(jsonArray.getJSONObject(i).getString("numsalon"),
                                jsonArray.getJSONObject(i).getString("nombre"),jsonArray.getJSONObject(i).getString("hora"));
                        arrayList.add(teacherList);
                    }
                    lv_schedule.setAdapter(new AdapterItem(this,arrayList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mn_logout, menu);
            return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        session.clear();
        goToLogin();
        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        session.setLoggedIn(false,null);
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK   | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}
