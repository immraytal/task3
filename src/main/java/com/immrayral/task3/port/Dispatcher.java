package com.immrayral.task3.port;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Dispatcher {

    private BlockingQueue<Ship> shipsQueue = new LinkedBlockingQueue<Ship>();

    public ReentrantLock getLock() {
        return lock;
    }

    private final ReentrantLock lock = new ReentrantLock();


    public Ship getShip() {
        if (this.lock.tryLock()) {
            try {
                return shipsQueue.poll();
            } finally {
                this.lock.unlock();
            }

        }
        return null;
    }

    public Iterator<Ship> getIterator() {
        if (this.lock.tryLock()) {
            try {
                return shipsQueue.iterator();
            } finally {
                this.lock.unlock();
            }

        }
        return null;
    }


    public void addShip(Ship ship) {
        if (!shipsQueue.contains(ship))
            shipsQueue.offer(ship);
    }

    public void removeShip(Ship ship) {
        shipsQueue.remove(ship);
    }
}


