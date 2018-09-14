package com.antfin.exam.lazysingleton.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LazymanNormalTest {

    public void getInstanceTest(){
        LazyMan.getInstance();
        assertEquals(LazyMan.getInitCount(),2);
        LazyMan.getInstance();
        assertEquals(LazyMan.getInitCount(),1);
    }
}
