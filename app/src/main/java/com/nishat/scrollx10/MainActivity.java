package com.nishat.scrollx10;

import android.app.*;import android.content.*;import android.graphics.Color;import android.graphics.Typeface;import android.net.Uri;import android.os.*;import android.provider.Settings;import android.view.*;import android.widget.*;

public class MainActivity extends Activity {
    LinearLayout root; TextView speedLabel; SeekBar speed; Switch reverse; RadioGroup directionGroup;
    int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);} TextView t(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(Color.rgb(23,33,31));v.setPadding(dp(4),dp(6),dp(4),dp(6));return v;}
    @Override public void onCreate(Bundle b){super.onCreate(b); build();}
    void build(){
        ScrollView sv=new ScrollView(this); root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(22),dp(34),dp(22),dp(24));sv.addView(root);setContentView(sv);
        TextView title=t("Scroll X10",30);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);root.addView(title);
        root.addView(t("Automatic scrolling with a floating controller",16));
        TextView status=t("",15);status.setPadding(0,dp(18),0,dp(8));root.addView(status);
        Button access=new Button(this);access.setText("Enable Accessibility Service");root.addView(access);access.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        Button overlay=new Button(this);overlay.setText("Display over other apps");root.addView(overlay);overlay.setOnClickListener(v->{if(!Settings.canDrawOverlays(this))startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())));else startService(new Intent(this,OverlayService.class));});
        root.addView(t("Speed",20));
        speedLabel=t("1.0×",28);speedLabel.setTypeface(Typeface.DEFAULT,Typeface.BOLD);root.addView(speedLabel);
        speed=new SeekBar(this);speed.setMax(199);speed.setProgress((int)((Prefs.speed(this)-0.1f)/0.1f));root.addView(speed,new LinearLayout.LayoutParams(-1,dp(55)));speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar s,int p,boolean f){float x=.1f+p*.1f;Prefs.speed(MainActivity.this,x);speedLabel.setText(String.format(java.util.Locale.US,"%.1f×",x));}public void onStartTrackingTouch(SeekBar s){}public void onStopTrackingTouch(SeekBar s){}});
        root.addView(t("Range: 0.1× to 20×. Higher values use shorter, repeated gesture passes.",14));
        root.addView(t("Scroll direction",20));
        directionGroup=new RadioGroup(this); directionGroup.setOrientation(RadioGroup.VERTICAL);
        String[] dirs={"Scroll up","Scroll down","Scroll left","Scroll right"};
        for(int i=0;i<dirs.length;i++){ RadioButton rb=new RadioButton(this); rb.setText(dirs[i]); rb.setTextSize(16); rb.setTag(i); directionGroup.addView(rb); if(Prefs.direction(this)==i) rb.setChecked(true); }
        directionGroup.setOnCheckedChangeListener((g,id)->{ View checked=g.findViewById(id); if(checked!=null && checked.getTag()!=null) Prefs.direction(this,(Integer)checked.getTag()); });
        root.addView(directionGroup);
        reverse=new Switch(this);reverse.setText("Reverse direction");reverse.setChecked(Prefs.reverse(this));root.addView(reverse);reverse.setOnCheckedChangeListener((v,c)->Prefs.reverse(this,c));
        Button start=new Button(this);start.setText("Start / Stop scrolling");root.addView(start);start.setOnClickListener(v->{ScrollAccessibilityService s=ScrollAccessibilityService.get();if(s==null){startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));return;}if(s.isRunning())s.stopScrolling();else s.startScrolling();});
        Button hide=new Button(this);hide.setText("Show floating controller");root.addView(hide);hide.setOnClickListener(v->{if(!Settings.canDrawOverlays(this)){startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName())));}else startService(new Intent(this,OverlayService.class));});
        root.addView(t("Tip: enable the accessibility service first, then grant overlay permission. The floating bar can be dragged around the screen. Choose up/down/left/right here; the selected direction is used by the floating controller.",14));
    }
    @Override protected void onResume(){super.onResume();if(speed!=null){float x=Prefs.speed(this);speed.setProgress((int)((x-.1f)/.1f));speedLabel.setText(String.format(java.util.Locale.US,"%.1f×",x));}}
}
