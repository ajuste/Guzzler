package com.guzzler;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.guzzler.bll.CharacterBll;
import com.guzzler.model.CharacterInfo;
import com.guzzler.ui.CharacterTemplateUI;
import com.guzzler.ui.CharacterUI;
import com.guzzler.ui.GameUI;
import com.guzzler.ui.misc.UIUtilManager;
import com.guzzler.ui.InteractionSurfaceView;

public class Interaction extends Activity {

    /**
     * Business layer.
     */
    private CharacterBll bll;
    private CharacterInfo character;
    private CharacterTemplateUI templateUi;
    private CharacterUI characterUi;
    private GameUI gameUi;
    private ImageView ivEnergyCounter;
    private ImageView ivHealthCounter;
    private ImageView ivFoodCounter;
    private ImageView ivFunCounter;
    private InteractionSurfaceView svInteraction;
    //private ImageView ivCharacterMain;
    private LinearLayout llCharacterMain;
    private Thread drawingThread;
    //private ImageView ivMainFlotable;

    private void onCounterClick(View counter) {

        Resources res = this.getResources();
        Context ctx = this.getApplicationContext();
        String msg = "";
        if (counter.equals(ivEnergyCounter)) {
            msg = res.getString(com.guzzler.R.string.screen_interation_toast_energy, Float.valueOf(this.character.getEnergy() * 100).intValue());
        } else if (counter.equals(ivFoodCounter)) {
            msg = res.getString(com.guzzler.R.string.screen_interation_toast_food, Float.valueOf(this.character.getFood() * 100).intValue());
        } else if (counter.equals(ivFunCounter)) {
            msg = res.getString(com.guzzler.R.string.screen_interation_toast_fun, Float.valueOf(this.character.getFun() * 100).intValue());
        } else if (counter.equals(ivHealthCounter)) {
            msg = res.getString(com.guzzler.R.string.screen_interation_toast_health, Float.valueOf(this.character.getHealth() * 100).intValue());
        }
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InteractionSurfaceView.DragAndDropParams dndParams = this.svInteraction.getDndParameters();
        InteractionSurfaceView.EyeParams eyeParams = this.svInteraction.getEyeParams();

        switch (UIUtilManager.checkForDragAndDrop(event)) {
            case DragStarted:
                eyeParams.lookingAt = dndParams.dragging = true;
                eyeParams.lookingAtX = dndParams.x = event.getX();
                eyeParams.lookingAtY = dndParams.y = event.getY();
                //this.svInteraction. //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //Toast.makeText(this.getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();
                //        this.characterUi.setFlotableImage(this.ivCharacterMain, getResources().getDrawable(com.guzzler.R.drawable.pig_128));

                return true;
            case Dragged:
                eyeParams.lookingAtX = dndParams.x = event.getX();
                eyeParams.lookingAtY = dndParams.y = event.getY();
                //  Toast.makeText(this.getApplicationContext(), "Dragged", Toast.LENGTH_SHORT).show();
                //      this.characterUi.moveFlotableTo(this.ivCharacterMain, (int) event.getX(), (int) event.getY(), this.ivCharacterMain.getWidth(), this.ivCharacterMain.getHeight());
                //    this.findViewById(R.id.characterMainLayout).invalidate();
                return true;
            case Dropped:
                eyeParams.lookingAt = dndParams.dragging = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {


        final Resources resources;
        final Context context;

        //  Initialize data.
        context = this.getApplicationContext();
        resources = this.getResources();
        this.templateUi = new CharacterTemplateUI(new CharacterTemplateUI.Dependencies(true, context, resources));
        this.characterUi = new CharacterUI(new CharacterUI.Dependencies(true, context, UIUtilManager.createResourceInstance(resources)));
        this.gameUi = new GameUI(new GameUI.Dependencies(true, context, resources));
        this.bll = new CharacterBll(new CharacterBll.Dependencies(true, context, UIUtilManager.createResourceInstance(resources)));

        //  Set content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.screen_interaction);
        } catch (Exception ex) {
            int i = 0;
        }

        //  Get playing character.
        this.character = this.bll.getPlayingCharacter();
        this.gameUi.loadResources(character);
        //  Floating.
        //this.ivMainFlotable = (ImageView)this.findViewById(com.guzzler.R.id.characterMainFlotable);
        //  Configure counters.
        this.ivEnergyCounter = (ImageView) this.findViewById(com.guzzler.R.id.characterMainEnergyLevel);
        this.ivFoodCounter = (ImageView) this.findViewById(com.guzzler.R.id.characterMainFoodLevel);
        this.ivFunCounter = (ImageView) this.findViewById(com.guzzler.R.id.characterMainFunLevel);
        this.ivHealthCounter = (ImageView) this.findViewById(com.guzzler.R.id.characterMainHealthLevel);
        this.ivEnergyCounter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCounterClick(view);
            }
        });
        this.ivFoodCounter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCounterClick(view);
            }
        });
        this.ivFunCounter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCounterClick(view);
            }
        });
        this.ivHealthCounter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCounterClick(view);
            }
        });
        //  Set character image

        this.llCharacterMain = (LinearLayout) this.findViewById(com.guzzler.R.id.characterMain);
        this.svInteraction = new InteractionSurfaceView(context, this, this.llCharacterMain, this.character, this.gameUi, this.characterUi);
        this.llCharacterMain.addView(svInteraction, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        //ivCharacterMain = (ImageView) findViewById(com.guzzler.R.id.characterMain);
        //this.characterUi.setMainImage(ivCharacterMain, character);
        /*//((Bitmap)((LayerDrawable) ivCharacterMain.getDrawable()).getDrawable(0)).
         .setImageDrawable(templateUi.getCharacterMain(this.bll.getPlayingCharacter().getTemplate()));
         */
        //  Update indicators
        this.characterUi.updateCounterLevels(this.character, this);
        /*
         //  Wire up events
         txtName = (EditText) findViewById(R.id.name);
         txtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
         boolean handled = false;
         if (actionId == EditorInfo.IME_ACTION_DONE) {
         bll.save(new CharacterInfo(txtName.getText().toString(), null));
         handled = true;
         }
         return handled;
         }
         });

         //  Fill character template list
         lvCharacterTemplates = (ListView) findViewById(R.id.characterTemplates);
         lvCharacterTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
         txtName.setVisibility(View.VISIBLE);
         txtName.requestFocus();
         }
         });

         //  Fill character templates.
         //    ui.fillGallery(context, lvCharacterTemplates);
         */
    }
}