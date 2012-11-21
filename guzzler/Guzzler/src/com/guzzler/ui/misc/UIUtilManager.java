/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui.misc;

import android.content.res.Resources;
import android.view.MotionEvent;
import com.guzzler.common.IResourcesManager;
import com.guzzler.ui.resources.DefaultResourcesImpl;

/**
 *
 * @author ajuste
 */
public class UIUtilManager {

    /**
     * Drag an drop phase 1: User clicked.
     */
    private static boolean DragPhase1 = false;
    /**
     * Drag an drop phase 2: User moved the through the screen while clicking.
     */
    private static boolean DragPhase2 = false;
    private static long dragEventCount = 0;

    public static IResourcesManager createResourceInstance(Resources resources) {
        return new DefaultResourcesImpl(resources);
    }

    private static void registerDnDPhase1(boolean flag) {
        UIUtilManager.DragPhase1 = flag;
        UIUtilManager.DragPhase2 &= flag;
    }

    private static void registerDnDPhase2(boolean flag) {
        UIUtilManager.DragPhase2 = flag && UIUtilManager.DragPhase1;
    }

    public static DragAndDropReviewResult checkForDragAndDrop(MotionEvent evt) {

        DragAndDropReviewResult result;

        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                UIUtilManager.registerDnDPhase1(true);
                result = DragAndDropReviewResult.None;
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (!UIUtilManager.DragPhase1) {
                    result = DragAndDropReviewResult.None;
                } else if (!UIUtilManager.DragPhase2) {
                    result = DragAndDropReviewResult.DragStarted;
                } else {
                    result = DragAndDropReviewResult.Dragged;
                    //result = dragEventCount++ % 2 == 0 ? DragAndDropReviewResult.Dragged : DragAndDropReviewResult.None;
                }
                registerDnDPhase2(true);
            }
            break;
            case MotionEvent.ACTION_UP: {
                result = UIUtilManager.DragPhase1 && UIUtilManager.DragPhase2 ? DragAndDropReviewResult.Dropped : DragAndDropReviewResult.None;
                registerDnDPhase1(false);
            }
            break;
            default: {
                result = DragAndDropReviewResult.None;
            }
            break;
        }
        return result;
    }

    public enum DragAndDropReviewResult {

        None,
        DragStarted,
        Dragged,
        Dropped
    }
}