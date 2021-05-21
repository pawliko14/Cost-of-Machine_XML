package main.java.Parameters;

public class Parameters {

	private static String PathOfSavingNomenclatuurTxt = "\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\Structure_examp.txt";
	private static String PathOfSavingAfterCalculationsTxt ="\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\KalkulacjaKoncowa.txt";
	private static String PathOfSavingAnaliza_MaszynPDF ="\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\Analiza_maszyn.pdf";
	private static String PathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF ="\\\\192.168.90.203\\Common\\Programy\\GTT_FAT\\Analiza_maszyn_Kalkulacja_koncowa.pdf";
	
	public static String getPathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF() {
		return PathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF;
	}
	public static void setPathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF(
			String pathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF) {
		PathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF = pathOfSavingAnaliza_Maszyn_kalkulacjaKoncowaPDF;
	}
	public static String getPathOfSavingAnaliza_MaszynPDF() {
		return PathOfSavingAnaliza_MaszynPDF;
	}
	public static void setPathOfSavingAnaliza_MaszynPDF(String pathOfSavingAnaliza_MaszynPDF) {
		PathOfSavingAnaliza_MaszynPDF = pathOfSavingAnaliza_MaszynPDF;
	}
	public static String getPathOfSavingNomenclatuurTxt() {
		return PathOfSavingNomenclatuurTxt;
	}
	public static void setPathOfSavingNomenclatuurTxt(String pathOfSavingNomenclatuurTxt) {
		PathOfSavingNomenclatuurTxt = pathOfSavingNomenclatuurTxt;
	}
	public static String getPathOfSavingAfterCalculationsTxt() {
		return PathOfSavingAfterCalculationsTxt;
	}
	public static void setPathOfSavingAfterCalculationsTxt(String pathOfSavingAfterCalculationsTxt) {
		PathOfSavingAfterCalculationsTxt = pathOfSavingAfterCalculationsTxt;
	}
	
	
}
