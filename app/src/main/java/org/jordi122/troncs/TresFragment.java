package org.jordi122.troncs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
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
public class TresFragment extends Fragment {
    AuxClassStrings auxStrings;
    Spinner spib1,spib2,spib3,spis1,spis2,spis3,spit1,spit2,spit3,spiq1,spiq2,spiq3,spic1,spic2,spic3,spin1,spin2,spin3, spinCastells;
    TextView lan,lac,laq,lat,las,lab,ldca,ldna,ldqa,ldta,ldsa,ldba,ldnb,ldcb,ldqb,ldtb,ldsb,ldbb,ldnc,ldcc,ldqc,ldtc,ldsc,ldbc;
    SQLController sqlcon;
    EditText nomCastell;
    ArrayList<String> sBaixos,sSegons,sTersos,sQuarts,sQuints, llistaCastells;
    ArrayList<String[]> nomialsada;
    String[] mesAlta;
    int comptador = 0;
    int[][] instPis,instAcu,instDif;
    Cursor c;
    ArrayAdapter<String> adaptCastells;
    ArrayList<Castellers> llistaComponents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        sqlcon = new SQLController(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tres, container, false);

        llistaComponents = new ArrayList<Castellers>();

        auxStrings = new AuxClassStrings();
        instPis = new int[6][3];
        instAcu = new int[6][3];
        instDif = new int[6][4];
        mesAlta =  new String[6];
        identificadors(rootView);

        //Inicialitzacio
        sBaixos =  new ArrayList<String>();
        sSegons =  new ArrayList<String>();
        sTersos = new ArrayList<String>();
        sQuarts = new ArrayList<String>();
        sQuints = new ArrayList<String>();
        sqlcon.open();
        llistaCastells = sqlcon.getListCastells("tress");
        sqlcon.close();

        emplenarNomiAlsada();

        emplenarStringsComponents("Baixos",sBaixos);
        emplenarStringsComponents("Segons",sSegons);
        emplenarStringsComponents("Terços",sTersos);
        emplenarStringsComponents("Quarts",sQuarts);
        emplenarStringsComponents("Quints",sQuints);

        funcAdaptadors();

        funcAdaptadorCastell();

