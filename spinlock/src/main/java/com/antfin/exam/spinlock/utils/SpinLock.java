package com.antfin.exam.spinlock.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinLock {
    private AtomicInteger ticketNum = new AtomicInteger(0);

    private AtomicInteger owner = new AtomicInteger(0);

    private static final ThreadLocal<Integer> myTicketLocal = new ThreadLocal<Integer>();

    public void lock() {
        int myTicket = ticketNum.getAndIncrement();
        myTicketLocal.set(myTicket);
        while (myTicket != owner.get()) {

        }
    }

    public void unlock() {
        int myTicket = myTicketLocal.get();
        owner.compareAndSet(myTicket, myTicket + 1);
    }


}
