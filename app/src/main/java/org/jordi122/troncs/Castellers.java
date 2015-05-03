package org.jordi122.troncs;

/**
 * Created by Jordi.Martinez on 11/08/2014.
 */
public class Castellers {
    private String nom;
    private int espatlla;
    private String posicio;

    public Castellers(String nom, String posicio, int espatlla) {
        this.nom = nom;
        this.espatlla = espatlla;
        this.posicio = posicio;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getEspatlla() {
        return espatlla;
    }

    public void setEspatlla(int espatlla) {
        this.espatlla = espatlla;
    }

    public String getPosicio() {
        return posicio;
    }

    public void setPosicio(String posicio) {
        this.posicio = posicio;
    }
    public void printData() {
        System.out.println("Casteller: "+ nom + " Posicio: "+ posicio + " Espatlla: "+espatlla);
    }
}
