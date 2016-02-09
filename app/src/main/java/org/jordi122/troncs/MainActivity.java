package org.jordi122.troncs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mNavigationDrawerFragment.selectItem(position);

        mNavigationDrawerFragment.mDrawerListView.setBackgroundResource(mNavigationDrawerFragment.llistaImatges[position]);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.copyDB) {
            File originalDB = this.getDatabasePath("castellers.db");
            File copyDB = new File(android.os.Environment.getExternalStorageDirectory()+"/castellers.db");
            try {
                copyDatabase(originalDB,copyDB);
                Toast.makeText(this,copyDB.getAbsolutePath().toString(),Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (id == R.id.importDB) {
            File originalDB = this.getDatabasePath("castellers.db");
            File copyDB = new File(android.os.Environment.getExternalStorageDirectory()+"/castellers.db");
            try {
                copyDatabase(copyDB,originalDB);
                Toast.makeText(this,copyDB.getAbsolutePath().toString(),Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (id == R.id.menu_left) {
            Log.d("Debug","mNavigationDrawerFragment.mCurrentSelectedPosition: " + String.valueOf(mNavigationDrawerFragment.mCurrentSelectedPosition));
            onNavigationDrawerItemSelected(posicioNova(mNavigationDrawerFragment.mCurrentSelectedPosition,false));
            return true;
            }
        if (id == R.id.menu_right) {
            Log.d("Debug","mNavigationDrawerFragment.mCurrentSelectedPosition: " + String.valueOf(mNavigationDrawerFragment.mCurrentSelectedPosition));
            onNavigationDrawerItemSelected(posicioNova(mNavigationDrawerFragment.mCurrentSelectedPosition,true));
            return true;
        }
        if(id == R.id.menu_capture) {


            LinearLayout li = tornarVista();
            li.setDrawingCacheEnabled(true);
            li.setDrawingCacheBackgroundColor(getResources().getColor(android.R.color.white));
            li.buildDrawingCache();
            Bitmap b = Bitmap.createBitmap(li.getDrawingCache());
            saveToPublicExternalFile(b);
        }

        return super.onOptionsItemSelected(item);
    }
    private int posicioNova(int position, boolean direccio) {
        if (direccio) {
            switch (position) {
                case 0:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 1;
                    break;
                case 1:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 2;
                    break;
                case 2:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 3;
                    break;
                case 3:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 0;
                    break;
                default:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 0;
                    break;
            }
        }
        else {
            switch (position) {
                case 0:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 3;
                    break;
                case 1:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 0;
                    break;
                case 2:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 1;
                    break;
                case 3:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 2;
                    break;
                default:
                    mNavigationDrawerFragment.mCurrentSelectedPosition = 0;
                    break;
            }
        }
        return mNavigationDrawerFragment.mCurrentSelectedPosition;

    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void saveToPublicExternalFile(Bitmap b) {
        if (isExternalStorageWritable()) {
            File Dir = new File(android.os.Environment.getExternalStorageDirectory(), "Troncs");
            if (!Dir.exists()) {
                if (Dir.mkdirs()) {
                    Toast.makeText(this, "Directori creat: " + Dir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
            }
            String nomFitxerOrg = tornarNom();
            String nomFitxer = nomFitxerOrg;
            File imatge = new File(Dir.getAbsolutePath()+nomFitxer);
            int i=1;
            while(imatge.exists()) {
                nomFitxer = nomFitxerOrg;
                nomFitxer = nomFitxer.replace(".","("+String.valueOf(i)+").");
                imatge = new File(Dir.getAbsolutePath()+nomFitxer);
                i++;
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imatge);
                b.compress(Bitmap.CompressFormat.JPEG,100,out);
                //b.compress(Bitmap.CompressFormat.PNG, 100, out);
                String missatge = "Imatge guardada a: " + imatge.getAbsolutePath();
                Toast.makeText(this,missatge,Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this,"No trobat: "+ imatge.getAbsolutePath(),Toast.LENGTH_LONG).show();

            }
        }

        else {
            Toast.makeText(this,"No es pot accedir al directori d'escriptura",Toast.LENGTH_SHORT).show();
        }
    }

    private String tornarNom() {
        String resultat = null;
        switch (mNavigationDrawerFragment.mCurrentSelectedPosition) {
            case 0:
                resultat = "/Castellers.jpg";
                break;
            case 1:
                resultat = "/Torre.jpg";
                break;
            case 2:
                resultat = "/Tres.jpg";
                break;
            case 3:
                resultat = "/Quatre.jpg";
                break;
        }

        return resultat;
    }
    private LinearLayout tornarVista() {
        LinearLayout li = null;
        switch (mNavigationDrawerFragment.mCurrentSelectedPosition) {
            case 0:
                li = (LinearLayout) findViewById(R.id.LinearLayout1);
                break;
            case 1:
                li = (LinearLayout) findViewById(R.id.linearTorre);
                break;
            case 2:
                li = (LinearLayout) findViewById(R.id.linearTres);
                break;
            case 3:
                li = (LinearLayout) findViewById(R.id.linearQuatre);
                break;
        }
        return li;
    }

    public void copyDatabase(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
