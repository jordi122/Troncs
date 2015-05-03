package org.jordi122.troncs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jordi.Martinez on 04/08/2014.
 */
public class QuatreFragment extends Fragment {
    AuxClassStrings auxStrings;
    Spinner spib1,spib2,spib3,spib4,spis1,spis2,spis3,spis4,spit1,spit2,spit3,spit4,spiq1,spiq2,spiq3,spiq4,spic1,spic2,spic3,spic4,spin1,spin2,spin3,spin4, spinCastells;
    TextView lan,lac,laq,lat,las,lab,ldca,ldna,ldqa,ldta,ldsa,ldba,ldnb,ldcb,ldqb,ldtb,ldsb,ldbb,ldnc,ldcc,ldqc,ldtc,ldsc,ldbc,ldnd,ldcd,ldqd,ldtd,ldsd,ldbd;
    SQLController sqlcon;
    EditText nomCastell;
    ArrayList<String> sBaixos,sSegons,sTersos,sQuarts,sQuints, llistaCastells;
    ArrayList<String[]> nomialsada;
    ArrayList<Castellers> llistaComponents;
    String[] mesAlta;
    int[][] instPis,instAcu,instDif;
    Cursor c;
    int comptador = 0;
    ArrayAdapter<String> adaptCastells;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        sqlcon = new SQLController(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_quatre, container, false);

        llistaComponents = new ArrayList<Castellers>();

        auxStrings = new AuxClassStrings();
        instPis = new int[6][4];
        instAcu = new int[6][4];
        instDif = new int[6][5];
        mesAlta =  new String[6];
        identificadors(rootView);

        //Inicialitzacio
        sBaixos =  new ArrayList<String>();
        sSegons =  new ArrayList<String>();
        sTersos = new ArrayList<String>();
        sQuarts = new ArrayList<String>();
        sQuints = new ArrayList<String>();
        sqlcon.open();
        llistaCastells = sqlcon.getListCastells("quatres");
        sqlcon.close();

        emplenarNomiAlsada();

        emplenarStringsComponents("Baixos",sBaixos);
        emplenarStringsComponents("Segons",sSegons);
        emplenarStringsComponents("Terços",sTersos);
        emplenarStringsComponents("Quarts",sQuarts);
        emplenarStringsComponents("Quints",sQuints);

        funcAdaptadors();
        funcAdaptadorCastell();

