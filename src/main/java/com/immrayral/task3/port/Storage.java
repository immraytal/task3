package com.immrayral.task3.port;

import org.apache.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

public class Storage {
    private double capacity;
    private double maxCapacity;
    private final Logger LOG = Logger.getLogger(Storage.class);

    public ReentrantLock getLock() {
        return lock;
    }

    public double getCapacity() {
        return capacity;
    }
    private final ReentrantLock lock = new ReentrantLock();

    public Storage(double capacity, double maxCapacity) {
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
    }

    public boolean tryTransfer(Ship ship) {

        this.lock.lock();
        try {
                if (this.capacity + ship.getCargo() > this.maxCapacity) {
                    this.lock.unlock();
                    return false;
                }
                this.capacity += ship.getCargo();
                ship.setCargo(0);
                if (capacity==maxCapacity)
                {
                    LOG.info("Storage is full!");
                }
                this.lock.unlock();
                return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            if (this.lock.tryLock()) {
                this.lock.unlock();
            }
        }
        return false;
    }
}
