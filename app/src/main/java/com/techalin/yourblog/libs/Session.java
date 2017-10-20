package com.techalin.yourblog.libs;

import java.util.HashMap;

public final class Session {

    //Reserved Sessions => ['auth']

    private static HashMap session = new HashMap();


    public static void addItem(String key, Object value) {
        session.put(key, value);
    }

    public static Object getItem(String key) {

        return isSet(key) ? session.get(key) : null;
    }

    public static HashMap getSession() {
        return session;
    }

    public static boolean isSet(String key) {
        return session.containsKey(key) ? true : false;
    }
}
