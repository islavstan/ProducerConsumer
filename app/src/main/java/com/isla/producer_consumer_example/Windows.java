package com.isla.producer_consumer_example;

import android.util.Log;

public class Windows {

    private static final String LOG = "LOG";
    private static final int FIRST_PRODUCE_RESULT = 10;
    private static final int FIRST_PRODUCE_DATE = 1;
    private static final int SECOND_PRODUCE_RESULT = 20;
    private static final int SECOND_PRODUCE_DATE = 10;
    private static final int THIRD_PRODUCE_RESULT = 40;
    private static final int MAX_DATE = 30;
    private static final int THIRD_PRODUCE_DATE = 20;
    private static final int ALL_MONTHS = 12;
    private static final int FIRST_SHOP_BUY_VALUE = 1;
    private static final int SECOND_SHOP_BUY_VALUE = 2;
    private static final int THIRD_SHOP_BUY_VALUE = 4;
    private static final int FOURTH_SHOP_BUY_VALUE = 6;
    private static final int FIFTH_SHOP_BUY_VALUE = 8;
    private static final long SLEEP_TIMEOUT = 500L;
    private int value = 0;
    private int month = 1;
    private int date = 1;
    private boolean isProduced = false;
    private boolean mPaused = false;

    void produce() throws InterruptedException {
        {
            while (month != ALL_MONTHS || date != MAX_DATE) {
                synchronized (this) {
                    if (mPaused) {
                        wait();
                    }
                    if (date == FIRST_PRODUCE_DATE || date == SECOND_PRODUCE_DATE ||
                            date == THIRD_PRODUCE_DATE) {
                        value += getProducePart(date);
                        isProduced = true;
                        Log.d(LOG, "value = " + value + " date = " + date + " month = " + month);
                        notify();
                        wait();
                    }
                }
            }
        }
    }

    /**
     * Call this on pause.
     */
    void onPause() {
        synchronized (this) {
            mPaused = true;
        }
    }

    /**
     * Call this on resume.
     */
    void onResume() {
        synchronized (this) {
            mPaused = false;
            notifyAll();
        }
    }

    void consume() throws InterruptedException {
        while (month != ALL_MONTHS || date != MAX_DATE) {
            synchronized (this) {
                if (mPaused) {
                    wait();
                }
                Log.d(LOG, "synchronized month = " + month + " date = " + date + " paused = " + mPaused);
                if (date == FIRST_PRODUCE_DATE || date == SECOND_PRODUCE_DATE ||
                        date == THIRD_PRODUCE_DATE) {
                    Log.d(LOG, "date " + isProduced);
                    if (!isProduced) {
                        notify();
                        wait();
                    }
                }
                isProduced = false;
                consumeAll();
                if (date == MAX_DATE) {
                    date = FIRST_PRODUCE_DATE;
                    month++;
                } else {
                    date++;
                }
                Thread.sleep(SLEEP_TIMEOUT);
            }
        }
    }

    private void consumeAll() {
        if (value - FIRST_SHOP_BUY_VALUE >= 0) {
            value = value - FIRST_SHOP_BUY_VALUE;
            Log.d(LOG, "first shop buy 1 windows. Value = " + value);
        }
        if (value - SECOND_SHOP_BUY_VALUE >= 0) {
            value = value - SECOND_SHOP_BUY_VALUE;
            Log.d(LOG, "second shop buy 2 windows. Value = " + value);
        }
        if (value - THIRD_SHOP_BUY_VALUE >= 0) {
            value = value - THIRD_SHOP_BUY_VALUE;
            Log.d(LOG, "third shop buy 4 windows. Value = " + value);
        }
        if (value - FOURTH_SHOP_BUY_VALUE >= 0) {
            value = value - FOURTH_SHOP_BUY_VALUE;
            Log.d(LOG, "fourth shop buy 6 windows. Value = " + value);
        }
        if (value - FIFTH_SHOP_BUY_VALUE >= 0) {
            value = value - FIFTH_SHOP_BUY_VALUE;
            Log.d(LOG, "fifth shop buy 8 windows. Value = " + value);
        }
    }

    private Integer getProducePart(int date) {
        switch (date) {
            case FIRST_PRODUCE_DATE:
                return FIRST_PRODUCE_RESULT;
            case SECOND_PRODUCE_DATE:
                return SECOND_PRODUCE_RESULT;
            case THIRD_PRODUCE_DATE:
                return THIRD_PRODUCE_RESULT;
            default:
                return 0;
        }
    }
}
