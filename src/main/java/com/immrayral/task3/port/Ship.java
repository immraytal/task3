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

    public Ship(int shipID, BlockingQueue<Ship> shipsQueue, List<Dock> docks, double cargo) {
        this.shipID = shipID;
        this.shipsQueue = shipsQueue;
        this.docks = docks;
        this.cargo = cargo;
    }

    public double getCargo() {
        return cargo;
    }

    public boolean tryAddCargo(double cargo) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
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
            lock.unlock();
        }
        return false;
    }


    public void setCargo(double cargo) {
        this.cargo = cargo;
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
        ReentrantLock lock = new ReentrantLock();
        for (Dock dock: docks) {
            if(dock.isFree())
                lock.lock();
            {
                try {
                    dock.setCurrentShip(this);
                    System.out.println("setCurr to DOCK - " + dock.dockID + " ship - " + shipID);
                    //    dock.notify();
                    isOn = true;
                }
                finally {
                    lock.unlock();
                }
            }
        }
        if (cargo==0) {return;}
        if (!isOn) {

            try {
                shipsQueue.offer(this, 2, TimeUnit.SECONDS);
                lock.lock();
                    this.wait(3000 + new Random().nextInt(3000));
                    if (cargo>0) {
                        shipsQueue.remove(this);
                        System.out.println("Ship #" + shipID + " going away with out shipment");
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }
}
