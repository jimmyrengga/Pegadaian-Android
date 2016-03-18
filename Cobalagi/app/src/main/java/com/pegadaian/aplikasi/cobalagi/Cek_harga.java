package com.pegadaian.aplikasi.cobalagi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pegadaian.aplikasi.cobalagi.domain.CekHarga;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by rinaldy on 17/03/16.
 */

public class Cek_harga extends ActivityHelper
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Button ok;
    TextView hsl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cek_harga);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ok = (Button) findViewById(R.id.ck_harga);
        hsl = (TextView) findViewById(R.id.hsl);

        ok.setOnClickListener(this);

        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //  fab.setOnClickListener(new View.OnClickListener() {
        //@Override
        //  public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //              .setAction("Action", null).show();
        //}
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    //public void onBackPressed() {
      //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if (drawer.isDrawerOpen(GravityCompat.START)) {
          //  drawer.closeDrawer(GravityCompat.START);
        //} else {
          //  super.onBackPressed();
        //}
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
            new DoCekHarga().execute();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_harga) {

            Intent intent = new Intent(this, Cek_harga.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class DoCekHarga extends AsyncTask<Void, Void, ResponseEntity>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog("Cheking, please wait ....");
        }

        @Override
        protected ResponseEntity doInBackground(Void... params) {
            try {
                return service.cekHarga(getHttpCookie());
            } catch (HttpStatusCodeException e){
                if(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
                    Log.i("500", e.getResponseBodyAsString());
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String,Object> map = null;
                    String msg = "";
                    try {
                        map = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    msg = (String) map.get("message");
                    return new ResponseEntity(msg, e.getStatusCode());
                } else {
                    return new ResponseEntity(e.getStatusCode().getReasonPhrase(), e.getStatusCode());
                }
            } catch (ResourceAccessException e){
                Log.i("CEK HARGA ERROR", e.getLocalizedMessage());
                return null;
            } catch (Exception e){
                Log.i("CEK HARGA ERROR", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity result) {
            super.onPostExecute(result);
            dismissLoadingDialog();
            if(result != null){
                if(result.getStatusCode().equals(HttpStatus.OK)){
                    CekHarga cekHarga = (CekHarga) result.getBody();
                    hsl.setText(cekHarga.getHarga().toPlainString());
                } else {
                    showMessageDialog((String) result.getBody());
                }
            } else {
                showMessageDialog("Tidak mendapat respon dari server");
            }
        }
    }
}
