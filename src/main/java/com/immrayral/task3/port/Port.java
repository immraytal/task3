package com.immrayral.task3.port;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port {
    private List<Dock> docks;
    private BlockingQueue<Ship> shipsQueue = new LinkedBlockingQueue<Ship>();

    public static void main(String[] args) throws InterruptedException {

    Storage storage = new Storage();
            Port port = new Port();
            int dockCount = 0;
            port.docks = new ArrayList<Dock>();
            for (int i = 0; i < 1; i++) {
                Dock dock = new Dock(port.shipsQueue, ++dockCount, 10 , storage);
                dock.setName("DOCK-" +dockCount);
                System.out.printf("DOCK - %d started\n", dockCount);
                port.docks.add(dock);
                dock.start();
            }

            int count = 1;

            for (int k=0;k<5;k++) {
                Ship ship = new Ship(count++, port.shipsQueue, port.docks, 10.0, storage);//new Random().nextInt(1000));
                ship.setName("Ship-" + count);
                ship.start();
            }
            Thread.sleep(500);


       /* for (Dock dock:
             port.docks) {
            dock.interrupt();
            System.out.printf("DOCK - %d stopped\n", dock.dockID);
        }*/


    }

}
