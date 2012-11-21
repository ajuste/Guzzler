/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common;

/**
 *
 * @author ajuste
 */
public interface IResourcesManager {

    /**
     * Gets the specified string by name.
     * @param name The resource name.
     * @return The string you are looking for.
     */
    String getString(String name);
    
    /**
     * Gets the drawable id given the name.
     * @param name The drawable name.
     * @return Return The drawable name.
     */
    Integer getDrawableId(String name);
    
    /**
     * Gets the system resource object.
     * @return Instance.
     */
    Object getResourceObject();
}