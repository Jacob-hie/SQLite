package com.hie2j.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_edit extends AppCompatActivity {
    private TextView textNo;
    private EditText editName;
    private EditText editAge;

    private Button btnUpdate;
    private Button btnDelete;

    String no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textNo = findViewById(R.id.edit_no);
        editName = findViewById(R.id.edit_name);
        editAge = findViewById(R.id.edit_age);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        int age = intent.getIntExtra("AGE",0);
        no = intent.getStringExtra("NO");

        editName.setText(name);
        editAge.setText(String.valueOf(age));
        textNo.setText(no);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                int age = Integer.parseInt(editAge.getText().toString());
                String no  = textNo.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("NAME",name);
                intent.putExtra("AGE",age);
                intent.putExtra("NO",no);
                setResult(3003,intent);

                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.delete(no);
                finish();
            }
        });

    }
}
