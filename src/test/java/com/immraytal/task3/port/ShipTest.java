package com.immraytal.task3.port;

import com.immrayral.task3.port.Dispatcher;
import com.immrayral.task3.port.Ship;
import com.immrayral.task3.port.Storage;
import org.junit.Assert;
import org.junit.Test;

public class ShipTest {

    Ship ship = new Ship(1 , 10.0, 100.0 , new Storage(0.0, 100.0), new Dispatcher());

    @Test
    public void getCargoTest() {
        Assert.assertEquals(10.0, ship.getCargo(), 0.0);
    }

    @Test
    public void setCargoTest() {
        ship.setCargo(11.0);
        Assert.assertEquals(11.0, ship.getCargo(), 0.0);
    }

    @Test
    public void tryAddCargoTest() {
        if (ship.tryAddCargo(10.0))
        {
            Assert.assertEquals(20.0, ship.getCargo(), 0.0);
        }
    }


}
