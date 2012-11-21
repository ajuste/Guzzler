/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;
import com.guzzler.bll.CharacterBll;
import com.guzzler.common.IResourcesManager;
import com.guzzler.model.CharacterInfo;
import com.guzzler.model.CharacterTemplateInfo;

/**
 *
 * @author ajuste
 */
public class CharacterUI {

    // <editor-fold defaultstate="collapsed" desc="Dependencies">
    /**
     * Class dependencies.
     */
    public static class Dependencies {

        /**
         * Business layer dependency.
         */
        CharacterBll bll;
        /**
         * UI for character template.
         */
        CharacterTemplateUI templateUI;

        public Dependencies(boolean buildDependencies, Context context, IResourcesManager resources) {
            if (buildDependencies) {
                this.bll = new CharacterBll(new CharacterBll.Dependencies(buildDependencies, context, resources));
                this.templateUI = new CharacterTemplateUI(new CharacterTemplateUI.Dependencies(buildDependencies, context, (Resources) resources.getResourceObject()));
            }
        }
    }
    private CharacterUI.Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Class constructor.
     *
     * @param dependencies Dependencies..
     */
    public CharacterUI(CharacterUI.Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CharacterUI">
/*
    public boolean setMainImage(ImageView iv, CharacterInfo c) {
        LayerDrawable ld = (LayerDrawable) iv.getDrawable();
        boolean ret = ld.setDrawableByLayerId(com.guzzler.R.id.itemCharacterMain, this.dependencies.templateUI.getCharacterMain(c.getTemplate()));
        ld.invalidateSelf();
        return ret;
    }

    public boolean setFlotableImage(ImageView iv, Drawable d) {
        LayerDrawable ld = (LayerDrawable) iv.getDrawable();
        boolean ret = ld.setDrawableByLayerId(com.guzzler.R.id.itemCharacterMainFlotable, d);
        ld.invalidateSelf();
        return ret;
    }
*/
    public Bitmap getCharacterMainBitmapAccordingToState(CharacterInfo character, GameUI.GameResources gameResources){
        return gameResources.templateCharacterBms[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG_WOEYES];
    }
    
    public Bitmap getCharacterEyesMainBitmapAccordingToState(CharacterInfo character, GameUI.GameResources gameResources){
        return gameResources.templateCharacterBms[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG_EYES];
    }
    
    public void moveFlotableTo(ImageView iv, int x, int y, int maxW, int maxH) {
        LayerDrawable ld = (LayerDrawable) iv.getDrawable();
        Drawable d = ld.getDrawable(1);
        
        d.setBounds(x, y, maxW - d.getMinimumWidth() + x, maxH - d.getMinimumHeight() + y);
        d.invalidateSelf();
    }

    public void updateCounterLevels(CharacterInfo character, Activity activity) {

        int energyLevel;
        int funLevel;
        int healthLevel;
        int foodLevel;

        // min 500
        // max 7000
        energyLevel = (int) (10000 * character.getEnergy());
        funLevel = (int) (10000 * character.getFun());
        healthLevel = (int) (10000 * character.getHealth());
        foodLevel = (int) (10000 * character.getFood());

        ImageView iv = (ImageView) activity.findViewById(com.guzzler.R.id.characterMainEnergyLevel);
        ((LayerDrawable) iv.getBackground()).getDrawable(1).setLevel(energyLevel);
        iv = (ImageView) activity.findViewById(com.guzzler.R.id.characterMainFoodLevel);
        ((LayerDrawable) iv.getBackground()).getDrawable(1).setLevel(foodLevel);
        iv = (ImageView) activity.findViewById(com.guzzler.R.id.characterMainFunLevel);
        ((LayerDrawable) iv.getBackground()).getDrawable(1).setLevel(funLevel);
        iv = (ImageView) activity.findViewById(com.guzzler.R.id.characterMainHealthLevel);
        ((LayerDrawable) iv.getBackground()).getDrawable(1).setLevel(healthLevel);

    }
    // </editor-fold>
}