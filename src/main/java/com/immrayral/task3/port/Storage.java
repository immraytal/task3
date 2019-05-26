package com.immrayral.task3.port;

import java.util.concurrent.BlockingQueue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Storage {
    public double capacity=0;
    private final double maxCapacity = 100.0;//new Random().nextInt(10000)*1.0;
    private BlockingQueue<Ship> shipsQueue = new LinkedBlockingQueue<Ship>();
    private final ReentrantLock lock = new ReentrantLock();


    public Ship getShip() {
        return shipsQueue.poll();
    }

    public void addShip(Ship ship){
        shipsQueue.offer(ship);
    }

    public void removeShip(Ship ship){
        shipsQueue.remove(ship);
    }

    public boolean tryTransfer(Ship ship) {
        if (capacity==100)
        {
            System.out.println("100!!!!!!!!!");
        }


        this.lock.lock();
        ship.getLock().lock();

        try {


                if (this.capacity + ship.getCargo() > this.maxCapacity) {
                    this.lock.unlock();
                    return false;
                }

                this.capacity += ship.getCargo();
                ship.setCargo(0);

            this.lock.unlock();
            ship.getLock().unlock();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.lock.tryLock()) {
                this.lock.unlock();
            }
            if (ship.getLock().tryLock()) {
                ship.getLock().unlock();
            }
        }

        return false;
    }
}
