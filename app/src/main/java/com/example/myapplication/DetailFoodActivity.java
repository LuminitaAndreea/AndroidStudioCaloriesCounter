package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.CustomDataAdapter;
import com.example.DatabaseHandler;
import com.example.Food;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {
    private DatabaseHandler dba;
    private ArrayList<Food> foodArrayList=new ArrayList<>();
    private CustomDataAdapter foodAdapter;
    private ListView listView;
    private TextView totalCal;
    private Food myfood;
    private static Integer totalCalories = 0;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        listView=findViewById(R.id.listView);
        totalCal=findViewById(R.id.Totalcalories);
        createNotificationChannel();
        refreshData();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void refreshData(){
        foodArrayList.clear();
        dba=new DatabaseHandler(getApplicationContext());
        ArrayList<Food>food=dba.getAllFood();

        for(int i=0;i<food.size();i++) {
            String name = food.get(i).getFoodName();
            String cal = food.get(i).getCalories();
            String Date = food.get(i).getRecordDate();
            String foodId = food.get(i).getFoodId();

            myfood = new Food();

            myfood.setFoodName(name);
            myfood.setCalories(cal);
            myfood.setRecordDate(Date);
            myfood.setFoodId(foodId);

            totalCalories += Integer.parseInt(cal);

            foodArrayList.add(myfood);
        }
        if (totalCalories >= 2300) {
            sendNotification();
        }
        dba.close();

        foodAdapter=new CustomDataAdapter(DetailFoodActivity.this,R.layout.list_row,foodArrayList);
        listView.setAdapter(foodAdapter);
        totalCal.setText(totalCalories.toString());
        foodAdapter.setNotifyOnChange(true);
    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from CalorieCounter");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've reached the maximum calories")
                .setContentText("Be careful! You have already eaten the maximum amount of calories per day.")
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;
    }
}
