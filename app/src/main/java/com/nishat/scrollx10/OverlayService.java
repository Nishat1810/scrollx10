package com.nishat.scrollx10;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.*;

public class OverlayService extends Service {
    private WindowManager wm; private View panel; private WindowManager.LayoutParams lp;
    private TextView stop, play, gear;
    private float downX,downY; private int startX,startY;
    @Override public void onCreate(){ super.onCreate(); showOverlay(); }
    private TextView tv(String text){ TextView v=new TextView(this); v.setText(text); v.setGravity(Gravity.CENTER); v.setTextSize(26); return v; }
    private GradientDrawable bg(int color,float r){ GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(r); return d; }
    private void showOverlay(){
        wm=(WindowManager)getSystemService(WINDOW_SERVICE);
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(8,8,8,8); box.setBackground(bg(Color.WHITE,38));
        stop=tv("○"); stop.setTextColor(Color.rgb(205,75,75)); stop.setTextSize(54); stop.setBackground(bg(Color.WHITE,100));
        play=tv("▶"); play.setTextColor(Color.WHITE); play.setTextSize(34); play.setBackground(bg(Color.rgb(10,165,121),30));
        gear=tv("⚙"); gear.setTextColor(Color.rgb(10,165,121)); gear.setTextSize(36); gear.setBackground(bg(Color.WHITE,30));
        box.addView(stop,new LinearLayout.LayoutParams(92,92));
        LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(108,108); pp.setMargins(0,14,0,14); box.addView(play,pp);
        box.addView(gear,new LinearLayout.LayoutParams(108,108));
        stop.setOnClickListener(v->{ if(ScrollAccessibilityService.get()!=null) ScrollAccessibilityService.get().stopScrolling(); update(); });
        play.setOnClickListener(v->{ if(ScrollAccessibilityService.get()!=null){ if(ScrollAccessibilityService.get().isRunning()) ScrollAccessibilityService.get().stopScrolling(); else ScrollAccessibilityService.get().startScrolling(); update(); } else openAccessibility(); });
        gear.setOnClickListener(v->{ Intent i=new Intent(this,MainActivity.class); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i); });
        box.setOnTouchListener((v,e)->{ if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getRawX();downY=e.getRawY();startX=lp.x;startY=lp.y;return true;} if(e.getAction()==MotionEvent.ACTION_MOVE){lp.x=startX+(int)(e.getRawX()-downX);lp.y=startY+(int)(e.getRawY()-downY);wm.updateViewLayout(panel,lp);return true;} return e.getAction()==MotionEvent.ACTION_UP; });
        panel=box; lp=new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,-3); lp.gravity=Gravity.START|Gravity.TOP; lp.x=24;lp.y=220; wm.addView(panel,lp); update();
    }
    private void update(){ if(play==null)return; boolean r=ScrollAccessibilityService.get()!=null&&ScrollAccessibilityService.get().isRunning(); play.setText(r?"Ⅱ":"▶"); }
    private void openAccessibility(){ try{startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));}catch(Exception ignored){} }
    @Override public int onStartCommand(Intent i,int f,int id){ return START_STICKY; }
    @Override public void onDestroy(){ if(panel!=null)wm.removeView(panel); super.onDestroy(); }
    @Override public android.os.IBinder onBind(Intent i){ return null; }
}
