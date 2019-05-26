package com.immrayral.task3.port;

import java.util.concurrent.locks.ReentrantLock;

public class Storage {
    public double capacity=0;
    private final double maxCapacity = 100.0;//new Random().nextInt(10000)*1.0;
    private final ReentrantLock lock = new ReentrantLock();

    public boolean tryTransfer(Ship ship) {
        this.lock.lock();
        if (capacity==100)
        {
            System.out.println("100!!!!!!!!!");
            }
        try {
            ship.getLock().lock();
            try {
                if (this.capacity + ship.getCargo() > this.maxCapacity) {
                    return false;
                }

                this.capacity += ship.getCargo();
                ship.setCargo(0);
            } finally {
                ship.getLock().unlock();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }

        return false;
    }
}
