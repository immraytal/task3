package com.immrayral.task3.port;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port {
    private List<Dock> docks;
    private BlockingQueue<Ship> shipsQueue = new LinkedBlockingQueue<Ship>();
    private static List<Ship> ships = new ArrayList<Ship>();

    public static void main(String[] args) throws InterruptedException {

    Storage storage = new Storage();
            Port port = new Port();
            int dockCount = 0;
            port.docks = new ArrayList<Dock>();
            for (int i = 0; i < 3; i++) {
                Dock dock = new Dock(port.shipsQueue, ++dockCount, 10 , storage);
                dock.setName("DOCK-" + dockCount);
                System.out.printf("DOCK - %d started\n", dockCount);
                port.docks.add(dock);
                dock.start();
            }

            int count = 1;

            for (int k=0;k<25;k++) {
                Ship ship = new Ship(k+1, port.shipsQueue, port.docks, 10.0, storage);//new Random().nextInt(1000));
                ship.setName("Ship-" + (k+1));
                ships.add(ship);
                ship.start();
            }
            Thread.sleep(500);


            for (int k=0;k<25;k++) {
                System.out.println("Ship-" + (k+1) + "  has " + ships.get(k).getCargo());
            }




    }

}
