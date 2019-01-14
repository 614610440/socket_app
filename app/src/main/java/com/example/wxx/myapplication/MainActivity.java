package com.example.wxx.myapplication;

import android.os.StrictMode;
import android.os.StrictMode.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;

public class MainActivity extends AppCompatActivity implements Runnable{
    private TextView tv_msg = null;
    private EditText ed_msg = null;
    private Button btn_send = null;

    private static final String HOST = "192.168.1.111";
    private static final int PORT = 8888;
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String content = "";

    /** callen when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_msg = (TextView)findViewById(R.id.TextView);
        ed_msg = (EditText)findViewById(R.id.EditText01);
        btn_send = (Button)findViewById(R.id.Button02);
        Log.v("debug","hello");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

//        setContentView(R.layout.activity_login);
        System.out.println("Hello World");
        try{
            socket = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            Log.v("debug","create socket");
            System.out.println("create socket");
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn_send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String msg = ed_msg.getText().toString();
                if (socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println(msg);
//                        out.write("Hello World");
                        out.flush();

                        System.out.println("send msg2");
                    }
                }
            }
        });

        new Thread(MainActivity.this).start();
//        new Thread(reciveMsg)
    }

    public void run(){
        /*
        try {
            while (true){
                /*
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        if ((content = in.readLine()) != null) {
                            content += "\n";
                            mHandler.sendMessage(mHandler.obtainMessage());
                            tv_msg.setText(content);
                            System.out.println("test: "+content);
//                            System.out.println("recive");
//                            mHandler.sendMessage("Hello World");
                            content = "";
                        } else {

                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }   */
        try{
            InputStream recive_stream = socket.getInputStream();
            byte buffer[] = new byte[1024];
            int read_fd = 0;
            while (true){
                read_fd = recive_stream.read(buffer);
                if (read_fd < 0){
                    System.out.println("no recive msg");
                    Thread.sleep(100);
                    continue;
                }
                else {
                    String new_str = new String(buffer, 0, read_fd);

                    if (new_str != null) {
                        System.out.println(new_str);
                        tv_msg.setText(new_str);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_msg.setText(tv_msg.getText().toString() + content);
//              tv_msg.setText("Hello World");
        }

        public void resetTextView(TextView view, String str){
            view.setText(str);
        }
    };

    /*
    public void reciveMsg() throws IOException {
        InputStream recive_stream = socket.getInputStream();
        byte buffer[] = new byte[1024];
        int read_fd = 0;
        while (true){
            try{
                read_fd = recive_stream.read(buffer);

                if (read_fd < 0){
                    System.out.println("no recive msg");
                    Thread.sleep(100);
                    continue;
                }
                else {
//                    mHandler.resetTextView(tv_msg, "Hello");
                    String new_str = new String(buffer, 0, read_fd);
                    System.out.println(new_str);
                    tv_msg.setText("Hello");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    */
}
