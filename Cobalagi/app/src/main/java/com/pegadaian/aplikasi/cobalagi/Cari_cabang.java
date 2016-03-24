package com.pegadaian.aplikasi.cobalagi;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pegadaian.aplikasi.cobalagi.domain.Cabang;
import com.pegadaian.aplikasi.cobalagi.domain.CekHarga;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ari on 23/03/16.
 */
public class Cari_cabang extends ActivityHelper
        implements NavigationView.OnNavigationItemSelectedListener {

    SearchView searchCabang;
    TextView TnamaPegadaian, Tprovinsi, Tkecamatan;
    ListView lvCabang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_cabang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchCabang = (SearchView)findViewById(R.id.searchView);

        lvCabang = (ListView) findViewById(R.id.listview_cabang);
        new OnSearch().execute();

        setSupportActionBar(toolbar);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

        } else if (id == R.id.nav_harga) {

            Intent intent = new Intent(this, Cek_harga.class);
            startActivity(intent);

        } else if (id == R.id.nav_caricabang) {

            Intent intent = new Intent(this, Cari_cabang.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class OnSearch extends AsyncTask<Void, Void, ResponseEntity>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog("Cheking, please wait ....");
    }
        @Override
        protected ResponseEntity doInBackground(Void... params) {
            try {
                return service.cariCabang(getHttpCookie());
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
                    Log.i("Data Not Found", e.getLocalizedMessage());
                    return null;
            } catch (Exception e){
                    Log.i("Data Not Found", e.getLocalizedMessage());
                    return null;
                }
        }

        @Override
        protected void onPostExecute(ResponseEntity result) {
            try {
            super.onPostExecute(result);
            dismissLoadingDialog();
            System.out.println("Result = = = = " + result);
            if(result != null){
                if(result.getStatusCode().equals(HttpStatus.OK)){
                    Cabang[] responseCabang = (Cabang[]) (result.getBody());
                    List<Cabang> listCabang = Arrays.asList(responseCabang);
                    System.out.println("result ==========="+listCabang);
                    if(listCabang.isEmpty()){
                        throw new Exception("Error");
                    }
                    lvCabang.setAdapter(new ListCabangAdapter(getApplicationContext(), listCabang));

                }else{
                    showMessageDialog((String) result.getBody());
                }
            }else{
                showLoadingDialog("Server Not Respond");
            }
        }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
