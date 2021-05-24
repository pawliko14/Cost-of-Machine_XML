package main.java.StartingClass;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import main.java.GttDatabaseManipulate.PushStructuresToGTTDB;
import main.java.Objetcs.OpenedMachines;
import main.java.Parameters.Parameters;
import main.java.costofmachine.FetchDataForEachMachine;
import main.java.costofmachine.FetchMachineAndExistingStructure;
import main.java.costofmachine.FetchSubprojectForEachMachine;


public class Main {


	// set to False if need real data from database
	private static 	boolean testingPurpose = true;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					redirectSysOutToFile();
					printCurrentTime();
						Logic();
					printCurrentTime();


				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		});
	}

	private static void printCurrentTime() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());

		System.out.println(formatter.format(date));
	}

	private static void redirectSysOutToFile() throws IOException {
		if(Files.exists(Paths.get(Parameters.getPathToLogFile()))) {
			File file = new File(Parameters.getPathToLogFile());
			PrintStream stream = new PrintStream(file);
			System.setOut(stream);
		}else {
			File file = new File(Parameters.getPathToLogFile());
			file.createNewFile();
			PrintStream stream = new PrintStream(file);
			System.setOut(stream);
		}

	}


	/**
	 *
	 * well done logic, temporarily commented, for testing previous methods
	 *
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void Logic() throws SQLException, IOException {

//		//push available projects to database
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


		PushGeneralProjectsStructuresToDatabase(collect);


		// CAREFUL - truncate previous data, replace with new one, takes ~~30min to finish this algorythm
		//	PushSubprojectsStructuresToDatabase(DBPusherGTT);


	}

	private static void PushGeneralProjectsStructuresToDatabase( List<OpenedMachines> collect) throws IOException, SQLException {

		if(testingPurpose)
		{
			List<OpenedMachines> testingOpenedMachines =  new ArrayList<>();
				testingOpenedMachines.add(new OpenedMachines("7112401",true));


			//clean - TRUNCATE - project table
			truncateTable("machine_structure_details");


			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < testingOpenedMachines.size(); i++) {
				FetchDataForEachMachine.run(testingOpenedMachines.get(i).getMachineNuber());
			}
		}
		else {

			//clean - TRUNCATE - project table
			truncateTable("machine_structure_details");


			for (int i = 0; i < collect.size(); i++) {
				FetchDataForEachMachine.run(collect.get(i).getMachineNuber());
			}

		}
	}

	private static void truncateTable(String machine_structure) throws SQLException {



			Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");

			// truncate existing data  for replacing
			PreparedStatement sttmnt = null;
			sttmnt = connGTT.prepareStatement("Truncate table " + machine_structure);
			sttmnt.addBatch();
			sttmnt.executeBatch();

			sttmnt.close();
			connGTT.close();

			System.out.println("Truncate Table : machine_structure_details done");
	}

	private static void PushSubprojectsStructuresToDatabase( ) throws IOException, SQLException {

		if(testingPurpose)
		{
			List<String> machines_remporary  = Arrays.asList("21050302");

			//clean - TRUNCATE - project table
			truncateTable("machine_subprojetcs_structure_details");


			// run for each machine in loop, for now only projects, without subprojetcs
			for (int i = 0; i < machines_remporary.size(); i++) {
				FetchSubprojectForEachMachine.run(machines_remporary.get(i));
			}
		}
		else {
			//clean - TRUNCATE - subProject table
			truncateTable("machine_subprojetcs_structure_details");

			// gather data for all subprojects from subprojects database
//			for (int i = 0; i < DBPusherGTT.getListOfSUbProjects().size(); i++)
//				FetchSubprojectForEachMachine.run(DBPusherGTT.getListOfSUbProjects().get(i));
		}

	}
}
