package GttDatabaseManipulate;

import costofmachine.Struktury;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PushStructuresToGTTDB {

    List<Struktury> MachineStructure;
    List<String> OpenedMachines;
    List<String> SubProjects;

    public PushStructuresToGTTDB(ArrayList<Struktury> MS)
    {
        MachineStructure = new ArrayList<>();
        OpenedMachines = new ArrayList<>();
        SubProjects = new ArrayList<>();
        MachineStructure = MS;
    }



    public void PushSubProjectsToDB() throws SQLException {

        if(this.OpenedMachines.isEmpty()) {
            System.out.println("Empty list of projects, cannot proceed");
        }
        else {

            // clear table of contents of  Machine Subprojetcs

            Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
            Connection connFATDB = DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb", "listy", "listy1234");



            // truncate existing data  for replacing
            PreparedStatement sttmnt = null;
                sttmnt = connGTT.prepareStatement("Truncate table Machine_subprojetcs");
                sttmnt.addBatch();
                sttmnt.executeBatch();
            sttmnt.close();

            // Add new subprojects to table based on OpenedMachines list
            PreparedStatement sttmnt_2 = null;
            for(int i = 0 ;i< this.OpenedMachines.size(); i++)
            {

                /*
                Adnotation - there must be '?_%' where '_' means there should occur next sign, for instance
                taking machine 200520, it will print all subprojects without 200520 itself
                 */
                    sttmnt_2 =connFATDB.prepareStatement("select ORDERNUMMER,STATUSCODE from bestelling b\n" +
                    "                where ORDERNUMMER  like ?\n" +
                    "                and (leverancier  = '2' or leverancier  ='5' or leverancier  ='6')\n" +
                    "                group by ORDERNUMMER");

               sttmnt_2.setString(1, this.OpenedMachines.get(i) + "_%");
                ResultSet resultSet = sttmnt_2.executeQuery();

                if(resultSet.next() == false)
                {
                    System.out.println("there are no subprojects for : " + OpenedMachines.get(i) );
                }
                else {

                    PreparedStatement sttmnt_3 = null;
                    while (resultSet.next()) {
                        String ORDERNUMMER = resultSet.getString("ORDERNUMMER");
                        String STATUSCODE = resultSet.getString("STATUSCODE");

                        //push data to database -> Machine_subprojetcs
                        sttmnt_3 = connGTT.prepareStatement("insert into Machine_subprojetcs(MACHINENUMBER ,MACHINENUMBERSUBPROJECT,STATUSCODE) values (?,?,?)");
                        sttmnt_3.setString(1, this.OpenedMachines.get(i));
                        sttmnt_3.setString(2, ORDERNUMMER);
                        sttmnt_3.setString(3, STATUSCODE);



                        sttmnt_3.addBatch();
                        sttmnt_3.executeBatch();
                    }
              //      sttmnt_3.close();
                }



            }



            sttmnt.close();
            connFATDB.close();
            connGTT.close();
        }


        System.out.println("pushing subprojects done");
    }

    /**
     * @throws SQLException
     */
    public void PushOpenProjectListTODB() throws SQLException {



        // get current state of projects
        this.GetListOfOpenedProjects();

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");

        // truncate existing data  for replacing

        PreparedStatement sttmnt = null;
            sttmnt = connGTT.prepareStatement("Truncate table Machine");
            sttmnt.addBatch();
            sttmnt.executeBatch();
        sttmnt.close();

        // add listofOpenProjects

        PreparedStatement sttmnt_2 = null;
        for(int  i = 0 ; i < OpenedMachines.size() ; i ++)
        {
            sttmnt_2 =connGTT.prepareStatement("insert into Machine (MACHINENUMBER ) values (?)");
            sttmnt_2.setString(1, this.OpenedMachines.get(i));



            sttmnt_2.addBatch();
            sttmnt_2.executeBatch();
        }
        sttmnt_2.close();

        connGTT.close();
    }


    private void GetListOfOpenedProjects() throws SQLException {


        Connection conn=DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");
        PreparedStatement sttmnt = null;

        sttmnt = conn.prepareStatement("SELECT AFDELINGSEQ  FROM BESTELLING b \n" +
                "WHERE STATUSCODE  = 'O'\n" +
                "and (AFDELING  =2 or AFDELING =5 or AFDELING = 14 or AFDELING = 7)\n" +
                "GROUP BY AFDELINGSEQ ");

        ResultSet resultSet = sttmnt.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("AFDELINGSEQ");
            this.OpenedMachines.add(name);



        }
        sttmnt.close();
        resultSet.close();
        conn.close();
    }



    private void PushStructureToDatabase() throws SQLException {

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
        PreparedStatement sttmnt = null;

        for(int i = 0 ; i < this.MachineStructure.size();i++)
        {
            sttmnt= connGTT.prepareStatement("insert into machine_structure_details (MACHINENUMBER ,PARENTARTICLE ,CHILDARTICLE ,QUANTITY ,`TYPE` ,`LEVEL` )\r\n" +
                    "values (?,?,?,?,?,?)");

            try
            {

                sttmnt.setString(1, this.MachineStructure.get(i).getGlownyProjekt());
                sttmnt.setString(2, this.MachineStructure.get(i).getARTIKELCODE());
                sttmnt.setString(3, this.MachineStructure.get(i).getONDERDEEL());
                sttmnt.setInt(4, 0); // temporary 0 as there is no quantity yet
                sttmnt.setString(5, this.MachineStructure.get(i).getTYP());
                sttmnt.setInt(6, this.MachineStructure.get(i).getPoziom());


                sttmnt.addBatch();
                sttmnt.executeBatch();


                // rows affected
                System.out.println("done for: " + i);

            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sttmnt.close();
        connGTT.close();

    }











 // test
    private  void insertionTest(Connection connection) {

        String SQL_INSERT = "insert into Machine (ID,MACHINENUMBER ) values (?,?)";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {
            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "21090");


            int row = preparedStatement.executeUpdate();
            System.out.println(row); //1

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
