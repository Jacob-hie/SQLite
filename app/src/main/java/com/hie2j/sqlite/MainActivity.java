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

public class MainActivity extends AppCompatActivity {
    private ListView stuListView;
    private Button btnAddStu;
    private Button btnCheckStu;
    private EditText editCheck;
    private static listAdapter stuAdapter;
    public static SQLiteDatabase db;
    private static ArrayList<Student> studentArrayList = new ArrayList<>();
    private static ArrayList<Student> searchResultList = new ArrayList<>();
    boolean isResultList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createOrOpenDatabase();
        createTable();
        initStudentArrayList();
        initAddStudnet();
        initAdapter();
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

                if (isResultList) {
                    s = searchResultList.get(position);
                }

                Toast.makeText(MainActivity.this, "姓名" + s.getName()
                        + "年龄" + s.getAge(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, Activity_edit.class);
                intent.putExtra("NAME", s.getName());
                intent.putExtra("AGE", s.getAge());
                intent.putExtra("NO", s.getNo());
                startActivityForResult(intent, 1002);

            }
        });
    }

    //初始化适配器
    private void initAdapter() {
        stuAdapter = new listAdapter(MainActivity.this, studentArrayList);
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
                search(studentArrayList, keyword);
                stuAdapter.changeData(searchResultList);
                isResultList = true;
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
                search(studentArrayList, keyword);
                stuAdapter.changeData(searchResultList);
                isResultList = true;
            }
        });
    }

    //搜索引擎
    private void search(ArrayList<Student> studentArrayList, String keyword) {
        searchResultList.clear();
        for (int i = 0; i < studentArrayList.size(); i++) {
            Student s = studentArrayList.get(i);
            // 如果学生姓名包含了关键字 这个学生就加入到结果列表
            if (s.getName().contains(keyword)) {
                searchResultList.add(s);
            }
            // 如果学生年龄包含关键字 这个学生就加入到结果列表
            else if (String.valueOf(s.getAge()).contains(keyword)) {
                searchResultList.add(s);
            }
            // 如果学生学号等于关键字 这个学生就加入到结果列表
            else if (s.getNo().equals(keyword)) {
                searchResultList.add(s);
            }
        }
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
    private void initStudentArrayList() {
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
        cursor.close();
    }

    //创建或打开数据库
    private void createOrOpenDatabase() {
        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";

        db = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    //创建数据表
    private void createTable() {
        if (db == null) {
            createOrOpenDatabase();
        }
        String sql = "create table if not exists student (stuno varchar(20),name varchar(20),age int)";
        db.execSQL(sql);
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
            String sql = "insert into student values('" + no + "','" + name + "'," + age + ")";
            db.execSQL(sql);

            Student stu = new Student(no, name, age);
            studentArrayList.add(stu);
            stuAdapter.notifyDataSetChanged();
        } else if (resultCode == 3003) {

            String name = data.getStringExtra("NAME");
            int age = data.getIntExtra("AGE", 0);
            String no = data.getStringExtra("NO");
            Log.e("MainActivity", "resultCode = " + no);
            String sql = "update student set name = '" + name + "',age = '" + age + "'where stuno = '" + no + "'";
            db.execSQL(sql);

            for (int i = 0; i < studentArrayList.size(); i++) {
                Student s = studentArrayList.get(i);
                Log.e("MainActivity", "resultCode = " + s.getNo());
                if (s.getNo().equals(no)) {
                    s.setName(name);
                    s.setAge(age);
                }
            }
            stuAdapter.notifyDataSetChanged();
            String keyword = editCheck.getText().toString().trim();
             //使用学生列表和关键字 得到搜索结果列表
            search(studentArrayList, keyword);
            stuAdapter.changeData(searchResultList);
            isResultList = true;
        }
    }

    //删除对应数据
    public static void delete(String stu) {

        for (int i = 0; i < studentArrayList.size(); i++) {
            if (studentArrayList.get(i).getNo().equals(stu)) {
                studentArrayList.remove(i);
                stuAdapter.notifyDataSetChanged();
            }
        }

        String sql = "delete from student where stuno = '" + stu + "'";
        db.execSQL(sql);
    }
}
