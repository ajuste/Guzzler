/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author ajuste
 */
public abstract class EntityOpenHelper extends SQLiteOpenHelper {

    protected int databaseVersion;
    protected String databaseName;

    public EntityOpenHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }
}
