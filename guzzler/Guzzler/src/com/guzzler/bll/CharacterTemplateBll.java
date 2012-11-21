/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.bll;

import com.guzzler.common.IResourcesManager;
import com.guzzler.dal.CharacterTempateDal;
import com.guzzler.model.CharacterTemplateInfo;
import java.util.List;

/**
 *
 * @author ajuste
 */
public class CharacterTemplateBll {

    // <editor-fold defaultstate="collapsed" desc="Mocking">
    /**
     * Class dependencies.
     */
    public static class Dependencies {

        /**
         * Dal dependency.
         */
        CharacterTempateDal dal;
        
        public Dependencies(boolean buildDependencies, IResourcesManager resourcesManager){
            if (buildDependencies){
                this.dal = new CharacterTempateDal(new CharacterTempateDal.Dependencies(buildDependencies, resourcesManager));
            }
        }
    }

    /**
     * Mocking constructor.
     *
     * @param dependencies Mocked dependencies.
     */
    public CharacterTemplateBll(Dependencies dependencies) {
        this.dependencies = dependencies;
    }
    private Dependencies dependencies;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructors">

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Business Layer">
    public List<CharacterTemplateInfo> getList() {
        return this.dependencies.dal.getList();
    }
    // </editor-fold>
}