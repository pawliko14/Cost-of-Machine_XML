package main.java.Parameters;

public class Parameters {

	private static String PathOfSavingNomenclatuurTxt = "\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\Structure_examp.txt";
	private static String PathToLogFile = "\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\StructuurAnalyze\\Log\\log.txt";
	private static String PathToLogFileError = "\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\StructuurAnalyze\\Log\\err.txt";

	public static String getPathToLogFileError() { return PathToLogFileError;}

	public static String getPathOfSavingNomenclatuurTxt() {
		return PathOfSavingNomenclatuurTxt;
	}
	public static String getPathToLogFile() { return PathToLogFile; }

}
