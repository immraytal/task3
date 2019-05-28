package com.immrayral.task3.port;

import java.util.ArrayList;
import java.util.List;

public class Port {
    private List<Dock> docks;
    private static List<Ship> ships = new ArrayList<Ship>();

    private static Dispatcher dispatcher = new Dispatcher();

    public static void main(String[] args) throws InterruptedException {

    Storage storage = new Storage(0.0, 100.0);
            Port port = new Port();
            int dockCount = 0;
            port.docks = new ArrayList<Dock>();
            for (int i = 0; i < 3; i++) {
                Dock dock = new Dock(++dockCount, 10 , storage, dispatcher);
                dock.setName("DOCK-" + dockCount);
                System.out.printf("DOCK - %d started\n", dockCount);
                port.docks.add(dock);
                dock.start();
            }

            int count = 1;

            for (int k=0;k<25;k++) {
                Ship ship = new Ship(k+1, 10.0, 100.0, storage, dispatcher);
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