        ImageButton button = (ImageButton) rootView.findViewById(R.id.butAddTr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCastell();
            }
        });
        ImageButton buttondel = (ImageButton) rootView.findViewById(R.id.butDelTr);
        buttondel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCastell(spinCastells.getSelectedItem().toString());
            }
        });

        return rootView;
    }

    /*Emplena les strings amb el nom i l'alçada segons la posició*/
    private void emplenarStringsComponents(String vPosicio, ArrayList<String> sPosicio) {
        sqlcon.open();
        c = sqlcon.llistaComponentsperPosicio(vPosicio);
        int rows = c.getCount();
        c.moveToFirst();
        for (int i = 0; i < rows; i++) {
            String dataTemp = c.getString(0)+ " " + c.getString(1);
            sPosicio.add(dataTemp);
            c.moveToNext();
        }
        c.moveToFirst();
        /*for (int i = 0; i < sPosicio.size(); i++) {
            String data = sPosicio.get(i);
            for (int j = 0; j < nomialsada.size(); j++) {
                String[] data2 = nomialsada.get(j);
                if (data.equals(data2[0])) {
                    data = data + " " + data2[1];
                    sPosicio.set(i,data);
                    break;
                }
            }
        }*/
        sqlcon.close();

    }

    private void funcAdaptadorCastell() {
        adaptCastells = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, llistaCastells);
        adaptCastells.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCastells.setAdapter(adaptCastells);
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
        spis1.setAdapter(adaptBaixos);
        spis2.setAdapter(adaptBaixos);
        spis3.setAdapter(adaptBaixos);
        spit1.setAdapter(adaptSegons);
        spit2.setAdapter(adaptSegons);
        spit3.setAdapter(adaptSegons);
        spiq1.setAdapter(adaptTersos);
        spiq2.setAdapter(adaptTersos);
        spiq3.setAdapter(adaptTersos);
        spic1.setAdapter(adaptQuarts);
        spic2.setAdapter(adaptQuarts);
        spic3.setAdapter(adaptQuarts);
        spin1.setAdapter(adaptQuints);
        spin2.setAdapter(adaptQuints);
        spin3.setAdapter(adaptQuints);


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

    }

    private void identificadors(View rootView) {

        spib1 = (Spinner) rootView.findViewById(R.id.spib1);
        spib2 = (Spinner) rootView.findViewById(R.id.spib2);
        spib3 = (Spinner) rootView.findViewById(R.id.spib3);
        spis1 = (Spinner) rootView.findViewById(R.id.spis1);
        spis2 = (Spinner) rootView.findViewById(R.id.spis2);
        spis3 = (Spinner) rootView.findViewById(R.id.spis3);
        spit1 = (Spinner) rootView.findViewById(R.id.spit1);
        spit2 = (Spinner) rootView.findViewById(R.id.spit2);
        spit3 = (Spinner) rootView.findViewById(R.id.spit3);
        spiq1 = (Spinner) rootView.findViewById(R.id.spiq1);
        spiq2 = (Spinner) rootView.findViewById(R.id.spiq2);
        spiq3 = (Spinner) rootView.findViewById(R.id.spiq3);
        spic1 = (Spinner) rootView.findViewById(R.id.spic1);
        spic2 = (Spinner) rootView.findViewById(R.id.spic2);
        spic3 = (Spinner) rootView.findViewById(R.id.spic3);
        spin1 = (Spinner) rootView.findViewById(R.id.spin1);
        spin2 = (Spinner) rootView.findViewById(R.id.spin2);
        spin3 = (Spinner) rootView.findViewById(R.id.spin3);

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

        spinCastells = (Spinner) rootView.findViewById(R.id.spinCastellsTr);
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
        for (int i = 1; i < instAcu.length; i++) {
            instAcu[i][0] = instPis[i][0] + instAcu[i-1][0];
            instAcu[i][1] = instPis[i][1] + instAcu[i-1][1];
            instAcu[i][2] = instPis[i][2] + instAcu[i-1][2];
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
    private String comparator(int a,int b,int c,int i) {
        if (a > b && a > c) {
            instDif[i][0] = a;
            return "A";
        }
        else if (b > a && b > c){
            instDif[i][0] = b;
            return "B";
        }
        else if (c > a && c > b){
            instDif[i][0] = c;
            return "C";
        }
        else if (a == b && a > c) {
            instDif[i][0] = a;
            return "AB";
        }
        else if (a == c && a > b) {
            instDif[i][0] = a;
            return "AC";
        }
        else if (b > a && b == c) {
            instDif[i][0] = b;
            return "BC";
        }
        else {
            instDif[i][0] = a;
            return "ABC";
        }
    }
    /**
     * Metode que actualitza les etiquetes
     *
     */
    private void fillLabels() {
        sumaRenglesInt();
        for (int i = 0; i < mesAlta.length; i++) {
            mesAlta[i]= comparator(instAcu[i][0],instAcu[i][1],instAcu[i][2],i);
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
        }
    }

    private void addCastell() {
        sqlcon.open();
        final List<String> castellersArray = new ArrayList<String>();
        final String nomCas = nomCastell.getText().toString();
        final String NomDB = sqlcon.getNomDB("tres");
        castellersArray.add("spib1");castellersArray.add(spib1.getSelectedItem().toString());
        castellersArray.add("spib2");castellersArray.add(spib2.getSelectedItem().toString());
        castellersArray.add("spib3");castellersArray.add(spib3.getSelectedItem().toString());
        castellersArray.add("spis1");castellersArray.add(spis1.getSelectedItem().toString());
        castellersArray.add("spis2");castellersArray.add(spis2.getSelectedItem().toString());
        castellersArray.add("spis3");castellersArray.add(spis3.getSelectedItem().toString());
        castellersArray.add("spit1");castellersArray.add(spit1.getSelectedItem().toString());
        castellersArray.add("spit2");castellersArray.add(spit2.getSelectedItem().toString());
        castellersArray.add("spit3");castellersArray.add(spit3.getSelectedItem().toString());
        castellersArray.add("spiq1");castellersArray.add(spiq1.getSelectedItem().toString());
        castellersArray.add("spiq2");castellersArray.add(spiq2.getSelectedItem().toString());
        castellersArray.add("spiq3");castellersArray.add(spiq3.getSelectedItem().toString());
        castellersArray.add("spic1");castellersArray.add(spic1.getSelectedItem().toString());
        castellersArray.add("spic2");castellersArray.add(spic2.getSelectedItem().toString());
        castellersArray.add("spic3");castellersArray.add(spic3.getSelectedItem().toString());
        castellersArray.add("spin1");castellersArray.add(spin1.getSelectedItem().toString());
        castellersArray.add("spin2");castellersArray.add(spin2.getSelectedItem().toString());
        castellersArray.add("spin3");castellersArray.add(spin3.getSelectedItem().toString());

        if (sqlcon.isNameExists("tress", nomCas)) {
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
        llistaCastells = sqlcon.getListCastells("tress");
    }

    private void deleteCastell(String castellidata) {
        sqlcon.open();
        String castell = castellidata.substring(0, castellidata.indexOf("/") - 3);
        sqlcon.deleteCastell("tress", castell);
        llistaCastells = sqlcon.getListCastells("tress");
        sqlcon.close();
        funcAdaptadorCastell();
    }

    private void escriuOsobreEscriuCastell(Boolean esborra, String castell,String NomDB, List<String> castellersArray) {
        sqlcon.open();
        if (esborra) {
            sqlcon.deleteCastell("tress", castell);
        }
        llistaCastells = sqlcon.getListCastells("tress");
        sqlcon.crearTaulaCastell(NomDB);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String data = dateFormat.format(date);
        sqlcon.addSqlCastell("tres", castell, data, NomDB);

        String[] spib1String = {"spib1", castellersArray.get(1)};
        String[] spib2String = {"spib2", castellersArray.get(3)};
        String[] spib3String = {"spib3", castellersArray.get(5)};
        String[] spis1String = {"spis1", castellersArray.get(7)};
        String[] spis2String = {"spis2", castellersArray.get(9)};
        String[] spis3String = {"spis3", castellersArray.get(11)};
        String[] spit1String = {"spit1", castellersArray.get(13)};
        String[] spit2String = {"spit2", castellersArray.get(15)};
        String[] spit3String = {"spit3", castellersArray.get(17)};
        String[] spiq1String = {"spiq1", castellersArray.get(19)};
        String[] spiq2String = {"spiq2", castellersArray.get(21)};
        String[] spiq3String = {"spiq3", castellersArray.get(23)};
        String[] spic1String = {"spic1", castellersArray.get(25)};
        String[] spic2String = {"spic2", castellersArray.get(27)};
        String[] spic3String = {"spic3", castellersArray.get(29)};
        String[] spin1String = {"spin1", castellersArray.get(31)};
        String[] spin2String = {"spin2", castellersArray.get(33)};
        String[] spin3String = {"spin3", castellersArray.get(35)};

        afegirCastelleraTaula(NomDB, spib1String);
        afegirCastelleraTaula(NomDB, spib2String);
        afegirCastelleraTaula(NomDB, spib3String);
        afegirCastelleraTaula(NomDB, spis1String);
        afegirCastelleraTaula(NomDB, spis2String);
        afegirCastelleraTaula(NomDB, spis3String);
        afegirCastelleraTaula(NomDB, spit1String);
        afegirCastelleraTaula(NomDB, spit2String);
        afegirCastelleraTaula(NomDB, spit3String);
        afegirCastelleraTaula(NomDB, spiq1String);
        afegirCastelleraTaula(NomDB, spiq2String);
        afegirCastelleraTaula(NomDB, spiq3String);
        afegirCastelleraTaula(NomDB, spic1String);
        afegirCastelleraTaula(NomDB, spic2String);
        afegirCastelleraTaula(NomDB, spic3String);
        afegirCastelleraTaula(NomDB, spin1String);
        afegirCastelleraTaula(NomDB, spin2String);
        afegirCastelleraTaula(NomDB, spin3String);

        llistaCastells = sqlcon.getListCastells("tress");
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
        ArrayList<String[]> allCastell = sqlcon.getAllCastell("tress",castell);
        spib1.setSelection(getPos(emplenarCasteller("spib1",allCastell),sBaixos));
        spib2.setSelection(getPos(emplenarCasteller("spib2",allCastell),sBaixos));
        spib3.setSelection(getPos(emplenarCasteller("spib3",allCastell),sBaixos));
        spis1.setSelection(getPos(emplenarCasteller("spis1",allCastell),sBaixos));
        spis2.setSelection(getPos(emplenarCasteller("spis2",allCastell),sBaixos));
        spis3.setSelection(getPos(emplenarCasteller("spis3",allCastell),sBaixos));
        spit1.setSelection(getPos(emplenarCasteller("spit1",allCastell),sSegons));
        spit2.setSelection(getPos(emplenarCasteller("spit2",allCastell),sSegons));
        spit3.setSelection(getPos(emplenarCasteller("spit3",allCastell),sSegons));
        spiq1.setSelection(getPos(emplenarCasteller("spiq1",allCastell),sTersos));
        spiq2.setSelection(getPos(emplenarCasteller("spiq2",allCastell),sTersos));
        spiq3.setSelection(getPos(emplenarCasteller("spiq3",allCastell),sTersos));
        spic1.setSelection(getPos(emplenarCasteller("spic1",allCastell),sQuarts));
        spic2.setSelection(getPos(emplenarCasteller("spic2",allCastell),sQuarts));
        spic3.setSelection(getPos(emplenarCasteller("spic3",allCastell),sQuarts));
        spin1.setSelection(getPos(emplenarCasteller("spin1",allCastell),sQuints));
        spin2.setSelection(getPos(emplenarCasteller("spin2",allCastell),sQuints));
        spin3.setSelection(getPos(emplenarCasteller("spin3",allCastell),sQuints));

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

