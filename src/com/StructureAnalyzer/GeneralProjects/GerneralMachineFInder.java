package com.StructureAnalyzer.GeneralProjects;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GerneralMachineFInder {

    private List<MainMachine> listOfProjects;
    private SqlEntries sqlEntries;

    public GerneralMachineFInder() {
        this.listOfProjects =  new ArrayList<>();
        sqlEntries = new SqlEntries();
    }


    /**
     *
     * finding all opened projects, some of them could be even 'C', but in further filtration
     * most of them are removed
     *
     * @return
     * @throws SQLException
     */
    private List<MainMachine> findAllOpenedProject( ) throws SQLException {
        return sqlEntries.fetchAllOpenedMachines();
    }


    private List<MainMachine> filterOpenedProjectOverStorenotes(List<MainMachine> allOpenedProjects) throws SQLException {
        return sqlEntries.filterOverStorenotes(allOpenedProjects);
    }




    public List<MainMachine> doOperations() throws SQLException {

        List<MainMachine> allOpenedProject = findAllOpenedProject();
        List<MainMachine> mainMachinesFiltered = filterOpenedProjectOverStorenotes(allOpenedProject);


        /*
        CAREFUL:
        this filter returns full list of opened projects, that should appear in gtt excel file
        some of them should not exist for instance 39/2019

        in further analyze this should be fixed!

         */
        List<MainMachine> sortedListByLeverancier  =  mainMachinesFiltered.stream()
                .sorted(Comparator.comparing(s -> s.getLeverancier()))
                .collect(Collectors.toList());


        System.out.println("finished, returning list to further processes");

        return sortedListByLeverancier;
    }

}
