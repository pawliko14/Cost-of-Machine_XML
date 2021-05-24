package com.StructureAnalyzer.GeneralProjects;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlEntries {



    public List<MainMachine> fetchAllOpenedMachines() throws SQLException {

        List<MainMachine> mainMachines = new ArrayList<>();

        Connection conn= DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");
        PreparedStatement sttmnt = null;

        /*
        StatusCode must be also 'C' because of project :
            36/2019 - this project appears on partsoverview list
         */
        sttmnt = conn.prepareStatement("select leverancier ,ORDERNUMMER \n" +
                "from bestelling b \n" +
                "where (STATUSCODE  = 'O' or STATUSCODE = 'C')\n" +
                "and leverancier  < 50\n" +
                "group by LEVERANCIERORDERNUMMER ");

        ResultSet rs = sttmnt.executeQuery();


        if (rs.next() == false) {
            System.out.println("there is no such record, proceed : ");

        } else {
            do {
            String leverancier = rs.getString("leverancier");
            String ordernummer = rs.getString("ORDERNUMMER");

            MainMachine mainMachine = new MainMachine(leverancier, ordernummer);
              mainMachines.add(mainMachine);

            } while (rs.next());
        }


        sttmnt.close();
        rs.close();
        conn.close();



        return mainMachines;
    }

    public List<MainMachine>  filterOverStorenotes(List<MainMachine> allOpenedProject) throws SQLException {

        List<MainMachine> returningList = new ArrayList<>();

        for(MainMachine mm : allOpenedProject) {

            String leverancier = mm.getLeverancier();
            String ordernummer = mm.getOrdernummer();

           boolean doesBestellingExists =  CheckIfBestellingDetailExist(leverancier,ordernummer);
           boolean  doesStoreNoteExists ;

            if(doesBestellingExists) {
                    if(filterOverWerkbon(leverancier,ordernummer) == true) {
                        doesStoreNoteExists =   retriveInformationFromStorenoteExists(leverancier,ordernummer);

                        if(doesStoreNoteExists == true) {
                            returningList.add(mm);
                        }
                    }
            }
        }


        return returningList;
    }

    private boolean retriveInformationFromStorenoteExists(String leverancier, String ordernummer) throws SQLException {
        boolean result= false;

        if(leverancier.length() == 0 || ordernummer.length() == 0) {
            System.out.println("LeverancierOrderNUmmer Does not exist, return false");
            return false;
        }

        Connection conn= DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");

        String sql = "select Leverancier ,ORDERNUMMER  from storenotesdetail s2b2s2b2s2b2s \n" +
                "where AFDELING  = ? \n" +
                "and AFDELINGSEQ  =  ? \n" +
                "and BESTELD  <> GELEVERD ";


        PreparedStatement sttmnt = conn.prepareStatement(sql);

        sttmnt.setString(1,leverancier);
        sttmnt.setString(2,ordernummer);

        ResultSet rs = sttmnt.executeQuery();


        if (rs.next() == false) {
            result = false;

        } else {
            do {
                result = true;
            } while (rs.next());
        }

        sttmnt.close();
        rs.close();
        conn.close();


        return result;
    }




    private boolean CheckIfBestellingDetailExist(String leverancier, String ordernummer) throws SQLException {

        boolean result= false;

        if(leverancier.length() == 0 || ordernummer.length() == 0) {
            System.out.println("LeverancierOrderNUmmer Does not exist, return false");
            return false;
        }

        Connection conn= DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");

        String sql = "select leverancier ,ORDERNUMMER  from bestelling b2s2b2s2b2s \n" +
                "where Leverancier  = ? \n" +
                "and ORDERNUMMER  =  ? \n" +
                "limit 1 ";

        PreparedStatement sttmnt = conn.prepareStatement(sql);


        sttmnt.setString(1,leverancier);
        sttmnt.setString(2,ordernummer);

        ResultSet rs = sttmnt.executeQuery();


        if (rs.next() == false) {
            result = false;

        } else {
            do {
                result = true;
            } while (rs.next());
        }

        sttmnt.close();
        rs.close();
        conn.close();


        return result;
    }


    public boolean filterOverWerkbon(String leverancier, String ordernummer) throws SQLException {

        boolean result= false;

        if(leverancier.length() == 0 || ordernummer.length() == 0) {
            System.out.println("leverancier ordernummer dont exist, cannot proceed with werkbonnumer checking");
            return false;
        }

        Connection conn= DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");

        String sql = "select WERKBONNUMMER from werkbon w \n" +
                "where afdeling  = ? \n" +
                "and AFDELINGSEQ = ? \n"  +
                "and STATUS <> '90' ";

        PreparedStatement sttmnt = conn.prepareStatement(sql);


        sttmnt.setString(1,leverancier);
        sttmnt.setString(2,ordernummer);

        ResultSet rs = sttmnt.executeQuery();


        if (rs.next() == false) {
            result = false;

        } else {
            do {
                result = true;
            } while (rs.next());
        }

        sttmnt.close();
        rs.close();
        conn.close();


        return result;
    }
}
