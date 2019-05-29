package com.immraytal.task3.port;

import com.immrayral.task3.port.Dispatcher;
import com.immrayral.task3.port.Ship;
import com.immrayral.task3.port.Storage;
import org.junit.Assert;
import org.junit.Test;

public class StorageTest {
    Storage storage = new Storage(0.0, 100.0);
    Ship ship = new Ship(1 , 10.0, 100.0 , new Storage(0.0, 100.0), new Dispatcher());


    @Test
    public void tryTransferTest() {
        storage.tryTransfer(ship);
        Assert.assertEquals(10.0, storage.getCapacity(), 0.0);
    }
}

