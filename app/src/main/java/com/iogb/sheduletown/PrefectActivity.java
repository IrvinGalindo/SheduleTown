package com.iogb.sheduletown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class PrefectActivity extends AppCompatActivity {
    private TextView tv_rango;
    private Spinner sp_area;
    private ListView lv_schedule;
    private Session session;
    private boolean trust;
    private AsyncHttpClient client;
    private RequestParams params;
    private Employee employee;
    private RelativeLayout view;
    private ArrayList<Integer> areas;
    private String fechaComplString,normalTime;
    private Date d;
    private ArrayList<PrefectList> prefectLists;
    private Button btn_send;
    private SimpleDateFormat ho;
    private AdapterItemPrefect ap;
    private AdapterItemPrefectOther apo;
    private int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefect);

        session=new Session(this);
        if(!session.loggedIn()) {
            goToLogin();
            trust=true;
        }
        view=(RelativeLayout)findViewById(R.id.rl_prefect);
        tv_rango=(TextView)findViewById(R.id.tv_rango);
        sp_area=(Spinner)findViewById(R.id.sp_area);
        lv_schedule = (ListView) findViewById(R.id.lv_sh);
        btn_send = (Button)findViewById(R.id.btn_send);

        if (!trust)
            start();
    }

    private void start() {
        areas=new ArrayList<Integer>();
        cont=0;
        fechaComplString="";
        normalTime="";
        employee=new Gson().fromJson(session.getFullInfo(),Employee.class);
        client = new AsyncHttpClient();
        params = new RequestParams();
        gtAllTea();
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefectLists=null;
                prefectLists=new ArrayList<PrefectList>();
                gtAllSche("http://scheduletown18.x10host.com/gts/gtAllTea.php");
                lv_schedule.setAdapter(null);
                ap=null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_check);
                cb.performClick();
                if (cb.isChecked()) {
                    PrefectList pl = (PrefectList) parent.getItemAtPosition(position);
                    pl.setAssistents(1);
                    ap.notifyDataSetChanged();
                }
                else {
                    PrefectList pl = (PrefectList) parent.getItemAtPosition(position);
                    pl.setAssistents(0);
                    ap.notifyDataSetChanged();
                }
            }

        });
       btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAssistens(ap);
                mensaje(getString(R.string.send_information));
                if(ap.getCount()>0)
                ap.clear();
            }
        });


    }

    private void sendAssistens(final AdapterItemPrefect ap) {
        String url = "http://scheduletown18.x10host.com/upd/InAss.php";

        for (int i = 0; i < prefectLists.size(); i++) {
           params = new RequestParams();
            PrefectList pl = prefectLists.get(i);
            params.put("idE",pl.getIdEmpleado());
            params.put("idH",pl.getIdHorario());
            params.put("h",trackTime(d)+":00");
            params.put("a",pl.isAssistents());
            params.put("d",fechaComplString);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    ap.clear();
                    lv_schedule.setAdapter(null);
                    prefectLists=null;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    mensaje(new String(statusCode+""));
                }
            });

        }

    }

    private void gtAllSche(String url) {
        normalTime=dayTime()+":00";
        String time=trackTime(d);
        tv_rango.setText(time);
        time+=":00";
        params.put("a",sp_area.getSelectedItem());
        params.put("h",time);
        params.put("hf",normalTime);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200)
               gtAllTeaJSON(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mensaje(new String(statusCode+""));
            }
        });

    }

    private String trackTime(Date d) {
        int horas[]={745,915,1045,1105,1230,1400,1700,1800,1900,2000};
        String horasString[]={"07:45","09:15","10:45","11:05","12:30","14:00","17 :00","18:00","19:00","20:00"};
        String sinPuntos[]=ho.format(d).split(":");
        String finaSinPunt=sinPuntos[0]+sinPuntos[1];
        int aComparar=Integer.parseInt(finaSinPunt);
        for (int i=0; i<horas.length;i++)
            if (i<horas.length-1)
                if (horas[i]<=aComparar && horas[i+1]>=aComparar)
                    return horasString[i];

        return "sin hora";
    }


    private void gtAllTea() {
       String url="http://scheduletown18.x10host.com/gts/gtAreas.php";

        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200) {
                    String s=new String(responseBody);
                    if (!s.equals("") && !s.isEmpty())
                    {
                        try {
                            JSONArray array = new JSONArray(s);
                            int num= array.getJSONObject(0).getInt("areas_count");
                            for (int i =0;i<num;i++)
                                areas.add(i+1);
                            sp_area.setAdapter( new ArrayAdapter<Integer>(getApplicationContext(),R.layout.spinner_item,areas));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 423)
               mensaje("Sin conexion al servidor");
            }
        });
    }

    private void mensaje(String s) {
        Snackbar.make(view,s,Snackbar.LENGTH_LONG).show();
    }

    private void gtAllTeaJSON(String s) {
        if (!s.equals("") && !s.isEmpty())
        {
            btn_send.setEnabled(true);
            try {
                JSONArray array = new JSONArray(s);

                for (int i =0;i<array.length();i++){
                    JSONObject list = array.getJSONObject(i);
                    PrefectList pl = new PrefectList(list.getString("idempleado"),list.getInt("idhorario"), list.getString("nombre"), list.getString("numsalon"));
                    pl.setPhoto(list.getString("foto"));
                    prefectLists.add(pl);
                }
                ap = new AdapterItemPrefect(this, prefectLists);
                lv_schedule.setAdapter(ap);


            } catch (JSONException e) {
                e.printStackTrace();
            }}else{
            btn_send.setEnabled(false);
        }
    }

    private String dayTime() {
        d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("yyyy/MM/dd");
        fechaComplString = fecc.format(d);
        ho = new SimpleDateFormat("HH:mm");
        return ho.format(d);
    }

    private void goToLogin() {
        session.setLoggedIn(false,null);
        session.clear();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK   | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

      public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mn_logout, menu);
         return  super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        goToLogin();
        return super.onOptionsItemSelected(item);
    }
}
