package com.guzzler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.guzzler.bll.CharacterBll;
import com.guzzler.common.SystemManager;
import com.guzzler.model.CharacterInfo;
import com.guzzler.model.CharacterTemplateInfo;
import com.guzzler.ui.CharacterTemplateUI;
import com.guzzler.ui.DataAdapter;
import com.guzzler.ui.misc.UIUtilManager;

public class CharacterCreation extends Activity {

    Gallery glCharacterTemplates;
    /**
     * Business layer.
     */
    private CharacterBll bll;

    private void gotoInteraction() {
        startActivity(new Intent(this.getApplicationContext(), Interaction.class));
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final EditText txtName;
        final ImageView imgCharacterTemplate;
        final Resources resources;
        final CharacterTemplateUI ui;
        final Context context;
        final Toast tsCharacterTemplate;

        //  Initialize system.
        SystemManager.initialize();

        //  Initialize data.
        context = this.getApplicationContext();
        resources = this.getResources();
        ui = new CharacterTemplateUI(new CharacterTemplateUI.Dependencies(true, context, resources));
        this.bll = new CharacterBll(new CharacterBll.Dependencies(true, context, UIUtilManager.createResourceInstance(resources)));
        tsCharacterTemplate = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        if (this.bll.getPlayingCharacter() != null){
            gotoInteraction();
        }

        //  Set content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_character_edit);

        //  Wire up events
        txtName = (EditText) findViewById(R.id.name);
        txtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    CharacterInfo character = new CharacterInfo();
                    character.setName(txtName.getText().toString());
                    character.setTemplate((CharacterTemplateInfo) glCharacterTemplates.getSelectedItem());
                    bll.save(character);
                    bll.setPlayingFlag(character, true);
                    gotoInteraction();
                    handled = true;
                }
                return handled;
            }
        });

        //  Fill character template list
        imgCharacterTemplate = (ImageView) findViewById(R.id.characterTemplateView);
        glCharacterTemplates = (Gallery) findViewById(R.id.characterTemplates);
        glCharacterTemplates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> gallery, View view, int position, long arg3) {

                CharacterTemplateInfo item = ((DataAdapter<CharacterTemplateInfo>) gallery.getAdapter()).getDataAt(position);
                imgCharacterTemplate.setImageDrawable(ui.getCharacterMain(item));
                txtName.setVisibility(View.VISIBLE);
                tsCharacterTemplate.setGravity(Gravity.BOTTOM, 0, 0);
                tsCharacterTemplate.cancel();
                tsCharacterTemplate.show();
                tsCharacterTemplate.setText(item.getName());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //  Fill character templates.
        ui.fillGallery(context, glCharacterTemplates);
    }
}