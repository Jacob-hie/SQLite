package com.hie2j.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IOnDelListener{
    private ListView stuListView;
    private Button btnAddStu;
    private Button btnCheckStu;
    private EditText editCheck;
    private static listAdapter stuAdapter;
    public static SQLiteDatabase db;
    private static ArrayList<Student> studentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAdapter();
        readDataFromDB();
        initAddStudnet();
        initSearchView();
        initStuListView();

    }

    //初始化listView
    private void initStuListView() {
        stuListView = findViewById(R.id.listview);
        stuListView.setAdapter(stuAdapter);
        stuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student s = studentArrayList.get(position);

                Toast.makeText(MainActivity.this, "姓名" + s.getName()
                        + "年龄" + s.getAge(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, Activity_edit.class);
                intent.putExtra("STUDENT", s);
//                intent.putExtra("NAME", s.getName());
//                intent.putExtra("AGE", s.getAge());
//                intent.putExtra("NO", s.getNo());
                startActivityForResult(intent, 1002);

            }
        });
    }

    //初始化适配器
    private void initAdapter() {
        stuAdapter = new listAdapter(MainActivity.this, studentArrayList,MainActivity.this);
    }

    //初始搜索模块
    private void initSearchView() {
        btnCheckStu = findViewById(R.id.btn_checkStu);
        editCheck = findViewById(R.id.edit_Check);
        btnCheckStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editCheck.getText().toString().trim();
                // 使用学生列表和关键字 得到搜索结果列表
                searchFromDB(keyword);
            }
        });
        editCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editCheck.getText().toString().trim();
                // 使用学生列表和关键字 得到搜索结果列表
                searchFromDB(keyword);
            }
        });
    }

    //搜索引擎
    private void searchFromDB(String key) {
        String where = "name like '%" + key + "%' OR stuno like '%" + key + "%' OR age like '%" + key + "%'";

        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);

        studentArrayList.clear();


        Cursor cursor = db.query("student", null, where,
                null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String stuno = cursor.getString(0);
                String name = cursor.getString(1);
                int age = cursor.getInt(2);

                Student student = new Student(stuno, name, age);
                studentArrayList.add(student);
            }
        }

        stuAdapter.notifyDataSetChanged();
        cursor.close();
        db.close();
    }

    //添加学生
    private void initAddStudnet() {
        btnAddStu = findViewById(R.id.btn_addStu);
        btnAddStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        Activity_add.class);
                startActivityForResult(intent, 1001);
            }
        });
    }

    //初始化学生列表
    private void readDataFromDB() {

        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";
        db = SQLiteDatabase.openOrCreateDatabase(path, null);

        String sql = "create table if not exists student (stuno varchar(20),name varchar(20),age int)";
        db.execSQL(sql);

        Cursor cursor = db.query("student", null, null, null
                , null, null, null);
        if (cursor == null) {
            Log.e("MainActivity", "cursor == null");
            return;
        }
        studentArrayList.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String no = cursor.getString(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
            studentArrayList.add(new Student(no, name, age));
        }
//        Log.e("MainActivity", "size = " + studentArrayList.get(0).getNo());
        stuAdapter.notifyDataSetChanged();
        cursor.close();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == 2002) {
            String no = data.getStringExtra("NO");
            String name = data.getStringExtra("NAME");
            int age = data.getIntExtra("AGE", 0);
            //往数据库添加数据
            Student student = new Student(no, name, age);
            addStudentToDB(student);
        } else if (resultCode == 3003) {

//            Student student = (Student) getIntent().getSerializableExtra("STUDENT");
            String no = data.getStringExtra("NO");
            String name = data.getStringExtra("NAME");
            int age = data.getIntExtra("AGE", 0);
            Student student = new Student(no, name, age);
//            if (student == null) {
//                Log.e("student", "error");
//            } else {
            Log.e("MainActivity", "name =" + student.getName());
            updateStudentToDB(student);
//            }

        }

    }

    private void addStudentToDB(Student student) {
        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);

        ContentValues values = new ContentValues();
        values.put("stuno", student.getNo());
        values.put("name", student.getName());
        values.put("age", student.getAge());

        db.insert("student", null, values);
        db.close();
        readDataFromDB();
    }

    private void updateStudentToDB(Student student) {
        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);

        //第一种 组装更新语句
//        String sql = "update studnet set name ='"+student.getName()+"', "+
//                "age = '"+student.getAge()+"', where stuno = '"+student.getStuno()+"'";
//        db.execSQL(sql);
//        db.close();
//        readDataFromDB();
        //第二种 使用update接口
        String where = "stuno = '" + student.getNo() + "'";
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("age", student.getAge());
        db.update("student", values, where, null);
        db.close();
        readDataFromDB();
        //第三种 使用带参数接口
//        String where = "stuno = ?";
//        String[] args = {student.getNo()};
//        ContentValues values = new ContentValues();
//        values.put("name",student.getName());
//        values.put("age",student.getAge());
//        db.update("student",values,where,args);
//        db.close();
//        readDataFromDB();
    }

    //删除对应数据
    public void delete(Student student) {
        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
        //第一种 组装删除语句
        String sql = "delete from student where stuno = '" + student.getNo() +"'";
        db.execSQL(sql);
        db.close();
        readDataFromDB();

        //第二种 使用delete接口
//        String where = "stuno = '"+student.getStuno()+"'";
//        db.delete("student",where,null);
//        db.close();
//        readDataFromDB();

        //第三种 使用带参数接口
//        String where = "stuno = ?";
//        String[] args = {student.getStuno()};
//        db.delete("studnet",where,args);
//        db.close();
//        readDataFromDB();
    }
}
