package com.pegadaian.aplikasi.cobalagi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pegadaian.aplikasi.cobalagi.domain.Emas;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActivityHelper
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtHargaLimaGram, txtHargaSepuluhGram, txtHargaDuaPuluhLimaGram, txtHargaLimaPuluhGram, txtHargaSeratusGram
            ,txtHargaDuaRatusLimaPuluhGram, txtHargaSatuKiloGram;
    TextView txtHargaJual, txtHargaBeli, tglUpdate;

    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtHargaLimaGram = (TextView) findViewById(R.id.txtHargaLimaGram);
        txtHargaSepuluhGram = (TextView) findViewById(R.id.txtHargaSepuluhGram);
        txtHargaDuaPuluhLimaGram = (TextView) findViewById(R.id.txtHargaDuaPuluhLimaGram);
        txtHargaLimaPuluhGram = (TextView) findViewById(R.id.txtHargaLimaPuluhGram);
        txtHargaSeratusGram = (TextView) findViewById(R.id.txtHargaSeratusGram);
        txtHargaDuaRatusLimaPuluhGram = (TextView) findViewById(R.id.txtHargaDuaRatusLimaPuluhGram);
        txtHargaSatuKiloGram = (TextView) findViewById(R.id.txtHargaSatuKiloGram);
        txtHargaBeli = (TextView) findViewById(R.id.txtHargaBeli);
        txtHargaJual = (TextView) findViewById(R.id.txtHargaJual);
        tglUpdate = (TextView) findViewById(R.id.tglUpdate);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                new DoHargaEmas().execute();
            }
        });
        new DoHargaEmas().execute();

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

    //harga emas
    public class DoHargaEmas extends AsyncTask<Void, Void, ResponseEntity>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showLoadingDialog("Load Harga Emas");
        }

        @Override
        protected ResponseEntity doInBackground(Void... params){
            try {
                return service.hargaMas(getHttpCookie());
            }catch (HttpStatusCodeException e){
                if (e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
                    Log.i("500", e.getResponseBodyAsString());
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = null;
                    String msg = "";
                    try{
                        map = mapper.readValue(e.getResponseBodyAsByteArray(), Map.class);
                    }catch (IOException ioe){
                         ioe.printStackTrace();
                    }
                    msg = (String) map.get("message");
                    return new ResponseEntity(msg, e.getStatusCode());
                }else{
                    return new ResponseEntity(e.getStatusCode().getReasonPhrase(), e.getStatusCode());
                }
            }catch (ResourceAccessException rae){
                Log.i("CEK HARGA EMAS ERROR", rae.getLocalizedMessage());
                return null;
            } catch (Exception e){
                Log.i("CEK HARGA ERROR", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity result){
            try {
            super.onPostExecute(result);
            dismissLoadingDialog();
            System.out.println("Result = = = = " + result);
            if(result != null){
                if(result.getStatusCode().equals(HttpStatus.OK)){
                    Emas[] responseEmas =(Emas[]) (result.getBody());

                    List<Emas> listHargaEmas = Arrays.asList(responseEmas);
                    System.out.println("result ==========="+listHargaEmas);
                    if(listHargaEmas.isEmpty()){
                        throw new Exception("Error");
                    }

                    for (Emas e : listHargaEmas){
                        switch (e.getBerat()){
                            case 5:
                                txtHargaLimaGram.setText(e.getHarga().toPlainString());
                                txtHargaJual.setText(e.getHargaBeli().toPlainString());
                                txtHargaBeli.setText(e.getHargaBeli().toPlainString());
                                tglUpdate.setText(e.getTanggal().toString());
                            break;
                            case 10:
                                txtHargaSepuluhGram.setText(e.getHarga().toPlainString());
                            break;
                            case 25:
                                txtHargaDuaPuluhLimaGram.setText(e.getHarga().toPlainString());
                            break;
                            case 50:
                                txtHargaLimaPuluhGram.setText(e.getHarga().toPlainString());
                            break;
                            case 100:
                                txtHargaSeratusGram.setText(e.getHarga().toPlainString());
                            break;
                            case 250:
                                txtHargaDuaRatusLimaPuluhGram.setText(e.getHarga().toPlainString());
                                break;
                            case 1:
                                txtHargaSatuKiloGram.setText(e.getHarga().toPlainString());
                            break;
                        }
                    }
                    refreshLayout.setRefreshing(false);

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


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    //public void onBackPressed() {
     //   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     //   if (drawer.isDrawerOpen(GravityCompat.START)) {
     //       drawer.closeDrawer(GravityCompat.START);
     //   } else {
      //      super.onBackPressed();
       // }
   // }

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


}
