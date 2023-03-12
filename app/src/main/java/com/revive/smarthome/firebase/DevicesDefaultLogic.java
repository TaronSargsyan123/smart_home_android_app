package com.revive.smarthome.firebase;

import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DevicesDefaultLogic {
    final FirebaseDatabase database = FirebaseDatabase.getInstance("https://revive-smart-home-692c2-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference usersRef = database.getReference("users");
    DatabaseReference analyticsRef = database.getReference("analytics");

    public void insertDataROneVOne(int stage, String email, String deviceName, String deviceType) {

        if (!Objects.equals(email, "")) {
            DefaultDatabaseInstance instance = new DefaultDatabaseInstance(email, deviceName, deviceType, stage);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }

    public void insertDataCROne(int stage, String email, String deviceName, String deviceType, String color) {

        if (!Objects.equals(email, "")) {
            CROneDatabaseInstance instance = new CROneDatabaseInstance(email, deviceName, deviceType, stage, color);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }

    public void insertDataCurVOne(String stage, String email, String deviceName, String deviceType) {

        if (!Objects.equals(email, "")) {
            CurVOneDatabaseInstance instance = new CurVOneDatabaseInstance(email, deviceName, deviceType, stage);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);
        }
    }

    public void insertDataThVOne(String email, String deviceName, String deviceType, String temperature, String humidity){
        if (!Objects.equals(email, "")) {
            ThVOneDatabaseInstance instance = new ThVOneDatabaseInstance(email, deviceName, deviceType, temperature, humidity);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);
        }

    }

    public void insertDataDOne(int stage, String email, String deviceName, String deviceType, int bright) {
        if (!Objects.equals(email, "")) {
            DOneDatabaseInstance instance = new DOneDatabaseInstance(email, deviceName, deviceType, stage, bright);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }


    private void insertAnalyticsData(String email, String deviceName, ArrayList<String> data){
        if (!Objects.equals(email, "")) {
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            analyticsRef.child(userName).child(deviceName).setValue(data);
        }
    }

    public void updateAnalyticsData(String email, String deviceName, String newData){
        if (!Objects.equals(email, "")) {

            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];

            analyticsRef.child(userName).child(deviceName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    ArrayList<String> temp;
                    if (!task.isSuccessful()) {
                        Log.e("ARR", "Error getting data", task.getException());
                    }
                    else {
                        try {
                            temp = (ArrayList<String>) task.getResult().getValue();
                            temp.add(newData);
                            insertAnalyticsData(email, deviceName, temp);
                        }catch (Exception ignored){
                            ArrayList<String> tempArrayList = new ArrayList<>();
                            tempArrayList.add(newData);
                            insertAnalyticsData(email, deviceName, tempArrayList);
                        }

                    }
                }
            });
        }
    }



    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String separator = "/";
        return year + separator + month + separator + day + separator + hour + separator + minute + separator + second + separator;
    }

    public void drawDevicesFromServer(LinearLayout layout){

    }

    public void drawAnalytics(String email, String deviceName, TextView textView){
        if (!Objects.equals(email, "")) {

            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];

            analyticsRef.child(userName).child(deviceName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    ArrayList<String> temp;
                    if (!task.isSuccessful()) {
                        Log.e("ARR", "Error getting data", task.getException());
                    }
                    else {
                        temp = (ArrayList<String>) task.getResult().getValue();
                        String finalString = "";
                        try {
                            for (String s: temp) {
                                finalString = finalString + "\n" + s;
                            }
                        }catch (Exception ignored){
                            finalString = "History is empty";
                        }

                        textView.setText(finalString);

                    }
                }
            });
        }
    }

    public void drawAnalyticsDOne(String email, String deviceName, LineChartView chart){
        if (!Objects.equals(email, "")) {

            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];

            analyticsRef.child(userName).child(deviceName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    ArrayList<String> temp;
                    if (!task.isSuccessful()) {
                        Log.e("ARR", "Error getting data", task.getException());
                    }
                    else {
                        temp = (ArrayList<String>) task.getResult().getValue();
                        String finalString = "";
                        try {
                            ArrayList<Integer> valuesArray = new ArrayList<>();
                            for (String s: temp) {
                                int tempInt = Integer.parseInt(s.split("/")[7]);
                                valuesArray.add(tempInt);
                            }
                            LineChartData data = new LineChartData();

                            Line line1 = new Line(generateDataForLine(valuesArray)).setColor(Color.BLUE).setCubic(false);

                            List<Line> lines = new ArrayList<>();
                            lines.add(line1);
                            data.setLines(lines);

                            Axis axisY = new Axis().setHasLines(true);
                            axisY.setName("Values");
                            data.setAxisYLeft(axisY);

                            chart.setLineChartData(data);



                        }catch (Exception ignored){
                            finalString = "History is empty";
                        }

                    }
                }
            });
        }


    }


    private List<PointValue> generateDataForLine(ArrayList<Integer> valuesInput) {
        List<PointValue> values = new ArrayList<>();
        Log.i("TAGAN", "here");
        int count = 0;
        for (Integer i: valuesInput) {
            values.add(new PointValue(count, i));
            Log.i("TAGAN", String.valueOf(i));
            count++;
        }
        return values;
    }



    public void deleteDevice(String userName, String deviceName){
        usersRef.child(userName).child(deviceName).removeValue();
    }

    public void clearData(String email){
        if (!Objects.equals(email, "")) {
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).removeValue();
        }
    }

}
