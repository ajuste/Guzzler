/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui;

import android.widget.BaseAdapter;
import java.util.List;

/**
 *
 * @author ajuste
 */
public abstract class DataAdapter<T> extends BaseAdapter {

    protected List<T> data;

    public DataAdapter(List<T> data) {
        this.data = data;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int id) {
        return id;
    }
    
    public T getDataAt(int position){
        return this.data.get(position);
    }

    public List<T> getData() {
        return data;
    }
};