package com.iogb.sheduletown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    private EditText ed_user,ed_pass;
    private AsyncHttpClient client;
    private RequestParams parameters;
    private Employee employee;
    private RelativeLayout linear;
    private ProgressBar pg_loggin;
    private Session session;
    private String url;
    private boolean trust;
    public static final String EMPLOYEE="employee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(this);
        if (session.loggedIn()){
            employee=new Gson().fromJson(session.getFullInfo(),Employee.class);
            trust =true;
            goToNext();
        }
        pg_loggin=(ProgressBar) findViewById(R.id.pg_logging);
        btn_login=(Button) findViewById(R.id.btn_enviar);
        ed_user =(EditText) findViewById(R.id.ed_usuario);
        ed_pass=(EditText) findViewById(R.id.ed_contrasenia);
        linear=(RelativeLayout)findViewById(R.id.ll_s);
        start();
    }

    private void start() {
        client=new AsyncHttpClient();
        parameters= new RequestParams();
        employee= new Employee();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_user.getText().toString().equals(""))
                {
                    ed_user.setError("Campo Obligatorio");
                    ed_user.requestFocus();
                }
                else {
                    if (ed_pass.getText().toString().equals("")){
                        ed_pass.setError("Campo Obligatorio");
                        ed_pass.requestFocus();
                    }
                    else
                    {
                        userValidator();
                    }

                }


            }
        });
    }

    private void userValidator() {
        pg_loggin.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);
        url="http://scheduletown18.x10host.com/lg.php";
        parameters.put("u",ed_user.getText().toString());
        parameters.put("p",ed_pass.getText().toString());
        client.post(url, parameters, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(statusCode==200)
                loginJSON(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(linear,getString(R.string.server_down),Snackbar.LENGTH_LONG).show();
                ed_user.requestFocus();
                pg_loggin.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loginJSON(String result) {
        if (result.equals("")) {
            Snackbar.make(linear,getString(R.string.errror_incorrectUser),Snackbar.LENGTH_LONG).show();
            ed_user.requestFocus();
            pg_loggin.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
        else {
            try {
                JSONArray jsonArreglo = null;
                if (!TextUtils.isEmpty(result)) {
                    jsonArreglo = new JSONArray(result);

                        employee.setType(jsonArreglo.getJSONObject(0).getInt("tipo"));
                        employee.setFullName(jsonArreglo.getJSONObject(0).getString("nombre"));
                        employee.setIdEmployee(jsonArreglo.getJSONObject(0).getString("idEmpleado"));

                   goToNext();
                }
            } catch (JSONException e) {
                Snackbar.make(linear, R.string.error_DB, Snackbar.LENGTH_LONG).show();
                ed_user.requestFocus();
                pg_loggin.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
        }
    }

    private void goToNext() {
        Intent intent;
    if (employee.getType()==2) {

        if(!trust) {
           String json = new Gson().toJson(employee);
            session.setLoggedIn(true,json);
        }
        intent= new Intent(getApplicationContext(),TeacherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    else {
        if (!trust) {
            String json = new Gson().toJson(employee);
            session.setLoggedIn(true,json);
        }
        intent= new Intent(getApplicationContext(),PrefectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    }


}
