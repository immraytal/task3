package com.immrayral.task3.port;

import org.apache.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

public class Ship extends Thread{

    private int shipID;
    private double cargo;
    private double maxCapacity;
    private final ReentrantLock lock = new ReentrantLock();
    private Storage storage;
    private Dispatcher dispatcher;
    private final Logger LOG = Logger.getLogger(Ship.class);


    public Ship(int shipID, double cargo, double maxCapacity, Storage storage, Dispatcher dispatcher ) {
        this.shipID = shipID;
        this.maxCapacity = maxCapacity;
        this.cargo = cargo;
        this.storage = storage;
        this.dispatcher = dispatcher;
    }

    public double getCargo() {
        if (this.lock.tryLock())
        try {
            return this.cargo;
        } finally {
            this.lock.unlock();
        }
        return  this.cargo;
    }

    public void setCargo(double cargo) {
        if (this.lock.tryLock())
        try {
            this.cargo = cargo;
        } finally {
            this.lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return this.lock;
    }

    public boolean tryAddCargo(double cargo) {
        this.lock.lock();
        try {
            boolean flag;
            if (cargo + this.cargo > maxCapacity) {
                flag = false;
            } else {
                if (cargo + this.cargo < 0) {
                    flag = false;
                } else {
                    this.cargo += cargo;
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            this.lock.unlock();
        }
        return false;
    }


    public int getShipID() {
        return shipID;
    }

    public void setShipID(int shipID) {
        this.shipID = shipID;
    }

    @Override
    public void run() {
                try {
                        if (cargo>0)
                        {
                            dispatcher.addShip(this);

                        } else {
                            this.interrupt();
                        }
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
    }
}
