package com.microservices.microservicesitems;

import java.util.List;

public interface I1
{

    void run();
}

interface I2 {
    void swin();
}

interface I3 extends I1, I2 {

    default void swin(){
        System.out.println("here im you wait");
    }

    default void fly() {
        System.out.println("shit");
    }

    static void ohShit() {

    }
}

abstract class  acciones implements I3 {

    abstract void doNothing();

    @Override
    public void swin() {

    }

}

abstract class accionesImpl extends acciones {

    public accionesImpl(String name) {

    }

    @Override
    public void run() {

    }

    @Override
    void doNothing() {

    }
}

class hijo extends accionesImpl {
    public hijo(String da) {
        super("name");
    }
}