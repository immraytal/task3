package com.immrayral.task3.port;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class Dock extends Thread {
    public int dockID;
    private boolean free = true;
    private Ship currentShip;
    private int counter;
    private Storage storage;
    private final ReentrantLock lock = new ReentrantLock();
    private Dispatcher dispatcher;
    private final Logger LOG = Logger.getLogger(Dock.class);

    public Dock(int dockNum, int counter, Storage storage, Dispatcher dispatcher ) {
        super();
        this.dockID = dockNum;
        this.counter = counter;
        this.storage = storage;
        this.dispatcher = dispatcher;
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
    public ReentrantLock getLock() {
        return this.lock;
    }

    @Override
    public void run() {
    boolean worked = false;
        Iterator<Ship> ships=dispatcher.getIterator();
        while(canWork()) {
            if (currentShip!=null) {
                lock.lock();
                try {
                free=false;
                worked = true;
                if(currentShip.getCargo()==0)
                {
                    currentShip.interrupt();
                    currentShip = null;
                    if (this.lock.tryLock())
                        try {
                            continue;
                        } finally {
                            this.lock.unlock();
                        }
                }
                    System.out.println();
                LOG.info("DOCK - " + dockID + ": SHIP - " + currentShip.getShipID() + " has arrived");
                    if (storage.tryTransfer(currentShip)) {
                        counter--;
                        LOG.info("DOCK - " + dockID + ": SHIP - " + currentShip.getShipID() + " transfer cargo to storage");
                        LOG.info("Current capacity - " + storage.capacity);//!!!!!!!!!!!!! storage public
                        free = true;
                    } else {
                        while (ships==null) {
                            ships = dispatcher.getIterator();
                        }
                        Boolean flag = false;
                        while (ships.hasNext()) {
                            Ship someShip = ships.next();
                            if (someShip.tryAddCargo(currentShip.getCargo())) {
                                LOG.info("DOCK - " + dockID + ": SHIP - " + currentShip.getShipID() + " transfer cargo to ship" + someShip.getShipID());
                                flag = true;
                                counter--;
                                free = true;
                                currentShip = dispatcher.getShip();
                                break;
                            }
                        }
                        if (!flag) {
                            LOG.info("DOCK - " + dockID + ": SHIP - " + currentShip.getShipID() + " can't transfer to anywhere cargo");
                            currentShip=null;
                        }

                    }
                } finally {
                    if (this.lock.tryLock())
                        this.lock.unlock();
                }
            } else
            {
                     free = true;
                     currentShip = dispatcher.getShip();
                     if (currentShip == null && worked) {
                         try {
                           this.interrupt();
                           LOG.info(this.getName() + " thread has been interrupt");
                           return;
                         }
                         catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
            }
        }
    }
}
