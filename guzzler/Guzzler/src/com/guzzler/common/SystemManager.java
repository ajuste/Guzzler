/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common;

import android.util.Log;

/**
 *
 * @author ajuste
 */
public class SystemManager {

    public static final Object CharacterMutex = new Object();
    private static boolean initialized = false;
    private static RunMode runMode = RunMode.Debug;
    
    public enum RunMode {
        Debug,
        Production
    }
    
    public static RunMode getRunMode(){
        return SystemManager.runMode;
    }

    /**
     * Initializes the system.
     * @return True if success or false if not.
     */
    public static boolean initialize() {
        if (!SystemManager.initialized) {
            try {
                FileManager.initialize(true, "Guzzler");
                SystemManager.initialized = true;
            } catch (Exception ex) {
                Log.wtf(SystemManager.class.getSimpleName(), "Failed to initialize system", ex);
            }
        }
        return SystemManager.initialized;
    }
}