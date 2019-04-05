package com.example.distanzmesser;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    public static TextView txt;
    public static Button btDistance;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btDistance = findViewById(R.id.buttonDistance);
        btDistance.setBackgroundColor(Color.LTGRAY);

        txt = findViewById(R.id.txt);

    }

    public void getDistance(View view){
        MyClientTask clientTask = new MyClientTask("192.168.2.100", 8080);
        clientTask.execute();
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        double distance;

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Socket socket = new Socket(dstAddress, dstPort);

                Log.i("myInfoTag","Client Nachricht: Get Distance" );
                setDistance(leseNachricht(socket));
                System.out.println("Client gelesene Distance: "+getDistance());

                socket.close();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        public void schreibeNachricht(Socket socket, double distance) throws IOException {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeDouble(distance);
        }

        public double leseNachricht(Socket socket) throws IOException {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            double distance = dis.readDouble();
            return distance;
        }

        @Override
        protected void onPostExecute(Void result) {
            txt.setText("Distance: "+getDistance()+" cm");
            super.onPostExecute(result);
        }

        private void setDistance(double distance){
            this.distance = distance;
        }

        private double getDistance(){
            return distance;
        }
    }
}
