/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui.resources;

import android.content.res.Resources;
import com.guzzler.common.IResourcesManager;

/**
 *
 * @author ajuste
 */
public class DefaultResourcesImpl implements IResourcesManager {

    private Resources resources = null;
    private String packageName = "";

    public DefaultResourcesImpl(Resources resources) {
        this(resources, "com.guzzler");
    }

    public DefaultResourcesImpl(Resources resources, String packageName) {
        this.resources = resources;
        this.packageName = packageName;
    }

    public String getString(String name) {
        return resources.getString(resources.getIdentifier(name, "string", this.packageName));
    }

    public Integer getDrawableId(String name) {
        return resources.getIdentifier(name, "drawable", this.packageName);
    }

    public Object getResourceObject() {
        return this.resources;
    }
}