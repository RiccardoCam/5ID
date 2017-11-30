/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import chattcp.Server.Task;

/**
 *
 * @author Sandro
 */
public class User {

    private Task t;
    private String username;
    private boolean disponibile;

    public User(Task t) {
        this.username = "";
        this.t = t;
        this.disponibile = true;

    }

    public Task getTask() {
        return t;
    }

    public void setDisponibilita(boolean a) {
        this.disponibile = a;
    }

    public boolean getDisponibilita() {
        return disponibile;
    }

    public void diminuisciPos() {
        this.t.diminuisciPos();
    }

    public void setPos(int c) {
        this.t.setPos(c);
    }

    public void setUsername(String usr) {
        this.username = usr;
    }

    public String getUsername() {
        return username;

    }

    @Override
    public String toString() {
        return username;
    }

}
