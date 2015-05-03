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
public class TorreFragment extends Fragment {
    AuxClassStrings auxStrings;
    Spinner spib1, spib2, spis1, spis2, spit1, spit2, spiq1, spiq2, spic1, spic2, spinCastells;
    TextView lac, laq, lat, las, lab, ldc, ldq, ldt, lds, ldb;
    EditText nomCastell;
    SQLController sqlcon;
    ArrayList<String> sBaixos, sSegons, sTersos, sQuarts, sQuints, llistaCastells;
    ArrayList<Castellers> llistaComponents;
    Cursor c;
    int comptador = 0;
    int[][] instPis, instAcu;
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

        View rootView = inflater.inflate(R.layout.fragment_torre, container, false);

        llistaComponents = new ArrayList<Castellers>();

        auxStrings = new AuxClassStrings();
        instPis = new int[5][2];
        instAcu = new int[5][3];
        identificadors(rootView);

        //Inicialitzacio
        sBaixos = new ArrayList<String>();
        sSegons = new ArrayList<String>();
        sTersos = new ArrayList<String>();
        sQuarts = new ArrayList<String>();
        sQuints = new ArrayList<String>();
        sqlcon.open();
        llistaCastells = sqlcon.getListCastells("torres");
        sqlcon.close();
        emplenarStringsComponents();

        funcAdaptadors();
        funcAdaptadorCastell();

