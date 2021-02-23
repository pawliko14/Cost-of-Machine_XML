package main.java.StartingClass;

import java.awt.EventQueue;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


import main.java.GttDatabaseManipulate.PushStructuresToGTTDB;
import main.java.costofmachine.FetchDataForEachMachine;
import main.java.costofmachine.FetchSubprojectForEachMachine;


public class Main {


	// set to False if need real data from database
	private static 	boolean testingPurpose = true;


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



					PushGeneralProjectsStructuresToDatabase(DBPusherGTT);


					// CAREFUL - truncate previous data, replace with new one, takes ~~30min to finish this algorythm
					PushSubprojectsStructuresToDatabase(DBPusherGTT);



				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		});
	}

	private static void PushGeneralProjectsStructuresToDatabase(PushStructuresToGTTDB DBPusherGTT) throws IOException, SQLException {

		if(testingPurpose)
		{
			List<String> machines_remporary  = Arrays.asList("210500", "210503");

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


			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < DBPusherGTT.getListOfOpenedMachines().size(); i++) {
				FetchDataForEachMachine.run(DBPusherGTT.getListOfOpenedMachines().get(i));
			}
		}
	}

	private static void PushSubprojectsStructuresToDatabase(PushStructuresToGTTDB DBPusherGTT) throws IOException, SQLException {

		if(testingPurpose)
		{
			List<String> machines_remporary  = Arrays.asList("21050302", "21050303");

			//clean - TRUNCATE - project table
			DBPusherGTT.TruncateStructuresTable();


			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < machines_remporary.size(); i++) {
				FetchDataForEachMachine.run(machines_remporary.get(i));
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
