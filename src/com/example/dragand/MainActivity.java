package com.example.dragand;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity{ 

    @Override 
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        SlidingView sv = new SlidingView(this); 
        View v1 = View.inflate(this, R.layout.t1, null); 
        View v2 = View.inflate(this, R.layout.t2, null); 
        sv.addView(v1); 
        sv.addView(v2); 
        setContentView(sv); 
         
    } 
} 
