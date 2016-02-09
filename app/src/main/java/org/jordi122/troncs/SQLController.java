package org.jordi122.troncs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Jordi.Martinez on 04/08/2014.
 */
public class SQLController {

    private MyDbHelper dbhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    /**
     * Constructor
     *
     * @param c Context to get the activity context
     */
    public SQLController(Context c) {
        ourcontext = c;
    }


    /**
     * Calls to create a database
     *
     * @return
     * @throws SQLException if something wrong
     */
    public SQLController open() throws SQLException {
        dbhelper = new MyDbHelper(ourcontext);
        try {
            dbhelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }
        try {
            database = dbhelper.openDataBase();

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return this;

    }

    /**
     * Close the database
     */
    public void close() {
        dbhelper.close();
    }

    /**
     * Put the data in a contentvalue to insert it in the database
     *
     * @param vNom      Name
     * @param vPosicio  Position of Name
     * @param vEspatlla Height of Name
     */
    public void insertData(String vNom, String vPosicio, String vEspatlla) {

        ContentValues cv = new ContentValues();
        cv.put(MyDbHelper.CASTELLERS_NOM, vNom);
        cv.put(MyDbHelper.CASTELLERS_POSICIO, vPosicio);
        cv.put(MyDbHelper.CASTELLERS_ESPATLLA, vEspatlla);

        if (database.update(MyDbHelper.TABLE_CASTELLERS,cv, "nom = ? and posicio = ?", new String[]{vNom, vPosicio})==0) {
            database.insert(MyDbHelper.TABLE_CASTELLERS, null, cv);
        }
    }

    /**
     * Delete the data in the database according the name and position
     *
     * @param vNom
     * @param vPosicio
     * @return
     */
    public int deleteData(String vNom, String vPosicio) {
        String[] valorsAesborrar = new String[]{vNom, vPosicio};
        return database.delete(MyDbHelper.TABLE_CASTELLERS, "nom = ? AND posicio = ?", valorsAesborrar);
    }

    /**
     * Query to database to read it all
     *
     * @return Cursor with all the data
     */
    public Cursor readEntry() {

        Cursor c = database.query(MyDbHelper.TABLE_CASTELLERS, new String[]{"nom", "posicio", "espatlla"}, "nom IS NOT ?", new String[]{"0"}, null,
                null, "nom");

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /**
     * Returns all the components by position
     *
     * @param vPosicio
     * @return Cursor that contains all names and heights according their position
     */
    public Cursor llistaComponentsperPosicio(String vPosicio) {
        String[] posicioCasteller = new String[]{vPosicio};
        Cursor c = database.query(MyDbHelper.TABLE_CASTELLERS, new String[]{"nom", "espatlla"}, "posicio IS ?", posicioCasteller, null,
                null, null);
        c.moveToFirst();
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    /**
     * Primer llista tots els components sense la posició i després esborra els repetits
     *
     * @return ArrayList<String[]> with all names and heights without repeating
     */
    public ArrayList<String[]> nomiAlsada() {

        Cursor c = database.query(MyDbHelper.TABLE_CASTELLERS, new String[]{"nom", "espatlla"}, null, null, null,
                null, "nom");
        c.moveToFirst();

        if (c != null) {
            c.moveToFirst();
        }
        String[] data, data2;
        ArrayList<String[]> nomialsada = new ArrayList<String[]>();
        for (int i = 0; i < c.getCount(); i++) {
            data = new String[2];
            data[0] = c.getString(0);
            data[1] = c.getString(1);
            nomialsada.add(data);
            c.moveToNext();
        }

        c.close();
        for (int i = 0; i < nomialsada.size(); i++) {
            data = nomialsada.get(i);
            for (int j = 0; j < i; j++) {
                data2 = nomialsada.get(j);
                if (data[0].equals(data2[0])) {
                    nomialsada.remove(i);
                    i--;
                }

            }
        }
        return nomialsada;

    }

    /**
     * @return Cursor with all databade
     */
    public Cursor readEntryDibuix() {

        Cursor c = database.query(MyDbHelper.TABLE_CASTELLERS, new String[]{"nom", "posicio", "espatlla"}, null, null, null,
                null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public long addSqlCastell(String castell, String nomCas, String data, String nomTaula) {
        ContentValues cv = new ContentValues();
        cv.put(MyDbHelper.TITOL_CASTELL, nomCas);
        cv.put(MyDbHelper.DATA_CASTELL, data);
        cv.put(MyDbHelper.NOM_TAULA, nomTaula);
        long resultat = 1;
        switch (castell) {
            case ("torre"):
                resultat = database.insert(MyDbHelper.TABLE_TORRES, null, cv);
                break;
            case ("tres"):
                resultat = database.insert(MyDbHelper.TABLE_TRESS, null, cv);
                break;
            case ("quatre"):
                resultat = database.insert(MyDbHelper.TABLE_QUATRES, null, cv);
                break;
        }
        return resultat;
    }


    public boolean crearTaulaCastell(String nomTaula) {
        try {
            database.execSQL("create table if not exists " + nomTaula
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL, posicio TEXT NOT NULL);");
            return true;
        } catch (SQLException e) {
            Toast.makeText(ourcontext, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public long addSqlCasteller(String nomTaula, String nom, String posicio) {
        ContentValues cv = new ContentValues();
        cv.put("nom", nom);
        cv.put("posicio", posicio);
        long resultat = database.insert(nomTaula, null, cv);
        return resultat;
    }


    public String getNomDB(String tipus) {
        int i = 1;
        String nouNom = tipus;
        while (isTableExists(nouNom)) {
            nouNom = tipus + String.valueOf(i);
            i++;
        }
        return nouNom;

    }

    public boolean isNameExists(String tipus, String name) {
        String Query = "Select * from " + tipus + " where titol = '" + name + "'";
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            return false;
        }
        return true;
    }

    public boolean isTableExists(String tableName) {

        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public ArrayList<String> getListCastells(String tipus) {
        //Cursor c = database.query(MyDbHelper.TABLE_CASTELLERS, new String[] { "nom", "posicio", "espatlla" }, null ,null, null,                null,null);
        Cursor c = database.query(tipus, new String[]{"titol", "data"}, null, null, null,
                null, "_id");
        c.moveToFirst();
        if (c != null) {
            c.moveToFirst();
        }
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < c.getCount(); i++) {
            data.add(c.getString(0) + " " + c.getString(1));
            c.moveToNext();
        }

        return data;
    }


    public void deleteCastell(String tipus, String castell) {
        String[] castellArray = {castell};
        Cursor c = database.query(tipus, new String[]{"nomDB"}, "titol=?", castellArray, null,
                null, null);
        if (c != null) {
            c.moveToFirst();
        }
        String data = c.getString(0);
        c.moveToNext();
        database.execSQL("DROP TABLE IF EXISTS " + data);
        database.delete(tipus, "titol=?", castellArray);


    }

    public ArrayList<String[]> getAllCastell(String tipus,String castell) {
        String[] castellArray = {castell};
        Cursor c = database.query(tipus, new String[]{"nomDB"}, "titol=?", castellArray, null,
                null, null);
        if (c != null) {
            c.moveToFirst();
        }
        String data = c.getString(0);


        c = database.query(data, new String[]{"nom", "posicio"}, null, null, null,
                null, "_id");
        c.moveToFirst();
        if (c != null) {
            c.moveToFirst();
        }
        ArrayList<String[]> datas = new ArrayList<String[]>();
        for (int i = 0; i < c.getCount(); i++) {
            datas.add(i, new String[]{c.getString(0), c.getString(1)});
            c.moveToNext();
        }

        return datas;
    }
    public void createAllTableif() {
        MyDbHelper.createTablesIf(database);
    }
}
