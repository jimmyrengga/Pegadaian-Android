package com.pegadaian.aplikasi.cobalagi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pegadaian.aplikasi.cobalagi.domain.Cabang;

import java.util.List;

/**
 * Created by ari on 24/03/16.
 */
public class ListCabangAdapter extends BaseAdapter {
    private List<Cabang> listCabang;
    private Context context;

    public ListCabangAdapter(Context context, List<Cabang> listCabang) {
        this.listCabang = listCabang;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listCabang.size();
    }

    @Override
    public Cabang getItem(int position) {
        return listCabang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.list_cabang_layout, parent, false);

        TextView lblNamaCabang = (TextView) convertView.findViewById(R.id.list_cabang_nama_cabang);
        TextView lblProvinsi = (TextView) convertView.findViewById(R.id.list_cabang_provinsi);
        TextView lblKota = (TextView) convertView.findViewById(R.id.list_cabang_kota);
        TextView lblKecamatan = (TextView) convertView.findViewById(R.id.list_cabang_kecamatan);
        TextView lblKelurahan = (TextView) convertView.findViewById(R.id.list_cabang_kelurahan);

        Cabang cabang = getItem(position);

        lblNamaCabang.setText(cabang.getNamaPegadain());
        lblProvinsi.setText(cabang.getProvinsi());
        lblKota.setText(cabang.getKota());
        lblKecamatan.setText(cabang.getKecamatan());
        lblKelurahan.setText(cabang.getKelurahan());

        return convertView;
    }
}
