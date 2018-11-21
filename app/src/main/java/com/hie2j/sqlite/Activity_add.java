package com.hie2j.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_add extends AppCompatActivity {
    private EditText editNo;
    private EditText editName;
    private EditText editAge;

    private Button btnAddStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editNo = findViewById(R.id.edit_no);
        editName = findViewById(R.id.edit_name);
        editAge = findViewById(R.id.edit_age);
        btnAddStudent = findViewById(R.id.btn_add);

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                int age = Integer.parseInt(editAge.getText().toString());
                String no = editNo.getText().toString();

                if (name.equals("") || editAge.getText().toString().equals("") || no.equals("")){
                    finish();
                }

                Intent intent = new Intent();
                intent.putExtra("NAME",name);
                intent.putExtra("AGE",age);
                intent.putExtra("NO",no);
                setResult(2002,intent);

                finish();
            }
        });

    }
}
