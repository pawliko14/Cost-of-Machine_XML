package main.java.StartingClass;

import java.awt.EventQueue;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import main.java.GttDatabaseManipulate.PushStructuresToGTTDB;
import main.java.Objetcs.OpenedMachines;
import main.java.costofmachine.FetchDataForEachMachine;
import main.java.costofmachine.FetchMachineAndExistingStructure;
import main.java.costofmachine.FetchSubprojectForEachMachine;


public class Main {


	// set to False if need real data from database
	private static 	boolean testingPurpose = false;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					//push available projects to database
					PushStructuresToGTTDB DBPusherGTT = new PushStructuresToGTTDB();


					// push Projects and Subprojects to database
					DBPusherGTT.PushOpenProjectListTODB();
					DBPusherGTT.PushSubProjectsToDB();


					// retrive information about machines structure existance
					FetchMachineAndExistingStructure fetchMachineAndExistingStructure = new FetchMachineAndExistingStructure();
						fetchMachineAndExistingStructure.FetchDataFromGTTDB();
					List<OpenedMachines> openedMachines = fetchMachineAndExistingStructure.getOpenedMachines();

					// filter openedMachines where structure/nomenclatuur exists
					List<OpenedMachines> collect = openedMachines.stream()
							.filter(s -> s.isExistanceInNomenclatuur() == true)
							.collect(Collectors.toList());


					PushGeneralProjectsStructuresToDatabase(DBPusherGTT,collect);


					// CAREFUL - truncate previous data, replace with new one, takes ~~30min to finish this algorythm
				//	PushSubprojectsStructuresToDatabase(DBPusherGTT);



				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		});
	}

	private static void PushGeneralProjectsStructuresToDatabase(PushStructuresToGTTDB DBPusherGTT, List<OpenedMachines> collect) throws IOException, SQLException {

		if(testingPurpose)
		{
			List<String> machines_remporary  = Arrays.asList("20052020");

			//clean - TRUNCATE - project table
			DBPusherGTT.TruncateStructuresTable();


			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < machines_remporary.size(); i++) {
				FetchDataForEachMachine.run(machines_remporary.get(i));
			}
		}
		else {

			//clean - TRUNCATE - project table
			DBPusherGTT.TruncateStructuresTable();

/*		previous version

			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < DBPusherGTT.getListOfOpenedMachines().size(); i++) {
				FetchDataForEachMachine.run(DBPusherGTT.getListOfOpenedMachines().get(i));
			}
			*/

			for (int i = 0; i < collect.size(); i++) {
				FetchDataForEachMachine.run(collect.get(i).getMachineNuber());
			}

		}
	}

	private static void PushSubprojectsStructuresToDatabase(PushStructuresToGTTDB DBPusherGTT) throws IOException, SQLException {

		if(testingPurpose)
		{
			List<String> machines_remporary  = Arrays.asList("21050302");

			//clean - TRUNCATE - project table
			DBPusherGTT.TruncateSubProjectsTable();


			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < machines_remporary.size(); i++) {
				FetchSubprojectForEachMachine.run(machines_remporary.get(i));
			}
		}
		else {
			//clean - TRUNCATE - subProject table
			DBPusherGTT.TruncateSubProjectsTable();

			// gather data for all subprojects from subprojects database
			for (int i = 0; i < DBPusherGTT.getListOfSUbProjects().size(); i++)
				FetchSubprojectForEachMachine.run(DBPusherGTT.getListOfSUbProjects().get(i));
		}

	}
}
