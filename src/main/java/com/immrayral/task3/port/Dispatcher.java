package com.immrayral.task3.port;

import java.util.concurrent.BlockingQueue;

public class Dispatcher {

    private BlockingQueue<Ship> shipsQueue;



    public void addShip(Ship ship){
        shipsQueue.offer(ship);
    }

    public void removeShip(Ship ship){
        shipsQueue.remove(ship);
    }
}