        ImageButton button = (ImageButton) rootView.findViewById(R.id.butAddTo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCastell();
            }
        });
        ImageButton buttondel = (ImageButton) rootView.findViewById(R.id.butDelTo);
        buttondel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCastell(spinCastells.getSelectedItem().toString());
            }
        });

        return rootView;
    }

    private void emplenarStringsComponents() {
        sqlcon.open();
        c = sqlcon.readEntryDibuix();
        int rows = c.getCount();
        c.moveToFirst();
        for (int i = 0; i < rows; i++) {
            Castellers vCasteller = new Castellers(c.getString(0), c.getString(1), Integer.parseInt(c.getString(2)));
            llistaComponents.add(vCasteller);
            c.moveToNext();
        }
        c.moveToFirst();
        sqlcon.close();

        for (Castellers cast : llistaComponents) {
            String vPosicio = cast.getPosicio();
            if (vPosicio.equals("Baixos")) {
                sBaixos.add(cast.getNom() + " " + cast.getEspatlla());
            } else if (vPosicio.equals("Segons")) {
                sSegons.add(cast.getNom() + " " + cast.getEspatlla());
            } else if (vPosicio.equals("Terços")) {
                sTersos.add(cast.getNom() + " " + cast.getEspatlla());
            } else if (vPosicio.equals("Quarts")) {
                sQuarts.add(cast.getNom() + " " + cast.getEspatlla());
            } else {
                sQuints.add(cast.getNom() + " " + cast.getEspatlla());
            }
        }

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

        adaptBaixos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spib1.setAdapter(adaptBaixos);
        spib2.setAdapter(adaptBaixos);
        spis1.setAdapter(adaptSegons);
        spis2.setAdapter(adaptSegons);
        spit1.setAdapter(adaptTersos);
        spit2.setAdapter(adaptTersos);
        spiq1.setAdapter(adaptQuarts);
        spiq2.setAdapter(adaptQuarts);
        spic1.setAdapter(adaptQuints);
        spic2.setAdapter(adaptQuints);


        spib1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spib1.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[0][0] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        spib2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spib2.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[0][1] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spis1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spis1.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[1][0] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spis2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spis2.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[1][1] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spit1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spit1.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[2][0] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spit2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spit2.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[2][1] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spiq1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spiq1.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[3][0] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spiq2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spiq2.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[3][1] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spic1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spic1.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[4][0] = cast.getEspatlla();
                                break;
                            }
                        }
                        sumaRenglesInt();
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spic2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        for (Castellers cast : llistaComponents) {
                            if (spic2.getSelectedItem().toString().contains(cast.getNom())) {
                                instPis[4][1] = cast.getEspatlla();
                                break;
                            }
                        }
                        fillLabels();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

    }

    private void identificadors(View rootView) {

        spib1 = (Spinner) rootView.findViewById(R.id.spib1);
        spib2 = (Spinner) rootView.findViewById(R.id.spib2);
        spis1 = (Spinner) rootView.findViewById(R.id.spis1);
        spis2 = (Spinner) rootView.findViewById(R.id.spis2);
        spit1 = (Spinner) rootView.findViewById(R.id.spit1);
        spit2 = (Spinner) rootView.findViewById(R.id.spit2);
        spiq1 = (Spinner) rootView.findViewById(R.id.spiq1);
        spiq2 = (Spinner) rootView.findViewById(R.id.spiq2);
        spic1 = (Spinner) rootView.findViewById(R.id.spic1);
        spic2 = (Spinner) rootView.findViewById(R.id.spic2);
        //TextView lac,laq,lat,las,lab,ldc,ldq,ldt,lds,ldb;
        lac = (TextView) rootView.findViewById(R.id.lblAltaQuints);
        laq = (TextView) rootView.findViewById(R.id.lblAltaQuarts);
        lat = (TextView) rootView.findViewById(R.id.lblAltaTersos);
        las = (TextView) rootView.findViewById(R.id.lblAltaSegons);
        lab = (TextView) rootView.findViewById(R.id.lblAltaBaixos);

        ldc = (TextView) rootView.findViewById(R.id.lblDifQuints);
        ldq = (TextView) rootView.findViewById(R.id.lblDifQuarts);
        ldt = (TextView) rootView.findViewById(R.id.lblDifTersos);
        lds = (TextView) rootView.findViewById(R.id.lblDifSegons);
        ldb = (TextView) rootView.findViewById(R.id.lblDifBaixos);

        spinCastells = (Spinner) rootView.findViewById(R.id.spinCastellsTo);
        nomCastell = (EditText) rootView.findViewById(R.id.nomCastell);

    }

    /**
     * Metode que suma les alçades d'una rengla i actualitza les taules
     */
    private void sumaRenglesInt() {
        instAcu[0][0] = instPis[0][0];
        instAcu[0][1] = instPis[0][1];
        instAcu[0][2] = instAcu[0][0] - instAcu[0][1];
        for (int i = 1; i < instAcu.length; i++) {
            instAcu[i][0] = instPis[i][0] + instAcu[i - 1][0];
            instAcu[i][1] = instPis[i][1] + instAcu[i - 1][1];
            instAcu[i][2] = instAcu[i][0] - instAcu[i][1];
        }

    }

    /**
     * Metode que compara les alçades
     *
     * @param a El valor de la diferencia
     * @return String amb la rengla més alta
     */
    private String comparator(int a) {
        if (a > 0) {
            return "A";
        } else if (a < 0) {
            return "B";
        } else {
            return "=";
        }
    }

    /**
     * Metode que actualitza les etiquetes
     */
    private void fillLabels() {
        lab.setText(comparator(instAcu[0][2]));
        las.setText(comparator(instAcu[1][2]));
        lat.setText(comparator(instAcu[2][2]));
        laq.setText(comparator(instAcu[3][2]));
        lac.setText(comparator(instAcu[4][2]));

        ldb.setText(Integer.toString(instAcu[0][2]));
        lds.setText(Integer.toString(instAcu[1][2]));
        ldt.setText(Integer.toString(instAcu[2][2]));
        ldq.setText(Integer.toString(instAcu[3][2]));
        ldc.setText(Integer.toString(instAcu[4][2]));
    }

    private void addCastell() {
        final List<String> castellersArray = new ArrayList<String>();
        final String nomCas = nomCastell.getText().toString();
        final String NomDB = sqlcon.getNomDB("torre");
        sqlcon.open();
        castellersArray.add("spib1");castellersArray.add(spib1.getSelectedItem().toString());
        castellersArray.add("spib2");castellersArray.add(spib2.getSelectedItem().toString());
        castellersArray.add("spis1");castellersArray.add(spis1.getSelectedItem().toString());
        castellersArray.add("spis2");castellersArray.add(spis2.getSelectedItem().toString());
        castellersArray.add("spit1");castellersArray.add(spit1.getSelectedItem().toString());
        castellersArray.add("spit2");castellersArray.add(spit2.getSelectedItem().toString());
        castellersArray.add("spiq1");castellersArray.add(spiq1.getSelectedItem().toString());
        castellersArray.add("spiq2");castellersArray.add(spiq2.getSelectedItem().toString());
        castellersArray.add("spic1");castellersArray.add(spic1.getSelectedItem().toString());
        castellersArray.add("spic2");castellersArray.add(spic2.getSelectedItem().toString());

        if (sqlcon.isNameExists("torres", nomCas)) {
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

    private boolean afegirCastelleraTaula(String nomTaula, String[] casteller) {
        boolean resultat;
        if (sqlcon.addSqlCasteller(nomTaula, casteller[1], casteller[0])<0) {
            resultat = false;
        }
        else {
            resultat = true;
        }
        llistaCastells = sqlcon.getListCastells("torres");
        return resultat;
    }

    private void deleteCastell(String castellidata) {
        sqlcon.open();
        String castell = castellidata.substring(0, castellidata.indexOf("/") - 3);
        sqlcon.deleteCastell("torres", castell);
        llistaCastells = sqlcon.getListCastells("torres");
        sqlcon.close();
        funcAdaptadorCastell();
    }

    private void escriuOsobreEscriuCastell(Boolean esborra, String castell,String NomDB, List<String> castellersArray) {
        sqlcon.open();
        if (esborra) {
            sqlcon.deleteCastell("torres", castell);
        }
        llistaCastells = sqlcon.getListCastells("torres");
        sqlcon.crearTaulaCastell(NomDB);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String data = dateFormat.format(date);
        sqlcon.addSqlCastell("torre", castell, data, NomDB);

        String[] spib1String = {"spib1", castellersArray.get(1)};
        String[] spib2String = {"spib2", castellersArray.get(3)};
        String[] spis1String = {"spis1", castellersArray.get(5)};
        String[] spis2String = {"spis2", castellersArray.get(7)};
        String[] spit1String = {"spit1", castellersArray.get(9)};
        String[] spit2String = {"spit2", castellersArray.get(11)};
        String[] spiq1String = {"spiq1", castellersArray.get(13)};
        String[] spiq2String = {"spiq2", castellersArray.get(15)};
        String[] spic1String = {"spic1", castellersArray.get(17)};
        String[] spic2String = {"spic2", castellersArray.get(19)};

        afegirCastelleraTaula(NomDB, spib1String);
        afegirCastelleraTaula(NomDB, spib2String);
        afegirCastelleraTaula(NomDB, spis1String);
        afegirCastelleraTaula(NomDB, spis2String);
        afegirCastelleraTaula(NomDB, spit1String);
        afegirCastelleraTaula(NomDB, spit2String);
        afegirCastelleraTaula(NomDB, spiq1String);
        afegirCastelleraTaula(NomDB, spiq2String);
        afegirCastelleraTaula(NomDB, spic1String);
        afegirCastelleraTaula(NomDB, spic2String);
        llistaCastells = sqlcon.getListCastells("torres");
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
        ArrayList<String[]> allCastell = sqlcon.getAllCastell("torres",castell);
        spib1.setSelection(getPos(emplenarCasteller("spib1",allCastell),sBaixos));
        spib2.setSelection(getPos(emplenarCasteller("spib2",allCastell),sBaixos));
        spis1.setSelection(getPos(emplenarCasteller("spis1",allCastell),sSegons));
        spis2.setSelection(getPos(emplenarCasteller("spis2",allCastell),sSegons));
        spit1.setSelection(getPos(emplenarCasteller("spit1",allCastell),sTersos));
        spit2.setSelection(getPos(emplenarCasteller("spit2",allCastell),sTersos));
        spiq1.setSelection(getPos(emplenarCasteller("spiq1",allCastell),sQuarts));
        spiq2.setSelection(getPos(emplenarCasteller("spiq2",allCastell),sQuarts));
        spic1.setSelection(getPos(emplenarCasteller("spic1",allCastell),sQuints));
        spic2.setSelection(getPos(emplenarCasteller("spic2",allCastell),sQuints));



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

