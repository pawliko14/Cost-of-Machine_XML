package main.java.costofmachine;

import main.java.Objetcs.OpenedMachines;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FetchMachineAndExistingStructure {

    private List<OpenedMachines> openedMachines;

    public void FetchDataFromGTTDB() throws SQLException {

        Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
        PreparedStatement sttmnt = null;

        sttmnt = connGTT.prepareStatement("select MACHINENUMBER ,EXISTINGSTRUCTURE from Machine m ");
        ResultSet resultSet = sttmnt.executeQuery();



        while (resultSet.next()) {

            String machineNumber = resultSet.getString("MACHINENUMBER");
            boolean existanceOfStructure = resultSet.getBoolean("EXISTINGSTRUCTURE");

            OpenedMachines obj = new OpenedMachines(machineNumber, existanceOfStructure);

            if( openedMachines != null) {
                this.openedMachines.add(obj);
            }

        }
        sttmnt.close();
        resultSet.close();
        connGTT.close();

    }




    @Override
    public String toString() {
        return "FetchMachineAndExistingStructure{" +
                "openedMachines=" + openedMachines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FetchMachineAndExistingStructure that = (FetchMachineAndExistingStructure) o;
        return Objects.equals(openedMachines, that.openedMachines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openedMachines);
    }

    public List<OpenedMachines> getOpenedMachines() {
        return openedMachines;
    }

    public void setOpenedMachines(List<OpenedMachines> openedMachines) {
        this.openedMachines = openedMachines;
    }

    public FetchMachineAndExistingStructure() {
        this.openedMachines = new ArrayList<>();
    }
}
