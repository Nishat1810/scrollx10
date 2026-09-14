package com.nishat.scrollx10;

import android.content.Context;

public final class Prefs {
    private static final String P="settings";
    public static final int DIR_UP=0;
    public static final int DIR_DOWN=1;
    public static final int DIR_LEFT=2;
    public static final int DIR_RIGHT=3;

    public static float speed(Context c){ return c.getSharedPreferences(P,0).getFloat("speed",1f); }
    public static void speed(Context c,float v){ c.getSharedPreferences(P,0).edit().putFloat("speed",v).apply(); }
    public static int direction(Context c){ return c.getSharedPreferences(P,0).getInt("direction",DIR_UP); }
    public static void direction(Context c,int v){ c.getSharedPreferences(P,0).edit().putInt("direction",v).apply(); }
    public static boolean reverse(Context c){ return c.getSharedPreferences(P,0).getBoolean("reverse",false); }
    public static void reverse(Context c,boolean v){ c.getSharedPreferences(P,0).edit().putBoolean("reverse",v).apply(); }
}
