/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.guzzler.common.IResourcesManager;
import com.guzzler.model.CharacterInfo;
import com.guzzler.ui.misc.UIUtilManager;

/**
 *
 * @author ajuste
 */
public class GameUI {

    // <editor-fold defaultstate="collapsed" desc="Dependencies">
    /**
     * Class dependencies.
     */
    public static class Dependencies {

        /**
         * Resources manager dependency.
         */
        IResourcesManager resourcesManager;
        /**
         * Character template UI.
         */
        CharacterTemplateUI characterTemplateUI;
        /**
         * Application resources.
         */
        Resources resources;

        public Dependencies(boolean buildDependencies, Context context, Resources resources) {
            if (buildDependencies) {
                this.resourcesManager = UIUtilManager.createResourceInstance(resources);
                this.characterTemplateUI = new CharacterTemplateUI(new CharacterTemplateUI.Dependencies(true, context, resources));
            }
            this.resources = resources;
        }
    }
    private GameUI.Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Class constructor.
     *
     * @param dependencies Dependencies..
     */
    public GameUI(GameUI.Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    // </editor-fold>
    private static GameResources Resources;

    public void loadResources(CharacterInfo c) {
        GameUI.Resources = new GameResources();
        GameUI.Resources.templateCharacterBms = this.dependencies.characterTemplateUI.getCharacterTemplateBitmaps(c.getTemplate());
    }

    public GameResources getResources() {
        return GameUI.Resources;
    }

    public class GameResources {

        public Bitmap[] templateCharacterBms;
    }
}
