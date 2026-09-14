package com.nishat.scrollx10;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.accessibility.AccessibilityEvent;

public class ScrollAccessibilityService extends AccessibilityService {
    private static ScrollAccessibilityService instance;
    private final Handler h=new Handler(Looper.getMainLooper());
    private boolean running=false;
    private boolean reverse=false;

    public static ScrollAccessibilityService get(){ return instance; }
    @Override public void onServiceConnected(){ super.onServiceConnected(); instance=this; }
    @Override public void onDestroy(){ stopScrolling(); if(instance==this) instance=null; super.onDestroy(); }
    @Override public void onAccessibilityEvent(AccessibilityEvent e){}
    @Override public void onInterrupt(){ stopScrolling(); }

    public void startScrolling(){ if(running)return; running=true; reverse=Prefs.reverse(this); tick(); }
    public void stopScrolling(){ running=false; h.removeCallbacksAndMessages(null); }
    public boolean isRunning(){ return running; }

    private void tick(){
        if(!running)return;
        float speed=Math.max(0.1f,Math.min(20f,Prefs.speed(this)));
        long duration=Math.max(12L,Math.round(300f/speed));

        DisplayMetrics dm=getResources().getDisplayMetrics();
        float w=dm.widthPixels, hh=dm.heightPixels;
        float marginX=Math.max(80f,w*0.12f), marginY=Math.max(120f,hh*0.15f);
        float cx=w/2f, cy=hh/2f;
        int dir=Prefs.direction(this);
        if(reverse) dir=opposite(dir);

        float x1=cx, y1=cy, x2=cx, y2=cy;
        switch(dir){
            case Prefs.DIR_DOWN:  y1=marginY; y2=hh-marginY; break;
            case Prefs.DIR_LEFT:  x1=w-marginX; x2=marginX; break;
            case Prefs.DIR_RIGHT: x1=marginX; x2=w-marginX; break;
            case Prefs.DIR_UP:
            default:              y1=hh-marginY; y2=marginY; break;
        }

        Path p=new Path(); p.moveTo(x1,y1); p.lineTo(x2,y2);
        GestureDescription.StrokeDescription stroke=new GestureDescription.StrokeDescription(p,0,duration);
        dispatchGesture(new GestureDescription.Builder().addStroke(stroke).build(),null,null);
        h.postDelayed(this::tick,Math.max(16L,duration+8L));
    }

    private int opposite(int d){
        switch(d){
            case Prefs.DIR_DOWN:return Prefs.DIR_UP;
            case Prefs.DIR_LEFT:return Prefs.DIR_RIGHT;
            case Prefs.DIR_RIGHT:return Prefs.DIR_LEFT;
            case Prefs.DIR_UP:
            default:return Prefs.DIR_DOWN;
        }
    }
}
