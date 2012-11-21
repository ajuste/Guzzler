/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.guzzler.bll.CharacterTemplateBll;
import com.guzzler.common.IResourcesManager;
import com.guzzler.model.CharacterTemplateInfo;
import com.guzzler.ui.misc.UIUtilManager;

/**
 *
 * @author ajuste
 */
public class CharacterTemplateUI {

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
         * Character template business layer.
         */
        CharacterTemplateBll bll;
        /**
         * Application resources.
         */
        Resources resources;

        public Dependencies(boolean buildDependencies, Context context, Resources resources) {
            if (buildDependencies) {
                this.resourcesManager = UIUtilManager.createResourceInstance(resources);
                this.bll = new CharacterTemplateBll(new CharacterTemplateBll.Dependencies(true, this.resourcesManager));
            }
            this.resources = resources;
        }
    }
    private CharacterTemplateUI.Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Class constructor.
     *
     * @param dependencies Dependencies..
     */
    public CharacterTemplateUI(CharacterTemplateUI.Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    // </editor-fold>
    
    public Bitmap[] getCharacterTemplateBitmaps(CharacterTemplateInfo ct){
        int bmIds[] = ct.getBitmaps();
        Bitmap result [] = new Bitmap[bmIds.length];
        
        for(int index = 0; index < bmIds.length; index++){
          result[index] = BitmapFactory.decodeResource(this.dependencies.resources, bmIds[index]);
        }        
        return result;
    }

    public Drawable getCharacterMain(CharacterTemplateInfo template) {
        return this.dependencies.resources.getDrawable(template.getBitmaps()[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG]);
    }

    public Drawable getCharacterTemplatePreview(CharacterTemplateInfo template) {
        return this.dependencies.resources.getDrawable(template.getBitmaps()[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_PREVIEW]);
    }

    public void fillGallery(final Context context, final Gallery view) {
        final LayoutInflater inflator = LayoutInflater.from(context);
        view.setAdapter(new DataAdapter<CharacterTemplateInfo>(this.dependencies.bll.getList()) {
            public View getView(int position, View convertView, ViewGroup parent) {

                LinearLayout layout = (LinearLayout) inflator.inflate(com.guzzler.R.layout.item_character_template, null);
                ImageView iv = (ImageView) layout.findViewById(com.guzzler.R.id.characterTemplateImage);
                iv.setImageDrawable(getCharacterTemplatePreview(super.data.get(position)));
                iv.setScaleType(ImageView.ScaleType.CENTER);
                iv.setLayoutParams(new Gallery.LayoutParams(64, 64));
                return iv;
            }
        });
    }
}