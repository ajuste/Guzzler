/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.dal;

import com.guzzler.common.IResourcesManager;
import com.guzzler.common.exception.RecordNotFoundException;
import com.guzzler.model.CharacterTemplateInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ajuste
 */
public class CharacterTempateDal {

    // <editor-fold defaultstate="collapsed" desc="Mocking">
    /**
     * Class dependencies.
     */
    public static class Dependencies {

        /**
         * Resources manager.
         */
        IResourcesManager resources;

        public Dependencies(boolean buildDependencies, IResourcesManager resources) {
            this.resources = resources;
        }
    }

    /**
     * Mocking constructor.
     *
     * @param dependencies Mocked dependencies.
     */
    public CharacterTempateDal(CharacterTempateDal.Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    private CharacterTempateDal.Dependencies dependencies;

    // </editor-fold>
    public List<CharacterTemplateInfo> getList() {
        List<CharacterTemplateInfo> result = new ArrayList<CharacterTemplateInfo>();
        CharacterTemplateInfo ct = new CharacterTemplateInfo();
        int[] bitmaps = new int[4];
        //  Pug
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG] = this.dependencies.resources.getDrawableId("pug_128");
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_PREVIEW] = this.dependencies.resources.getDrawableId("pug_64");
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG_WOEYES] = this.dependencies.resources.getDrawableId("pug_woeyes_128");
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG_EYES] = this.dependencies.resources.getDrawableId("pug_eye_128");
        ct.setId(UUID.fromString("65F15E30-8514-4424-B8FC-96EB59EA26D6"));
        ct.setName(this.dependencies.resources.getString("model_character_tempalte_name_pug"));
        ct.setBitmaps(bitmaps);
        result.add(ct);
        //  Pig
        ct = new CharacterTemplateInfo();
        bitmaps = new int[4];
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG] = this.dependencies.resources.getDrawableId("pig_128");
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_PREVIEW] = this.dependencies.resources.getDrawableId("pig_64");
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG_WOEYES] = this.dependencies.resources.getDrawableId("pug_woeyes_128");
        bitmaps[CharacterTemplateInfo.IMAGE_INDEX_NORMAL_BIG_EYES] = this.dependencies.resources.getDrawableId("pug_eye_128");
        ct.setId(UUID.fromString("0582F232-C018-4AE4-AC66-E3FE20EEBD19"));
        ct.setName(this.dependencies.resources.getString("model_character_tempalte_name_pig"));
        ct.setBitmaps(bitmaps);
        result.add(ct);
        return result;
    }
    
    public CharacterTemplateInfo read(UUID id) throws RecordNotFoundException {
        for(CharacterTemplateInfo t : this.getList()){
            if (t.getId().equals(id)){
                return t;
            }
        }
        throw new RecordNotFoundException(null, null, id, null);
    }
}