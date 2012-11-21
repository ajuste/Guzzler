/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.guzzler.common.DatabaseManager;
import com.guzzler.common.EntityOpenHelper;
import com.guzzler.common.FileManager;
import com.guzzler.common.IResourcesManager;
import com.guzzler.common.SystemManager;
import java.io.File;

/**
 * Helper for Guzzler database.
 *
 * @author ajuste
 */
public class GuzzlerOpenHelper extends EntityOpenHelper {

    /**
     * Databases directory name.
     */
    private static final String DatabaseDirectory = "dbs";
    /**
     * Main database file name.
     */
    private static final String DatabaseFileName = "main";
    /**
     * User in debug only to drop the database only one time at initialization.
     */
    private static boolean databaseDropped = false;

    // <editor-fold defaultstate="collapsed" desc="Mocking">
    /**
     * Class dependencies.
     */
    public static class Dependencies {

        /**
         * Resources manager.
         */
        IResourcesManager resources;
        /**
         * Application context.
         */
        Context context;

        public Dependencies(IResourcesManager resources, Context context) {
            this.resources = resources;
            this.context = context;
        }
    }

    /**
     * Mocking constructor.
     *
     * @param dependencies Mocked dependencies.
     */
    public GuzzlerOpenHelper(GuzzlerOpenHelper.Dependencies dependencies) {
        super(dependencies.context, DatabaseManager.getDatabaseName(dependencies.resources), DatabaseManager.getDatabaseVersion(dependencies.resources));
        this.dependencies = dependencies;
    }
    private GuzzlerOpenHelper.Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Private">  
    /**
     * Drops the database.
     *
     * @param db The database
     */
    private void dropDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE Character;");
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Override">  

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE Character ("
                + "Id TEXT(16) PRIMARY KEY, "
                + "Name TEXT, "
                + "TemplateCharacterId BINARY(16), "
                + "Playing INT,"
                + "Experience INT,"
                + "Level INT,"
                + "Score INT,"
                + "Health FLOAT,"
                + "Fun FLOAT,"
                + "Food FLOAT,"
                + "Energy FLOAT,"
                + "Poo FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void recreateDatabase(SQLiteDatabase db) {
        if (!"1".equals(this.dependencies.resources.getString("global_db_recreate"))){
            return;
        }
        if (!GuzzlerOpenHelper.databaseDropped && SystemManager.getRunMode() == SystemManager.RunMode.Debug) {
            try {
                db.beginTransaction();
                this.dropDatabase(db);
                this.onCreate(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            GuzzlerOpenHelper.databaseDropped = true;
        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        File dbDirectory;
        File dbFile;
        SQLiteDatabase db = null;
        int physicalDatabaseVersion;

        try {
            //  Get references to files and directories.
            dbDirectory = FileManager.getInstance().ensureDirectoryOnAppRoot(GuzzlerOpenHelper.DatabaseDirectory);
            dbFile = new File(dbDirectory, GuzzlerOpenHelper.DatabaseFileName);//FileManager.getInstance().ensureFileOnDirectory(dbDirectory, GuzzlerOpenHelper.DatabaseFileName);            
            db = SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsoluteFile(), null);
            physicalDatabaseVersion = db.getVersion();
            //  Check version in order to create or upgrade.
            if (physicalDatabaseVersion != this.databaseVersion) {
                db.beginTransaction();
                if (physicalDatabaseVersion == 0) {
                    onCreate(db);
                } else {
                    onUpgrade(db, physicalDatabaseVersion, this.databaseVersion);
                }
                db.setVersion(this.databaseVersion);
                db.setTransactionSuccessful();
            }else{
                this.recreateDatabase(db);
            }
            //  If everything went ok, then commit.
            onOpen(db);
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), "Failed to get writable database", ex);
        } finally {
            if (db != null && db.inTransaction()) {
                db.endTransaction();
            }
        }
        return db;
    }
}
// </editor-fold>