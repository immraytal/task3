package com.immrayral.task3.port;

import java.util.concurrent.locks.ReentrantLock;

public class Storage {
    private double capacity=0;
    private final double maxCapacity = 100.0;//new Random().nextInt(10000)*1.0;
    private ReentrantLock lock = new ReentrantLock();

    public boolean tryTransfer(Ship ship) {
        boolean flag;
        lock.lock();
        try {

            if (capacity + ship.getCargo() > maxCapacity) {
                flag = false;
            } else {
                capacity += ship.getCargo();
                ship.setCargo(0);

                flag = true;
            }

            return flag;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }
}
