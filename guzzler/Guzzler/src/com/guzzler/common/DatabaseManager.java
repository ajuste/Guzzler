/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.guzzler.common.exception.RecordAlreadyExistsException;

/**
 *
 * @author ajuste
 */
public class DatabaseManager {

    private static final String RESOURCE_KEY_DB_VER = "global_db_version";
    private static final String RESOURCE_KEY_DB_NAME = "global_db_name";

    public static int getDatabaseVersion(IResourcesManager resource) {
        return Integer.parseInt(resource.getString(RESOURCE_KEY_DB_VER));
    }

    public static String getDatabaseName(IResourcesManager resource) {
        return resource.getString(RESOURCE_KEY_DB_NAME);
    }

    public static RecordAlreadyExistsException createRecordAlreadyExistsException(IEntity entity, String additionalInformation, Exception cause) {
        return new RecordAlreadyExistsException(cause, entity.getClass(), entity.getId(), additionalInformation);
    }

    public static void closeDb(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public static void closeDb(SQLiteDatabase db, Cursor cursor) {
        DatabaseManager.closeCursor(cursor);
        DatabaseManager.closeDb(db);
    }
}