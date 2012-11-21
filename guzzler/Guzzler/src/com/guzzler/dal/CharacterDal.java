/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.guzzler.common.DatabaseManager;
import com.guzzler.common.IResourcesManager;
import com.guzzler.common.exception.RecordAlreadyExistsException;
import com.guzzler.common.exception.RecordNotFoundException;
import com.guzzler.model.CharacterInfo;
import java.util.UUID;

/**
 *
 * @author ajuste
 */
public class CharacterDal {

    private static final String TableName = "Character";
    // <editor-fold defaultstate="collapsed" desc="Dependencies">

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
        /**
         * Database databaseHelper.
         */
        GuzzlerOpenHelper databaseHelper;
        /**
         * Character template dal.
         */
        CharacterTempateDal characterTemplateDal;

        /**
         * Dependencies constructor.
         *
         * @param buildDependencies If true then default dependencies will be constructed.
         * @param resources The resources manager.
         * @param context The application context.
         */
        public Dependencies(boolean buildDependencies, IResourcesManager resources, Context context) {
            if (buildDependencies) {
                this.databaseHelper = new GuzzlerOpenHelper(new GuzzlerOpenHelper.Dependencies(resources, context));
                this.characterTemplateDal = new CharacterTempateDal(new CharacterTempateDal.Dependencies(buildDependencies, resources));
            }
            this.resources = resources;
            this.context = context;
        }
    }

    /**
     * Constructor.
     *
     * @param dependencies Mocked dependencies.
     */
    public CharacterDal(CharacterDal.Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    private CharacterDal.Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CharacterDAL">
    public void save(CharacterInfo entity) throws RecordAlreadyExistsException {
        SQLiteDatabase db = null;
        ContentValues values;
        boolean insert;
        try {
            insert = entity.getId() == null;
            values = new ContentValues();
            if (insert) {
                entity.setId(UUID.randomUUID());
                values.put(Columns.Id, entity.getId().toString());
            }
            values.put(Columns.Name, entity.getName());
            values.put(Columns.TemplateCharacterId, entity.getTemplate().getId().toString());
            values.put(Columns.Playing, entity.isPlaying() ? 1 : 0);
            values.put(Columns.Experience, entity.getExperience());
            values.put(Columns.Level, entity.getLevel());
            values.put(Columns.Score, entity.getScore());
            values.put(Columns.Health, entity.getHealth());
            values.put(Columns.Fun, entity.getFun());
            values.put(Columns.Food, entity.getFood());
            values.put(Columns.Energy, entity.getEnergy());
            values.put(Columns.Poo, entity.getPoo());

            db = this.dependencies.databaseHelper.getWritableDatabase();
            if (insert) {
                db.insertOrThrow(CharacterDal.TableName, null, values);
            } else {
                db.update(CharacterDal.TableName, values, Columns.Id + "=?", new String[]{entity.getId().toString()});
            }
        } catch (SQLException ex) {
            throw DatabaseManager.createRecordAlreadyExistsException(entity, "Character save", ex);
        } finally {
            DatabaseManager.closeDb(db);
        }
    }

    /**
     * Reads a character given its id.
     *
     * @param id The character id.
     * @return The character.
     * @throws RecordNotFoundException Thrown if record is not found.
     */
    public CharacterInfo read(UUID id) throws RecordNotFoundException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.dependencies.databaseHelper.getWritableDatabase();
            cursor = db.query(
                    CharacterDal.TableName,
                    new String[]{
                        Columns.Id,
                        Columns.Name,
                        Columns.TemplateCharacterId,
                        Columns.Playing,
                        Columns.Experience,
                        Columns.Level,
                        Columns.Score,
                        Columns.Health,
                        Columns.Fun,
                        Columns.Food,
                        Columns.Energy,
                        Columns.Poo},
                    Columns.Id + "=?", new String[]{id.toString()}, null, null, "1");
            if (!cursor.moveToFirst()) {
                throw new RecordNotFoundException(null, CharacterInfo.class, id, null);
            }
            return this.loadReadModel(cursor);
        } catch (SQLiteException ex) {
            Log.e(this.getClass().getSimpleName(), "Character read.", ex);
            throw ex;
        } finally {
            DatabaseManager.closeDb(db, cursor);
        }
    }

    /**
     * Gets an instance of the current playing character or null if none.
     *
     * @return The character instance.
     */
    public CharacterInfo getPlayingCharacter() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.dependencies.databaseHelper.getWritableDatabase();
            //  Get the id of the playing character.
            cursor = db.query(CharacterDal.TableName, new String[]{Columns.Id}, Columns.Playing + "=?", new String[]{"1"}, null, null, null);
            return cursor.moveToNext() ? this.read(UUID.fromString(cursor.getString(0))) : null;
        } catch (RecordNotFoundException ex) {
            //  should not happen.
            Log.wtf(this.getClass().getSimpleName(), "Character - Get playing character.", ex);
            throw new RuntimeException(ex);
        } catch (SQLiteException ex) {
            Log.e(this.getClass().getSimpleName(), "Character - Get playing character.", ex);
            throw ex;
        } finally {
            DatabaseManager.closeDb(db, cursor);
        }
    }

    /**
     * Set playing flag for a character.
     *
     * @param id The id of the character.
     * @param flag True for set playing, false otherwise.
     * @exception RecordNotFoundException The character was not found.
     */
    public void setPlayingFlag(UUID id, boolean flag) throws RecordNotFoundException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(Columns.Playing, flag ? 1 : 0);

            db = this.dependencies.databaseHelper.getWritableDatabase();
            if (db.update(TableName, values, Columns.Id + "=?", new String[]{id.toString()}) == 0) {
                throw new RecordNotFoundException(null, CharacterInfo.class, id, null);
            }
        } catch (SQLiteException ex) {
            Log.e(this.getClass().getSimpleName(), "Character set playing flag.", ex);
            throw ex;
        } finally {
            DatabaseManager.closeDb(db, cursor);
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Private">

    /**
     * Loads the model for a character on a read operation.
     * @param cursor The cursor pointing to the character.
     * @return The character.
     * @throws RecordNotFoundException  Thrown if any of the references are not found.
     */
    private CharacterInfo loadReadModel(Cursor cursor) throws RecordNotFoundException {
        CharacterInfo result = new CharacterInfo();
        result.setId(UUID.fromString(cursor.getString(ReadIndexes.IdIndex)));
        result.setName(cursor.getString(ReadIndexes.NameIndex));
        result.setTemplate(dependencies.characterTemplateDal.read(UUID.fromString(cursor.getString(ReadIndexes.TemplateIndex))));
        result.setPlaying(cursor.getInt(ReadIndexes.PlayingIndex) == 1);
        result.setExperience(cursor.getInt(ReadIndexes.Experience));
        result.setLevel(cursor.getInt(ReadIndexes.Level));
        result.setScore(cursor.getInt(ReadIndexes.Score));
        result.setHealth(cursor.getFloat(ReadIndexes.Health));
        result.setFun(cursor.getFloat(ReadIndexes.Fun));
        result.setFood(cursor.getFloat(ReadIndexes.Food));
        result.setEnergy(cursor.getFloat(ReadIndexes.Energy));
        result.setPoo(cursor.getFloat(ReadIndexes.Poo));
        return result;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DB Columns">

    /**
     * Cursor read indexes.
     */
    private static final class ReadIndexes {

        /**
         * The id index.
         */
        static final int IdIndex = 0;
        /**
         * The name index.
         */
        static final int NameIndex = 1;
        /**
         * The template index.
         */
        static final int TemplateIndex = 2;
        /**
         * The playing index.
         */
        static final int PlayingIndex = 3;
        static final int Experience = 4;
        static final int Level = 5;
        static final int Score = 6;
        static final int Health = 7;
        static final int Fun = 8;
        static final int Food = 9;
        static final int Energy = 10;
        static final int Poo = 11;
    }

    private static final class Columns {

        static final String Id = "Id";
        static final String Name = "Name";
        static final String TemplateCharacterId = "TemplateCharacterId";
        static final String Playing = "Playing";
        static final String Experience = "Experience";
        static final String Level = "Level";
        static final String Score = "Score";
        static final String Health = "Health";
        static final String Fun = "Fun";
        static final String Food = "Food";
        static final String Energy = "Energy";
        static final String Poo = "Poo";
    }
    // </editor-fold>
}