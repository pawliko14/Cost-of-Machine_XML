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
                String GlownyProject = this.MachineStructure.get(i).getGlownyProjekt();
                String Artikelcode = this.MachineStructure.get(i).getARTIKELCODE();
                String OnderDeel = this.MachineStructure.get(i).getONDERDEEL();
                double ilosc = this.MachineStructure.get(i).getILOSC();
                String Type = this.MachineStructure.get(i).getTYP();
                int poziom = this.MachineStructure.get(i).getPoziom();

                // handle nulls
                if(GlownyProject == null || GlownyProject.equals("")) {
                    GlownyProject = "null";
                }
                if(Artikelcode == null || Artikelcode.equals("")) {
                    Artikelcode = "null";
                }
                if(OnderDeel == null || OnderDeel.equals("")) {
                    OnderDeel = "null";
                }
                if(ilosc == 0) {
                    ilosc = 0;
                }
                if(Type == null || Type.equals("")) {
                    Type = "N";
                }

                sttmnt.setString(1, GlownyProject);
                sttmnt.setString(2, Artikelcode);
                sttmnt.setString(3, OnderDeel);
                sttmnt.setDouble(4, ilosc);
                sttmnt.setString(5, Type);
                sttmnt.setInt(6, poziom);


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
