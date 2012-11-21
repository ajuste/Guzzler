/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.model;

import com.guzzler.common.IEntity;
import java.util.UUID;

/**
 *
 * @author ajuste
 */
public class CharacterTemplateInfo implements IEntity, Comparable<CharacterTemplateInfo> {

    public static final int IMAGE_INDEX_NORMAL_BIG = 0;
    public static final int IMAGE_INDEX_NORMAL_PREVIEW = 1;
    public static final int IMAGE_INDEX_NORMAL_BIG_EYES = 2;
    public static final int IMAGE_INDEX_NORMAL_BIG_WOEYES = 3;
    protected String name = "";
    protected int[] bitmaps = {};
    protected UUID id = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(int[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int compareTo(CharacterTemplateInfo other) {
        return (this.id != null ? this.id.hashCode() : 0) - (other.id != null ? other.id.hashCode() : 0);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}