/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.guzzler.Interaction;

/**
 *
 * @author ajuste
 */
public class InteractionDrawingRunnable implements Runnable {

    private Interaction activity;
    private SurfaceHolder surfaceHolder;
    private long lastRun = 0l;
    private static final long DrawSpan = 100l;
    private boolean running = false;
    private InteractionSurfaceView surface;

    public InteractionDrawingRunnable(Interaction activity, SurfaceHolder surfaceHolder, InteractionSurfaceView surface) {
        this.activity = activity;
        this.surfaceHolder = surfaceHolder;
        this.surface = surface;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        long timeDiff;
        this.running = true;
        Canvas canvas = null;
        do {
            try {
                this.lastRun = System.currentTimeMillis();
                try {
                    canvas = this.surfaceHolder.lockCanvas(null);
                    if (canvas != null) {
                        this.surface.onDraw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    canvas = null;
                }
                timeDiff = System.currentTimeMillis() - this.lastRun;
                if (timeDiff < InteractionDrawingRunnable.DrawSpan) {
                    Thread.sleep(timeDiff);
                }
            } catch (Exception ex) {
                Log.e(this.getClass().getSimpleName(), "run - At drawing thread", ex);
            } finally {
                continue;
            }
        } while (this.running);
    }
}
