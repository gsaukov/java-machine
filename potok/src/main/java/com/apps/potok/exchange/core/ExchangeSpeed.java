package com.apps.potok.exchange.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExchangeSpeed {

    @Value("${exchange.speed}")
    private Speed speed;

    public void orderServerSpeedControl() {
        speedControl(speed.getOrderServerSpeed());
    }

    public void mkDataServerSpeedControl() {
        speedControl(speed.getMkDataServerSpeed());
    }

    public void notifierSpeedControl() {
        speedControl(5);
    }

    public void speedControl(long timeout) {
        if (timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (timeout == 0) {
            //full speed
        } else {
            //negative timeout kill the thread
            Thread.currentThread().interrupted();
        }
    }

    public Speed getSpeed() {
        return speed;
    }

    public enum Speed {
        MAX(0, 0),
        FAST(1, 300),
        NORMAL(10, 3000),
        SLOW(100, 30000),
        DEAD(1000, 300000),
        STOP(-1, -1);

        long orderServerSpeed, mkDataServerSpeed;

        Speed(long orderServerSpeed, long mkDataServerSpeed) {
            this.orderServerSpeed = orderServerSpeed;
            this.mkDataServerSpeed = mkDataServerSpeed;
        }

        public long getOrderServerSpeed() {
            return orderServerSpeed;
        }

        public long getMkDataServerSpeed() {
            return mkDataServerSpeed;
        }
    }

}