package com.hie2j.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

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

        Student student = (Student) getIntent().getSerializableExtra("STUDENT");
        editName.setText(student.getName());
        editAge.setText(String.valueOf(student.getAge()));
        textNo.setText(student.getNo());
        no = student.getNo();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                int age = Integer.parseInt(editAge.getText().toString());
                String no = textNo.getText().toString();
                Student student = new Student(no,name,age);
                Log.e("student",""+student.getName());
                Intent intent = new Intent();
//                intent.putExtra("STUDENT",student);
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
//                MainActivity.delete(no);
//                finish();
            }
        });

    }
}
