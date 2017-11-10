package com.tramontini.marco.risponditore.server;


public class Main {

    private final static int PORTA=1234;

    public static void main(String ... args) throws Exception {
        Risponditore r = new Risponditore();
        new Server(PORTA);
    }
}
