package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CustomDataAdapter extends ArrayAdapter<Food> {
    private int layoutResource;
    private Activity activity;
    private ArrayList<Food> foodArrayList=new ArrayList<>();

    public CustomDataAdapter(@NonNull Activity act, int resource, ArrayList<Food>data){
        super(act,resource,data);
        layoutResource=resource;
        activity=act;
        foodArrayList=data;

        notifyDataSetChanged();
    }
    @Override
    public  int getCount(){
        return foodArrayList.size();
    }
    @Nullable
    @Override
    public Food getItem(int position){
        return foodArrayList.get(position);
    }
    @Override
    public  int getPosition(@Nullable Food item){
        return super.getPosition(item);
    }
    @Override
    public long getItemId(int position){
        return super.getItemId(position);
    }
    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent){
        View row=convertView;
        ViewHolder holder=null;
        if(row==null||(row.getTag()==null)) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResource, null);
            holder = new ViewHolder();
            holder.foodName = row.findViewById(R.id.name);
            holder.foodCalories = row.findViewById(R.id.calories);
            holder.foodDate = row.findViewById(R.id.dateText);

            row.setTag(holder);
        }else{
            holder=(ViewHolder)row.getTag();
        }

        holder.food=getItem(position);
        holder.foodName.setText(holder.food.getFoodName());
        holder.foodCalories.setText(holder.food.getCalories());
        holder.foodDate.setText(holder.food.getRecordDate());
        return  row;
    }
    public  class ViewHolder{
        Food food;
        TextView foodName, foodCalories, foodDate;
    }
}
