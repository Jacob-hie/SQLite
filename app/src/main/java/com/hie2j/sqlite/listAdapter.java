package com.hie2j.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class listAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Student> studentArrayList;

    public listAdapter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
    }
    /**
     * 改变数据源
     * */
    public void changeData(ArrayList<Student> studentArrayList){
        this.studentArrayList = studentArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return studentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list,parent,false);
            //使用Holder保存子组件 避免每次使用时重复find
            StuViewHolder holder = new StuViewHolder();
            holder.ageView = convertView.findViewById(R.id.stu_age);
            holder.nameView = convertView.findViewById(R.id.stu_name);
            holder.noView = convertView.findViewById(R.id.stu_no);

            convertView.setTag(holder);
        }

        Student stu = studentArrayList.get(position);
        StuViewHolder holder = (StuViewHolder) convertView.getTag();

        holder.noView.setText(stu.getNo());
        holder.nameView.setText(stu.getName());
        holder.ageView.setText(String.valueOf(stu.getAge()));

        return convertView;
    }
}
