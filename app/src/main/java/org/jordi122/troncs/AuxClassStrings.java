package org.jordi122.troncs;

import java.util.ArrayList;

/**
 * Created by Jordi.Martinez on 07/08/2014.
 */
public class AuxClassStrings{

    /**
     * Method that deletes all digits from a String
     *
     * @param vString The string to be modified
     * @return The modified String withiut digits
     */
    public String deleteDigits(String vString) {
        vString = vString.replaceAll("[0-9]","");
        return vString;
    }

    /**
     * Method that deletes all white spaces from a String
     *
     * @param vString The string to be modified
     * @return The modified String withiut white spaces
     */
    public String deleteWhiteSpaces(String vString) {
        vString = vString.replaceAll("\\s+","");
        return vString;
    }

    /**
     * Method that deletes all letters from a String
     *
     * @param vString The string to be modified
     * @return The modified String withiut letters
     */
    public String deleteLetters(String vString) {
        vString = vString.replaceAll("[^\\d.]", "");
        return vString;
    }
    /**
     * Method that deletes all points from a String
     *
     * @param vString The string to be modified
     * @return The modified String withiut letters
     */
    public String deletePoints(String vString) {
        vString = vString.replaceAll("\\.", "");
        return vString;
    }

    /**
     * Method that sums two Strings returning another string
     *
     * @param listStrings List of strings to be summed
     * @return The sum in String format
     */
    public String sumaString(ArrayList<String> listStrings) {
        //Initialization arrayList
        String tempString;
        //Delete all letters and white spaces
        for (int i = 0; i < listStrings.size(); i++) {
            tempString = listStrings.get(i);
            tempString = deleteLetters(tempString);
            tempString = deleteWhiteSpaces(tempString);
            listStrings.set(i,tempString);
        }

        //Variable to store integers and the result
        int[] listInt = new int[listStrings.size()];
        int sum = 0;

        // Convert the list of strings to a list of ints
        for (int i = 0; i < listStrings.size(); i++) {
            listInt[i] = Integer.parseInt(listStrings.get(i));
        }

        // Make the trick
        for (int i = 0; i < listInt.length ; i++) {
            sum += listInt[i];
        }

        return Integer.toString(sum);
    }





}