package com.example.sagar.popupshops_buyerside;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Vikram on 03-Nov-17.
 */

    public class view extends LoginActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view);

            Button listview = (Button)findViewById(R.id.lis);
            Button addtolist = (Button)findViewById(R.id.ad);
            final Button locate = (Button)findViewById(R.id.loc);
            EditText TextHandler = (EditText)findViewById(R.id.editer);
            TextHandler.setOnKeyListener(new TextHandler());

            listview.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(com.example.sagar.popupshops_buyerside.view.this, recycle.class);
                    startActivity(intent);
                }
            });

            addtolist.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(com.example.sagar.popupshops_buyerside.view.this, add.class);
                    startActivity(intent);
                }
            });

            locate.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view) {
                    if (view.getId() == R.id.loc) {
                        locate.setText("Location Updated");
                    }
                }
            });
        }

        public class TextHandler implements View.OnKeyListener
        {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                int viewID = view.getId();
                EditText input_text = (EditText)findViewById(viewID);
                String the_text = input_text.getText().toString();

                if (KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    Toast.makeText(view.this,"Profile Updated" + the_text,Toast.LENGTH_LONG);
                }
                return false;
            }
        }
    }
