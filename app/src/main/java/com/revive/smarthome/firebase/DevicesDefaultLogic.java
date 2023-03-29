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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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
                            Log.i("ARR", valuesArray.size()+"");
                            LineChartData data = new LineChartData();

                            Line line1 = new Line(generateDataForLine(valuesArray)).setColor(Color.parseColor("#f39c63")).setCubic(false);

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


    public void drawAnalyticColumns(String email, String deviceName, ColumnChartView chart){
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

                            Axis axisX = new Axis();
                            axisX.setName("Date");
                            Axis axisY = new Axis();
                            axisY.setName("Values");
                            List<AxisValue> axisValuesX = new ArrayList<>();
                            List<AxisValue> axisValuesY = new ArrayList<>();

                            column.setValues(new ArrayList<SubcolumnValue>() {{
                                float i = (float) -0.15 * temp.size() / 2;
                                for (String s: temp) {
                                    String date = s.split("/")[0] + "/" + s.split("/")[1];
                                    int count = Integer.parseInt(s.split("/")[2]);
                                    add(new SubcolumnValue((float) count, Color.parseColor("#f39c63")).setLabel(date).setValue(count));
                                    axisValuesX.add(new AxisValue(i).setLabel(date));
                                    axisValuesY.add(new AxisValue(count).setLabel(String.valueOf(count)));
                                    i += 0.2;

                                }
                            }});
                            axisX.setValues(axisValuesX);
                            axisY.setValues(axisValuesY);
                            columns.add(column);
                            data.setColumns(columns);
                            data.setAxisXBottom(axisX);
                            data.setAxisYLeft(axisY);
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


    public void updateAnalyticsDataColumns(String email, String deviceName){
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
                                ArrayList<String> daysMonthsList = new ArrayList<>();
                                int index = -1;
                                for (String input: temp) {
                                    index++;
                                    daysMonthsList.add(input.split("/")[0] + "/" + input.split("/")[1]);
                                    if (Objects.equals(input.split("/")[0], day) && Objects.equals(input.split("/")[1], month)) {
                                        int count = Integer.parseInt(input.split("/")[2]) + 1;
                                        input = day + "/" + month + "/" + count;

                                        temp.remove(index);
                                        temp.add(input);

                                    }
                                }
                                if (!daysMonthsList.contains(day + "/" + month)){
                                    String input = day + "/" + month + "/1";
                                    temp.add(input);
                                }


                            }catch (Exception e){
                                temp = (ArrayList<String>) task.getResult().getValue();
                                String input = day +  "/" + month + "/1";
                                temp.add(input);
                                Set<String> set = new HashSet<>(temp);
                                temp.clear();
                                temp.addAll(set);
                            }
                            insertAnalyticsData(email, deviceName, temp);
                        }catch (Exception ignored){
//                            temp = (ArrayList<String>) task.getResult().getValue();
//                            String input = day +  "/" + month + "/1";
//                            temp.add(input);
//                            Set<String> set = new HashSet<>(temp);
//                            temp.clear();
//                            temp.addAll(set);
//                            insertAnalyticsData(email, deviceName, temp);
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
            analyticsRef.child(userName).removeValue();
        }
    }

}
