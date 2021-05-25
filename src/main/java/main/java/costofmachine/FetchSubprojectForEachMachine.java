package main.java.costofmachine;

import main.java.GttDatabaseManipulate.PushMachineSubprojectToStructureDetail;
import main.java.Objetcs.Struktury;
import main.java.Parameters.Parameters;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;

public class FetchSubprojectForEachMachine {

    static String GlownyProjektDlaArtykulu = "";
    private static Map<String,String> ListaGlownychZlozenIPodzlozen;
    private static ArrayList<Struktury> ListofStructuresTest;
    private static int iloscZaglebien = 0;
    private static Double CalosciowaCenaPracy = 0.0;
    private static Double CalosciowaCenaKonstrukcja = 0.0;
    private static Double CalosciowaCenaMaterialu = 0.0;
    private static int CenaPracoGodziny = 120; // kiedys, dowiedziec sie kiedy cena pracy to bylo 100zl, aktualnie jest 120zl

    public FetchSubprojectForEachMachine()
    {

    }





    public static void run(String Maszynka) throws IOException, SQLException {
        Connection conn= DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");



        try {
            ListofStructuresTest = new ArrayList<Struktury>();
            ListaGlownychZlozenIPodzlozen = new LinkedHashMap<String,String>(); // LinkedHashMap - preserver the insertion order, have to used Linked one
            getListaGLownychZlozen(Maszynka,conn);

            addHighestLevelArticles(ListaGlownychZlozenIPodzlozen,conn, Maszynka);

            Set<Map.Entry<String,String>> entrySet = ListaGlownychZlozenIPodzlozen.entrySet();
            int it = 0;
            for(Map.Entry<String, String> entry: entrySet) {
                GlownyProjektDlaArtykulu = entry.getValue();
                System.out.println(" "+ it + ": " + entry.getKey() + " : " + entry.getValue());
                GetAllArticelInProject(entry.getKey(),conn,GlownyProjektDlaArtykulu);
                iloscZaglebien= 0; // reset deppth of the structure
                it++;
            }
            ShowAll();

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        PushMachineSubprojectToStructureDetail machineSUbProjectPusher = new PushMachineSubprojectToStructureDetail(ListofStructuresTest);
        machineSUbProjectPusher.PushStructureToDatabase();



        conn.close();
        System.out.println("done");
    }

    private static void addHighestLevelArticles(Map<String, String> listaGlownychZlozenIPodzlozen, Connection conn, String maszynka) throws SQLException {

        Struktury StrukturaTmp;
        PreparedStatement pstmnt= null;


        Set<Map.Entry<String,String>> entrySet = ListaGlownychZlozenIPodzlozen.entrySet();
        for(Map.Entry<String, String> entry: entrySet) {
            String f_article = entry.getKey();

            String sql = "select seq,ARTIKELCODE,ONDERDEEL,CFOMSONDERDEEL,TYP,ILOSC,JEDNOSTKA \n" +
                    "from struktury \n" +
                    "where ONDERDEEL = ? \n" +
                    "and ARTIKELCODE  = ? \n" +
                    "order by seq";

            pstmnt = conn.prepareStatement(sql);
            pstmnt.setString(1,f_article);
            pstmnt.setString(2,maszynka);


            ResultSet rs2 = pstmnt.executeQuery();


            while(rs2.next())
            {
                StrukturaTmp = new Struktury(-1); // -1 temporary, this article should be at the top of all other articles (eq. -> 0 )

                StrukturaTmp.setGlownyProjekt(maszynka);
                StrukturaTmp.setSeq(rs2.getString("seq"));
                StrukturaTmp.setARTIKELCODE(rs2.getString("ARTIKELCODE"));
                StrukturaTmp.setONDERDEEL(rs2.getString("ONDERDEEL"));
                StrukturaTmp.setCFOMSONDERDEEL(rs2.getString("CFOMSONDERDEEL"));
                StrukturaTmp.setTYP(rs2.getString("TYP"));
                StrukturaTmp.setILOSC(rs2.getDouble("ILOSC"));

                ListofStructuresTest.add(StrukturaTmp);
            }
            rs2.close();

        }
        pstmnt.close();

    }

    private static void ShowAll() throws FileNotFoundException {

        try (PrintStream out = new PrintStream(new FileOutputStream(Parameters.getPathOfSavingNomenclatuurTxt())))
        {
            for(int i = 0 ; i <ListofStructuresTest.size();i++ )
            {
                out.println("---Poziom---: " + ListofStructuresTest.get(i).getPoziom());
                out.println("Glowny Projekt: " + ListofStructuresTest.get(i).getGlownyProjekt());
                out.println("Seqwencja: " + ListofStructuresTest.get(i).getSeq());
                out.println("Artikel Nadrzedny: " + ListofStructuresTest.get(i).getARTIKELCODE());
                out.println("Artikel: " + ListofStructuresTest.get(i).getONDERDEEL());
                out.println("Opis Artikel: " + ListofStructuresTest.get(i).getCFOMSONDERDEEL());
                out.println("TYP: " + ListofStructuresTest.get(i).getTYP());
                out.println("ILOSC: " + ListofStructuresTest.get(i).getILOSC());
                out.println("JEDNOSTKA: " + ListofStructuresTest.get(i).getJEDNOSTKA());
                out.println("Czy sprawdzony?: " + ListofStructuresTest.get(i).isCzySprawdzony());
                out.println("CenaMaterialu: " + ListofStructuresTest.get(i).getCenaMaterialu());
                out.println("CenaMaterialu Razy Ilosc: " + ListofStructuresTest.get(i).getCenaMaterialuRazyIlosc());
                out.println("Cena Pracy Za sztuke: " + ListofStructuresTest.get(i).getCenaPracy());
                out.println("Cena Pracy Razy Ilosc: " + ListofStructuresTest.get(i).getCenaPracyRazyIlosc());
                out.println("                              ");
            }
        }

    }



    public static void GetAllArticelInProject(String articlecode,Connection conn, String GlownyProjekt) throws SQLException{

        Statement b = conn.createStatement();
        ResultSet rs2 = b.executeQuery("select seq,ARTIKELCODE,ONDERDEEL,CFOMSONDERDEEL,TYP,ILOSC,JEDNOSTKA from struktury where ARTIKELCODE = '"+articlecode+"' order by seq");
        Struktury StrukturaTmp;


        while(rs2.next()){

            StrukturaTmp = new Struktury(iloscZaglebien); // increase depth of the structure
            StrukturaTmp.setGlownyProjekt(GlownyProjekt);
            StrukturaTmp.setSeq(rs2.getString("seq"));
            StrukturaTmp.setARTIKELCODE(rs2.getString("ARTIKELCODE"));
            StrukturaTmp.setONDERDEEL(rs2.getString("ONDERDEEL"));
            StrukturaTmp.setCFOMSONDERDEEL(rs2.getString("CFOMSONDERDEEL"));
            StrukturaTmp.setTYP(rs2.getString("TYP"));
            StrukturaTmp.setILOSC(Double.parseDouble(rs2.getString("ILOSC")));
            StrukturaTmp.setJEDNOSTKA(rs2.getString("JEDNOSTKA"));


            if(rs2.getString("TYP") != null && !rs2.getString("TYP").isEmpty()) {
                String typeFrom = PushValidTypeForHigherLevelOfStructures(articlecode, GlownyProjekt ,conn);
                StrukturaTmp.setTYP_Nadrzednego(typeFrom);
            }
            else {
                StrukturaTmp.setTYP_Nadrzednego("WRONG"); // if the type is not known set is as Y
            }






            ListofStructuresTest.add(StrukturaTmp);

            if(rs2.getString("TYP") != null && !rs2.getString("TYP").isEmpty()) {
                if( rs2.getString("typ").equals("Y") ||rs2.getString("typ").equals("A") || rs2.getString("typ").equals("F")||rs2.getString("onderdeel").startsWith("%") || rs2.getString("typ").equals("P")){
                    iloscZaglebien++;

                    //recursives
                    GetAllArticelInProject(rs2.getString("onderdeel"),conn, GlownyProjekt);
                }
            }
        }

        iloscZaglebien -=  1;
    }


    private static String PushValidTypeForHigherLevelOfStructures(String articlecode, String onderdeel,Connection conn) throws SQLException {

        String results = null;

        Statement b = conn.createStatement();
        ResultSet rs2 = b.executeQuery("select TYP from struktury s \r\n" +
                "	where ONDERDEEL  = '"+articlecode+"'\r\n" +
                "	and ARTIKELCODE  = '"+onderdeel+"'");

        if(rs2.next())
        {
            results = rs2.getString("TYP");
        }

        return results;
    }


    public static void getListaGLownychZlozen(String art, Connection conn) throws SQLException
    {
        Statement b = conn.createStatement();
        //	ResultSet rs2 = b.executeQuery("select ARTIKELCODE, ONDERDEEL from struktury where ARTIKELCODE like '"+art+"%' order by ARTIKELCODE asc ");


        //temporary
        ResultSet rs2 = b.executeQuery("select ARTIKELCODE, ONDERDEEL from struktury where ARTIKELCODE = '"+art+"' order by SEQ asc ");


        while(rs2.next()){
            ListaGlownychZlozenIPodzlozen.put(rs2.getString("ONDERDEEL"), rs2.getString("ARTIKELCODE"));
        }
        b.close();
        rs2.close();

    }






}
