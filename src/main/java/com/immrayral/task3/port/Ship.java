package com.immrayral.task3.port;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Ship extends Thread{

    private int shipID;
    private BlockingQueue<Ship> shipsQueue;
    private List<Dock> docks;
    private double cargo;
    private final double capacity = 100.0; //new Random().nextInt(5000)*1.0;
    private final ReentrantLock lock = new ReentrantLock();

    public Ship(int shipID, BlockingQueue<Ship> shipsQueue, List<Dock> docks, double cargo) {
        this.shipID = shipID;
        this.shipsQueue = shipsQueue;
        this.docks = docks;
        this.cargo = cargo;
    }

    public double getCargo() {
        this.lock.lock();
        try {
            return this.cargo;
        } finally {
            this.lock.unlock();
        }
    }

    public void setCargo(double cargo) {
        this.lock.lock();
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
            if (cargo + this.cargo > capacity) {
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
            e.printStackTrace();
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
        Boolean isOn = false;
        for (Dock dock: docks) {
            if(dock.isFree()){
                this.lock.lock();
                try {
                    dock.setCurrentShip(this);
                    dock.setShipsQueue(this.shipsQueue);

                    System.out.println("setCurr to DOCK - " + dock.dockID + " ship - " + shipID);
                    //    dock.notify();
                    isOn = true;
                }
                finally {

                    this.lock.unlock();
                }
            }
        }
        if (cargo==0) {return;}
        if (!isOn) {

            try {
                shipsQueue.offer(this);
                this.lock.lock();
                if (cargo==0) {
                    System.out.println("Ship #" + shipID + " has 0 cargo");
                    shipsQueue.remove(this);
                    return;
                }
                   /* this.wait(3000 + new Random().nextInt(3000));
                    if (cargo>0) {
                        shipsQueue.remove(this);
                        System.out.println("Ship #" + shipID + " going away with out shipment");
                    }*/
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.lock.unlock();
            }

        }
    }
}
