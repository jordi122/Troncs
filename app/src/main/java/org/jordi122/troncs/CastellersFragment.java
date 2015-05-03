package org.jordi122.troncs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

/**
 * Created by Jordi.Martinez on 04/08/2014.
 */
public class CastellersFragment extends Fragment implements TextWatcher {
    TableLayout table_layout;
    EditText nom_et, espatlla_et;
    Spinner posicio_sp;
    Button addmem_btn, esb_btn,ref_btn;
    SQLController sqlcon;

    ProgressDialog PD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        sqlcon = new SQLController(getActivity());
        nom_et = (EditText) rootView.findViewById(R.id.entNom);
        posicio_sp = (Spinner) rootView.findViewById(R.id.CmbPosicions);
        espatlla_et = (EditText) rootView.findViewById(R.id.entEspatlla);
        espatlla_et.addTextChangedListener(this);
        addmem_btn = (Button) rootView.findViewById(R.id.butAfegeix);
        ref_btn = (Button) rootView.findViewById(R.id.butRefresh);
        esb_btn = (Button) rootView.findViewById(R.id.butEsborra);
        table_layout = (TableLayout) rootView.findViewById(R.id.tableLayout1);
        BuildTable();
        addmem_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                table_layout.removeAllViews();
                new MyAsync().execute("add");
                Toast myToast = Toast.makeText(getActivity(), "Insertat/Actualitzat " + nom_et.getText() + " amb alçada " + espatlla_et.getText() + " a la posicó de " + posicio_sp.getSelectedItem().toString(), Toast.LENGTH_LONG );
                myToast.show();
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        BuildTable();
                    }
                }, 500);*/
            }
        });


        esb_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                table_layout.removeAllViews();
                new MyAsync().execute("delete");
                Toast myToast = Toast.makeText(getActivity(), "Esborrat " + nom_et.getText().toString() + " a " + posicio_sp.getSelectedItem().toString(), Toast.LENGTH_LONG );
                myToast.show();
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        BuildTable();
                    }
                }, 500);*/

            }
        });
        ref_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                table_layout.removeAllViews();
        /*Metode per a endarrerir una execució*/
                Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    BuildTable();
                }
            }, 5000);
        }
        });
        return rootView;


    }

    private void BuildTable() {

        sqlcon.open();
        Cursor c = sqlcon.readEntry();
        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();
        // outer for loop
        for (int i = 0; i < rows; i++) {
            final TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 0; j < cols; j++) {
                TextView tv = new TextView(getActivity());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                //tv.setBackgroundResource(R.drawable.cell_shape);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);
                tv.setText(c.getString(j));
                row.addView(tv);
            }

            c.moveToNext();
            table_layout.addView(row);
            //row.setClickable(true);

            row.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    row.setBackgroundColor(-3355444);
                    //row.setBackgroundResource(android.R.drawable.list_selector_background);
                    TableRow t = (TableRow) v;
                    TextView nom = (TextView) t.getChildAt(0);
                    TextView posicio = (TextView) t.getChildAt(1);
                    TextView espatlla = (TextView) t.getChildAt(2);
                    nom_et.setText(nom.getText().toString());
                    espatlla_et.setText(espatlla.getText().toString());
                    if (posicio.getText().toString().equals("Baixos")) {
                        posicio_sp.setSelection(0);
                    }
                    else if (posicio.getText().toString().equals("Segons")) {
                        posicio_sp.setSelection(1);
                    }
                    else if (posicio.getText().toString().equals("Terços")) {
                        posicio_sp.setSelection(2);
                    }
                    else if (posicio.getText().toString().equals("Quarts")) {
                        posicio_sp.setSelection(3);
                    }
                    else if (posicio.getText().toString().equals("Quints")) {
                        posicio_sp.setSelection(4);
                    }
                    else {
                        posicio_sp.setSelection(5);
                    }

                    /*Metode per a endarrerir una execució*/
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            row.setBackgroundColor(0);
                        }
                    }, 5000);

                }
            });
        }
        sqlcon.createAllTableif();
        sqlcon.close();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            int numero =  Integer.parseInt(editable.toString());
            /*if (numero>160 || numero<120) {
                Toast myToast = Toast.makeText(getActivity(),"Ha d'estar entre 120 i 160",Toast.LENGTH_SHORT);
                myToast.show();
            }*/
        }
        catch (NumberFormatException e){}
    }


    private class MyAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... metode) {
            if (metode[0]=="add") {
                String nom = nom_et.getText().toString();
                String posicio = posicio_sp.getSelectedItem().toString();
                String espatlla = espatlla_et.getText().toString();
                // inserting data
                sqlcon.open();
                sqlcon.insertData(nom, posicio, espatlla);
                sqlcon.close();
                return null;
            }
            else if (metode[0]=="delete") {
                String nom = nom_et.getText().toString();
                String posicio = posicio_sp.getSelectedItem().toString();
                // deleting data
                sqlcon.open();
                sqlcon.deleteData(nom,posicio);
                sqlcon.close();
                return null;
            }
            else {
                return null;
            }
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            table_layout.removeAllViews();

            PD = new ProgressDialog(getActivity());
            PD.setTitle("Esperant..");
            PD.setMessage("Carregant...");
            PD.setCancelable(false);
            PD.show();
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            BuildTable();
            PD.dismiss();
        }
    }


}
