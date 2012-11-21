/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.guzzler.Interaction;
import com.guzzler.model.CharacterInfo;

/**
 *
 * @author ajuste
 */
public class InteractionSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private enum Gesture {

        Normal(500);
        public int step = 0;
        public int steps = 0;

        Gesture(int steps) {
            this.steps = steps;
        }
    }
    private Thread drawingThread;
    private InteractionDrawingRunnable runnable;
    private Interaction interaction;
    private DragAndDropParams dndParameters = new DragAndDropParams();
    private EyeParams eyeParams = new EyeParams();
    private CharacterInfo character;
    private GameUI gameUI;
    private CharacterUI characterUI;
    private Gesture currentGesture = Gesture.Normal;
    private DrawingObjects drwObjects;
    private static final int EyeStepIni = 150;
    private static final int EyeStepEnd = 160;
    private static final int HeadTwistLeftIni = 460;
    private static final int HeadTwistLeftEnd = 500;
    private static final int HeadTwistRightIni = 60;
    private static final int HeadTwistRightEnd = 100;

    public InteractionSurfaceView(Context context, Interaction interaction, View view, CharacterInfo character, GameUI gameUI, CharacterUI characterUI) {
        super(context);
        this.getHolder().addCallback(this);
        this.interaction = interaction;
        this.gameUI = gameUI;
        this.characterUI = characterUI;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.runnable = new InteractionDrawingRunnable(interaction, holder, this);
        this.drawingThread = new Thread(runnable);
        this.drawingThread.start();
        this.drwObjects = new DrawingObjects();
        this.drwObjects.rectChar = new Rect();
        this.drwObjects.rectEyesBm = new Rect();
        this.drwObjects.paintCharacterBm = new Paint();
        this.drwObjects.paintCharacterBm.setAntiAlias(true);
        this.drwObjects.paintEyesBm = new Paint();
        this.drwObjects.pathMouth = new Path();
        this.drwObjects.paintEyes = new Paint();
        this.drwObjects.rectEyes = new Rect();
        this.drwObjects.matrix = new Matrix();
        this.drwObjects.paintEyesBackground = new Paint();
        this.drwObjects.paintDrag = new Paint();
        this.drwObjects.paintDrag.setColor(Color.BLACK);
        this.drwObjects.paintDrag.setAntiAlias(true);

        //  TODO: Character specific
        this.drwObjects.paintEyes.setColor(Color.rgb(205, 183, 158));
        this.drwObjects.paintEyesBackground.setColor(Color.WHITE);


    }

    private void release() {
        this.drawingThread = null;
        this.runnable = null;
        this.drwObjects.paintCharacterBm = null;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        this.runnable.setRunning(false);
        try {
            this.drawingThread.join();
        } catch (InterruptedException ex) {
            Log.wtf(this.getClass().getSimpleName(), "Stop drawing thread", ex);
        } finally {
            this.release();
        }
    }

    private void drawEye(Canvas canvas, boolean left, int bmEyesWidth, int bmEyesHeight, Bitmap bmEyes) {

        DrawingObjects drwO = this.drwObjects;

        //  TODO: Get offset from char spec.     
        int offsetX = left ? 0 : 60;
        int eyeRadio = bmEyesHeight / 4;
        float eyeDy;

        drwO.rectEyesBm.set(21 + offsetX, 56, 21 + bmEyesWidth + offsetX, 56 + bmEyesHeight);

        //  Offset eyes with char location.
        drwO.rectEyesBm.top += drwO.rectChar.top;
        drwO.rectEyesBm.bottom += drwO.rectChar.top;
        drwO.rectEyesBm.left += drwO.rectChar.left;
        drwO.rectEyesBm.right += drwO.rectChar.left;

        //  Draw eyes background (same color as border of the eyes bitmap).
        drwO.rectEyes.set(drwO.rectEyesBm);   //  based on the eyes
        drwO.rectEyes.bottom += 5;
        drwO.rectEyes.right += 5;
        drwO.rectEyes.left -= 5;
        drwO.rectEyes.top -= 5;
        canvas.drawRect(drwO.rectEyes, drwO.paintEyesBackground);

        //  Draw eye bitmap
        if (this.eyeParams.lookingAt) {
            double angle = Math.atan2(this.eyeParams.lookingAtY - drwO.rectEyesBm.centerY(), drwO.rectEyesBm.centerX() - this.eyeParams.lookingAtX);
            float dx = (float) (eyeRadio * Math.cos(angle));
            float dy = (float) (eyeRadio * Math.sin(angle));
            drwO.rectEyesBm.top += dy;
            drwO.rectEyesBm.bottom += dy;
            drwO.rectEyesBm.left -= dx;
            drwO.rectEyesBm.right -= dx;
        }
        canvas.drawBitmap(bmEyes, null, drwO.rectEyesBm, drwO.paintEyesBm);

        //  Draw eye closing (if needed)
        if (this.currentGesture.step >= EyeStepIni && this.currentGesture.step <= EyeStepEnd) {
            eyeDy = ((float) (this.currentGesture.step - EyeStepIni)) / (EyeStepEnd - EyeStepIni);
            drwO.rectEyes.bottom = (int) (drwO.rectEyes.top + (eyeDy * bmEyesHeight));
            canvas.drawRect(drwO.rectEyes, drwO.paintEyes);
        }
    }

    private void ApplyHeadTwist() {
        float percentage;
        if (this.currentGesture.step >= HeadTwistLeftIni && this.currentGesture.step <= HeadTwistLeftEnd) {
            //  Completitud of head twist.
            percentage = ((float) (this.currentGesture.step - HeadTwistLeftIni)) / (HeadTwistLeftEnd - HeadTwistLeftIni);
            percentage = 1 - Math.abs(1 - 2 * percentage);
            this.drwObjects.matrix.preRotate(4 * percentage, this.drwObjects.rectChar.centerX(), this.drwObjects.rectChar.centerY());
        }
        if (this.currentGesture.step >= HeadTwistRightIni && this.currentGesture.step <= HeadTwistRightEnd) {
            //  Completitud of head twist.
            percentage = ((float) (this.currentGesture.step - HeadTwistRightIni)) / (HeadTwistRightEnd - HeadTwistRightIni);
            percentage = 1 - Math.abs(1 - 2 * percentage);
            this.drwObjects.matrix.preRotate(-4 * percentage, this.drwObjects.rectChar.centerX(), this.drwObjects.rectChar.centerY());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        Bitmap bmCharacter;
        Bitmap bmEyes;
        DrawingObjects drwO = this.drwObjects;
        //canvas.
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int bmCharWidth;
        int bmCharHeight;
        int bmEyesHeight;
        int bmEyesWidth;
        //  Reset matrix
        drwO.matrix.reset();
        canvas.drawColor(Color.RED);
        //  
        bmCharacter = this.characterUI.getCharacterMainBitmapAccordingToState(character, this.gameUI.getResources());
        bmEyes = this.characterUI.getCharacterEyesMainBitmapAccordingToState(character, this.gameUI.getResources());
        //  Get bitmaps.
        bmCharWidth = bmCharacter.getWidth();
        bmCharHeight = bmCharacter.getHeight();
        bmEyesHeight = bmEyes.getHeight();
        bmEyesWidth = bmEyes.getWidth();
        //  Calculate character position.
        drwO.rectChar.top = (canvasHeight - bmCharHeight) / 2;
        drwO.rectChar.bottom = drwO.rectChar.top + bmCharHeight; //canvasHeight - this.rectChar.top;
        drwO.rectChar.left = (canvasWidth - bmCharWidth) / 2;
        drwO.rectChar.right = canvasWidth - (canvasWidth - bmCharWidth) / 2;
        //  Apply head twist
        this.ApplyHeadTwist();
        //  Set matrix.
        canvas.setMatrix(drwO.matrix);
        //  Draw eyes.
        this.drawEye(canvas, true, bmEyesWidth, bmEyesHeight, bmEyes);
        this.drawEye(canvas, false, bmEyesWidth, bmEyesHeight, bmEyes);
        //  Draw character.
        canvas.drawBitmap(bmCharacter, null, drwO.rectChar, drwO.paintCharacterBm);
        this.currentGesture.step = (this.currentGesture.step + 1) % this.currentGesture.steps;

        if (this.dndParameters.dragging && this.dndParameters != null) {
            int bmDragWidth = this.dndParameters.bitmap.getWidth();
            int bmDragHeight = this.dndParameters.bitmap.getHeight();
            canvas.drawCircle(this.dndParameters.x - bmDragWidth / 2, this.dndParameters.y - bmCharHeight / 2, bmDragWidth, drwO.paintDrag);
        }
        canvas.setMatrix(null);
    }

    public class DragAndDropParams {

        public boolean dragging = false;
        public float x = 0;
        public float y = 0;
        public Bitmap bitmap;
    }

    public class EyeParams {

        public boolean lookingAt = false;
        public float lookingAtX = -1.0f;
        public float lookingAtY = -1.0f;
    }

    private class DrawingObjects {

        Paint paintEyes;
        Path pathMouth;
        Rect rectChar;
        Rect rectEyesBm;
        Rect rectEyes;
        Paint paintCharacterBm;
        Paint paintEyesBm;
        Paint paintEyesBackground;
        Paint paintDrag;
        Matrix matrix;
    }

    public DragAndDropParams getDndParameters() {
        return dndParameters;
    }

    public EyeParams getEyeParams() {
        return eyeParams;
    }
}