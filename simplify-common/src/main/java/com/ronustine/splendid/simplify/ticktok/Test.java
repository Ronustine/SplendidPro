package com.ronustine.splendid.simplify.ticktok;

import java.util.Timer;

/**
 * Created by ronustine on 2016/10/29.
 */

public class Test {
    public static void main(String[] args) {
        ReminderTest a = new ReminderTest();
        TimeCounter timeCounter = new TimeCounter();
        timeCounter.addReminder(a);

        Timer timer = new Timer();
        System.out.println("准备提醒");
        timer.schedule(timeCounter, 10000);
    }
}
