package com.geval6.word.Singleton;

import java.util.HashMap;

public class SingletonClass {
    private static SingletonClass mInstance;
    public HashMap itemsForGospel;

    static {
        mInstance = null;
    }

    private SingletonClass() {
    }

    public static SingletonClass getInstance() {
        if (mInstance == null) {
            mInstance = new SingletonClass();
        }
        return mInstance;
    }
}
