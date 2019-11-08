package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookMainActivity extends AppCompatActivity {
 private Button buttonOK,buttoncancel;
 private EditText edittextbookname;
    private int insertPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_main);
        buttonOK=(Button)findViewById(R.id.bok);
        buttoncancel=(Button)findViewById(R.id.bcancel);
        edittextbookname=(EditText)findViewById(R.id.editbook);
        edittextbookname.setText(getIntent().getStringExtra("title"));
        insertPosition = getIntent().getIntExtra("insert_position", 0);
        buttonOK.setOnClickListener(new mClick());
        buttoncancel.setOnClickListener(new nClick());
    }


    private class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.putExtra("title",edittextbookname.getText().toString());
            setResult(RESULT_OK,intent);
            EditBookMainActivity.this.finish();
        }
    }

    private class nClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditBookMainActivity.this.finish();
        }
    }
}
