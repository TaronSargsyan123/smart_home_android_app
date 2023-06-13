package com.revive.smarthome;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


// _________________________________________________________________________________________________
// ______________________________________CLASS DON'T USED___________________________________________
// _________________________________________________________________________________________________
public class Network extends Thread {
    private Socket s;
    public String message;
    private String host;
    private int port;


    public Network(String host, int port) {

        this.host = host;
        this.port = port;



    }

    public void setMessage(String  temp) {
        message = temp;
    }


    private void doInBackground() {

        try {
            Log.i("HOST FROM RELAY", host);
            Log.i("PORT FROM RELAY", String.valueOf(port));
            s = new Socket(host, port);

            DataOutputStream dout = new DataOutputStream(s.getOutputStream());


            dout.writeUTF(String.valueOf(message));
            dout.flush();
            dout.close();
            s.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            setMessage(message);
            doInBackground();

        } catch (Exception ignored) {
        }

    }
}
