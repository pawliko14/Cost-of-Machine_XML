package main.java.GttDatabaseManipulate;


import main.java.Objetcs.Struktury;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PushMachineSubprojectToStructureDetail {
    List<Struktury> MachineStructure;
    private String Machine_Name;
    private List<String> Machine_list;

    public PushMachineSubprojectToStructureDetail(List<Struktury> machineStructure) {
        MachineStructure = new ArrayList<>();
        MachineStructure = machineStructure;
        Machine_list = new ArrayList<>();

    }


    public String getMachine_name()
    {
        return this.Machine_Name;
    }



    public void getAllSubprojects() throws SQLException {
        getMachinesListfromSUbprojectsTable();

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
        PreparedStatement sttmnt = null;

        sttmnt = connGTT.prepareStatement("SELECT MACHINENUMBER FROM Machine_subprojetcs ms\n" +
                "where STATUSCODE  = 'O' GROUP BY MACHINENUMBER  ");

        ResultSet resultSet = sttmnt.executeQuery();

        if(resultSet.next() == false)
        {
            System.out.println("there is no data in SUbProjects tabble");
        }

        else {
            while (resultSet.next()) {

                String Machinenumber = resultSet.getString("MACHINENUMBER");

                try {
                    Machine_list.add(Machinenumber);
                }
                catch(Exception e)
                {
                    System.out.println("cannot add Machine Number to list");
                }
            }
        }


        connGTT.close();
        resultSet.close();
        sttmnt.close();


    }

    /**
     * Retrieve all Machine from SUbprojects table
     */
    private void getMachinesListfromSUbprojectsTable() throws SQLException {
        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
        PreparedStatement sttmnt = null;
        
        sttmnt = connGTT.prepareStatement("SELECT MACHINENUMBER FROM Machine_subprojetcs ms\n" +
                "where STATUSCODE  = 'O' GROUP BY MACHINENUMBER  ");

        ResultSet resultSet = sttmnt.executeQuery();

        if(resultSet.next() == false)
        {
            System.out.println("there is no data in SUbProjects tabble");
        }

        else {
            while (resultSet.next()) {

                String Machinenumber = resultSet.getString("MACHINENUMBER");

                try {
                    Machine_list.add(Machinenumber);
                }
                catch(Exception e)
                {
                    System.out.println("cannot add Machine Number to list");
                }
            }
        }


        connGTT.close();
        resultSet.close();
        sttmnt.close();
    }


    /**
     * PUSH COLECTED DATA FROM SUBPROJECTS TO DATABASE
     */
    public void PushStructureToDatabase() throws SQLException {

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
        PreparedStatement sttmnt = null;

        for(int i = 0 ; i < this.MachineStructure.size();i++)
        {
            sttmnt= connGTT.prepareStatement("insert into machine_subprojetcs_structure_details \n" +
                    "(MACHINENUMBER ,MACHINESUBPROJECTNUMBER ,PARENTARTICLE ,CHILDARTICLE ,QUANTITY ,`TYPE` ,`LEVEL` )\n" +
                    "values(?,?,?,?,?,?,?)");

            try
            {
                sttmnt.setString(1, "temporary");
                sttmnt.setString(2, this.MachineStructure.get(i).getGlownyProjekt());
                sttmnt.setString(3, this.MachineStructure.get(i).getARTIKELCODE());
                sttmnt.setString(4, this.MachineStructure.get(i).getONDERDEEL());
                sttmnt.setDouble(5, this.MachineStructure.get(i).getILOSC());
                sttmnt.setString(6, this.MachineStructure.get(i).getTYP());
                sttmnt.setInt(7, this.MachineStructure.get(i).getPoziom());


                sttmnt.addBatch();
                sttmnt.executeBatch();


            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //      sttmnt.close();
        connGTT.close();

    }







}