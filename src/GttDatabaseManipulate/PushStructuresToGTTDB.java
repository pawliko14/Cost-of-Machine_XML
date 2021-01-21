package GttDatabaseManipulate;

import costofmachine.Struktury;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PushStructuresToGTTDB {

    List<Struktury> MachineStructure;
    List<String> OpenedMachines;

    public PushStructuresToGTTDB(ArrayList<Struktury> MS)
    {
        MachineStructure = new ArrayList<>();
        OpenedMachines = new ArrayList<>();
        MachineStructure = MS;
    }

    public void PushOpenProjectListTODB() throws SQLException {



        // get current state of projects
        this.GetListOfOpenedProjects();

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");

        // truncate existing data  for replacing

        PreparedStatement sttmnt = null;
        sttmnt = connGTT.prepareStatement("Truncate table Machine");
        sttmnt.addBatch();
        sttmnt.executeBatch();


        // add listofOpenProjects

        PreparedStatement sttmnt_2 = null;
        for(int  i = 0 ; i < OpenedMachines.size() ; i ++)
        {
            sttmnt =connGTT.prepareStatement("insert into Machine (MACHINENUMBER ) values (?)");
            sttmnt.setString(1, this.OpenedMachines.get(i));



            sttmnt.addBatch();
            sttmnt.executeBatch();
        }


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
