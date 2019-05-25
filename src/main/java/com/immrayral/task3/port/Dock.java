package com.immrayral.task3.port;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Dock extends Thread {
    public int dockID;
    private boolean free = true;
    private BlockingQueue<Ship> shipsQueue;
    private Ship currentShip;
    private int counter;
    private Storage storage = new Storage();

    public Dock(BlockingQueue<Ship> shipsQueue, int dockNum, int counter) {
        super();
        this.dockID = dockNum;
        this.shipsQueue = shipsQueue;
        this.counter = counter;
    }

    public boolean isFree() {return free;}

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Ship getCurrentShip() {
        return currentShip;
    }

    public void setCurrentShip(Ship currentShip) {
        this.currentShip = currentShip;
    }

    private boolean canWork() {
        return counter>0;
    }

    @Override
    public void run() {
        ReentrantLock lock = new ReentrantLock();
        while(canWork()) {
            if (currentShip!=null && currentShip.getCargo()>0) {
                System.out.println("NOT NULL - SHIP" + currentShip.getShipID());
                lock.lock();
                free=false;
                try {
                    if (storage.tryTransfer(currentShip)) {
                        counter--;
                        System.out.println("DOCK - " + dockID + "  Ship " + currentShip.getShipID() + "transfer cargo to storage");
                    } else {

                        Iterator<Ship> ships = shipsQueue.iterator();
                        Boolean flag = false;
                        while (ships.hasNext()) {
                            Ship someShip = ships.next();
                            if (someShip.tryAddCargo(currentShip.getCargo())) {
                                System.out.println("DOCK - " + dockID + "  Ship " + currentShip.getShipID() + "transfer cargo to ship" + someShip.getShipID());
                                flag = true;
                                break;
                            }

                        }
                        if (!flag) {
                            System.out.println("DOCK - " + dockID + "  Ship " + currentShip.getShipID() + " can't transfer to anywhere cargo");
                        }

                    }
                } finally {
                    lock.unlock();
                }
            }
//
//            else {
//                try {
//                    lock.lock();
//                        free = true;
//                        wait();
//                        lock.unlock();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }
}