        ImageButton button = (ImageButton) rootView.findViewById(R.id.butAddQu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCastell();
            }
        });
        ImageButton buttondel = (ImageButton) rootView.findViewById(R.id.butDelQu);
        buttondel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCastell(spinCastells.getSelectedItem().toString());
            }
        });
        return rootView;
    }

    private void emplenarStringsComponents(String vPosicio, ArrayList<String> sPosicio) {
        sqlcon.open();
        c = sqlcon.llistaComponentsperPosicio(vPosicio);
        int rows = c.getCount();
        c.moveToFirst();
        for (int i = 0; i < rows; i++) {
            sPosicio.add(c.getString(0));
            c.moveToNext();
        }
        c.moveToFirst();
        for (int i = 0; i < sPosicio.size(); i++) {
            String data = sPosicio.get(i);
            for (int j = 0; j < nomialsada.size(); j++) {
                String[] data2 = nomialsada.get(j);
                if (data.equals(data2[0])) {
                    data = data + " " + data2[1];
                    sPosicio.set(i,data);
                    break;
                }
            }
        }
        sqlcon.close();

    }

    private void funcAdaptadorCastell() {
        if (llistaCastells!=null) {

            adaptCastells = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, llistaCastells);
            adaptCastells.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCastells.setAdapter(adaptCastells);
        }
        spinCastells.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {

                        if (comptador!=0) {
                            emplenarCastell(spinCastells.getSelectedItem().toString());

                        }
                        comptador++;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    private void funcAdaptadors() {
        ArrayAdapter<String> adaptBaixos =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, sBaixos);
        adaptBaixos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptSegons =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, sSegons);
        adaptSegons.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptTersos =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, sTersos);
        adaptTersos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptQuarts =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, sQuarts);
        adaptQuarts.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptQuints =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, sQuints);
        adaptQuints.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spib1.setAdapter(adaptBaixos);
        spib2.setAdapter(adaptBaixos);
        spib3.setAdapter(adaptBaixos);
        spib4.setAdapter(adaptBaixos);
        spis1.setAdapter(adaptBaixos);
        spis2.setAdapter(adaptBaixos);
        spis3.setAdapter(adaptBaixos);
        spis4.setAdapter(adaptBaixos);
        spit1.setAdapter(adaptSegons);
        spit2.setAdapter(adaptSegons);
        spit3.setAdapter(adaptSegons);
        spit4.setAdapter(adaptSegons);
        spiq1.setAdapter(adaptTersos);
        spiq2.setAdapter(adaptTersos);
        spiq3.setAdapter(adaptTersos);
        spiq4.setAdapter(adaptTersos);
        spic1.setAdapter(adaptQuarts);
        spic2.setAdapter(adaptQuarts);
        spic3.setAdapter(adaptQuarts);
        spic4.setAdapter(adaptQuarts);
        spin1.setAdapter(adaptQuints);
        spin2.setAdapter(adaptQuints);
        spin3.setAdapter(adaptQuints);
        spin4.setAdapter(adaptQuints);



        spib1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spib1.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[0][0] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        spib2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spib2.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[0][1] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spib3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spib3.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[0][2] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spib4.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spib4.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[0][3] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spis1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spis1.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[1][0] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spis2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spis2.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[1][1] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spis3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spis3.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[1][2] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spis4.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spis4.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[1][3] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spit1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spit1.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[2][0] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spit2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spit2.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[2][1] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spit3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spit3.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[2][2] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spit4.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spit4.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[2][3] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spiq1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spiq1.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[3][0] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spiq2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spiq2.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[3][1] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spiq3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spiq3.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[3][2] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spiq4.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spiq4.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[3][3] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spic1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spic1.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[4][0] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spic2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spic2.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[4][1] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spic3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spic3.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[4][2] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spic4.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spic4.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[4][3] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spin1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spin1.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[5][0] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spin2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spin2.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[5][1] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spin3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spin3.getSelectedItem().toString());
                        tempString = auxStrings.deletePoints(tempString);
                        instPis[5][2] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spin4.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        String tempString = auxStrings.deleteLetters(spin4.getSelectedItem().toString());
                        instPis[5][3] = Integer.parseInt(tempString);
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }

    private void identificadors(View rootView) {

        spib1 = (Spinner) rootView.findViewById(R.id.spib1);
        spib2 = (Spinner) rootView.findViewById(R.id.spib2);
        spib3 = (Spinner) rootView.findViewById(R.id.spib3);
        spib4 = (Spinner) rootView.findViewById(R.id.spib4);
        spis1 = (Spinner) rootView.findViewById(R.id.spis1);
        spis2 = (Spinner) rootView.findViewById(R.id.spis2);
        spis3 = (Spinner) rootView.findViewById(R.id.spis3);
        spis4 = (Spinner) rootView.findViewById(R.id.spis4);
        spit1 = (Spinner) rootView.findViewById(R.id.spit1);
        spit2 = (Spinner) rootView.findViewById(R.id.spit2);
        spit3 = (Spinner) rootView.findViewById(R.id.spit3);
        spit4 = (Spinner) rootView.findViewById(R.id.spit4);
        spiq1 = (Spinner) rootView.findViewById(R.id.spiq1);
        spiq2 = (Spinner) rootView.findViewById(R.id.spiq2);
        spiq3 = (Spinner) rootView.findViewById(R.id.spiq3);
        spiq4 = (Spinner) rootView.findViewById(R.id.spiq4);
        spic1 = (Spinner) rootView.findViewById(R.id.spic1);
        spic2 = (Spinner) rootView.findViewById(R.id.spic2);
        spic3 = (Spinner) rootView.findViewById(R.id.spic3);
        spic4 = (Spinner) rootView.findViewById(R.id.spic4);
        spin1 = (Spinner) rootView.findViewById(R.id.spin1);
        spin2 = (Spinner) rootView.findViewById(R.id.spin2);
        spin3 = (Spinner) rootView.findViewById(R.id.spin3);
        spin4 = (Spinner) rootView.findViewById(R.id.spin4);

        lan = (TextView) rootView.findViewById(R.id.lblAltaSisens);
        lac = (TextView) rootView.findViewById(R.id.lblAltaQuints);
        laq = (TextView) rootView.findViewById(R.id.lblAltaQuarts);
        lat = (TextView) rootView.findViewById(R.id.lblAltaTersos);
        las = (TextView) rootView.findViewById(R.id.lblAltaSegons);
        lab = (TextView) rootView.findViewById(R.id.lblAltaBaixos);

        ldna = (TextView) rootView.findViewById(R.id.lblDifSisensA);
        ldca = (TextView) rootView.findViewById(R.id.lblDifQuintsA);
        ldqa = (TextView) rootView.findViewById(R.id.lblDifQuartsA);
        ldta = (TextView) rootView.findViewById(R.id.lblDifTersosA);
        ldsa = (TextView) rootView.findViewById(R.id.lblDifSegonsA);
        ldba = (TextView) rootView.findViewById(R.id.lblDifBaixosA);
        ldnb = (TextView) rootView.findViewById(R.id.lblDifSisensB);
        ldcb = (TextView) rootView.findViewById(R.id.lblDifQuintsB);
        ldqb = (TextView) rootView.findViewById(R.id.lblDifQuartsB);
        ldtb = (TextView) rootView.findViewById(R.id.lblDifTersosB);
        ldsb = (TextView) rootView.findViewById(R.id.lblDifSegonsB);
        ldbb = (TextView) rootView.findViewById(R.id.lblDifBaixosB);
        ldnc = (TextView) rootView.findViewById(R.id.lblDifSisensC);
        ldcc = (TextView) rootView.findViewById(R.id.lblDifQuintsC);
        ldqc = (TextView) rootView.findViewById(R.id.lblDifQuartsC);
        ldtc = (TextView) rootView.findViewById(R.id.lblDifTersosC);
        ldsc = (TextView) rootView.findViewById(R.id.lblDifSegonsC);
        ldbc = (TextView) rootView.findViewById(R.id.lblDifBaixosC);
        ldnd = (TextView) rootView.findViewById(R.id.lblDifSisensD);
        ldcd = (TextView) rootView.findViewById(R.id.lblDifQuintsD);
        ldqd = (TextView) rootView.findViewById(R.id.lblDifQuartsD);
        ldtd = (TextView) rootView.findViewById(R.id.lblDifTersosD);
        ldsd = (TextView) rootView.findViewById(R.id.lblDifSegonsD);
        ldbd = (TextView) rootView.findViewById(R.id.lblDifBaixosD);

        spinCastells = (Spinner) rootView.findViewById(R.id.spinCastellsQu);
        nomCastell = (EditText) rootView.findViewById(R.id.nomCastell);
    }

    private void emplenarNomiAlsada() {
        sqlcon.open();
        nomialsada = sqlcon.nomiAlsada();
        sqlcon.close();
    }


    private void sumaRenglesInt() {
        instAcu[0][0] = instPis[0][0];
        instAcu[0][1] = instPis[0][1];
        instAcu[0][2] = instPis[0][2];
        instAcu[0][3] = instPis[0][3];
        for (int i = 1; i < instAcu.length; i++) {
            instAcu[i][0] = instPis[i][0] + instAcu[i-1][0];
            instAcu[i][1] = instPis[i][1] + instAcu[i-1][1];
            instAcu[i][2] = instPis[i][2] + instAcu[i-1][2];
            instAcu[i][3] = instPis[i][3] + instAcu[i-1][3];
        }


    }
    /**
     * Metode que diu quina rengla és la més alta i guarda el valor en la taula de diferencies
     *
     * @param a El valor de la regla a
     * @param b El valor de la regla b
     * @param c El valor de la regla c
     * @param i El pis per a emmagatzemar en la taula de diferencies
     * @return String amb la rengla més alta
     */
    private String comparator(int a,int b,int c,int d, int i) {
        String resultat = "";
        int vResultat;
        if (a > b) {
            resultat += "A";
            vResultat = a;
        }
        else if (a==b) {
            resultat += "AB";
            vResultat = a;
        }
        else {
            resultat += "B";
            vResultat = b;
        }

        if (c > vResultat) {
            resultat += "C";
            vResultat = c;
        }
        else if (c==vResultat) {
            resultat += "C";
        }
        if (d > vResultat) {
            resultat += "D";
            vResultat = d;
        }
        else if (c==vResultat) {
            resultat += "D";
        }
        instDif[i][0] = vResultat;
        return resultat;
    }
    /**
     * Metode que actualitza les etiquetes
     *
     */
    private void fillLabels() {
        sumaRenglesInt();
        for (int i = 0; i < mesAlta.length; i++) {
            mesAlta[i]= comparator(instAcu[i][0],instAcu[i][1],instAcu[i][2],instAcu[i][3],i);
        }
        lab.setText(mesAlta[0]);
        las.setText(mesAlta[1]);
        lat.setText(mesAlta[2]);
        laq.setText(mesAlta[3]);
        lac.setText(mesAlta[4]);
        lan.setText(mesAlta[5]);

        emplenarDiferencies();

        ldba.setText(Integer.toString(instDif[0][1]));
        ldsa.setText(Integer.toString(instDif[1][1]));
        ldta.setText(Integer.toString(instDif[2][1]));
        ldqa.setText(Integer.toString(instDif[3][1]));
        ldca.setText(Integer.toString(instDif[4][1]));
        ldna.setText(Integer.toString(instDif[5][1]));

        ldbb.setText(Integer.toString(instDif[0][2]));
        ldsb.setText(Integer.toString(instDif[1][2]));
        ldtb.setText(Integer.toString(instDif[2][2]));
        ldqb.setText(Integer.toString(instDif[3][2]));
        ldcb.setText(Integer.toString(instDif[4][2]));
        ldnb.setText(Integer.toString(instDif[5][2]));

        ldbc.setText(Integer.toString(instDif[0][3]));
        ldsc.setText(Integer.toString(instDif[1][3]));
        ldtc.setText(Integer.toString(instDif[2][3]));
        ldqc.setText(Integer.toString(instDif[3][3]));
        ldcc.setText(Integer.toString(instDif[4][3]));
        ldnc.setText(Integer.toString(instDif[5][3]));

        ldbd.setText(Integer.toString(instDif[0][4]));
        ldsd.setText(Integer.toString(instDif[1][4]));
        ldtd.setText(Integer.toString(instDif[2][4]));
        ldqd.setText(Integer.toString(instDif[3][4]));
        ldcd.setText(Integer.toString(instDif[4][4]));
        ldnd.setText(Integer.toString(instDif[5][4]));

    }
    /**
     * Metode que actualitza la taula de les diferencies
     *
     */
    private void emplenarDiferencies() {
        for (int i = 0; i < instDif.length;i++) {
            instDif[i][1] = instAcu[i][0] - instDif[i][0];
            instDif[i][2] = instAcu[i][1] - instDif[i][0];
            instDif[i][3] = instAcu[i][2] - instDif[i][0];
            instDif[i][4] = instAcu[i][3] - instDif[i][0];
        }
    }

    private void addCastell() {
        sqlcon.open();
        final List<String> castellersArray = new ArrayList<String>();
        final String nomCas = nomCastell.getText().toString();
        final String NomDB = sqlcon.getNomDB("quatre");
        castellersArray.add("spib1");castellersArray.add(spib1.getSelectedItem().toString());
        castellersArray.add("spib2");castellersArray.add(spib2.getSelectedItem().toString());
        castellersArray.add("spib3");castellersArray.add(spib3.getSelectedItem().toString());
        castellersArray.add("spib4");castellersArray.add(spib4.getSelectedItem().toString());
        castellersArray.add("spis1");castellersArray.add(spis1.getSelectedItem().toString());
        castellersArray.add("spis2");castellersArray.add(spis2.getSelectedItem().toString());
        castellersArray.add("spis3");castellersArray.add(spis3.getSelectedItem().toString());
        castellersArray.add("spis4");castellersArray.add(spis4.getSelectedItem().toString());
        castellersArray.add("spit1");castellersArray.add(spit1.getSelectedItem().toString());
        castellersArray.add("spit2");castellersArray.add(spit2.getSelectedItem().toString());
        castellersArray.add("spit3");castellersArray.add(spit3.getSelectedItem().toString());
        castellersArray.add("spit4");castellersArray.add(spit4.getSelectedItem().toString());
        castellersArray.add("spiq1");castellersArray.add(spiq1.getSelectedItem().toString());
        castellersArray.add("spiq2");castellersArray.add(spiq2.getSelectedItem().toString());
        castellersArray.add("spiq3");castellersArray.add(spiq3.getSelectedItem().toString());
        castellersArray.add("spiq4");castellersArray.add(spiq4.getSelectedItem().toString());
        castellersArray.add("spic1");castellersArray.add(spic1.getSelectedItem().toString());
        castellersArray.add("spic2");castellersArray.add(spic2.getSelectedItem().toString());
        castellersArray.add("spic3");castellersArray.add(spic3.getSelectedItem().toString());
        castellersArray.add("spic4");castellersArray.add(spic4.getSelectedItem().toString());
        castellersArray.add("spin1");castellersArray.add(spin1.getSelectedItem().toString());
        castellersArray.add("spin2");castellersArray.add(spin2.getSelectedItem().toString());
        castellersArray.add("spin3");castellersArray.add(spin3.getSelectedItem().toString());
        castellersArray.add("spin4");castellersArray.add(spin4.getSelectedItem().toString());
        if (sqlcon.isNameExists("quatres", nomCas)) {
            AlertDialog.Builder sobreEscriu = new AlertDialog.Builder(getActivity());
            sobreEscriu.setTitle("Aquest nom ja és usat");
            sobreEscriu.setMessage("Vols sobreescriure?");
            sobreEscriu.setPositiveButton("Sí",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    escriuOsobreEscriuCastell(true,nomCas, NomDB, castellersArray);

                }
            });
            sobreEscriu.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog alertDialog = sobreEscriu.create();
            alertDialog.show();
        }
        else if(nomCas.contains("/")) {
            Toast.makeText(getActivity(), "El nom no pot contenir '/'", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            escriuOsobreEscriuCastell(false,nomCas, NomDB, castellersArray);

        }
    }

    private void afegirCastelleraTaula(String nomTaula, String[] casteller) {
        sqlcon.addSqlCasteller(nomTaula, casteller[1], casteller[0]);
        llistaCastells = sqlcon.getListCastells("quatres");
    }

    private void deleteCastell(String castellidata) {
        sqlcon.open();
        String castell = castellidata.substring(0, castellidata.indexOf("/") - 3);
        sqlcon.deleteCastell("quatres", castell);
        llistaCastells = sqlcon.getListCastells("quatres");
        sqlcon.close();
        funcAdaptadorCastell();
    }

    private void escriuOsobreEscriuCastell(Boolean esborra, String castell,String NomDB, List<String> castellersArray) {
        sqlcon.open();
        if (esborra) {
            sqlcon.deleteCastell("quatres", castell);
        }
        llistaCastells = sqlcon.getListCastells("quatres");
        sqlcon.crearTaulaCastell(NomDB);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String data = dateFormat.format(date);
        sqlcon.addSqlCastell("quatre", castell, data, NomDB);

        String[] spib1String = {"spib1", castellersArray.get(1)};
        String[] spib2String = {"spib2", castellersArray.get(3)};
        String[] spib3String = {"spib3", castellersArray.get(5)};
        String[] spib4String = {"spib4", castellersArray.get(7)};
        String[] spis1String = {"spis1", castellersArray.get(9)};
        String[] spis2String = {"spis2", castellersArray.get(11)};
        String[] spis3String = {"spis3", castellersArray.get(13)};
        String[] spis4String = {"spis4", castellersArray.get(15)};
        String[] spit1String = {"spit1", castellersArray.get(17)};
        String[] spit2String = {"spit2", castellersArray.get(19)};
        String[] spit3String = {"spit3", castellersArray.get(21)};
        String[] spit4String = {"spit4", castellersArray.get(23)};
        String[] spiq1String = {"spiq1", castellersArray.get(25)};
        String[] spiq2String = {"spiq2", castellersArray.get(27)};
        String[] spiq3String = {"spiq3", castellersArray.get(29)};
        String[] spiq4String = {"spiq4", castellersArray.get(31)};
        String[] spic1String = {"spic1", castellersArray.get(33)};
        String[] spic2String = {"spic2", castellersArray.get(35)};
        String[] spic3String = {"spic3", castellersArray.get(37)};
        String[] spic4String = {"spic4", castellersArray.get(39)};
        String[] spin1String = {"spin1", castellersArray.get(41)};
        String[] spin2String = {"spin2", castellersArray.get(43)};
        String[] spin3String = {"spin3", castellersArray.get(45)};
        String[] spin4String = {"spin4", castellersArray.get(47)};

        afegirCastelleraTaula(NomDB, spib1String);
        afegirCastelleraTaula(NomDB, spib2String);
        afegirCastelleraTaula(NomDB, spib3String);
        afegirCastelleraTaula(NomDB, spib4String);
        afegirCastelleraTaula(NomDB, spis1String);
        afegirCastelleraTaula(NomDB, spis2String);
        afegirCastelleraTaula(NomDB, spis3String);
        afegirCastelleraTaula(NomDB, spis4String);
        afegirCastelleraTaula(NomDB, spit1String);
        afegirCastelleraTaula(NomDB, spit2String);
        afegirCastelleraTaula(NomDB, spit3String);
        afegirCastelleraTaula(NomDB, spit4String);
        afegirCastelleraTaula(NomDB, spiq1String);
        afegirCastelleraTaula(NomDB, spiq2String);
        afegirCastelleraTaula(NomDB, spiq3String);
        afegirCastelleraTaula(NomDB, spiq4String);
        afegirCastelleraTaula(NomDB, spic1String);
        afegirCastelleraTaula(NomDB, spic2String);
        afegirCastelleraTaula(NomDB, spic3String);
        afegirCastelleraTaula(NomDB, spic4String);
        afegirCastelleraTaula(NomDB, spin1String);
        afegirCastelleraTaula(NomDB, spin2String);
        afegirCastelleraTaula(NomDB, spin3String);
        afegirCastelleraTaula(NomDB, spin4String);

        llistaCastells = sqlcon.getListCastells("quatres");
        sqlcon.close();
        funcAdaptadorCastell();
        spinCastells.setSelection(llistaCastells.size()-1);
        if (esborra) {
            Toast.makeText(getActivity(), "Sobreescrit " + castell + ".", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(), "Insertat " + castell + ".", Toast.LENGTH_LONG).show();
        }
    }

    private void emplenarCastell(String castellidata) {
        sqlcon.open();
        String castell = castellidata.substring(0, castellidata.indexOf("/") - 3);
        ArrayList<String[]> allCastell = sqlcon.getAllCastell("quatres",castell);
        spib1.setSelection(getPos(emplenarCasteller("spib1",allCastell),sBaixos));
        spib2.setSelection(getPos(emplenarCasteller("spib2",allCastell),sBaixos));
        spib3.setSelection(getPos(emplenarCasteller("spib3",allCastell),sBaixos));
        spib4.setSelection(getPos(emplenarCasteller("spib4",allCastell),sBaixos));
        spis1.setSelection(getPos(emplenarCasteller("spis1",allCastell),sBaixos));
        spis2.setSelection(getPos(emplenarCasteller("spis2",allCastell),sBaixos));
        spis3.setSelection(getPos(emplenarCasteller("spis3",allCastell),sBaixos));
        spis4.setSelection(getPos(emplenarCasteller("spis4",allCastell),sBaixos));
        spit1.setSelection(getPos(emplenarCasteller("spit1",allCastell),sSegons));
        spit2.setSelection(getPos(emplenarCasteller("spit2",allCastell),sSegons));
        spit3.setSelection(getPos(emplenarCasteller("spit3",allCastell),sSegons));
        spit4.setSelection(getPos(emplenarCasteller("spit4",allCastell),sSegons));
        spiq1.setSelection(getPos(emplenarCasteller("spiq1",allCastell),sTersos));
        spiq2.setSelection(getPos(emplenarCasteller("spiq2",allCastell),sTersos));
        spiq3.setSelection(getPos(emplenarCasteller("spiq3",allCastell),sTersos));
        spiq4.setSelection(getPos(emplenarCasteller("spiq4",allCastell),sTersos));
        spic1.setSelection(getPos(emplenarCasteller("spic1",allCastell),sQuarts));
        spic2.setSelection(getPos(emplenarCasteller("spic2",allCastell),sQuarts));
        spic3.setSelection(getPos(emplenarCasteller("spic3",allCastell),sQuarts));
        spic4.setSelection(getPos(emplenarCasteller("spic4",allCastell),sQuarts));
        spin1.setSelection(getPos(emplenarCasteller("spin1",allCastell),sQuints));
        spin2.setSelection(getPos(emplenarCasteller("spin2",allCastell),sQuints));
        spin3.setSelection(getPos(emplenarCasteller("spin3",allCastell),sQuints));
        spin4.setSelection(getPos(emplenarCasteller("spin4",allCastell),sQuints));
    }

    private String emplenarCasteller(String posicio, ArrayList<String[]> allCastell) {
        for (int i = 0; i < allCastell.size(); i++) {
            if (posicio.equals(allCastell.get(i)[1])) {
                return allCastell.get(i)[0];
            }
        }
        return "No trobat";
    }

    private int getPos(String casteller, ArrayList<String> posicio) {
        for (int i = 0; i < posicio.size(); i++) {
            if (casteller.equals(posicio.get(i))) {
                return i;
            }
        }
        return -1;
    }


}

