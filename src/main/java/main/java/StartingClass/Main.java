package main.java.StartingClass;

import java.awt.EventQueue;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


import main.java.GttDatabaseManipulate.PushStructuresToGTTDB;
import main.java.costofmachine.FetchDataForEachMachine;
import main.java.costofmachine.FetchSubprojectForEachMachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class Main {

	protected static final String DEST =  "C://Users/el08/Desktop/programiki/Cost_of_machine/Analiza_maszyn.pdf";
	protected static String text = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					Logger logger = LoggerFactory.getLogger(Main.class);
					logger.info("Hello World");



					//push available projects to database

					PushStructuresToGTTDB DBPusherGTT = new PushStructuresToGTTDB();


					// push Projects and Subprojects to database
					DBPusherGTT.PushOpenProjectListTODB();
					DBPusherGTT.PushSubProjectsToDB();

					// print existing subprojetcs
				//	DBPusherGTT.getListOfSUbProjects().forEach(System.out::println);



				//	PushGeneralProjectsStructuresToDatabase(DBPusherGTT);
					PushSubprojectsStructuresToDatabase(DBPusherGTT);



				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		});
	}

	/**
	 *
	 * Pushes data(Structure) from General Project ( without subprojects) to table in  database
	 *
	 * @param DBPusherGTT
	 * @throws IOException
	 * @throws SQLException
	 *
	 *
	 */
	private static void PushGeneralProjectsStructuresToDatabase(PushStructuresToGTTDB DBPusherGTT) throws IOException, SQLException {

		//clean - TRUNCATE - project table
		DBPusherGTT.TruncateStructurestable();

		// run for each machine in loop, for now only projects, without subprojetcs
		for(int i = 0 ; i < DBPusherGTT.getListOfOpenedMachines().size();i++) {
			FetchDataForEachMachine.run(DBPusherGTT.getListOfOpenedMachines().get(i));
		}
	}

	/**
	 * 	  pushes data(Structure) from SUbProject of General Project to table in database
	 *
	 * @param DBPusherGTT
	 * @throws IOException
	 * @throws SQLException
	 *
	 */
	private static void PushSubprojectsStructuresToDatabase(PushStructuresToGTTDB DBPusherGTT) throws IOException, SQLException {

		List<String> test = Arrays.asList("21050602");


		//clean - TRUNCATE - subProject table
		DBPusherGTT.TruncateSubProjectsTable();






		//depoloy purpose

		// gather data for all subprojects from subprojects database
		for(int i = 0 ; i < DBPusherGTT.getListOfSUbProjects().size();i++)
			FetchSubprojectForEachMachine.run(DBPusherGTT.getListOfSUbProjects().get(i));




	}
}
