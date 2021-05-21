package main.java.GttDatabaseManipulate;
import main.java.Objetcs.Struktury;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PushMachineTOStuctureDetail {

    List<Struktury> MachineStructure;

    public PushMachineTOStuctureDetail(List<Struktury> machineStructure) {
        MachineStructure = new ArrayList<>();
        MachineStructure = machineStructure;
    }


    public void PushStructureToDatabase() throws SQLException {

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
        PreparedStatement sttmnt = null;

        for(int i = 0 ; i < this.MachineStructure.size();i++)
        {
            sttmnt= connGTT.prepareStatement("insert into machine_structure_details (MACHINENUMBER ,PARENTARTICLE ,ARTICLE ,QUANTITY ,`TYPE` ,`LEVEL` )\r\n" +
                    "values (?,?,?,?,?,?)");

            try
            {
                sttmnt.setString(1, this.MachineStructure.get(i).getGlownyProjekt());
                sttmnt.setString(2, this.MachineStructure.get(i).getARTIKELCODE());
                sttmnt.setString(3, this.MachineStructure.get(i).getONDERDEEL());
                sttmnt.setDouble(4, this.MachineStructure.get(i).getILOSC());
                sttmnt.setString(5, this.MachineStructure.get(i).getTYP());
                sttmnt.setInt(6, this.MachineStructure.get(i).getPoziom());


                sttmnt.addBatch();
                sttmnt.executeBatch();

                System.out.println("done for: " + this.MachineStructure.get(i).getONDERDEEL());

            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sttmnt.close();
        connGTT.close();
    }


}
