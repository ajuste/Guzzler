/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.bll;

import android.content.Context;
import android.util.Log;
import com.guzzler.common.IResourcesManager;
import com.guzzler.common.exception.RecordAlreadyExistsException;
import com.guzzler.common.exception.RecordNotFoundException;
import com.guzzler.dal.CharacterDal;
import com.guzzler.model.CharacterInfo;
import java.util.Collection;
import java.util.UUID;

/**
 * Character business layer.
 *
 * @author ajuste
 */
public class CharacterBll {

    /**
     * Character the user has selected.
     */
    private static CharacterInfo currentCharacter;

    // <editor-fold defaultstate="collapsed" desc="Dependencies">
    /**
     * Class dependencies.
     */
    public static class Dependencies {

        /**
         * Dal dependency.
         */
        CharacterDal dal;

        public Dependencies(boolean buildDependencies, Context context, IResourcesManager resources) {
            if (buildDependencies) {
                this.dal = new CharacterDal(new CharacterDal.Dependencies(buildDependencies, resources, context));
            }
        }
    }
    private Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Class constructor.
     *
     * @param dependencies Dependencies..
     */
    public CharacterBll(Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Business Layer">

    public CharacterInfo getPlayingCharacter() {
        return this.dependencies.dal.getPlayingCharacter();
    }

    /**
     * Set playing flag for a character.
     *
     * @param character The character
     * @param flag True for set playing, false otherwise.
     * @exception RecordNotFoundException The character was not found.
     * @return The operation result.
     */
    public SetPlayingResult setPlayingFlag(CharacterInfo character, boolean flag) {
        if (character == null) {
            throw new IllegalArgumentException("character");
        }
        try {
            this.dependencies.dal.setPlayingFlag(character.getId(), flag);
        } catch (RecordNotFoundException ex) {
            return SetPlayingResult.CharacterNotFound;
        }
        return SetPlayingResult.Success;
    }

    /**
     * Saves a character.
     *
     * @param character The character.
     */
    public SaveResult save(CharacterInfo character) {
        if (character == null) {
            throw new IllegalArgumentException("character");
        }
        try {
            this.dependencies.dal.save(character);
        } catch (RecordAlreadyExistsException ex) {
            return SaveResult.AlreadyExists;
        }
        return SaveResult.Success;
    }

    /**
     * Reads a character.
     *
     * @param id The id of the character to be read.
     * @return The character instance.
     */
    public CharacterInfo read(UUID id) {
        CharacterInfo result = null;
        if (id == null) {
            throw new IllegalArgumentException("id");
        }
        try {
            result = this.dependencies.dal.read(id);
        } catch (RecordNotFoundException ex) {
            Log.e(this.getClass().getSimpleName(), "Character not found.", ex);
        }
        return result;
    }

    public Collection<CharacterInfo> getList() {
        throw new UnsupportedOperationException("not implemented");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Operation results">
    public enum SaveResult {

        /**
         * Operation was successful.
         */
        Success,
        /**
         * Character with the same name already exists.
         */
        AlreadyExists
    }

    public enum SetPlayingResult {

        Success,
        CharacterNotFound
    }
    // </editor-fold>
}