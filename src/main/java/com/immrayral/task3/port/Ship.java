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
    private Storage storage;

    public Ship(int shipID, BlockingQueue<Ship> shipsQueue, List<Dock> docks, double cargo, Storage storage) {
        this.shipID = shipID;
        this.shipsQueue = shipsQueue;
        this.docks = docks;
        this.cargo = cargo;
        this.storage = storage;
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
        while (this.isAlive()) {


            //this.lock.lock();
                try {

                        if (cargo>0)
                        {
                            //this.lock.lock();
                            storage.addShip(this);
                           // this.lock.unlock();

                        } else {
                            this.interrupt();
                        }



                  /*
                    if (cargo>0) {
                        shipsQueue.remove(this);
                        System.out.println("Ship #" + shipID + " going away with out shipment");
                        isOn = true;
                        this.lock.unlock();
                        return;
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                   if (this.lock.tryLock())
                       this.lock.unlock();
                }


        }

            System.out.println("Ship #" + shipID + " has " +  cargo + " cargo");


    }


}
