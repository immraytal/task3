package com.immraytal.task3.port;

import com.immrayral.task3.port.Dispatcher;
import com.immrayral.task3.port.Dock;
import com.immrayral.task3.port.Ship;
import com.immrayral.task3.port.Storage;
import org.junit.Assert;
import org.junit.Test;

public class DockTest {
    Storage storage = new Storage(0.0, 100.0);
    Dispatcher dispatcher = new Dispatcher();
    Dock dock = new Dock(1, 10, storage , dispatcher);
    Ship ship = new Ship(1 , 10.0, 100.0 , storage, dispatcher);

    @Test
    public void runTest() {
        dispatcher.addShip(ship);
        dock.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
            Assert.assertTrue(storage.getCapacity()==10.0 && ship.getCargo()==0.0);
    }
}
