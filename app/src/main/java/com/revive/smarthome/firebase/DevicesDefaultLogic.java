package com.revive.smarthome.firebase;

import android.graphics.Color;
import android.util.Log;
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
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
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


    public void drawAnalyticsROneVOne(String email, String deviceName, ColumnChartView chart){
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
                        try {
                            ColumnChartData data = new ColumnChartData();
                            List<Column> columns = new ArrayList<>();
                            Column column = new Column();


                            column.setValues(new ArrayList<SubcolumnValue>() {{
                                for (String s: temp) {
                                    String date = s.split("/")[0] + "/" + s.split("/")[1];
                                    int count = Integer.parseInt(s.split("/")[2]);
                                    add(new SubcolumnValue((float) count, Color.parseColor("#f39c63")).setLabel(date).setValue(count));
                                }
                            }});

                            Axis axisX = new Axis();
                            axisX.setName("Date");
                            columns.add(column);
                            data.setColumns(columns);
                            chart.setColumnChartData(data);

                        }catch (Exception ignored){

                        }

                    }
                }
            });
        }

    }

    public String getDateROneVOne(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        String separator = "/";
        return month + separator + day + separator;
    }


    public void updateAnalyticsDataROneVOne(String email, String deviceName){
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
                        Calendar calendar = Calendar.getInstance();
                        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                        try {
                            try {
                                temp = (ArrayList<String>) task.getResult().getValue();
                                Log.e("ARR", temp.toString(), task.getException());
                                int index = -1;
                                for (String input: temp) {
                                    index++;
                                    if (Objects.equals(input.split("/")[0], day) && Objects.equals(input.split("/")[1], month)){
                                        int count = Integer.parseInt(input.split("/")[2])+1;
                                        input = day +  "/" + month + "/" + count;

                                        temp.remove(index);
                                        temp.add(input);

                                    }
                                }
                            }catch (Exception e){
                                temp = new ArrayList<>();
                                temp.add(day+"/"+month+"/1");
                            }
                            insertAnalyticsData(email, deviceName, temp);
                        }catch (Exception ignored){
                            ArrayList<String> tempArrayList = new ArrayList<>();
                            insertAnalyticsData(email, deviceName, tempArrayList);
                        }

                    }
                }
            });
        }
    }




    private List<PointValue> generateDataForLine(ArrayList<Integer> valuesInput) {
        List<PointValue> values = new ArrayList<>();
        int count = 0;
        for (Integer i: valuesInput) {
            values.add(new PointValue(count, i));
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
