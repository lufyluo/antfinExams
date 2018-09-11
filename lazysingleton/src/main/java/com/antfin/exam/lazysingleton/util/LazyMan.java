package com.antfin.exam.lazysingleton.util;

public class LazyMan {
    private static int initCount = 0;
    private static LazyMan instance = null;
    private LazyMan(){
        initCount = initCount +1;
    }

    public static int getInitCount() {
        return initCount;
    }


    public static LazyMan getInstance() {
        if(instance == null){
            synchronized( LazyMan.class){
                if(instance == null)
                {
                    instance = new LazyMan();
                }
            }
        }
        return instance;
    }
}
