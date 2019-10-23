package costofmachine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GetDataFromAftercalculations {
	
	private ArrayList<AfterCalculationsStrukture> Lista = new ArrayList<AfterCalculationsStrukture>();
	
	GetDataFromAftercalculations()
	{
		
	}
	
	public void GetWholeData_from_receptie(Connection conn, String proj) throws SQLException
	{
		String project = "2/" + proj;
		
		//Lista = new ArrayList<AfterCalculationsStrukture>();
		
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select r.LEVERANCIER, r.ORDERNUMMER,re.ARTIKELCODE,re.SEQUENTIE,re.ARTIKELOMSCHRIJVING,re.BESTELD, re.GELEVERD,re.CFRECEPTIEDATUM,re.CFEFFLEVERINGSDATUM,r.PROJECT,r.PRODPROJECT,\r\n" + 
				"a.DATUM,a.LONEN,a.MATERIAAL,a.CFFIRMAMUNT,a.MATERIAAL * re.GELEVERD as TOTAAL\r\n" + 
				"from receptie r\r\n" + 
				"left join receptiedetail re \r\n" + 
				"on r.ORDERNUMMER  = re.ORDERNUMMER\r\n" + 
				"and r.LEVERANCIER = re.LEVERANCIER\r\n" + 
				"left join artikel_kostprijs a \r\n" + 
				"on re.ARTIKELCODE = a.ARTIKELCODE\r\n" + 
				"where  PROJECT ='2/190521'\r\n" + 
				"and re.GELEVERD >= 1\r\n" + 
				"and a.SOORT = '4'\r\n" + 
				"order by r.ORDERNUMMER, re.SEQUENTIE"); 
		
		while(rs.next()) {
			AfterCalculationsStrukture obiekt = new AfterCalculationsStrukture();
			
			String Leverancier = rs.getString("Leverancier");
			String ORDERNUMMER = rs.getString("ORDERNUMMER");
			String SEQUENTIE = rs.getString("SEQUENTIE");
			String ARTIKELCODE = rs.getString("ARTIKELCODE");
			String ARTIKELOMSCHRIJVING = rs.getString("ARTIKELOMSCHRIJVING");
			String BESTELD = rs.getString("BESTELD");
			String GELEVERD = rs.getString("GELEVERD");
			String PROJECTNUMMER = rs.getString("PROJECT");
			String BESTELDATUM = rs.getString("DATUM");
		//	String LEVERDATUM = rs.getString("LEVERDATUM");
		//	String LEVERINGSDATUMEFFECTIEF = rs.getString("LEVERINGSDATUMEFFECTIEF");
			String LEVERINGSDATUMINGAVERECEPTIE = rs.getString("CFRECEPTIEDATUM");
			String EENHEIDSPRIJS = rs.getString("MATERIAAL");
			String MUNT = rs.getString("CFFIRMAMUNT");
			String TOTAAL = rs.getString("TOTAAL");
		//	String MONTAGEOMSCHRIJVING = rs.getString("MONT");
			
			obiekt.setARTIKELCODE(ARTIKELCODE);
			obiekt.setLeverancier(Leverancier);
			obiekt.setORDERNUMMER(ORDERNUMMER);
			obiekt.setSEQUENTIE(SEQUENTIE);
			obiekt.setARTIKELOMSCHRIJVING(ARTIKELOMSCHRIJVING);
			obiekt.setBESTELD(BESTELD);
			obiekt.setGELEVERD(GELEVERD);
			obiekt.setPROJECTNUMMER(PROJECTNUMMER);
			obiekt.setBESTELD(BESTELDATUM);
			obiekt.setLEVERDATUM("Brak LeverDatum");
			obiekt.setLEVERINGSDATUMEFFECTIEF("brak LEVERINGSDATUMEFFECTIEF");
			obiekt.setLEVERINGSDATUMINGAVERECEPTIE(LEVERINGSDATUMINGAVERECEPTIE);
			obiekt.setEENHEIDSPRIJS(EENHEIDSPRIJS);
			obiekt.setMUNT(MUNT);
			obiekt.setTOTAAL(TOTAAL);
			obiekt.setMONTAGEOMSCHRIJVING("brak MONTAGEOMSCHRIJVING");
			
			Lista.add(obiekt);
		}
		s.close();
		rs.close();
	}
	
	// tutaj musi byc project z kompletna nazwa '2/190521' (na przyklad)
	public void GetWholeData_from_storenotesDetail(Connection conn, String proj) throws SQLException
	{
		String project = "2/" + proj;
		
	//	Lista = new ArrayList<AfterCalculationsStrukture>();
		
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select Leverancier,ORDERNUMMER,SEQUENTIE,ARTIKELCODE,ARTIKELOMSCHRIJVING,BESTELD,GELEVERD,\r\n" + 
				"PROJECTNUMMER,BESTELDATUM,LEVERDATUM,LEVERINGSDATUMEFFECTIEF,LEVERINGSDATUMINGAVERECEPTIE,\r\n" + 
				"EENHEIDSPRIJS,MUNT,TOTAAL,concat(MONTAGE,'-', MONTAGEOMSCHRIJVING) as MONT \r\n" + 
				"from storenotesdetail where PROJECTNUMMER like '"+project+"%' and GELEVERD >=1 AND MONTAGEOMSCHRIJVING IS NOT NULL order by ORDERNUMMER,SEQUENTIE asc "); // check if element was delivered, only deliverd ones interss us
		while(rs.next()) {
			AfterCalculationsStrukture obiekt = new AfterCalculationsStrukture();
			
			String Leverancier = rs.getString("Leverancier");
			String ORDERNUMMER = rs.getString("ORDERNUMMER");
			String SEQUENTIE = rs.getString("SEQUENTIE");
			String ARTIKELCODE = rs.getString("ARTIKELCODE");
			String ARTIKELOMSCHRIJVING = rs.getString("ARTIKELOMSCHRIJVING");
			String BESTELD = rs.getString("BESTELD");
			String GELEVERD = rs.getString("GELEVERD");
			String PROJECTNUMMER = rs.getString("PROJECTNUMMER");
			String BESTELDATUM = rs.getString("BESTELDATUM");
			String LEVERDATUM = rs.getString("LEVERDATUM");
			String LEVERINGSDATUMEFFECTIEF = rs.getString("LEVERINGSDATUMEFFECTIEF");
			String LEVERINGSDATUMINGAVERECEPTIE = rs.getString("LEVERINGSDATUMINGAVERECEPTIE");
			String EENHEIDSPRIJS = rs.getString("EENHEIDSPRIJS");
			String MUNT = rs.getString("MUNT");
			String TOTAAL = rs.getString("TOTAAL");
			String MONTAGEOMSCHRIJVING = rs.getString("MONT");
			
			obiekt.setARTIKELCODE(ARTIKELCODE);
			obiekt.setLeverancier(Leverancier);
			obiekt.setORDERNUMMER(ORDERNUMMER);
			obiekt.setSEQUENTIE(SEQUENTIE);
			obiekt.setARTIKELOMSCHRIJVING(ARTIKELOMSCHRIJVING);
			obiekt.setBESTELD(BESTELD);
			obiekt.setGELEVERD(GELEVERD);
			obiekt.setPROJECTNUMMER(PROJECTNUMMER);
			obiekt.setBESTELDATUM(BESTELDATUM);
			obiekt.setLEVERDATUM(LEVERDATUM);
			obiekt.setLEVERINGSDATUMEFFECTIEF(LEVERINGSDATUMEFFECTIEF);
			obiekt.setLEVERINGSDATUMINGAVERECEPTIE(LEVERINGSDATUMINGAVERECEPTIE);
			obiekt.setEENHEIDSPRIJS(EENHEIDSPRIJS);
			obiekt.setMUNT(MUNT);
			obiekt.setTOTAAL(TOTAAL);
			obiekt.setMONTAGEOMSCHRIJVING(MONTAGEOMSCHRIJVING);
			
			Lista.add(obiekt);
		}
		s.close();
		rs.close();
	}
	public ArrayList<AfterCalculationsStrukture> PrzekazObiekt()
	{		
		return Lista;
	}
	
	public void PrintTofile(ArrayList<AfterCalculationsStrukture> tescik)
	{
		try (PrintStream out = new PrintStream(new FileOutputStream(Parameters.getPathOfSavingAfterCalculationsTxt())))
		{					
			for(int i = 0 ; i <tescik.size();i++ )
			{
				out.println("Obiekt: [" + i +"] ");
				out.println("Leverancier: " + tescik.get(i).getLeverancier());
				out.println("ORDERNUMMER: " + tescik.get(i).getORDERNUMMER());
				out.println("SEQUENTIE: " + tescik.get(i).getSEQUENTIE());
				out.println("ARTIKELCODE: " + tescik.get(i).getARTIKELCODE());
				out.println("ARTIKELOMSCHRIJVING: " + tescik.get(i).getARTIKELOMSCHRIJVING());
				out.println("BESTELD: " + tescik.get(i).getBESTELD());
				out.println("GELEVERD: " + tescik.get(i).getGELEVERD());
				out.println("PROJECTNUMMER: " + tescik.get(i).getPROJECTNUMMER());
				out.println("LEVERDATUM: " + tescik.get(i).getLEVERDATUM());
				out.println("LEVERINGSDATUMEFFECTIEF: " + tescik.get(i).getLEVERINGSDATUMEFFECTIEF());
				out.println("LEVERINGSDATUMINGAVERECEPTIE: " + tescik.get(i).getLEVERINGSDATUMINGAVERECEPTIE());
				out.println("EENHEIDSPRIJS: " + tescik.get(i).getEENHEIDSPRIJS());
				out.println("MUNT: " + tescik.get(i).getMUNT());
				out.println("TOTAAL: " + tescik.get(i).getTOTAAL());     
				out.println("MONTAGEOMSCHRIJVING: " + tescik.get(i).getMONTAGEOMSCHRIJVING());
				out.println("-------------------------"); 
			}
		} catch (FileNotFoundException e) {
			System.out.println("nie moze znalezc pliku Kalkulacja_Koncowa, badz jest jakis problem z plikiem txt");
			e.printStackTrace();
		}
	}
	
	public void ShowAllInList(ArrayList<AfterCalculationsStrukture> tescik)
	{
		for(int i = 0 ; i <tescik.size();i++)
		{
			tescik.get(i).PrintAllData();
		}
	}


}
