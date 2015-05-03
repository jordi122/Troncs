package org.jordi122.troncs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Jordi.Martinez on 04/08/2014.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    // TABLE INFORMATTION
    public static final String TABLE_CASTELLERS = "castellers";
    public static final String TABLE_TORRES = "torres";
    public static final String TABLE_TRESS = "tress";
    public static final String TABLE_QUATRES = "quatres";
    public static final String CASTELLERS_NOM = "nom";
    public static final String CASTELLERS_POSICIO = "posicio";
    public static final String CASTELLERS_ESPATLLA = "espatlla";
    public static final String TITOL_CASTELL = "titol";
    public static final String DATA_CASTELL = "data";
    public static final String NOM_TAULA = "nomDB";
    private static final String UID = "_id";

    // DATABASE INFORMATION
    private String DB_PATH;// = "/data/data/org.jordi122.troncs/databases/";
    static final String DB_NAME = "castellers.db";
    static final int DB_VERSION = 1;

    // TABLE CREATION STATEMENT

    private final Context myContext;
    private SQLiteDatabase myDataBase;
    private static final String CREATE_TABLE = "create table if not exists " + TABLE_CASTELLERS
            + "(" + CASTELLERS_NOM + " TEXT NOT NULL, "
            + CASTELLERS_POSICIO + " TEXT NOT NULL ," + CASTELLERS_ESPATLLA
            + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_TORRE = "create table if not exists " + TABLE_TORRES
            +" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITOL_CASTELL + " TEXT NOT NULL, "
            + DATA_CASTELL + " TEXT NOT NULL, "+ "nomDB TEXT NOT NULL);";
    private static final String CREATE_TABLE_TRES = "create table if not exists " + TABLE_TRESS
            +" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITOL_CASTELL + " TEXT NOT NULL, "
            + DATA_CASTELL + " TEXT NOT NULL, "+ "nomDB TEXT NOT NULL);";
    private static final String CREATE_TABLE_QUATRE = "create table if not exists " + TABLE_QUATRES
            +" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITOL_CASTELL + " TEXT NOT NULL, "
            + DATA_CASTELL + " TEXT NOT NULL, "+ "nomDB TEXT NOT NULL);";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist

        }
        else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            myDataBase = this.getWritableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }
       /* if (isTableExists("torres",myDataBase)) {
            createTablesIf(myDataBase);
        }*/

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();

    }


    public SQLiteDatabase openDataBase() throws SQLException{

        //Open the database
        File dbFile = myContext.getDatabasePath(DB_NAME);
        String myPath = dbFile.getAbsolutePath();
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        File dbFile = myContext.getDatabasePath(DB_NAME);
        String outFileName = dbFile.getAbsolutePath();

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        checkDataBase();

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		/*db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASTELLERS);
		onCreate(db);*/

    }

    public static void createTablesIf(SQLiteDatabase db) {
        try {
            System.out.println("Creating tables");
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE_QUATRE);
            db.execSQL(CREATE_TABLE_TORRE);
            db.execSQL(CREATE_TABLE_TRES);
        } catch (android.database.SQLException e) {
            //Toast.makeText(activi, "Exception: " + e, Toast.LENGTH_SHORT).show();
        }
    }

/*    public boolean isTableExists(String tableName,SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }*/



}

