package costofmachine;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CountMaterial2test {
	 static String MASZYNA = Main.text; // na sztywno, potem mozliwosc zmiany w gui
	 
	 static String Maszynka = "210900P"; // <- element do testow
	 
	 
	 static String GlownyProjektDlaArtykulu = "";
	 private static Map<String,String> ListaGlownychZlozenIPodzlozen;
	 //testowa struktura, na potrzeby programu Asi
	 private static ArrayList<Struktury>ListofStructuresTest;
	 private static int iloscZaglebien = 0; 
	 private static Double CalosciowaCenaPracy = 0.0;
	 private static Double CalosciowaCenaKonstrukcja = 0.0;
	 private static Double CalosciowaCenaProgramisciCNC = 0.0;
	 private static Double CalosciowaCenaElektronicy = 0.0;
	 private static Double CalosciowaCenaMaterialu = 0.0; 
	 private static int CenaPracoGodziny = 120; // kiedys, dowiedziec sie kiedy cena pracy to bylo 100zl, aktualnie jest 120zl
	 
	 
		public static void run() throws DocumentException, IOException, SQLException {
			Connection conn=DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");
			Connection connection= DB.GttDBConnection.dbConnector();

			
		
			
			try {
				
							
					
					 ListofStructuresTest = new ArrayList<Struktury>();				 			 			 
					 ListaGlownychZlozenIPodzlozen = new LinkedHashMap<String,String>(); // LinkedHashMap - preserver the insertion order, have to used Linked one
					 getListaGLownychZlozen(Maszynka,conn);
					 
						
							
										Set<Entry<String,String>> entrySet = ListaGlownychZlozenIPodzlozen.entrySet();
										int it = 0;
										for(Entry<String, String> entry: entrySet) {
											GlownyProjektDlaArtykulu = entry.getValue();
											System.out.println(" "+ it + ": " + entry.getKey() + " : " + entry.getValue());
											GetAllArticelInProject(entry.getKey(),conn,GlownyProjektDlaArtykulu);		
											iloscZaglebien= 0; // reset deppth of the structure
											it++;
										}					
						
									GetAllPrices(conn);									
									ShowAll();
	 							 
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			PodsumowanieKoncowe();
//			GetCenaKonstrukcja_CNC_Elektronicy(conn);			
//			Podsumowanie();
//			
//			
//			// generowanie dokumentu
			GenerateDocumentStruktury dokument_Struktury = new GenerateDocumentStruktury();
			dokument_Struktury.Generate(ListofStructuresTest, conn, Maszynka);
					
			
			PushTOdatabaseAllDataFromStructure();
			
			
			// ---------------------------------------------------------------
			// sprawdzenie afterkalkulacji -> czyli kalkulacji koncowej(storenotesdetail)
			// ---------------------------------------------------------------

			GetDataFromAftercalculations AfterCalculationsObiekt = new GetDataFromAftercalculations();
			
			AfterCalculationsObiekt.GetWholeData_from_storenotesDetail(conn, Maszynka);
			AfterCalculationsObiekt.GetWholeData_from_receptie(conn, Maszynka);
				
			ArrayList<AfterCalculationsStrukture> tescik = AfterCalculationsObiekt.PrzekazObiekt();
			
			AfterCalculationsObiekt.ShowAllInList(tescik);		
			AfterCalculationsObiekt.PrintTofile(tescik);
			
		// generowanie dokumentu
		GenerateDocumentKalkulacjaKoncowa dokument_kalkulacja = new GenerateDocumentKalkulacjaKoncowa();
		
		//dokument_kalkulacja.Generate(tescik, conn, Maszynka); // first version, without passing main array
			dokument_kalkulacja.Generate_v2(tescik, conn, Maszynka,ListofStructuresTest);

			
			conn.close();		
			
			printInfoOfListofStructuresTest();
			
			System.out.println("done");
	}
		
		
		   public static void PushTOdatabaseAllDataFromStructure() throws SQLException {
			
			   Connection connGTT = DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
			   PreparedStatement sttmnt = null;
			
			for(int i = 0 ; i < ListofStructuresTest.size();i++)
			{
					sttmnt= connGTT.prepareStatement("insert into machine_structure_details (MACHINENUMBER ,PARENTARTICLE ,CHILDARTICLE ,QUANTITY ,`TYPE` ,`LEVEL` )\r\n" + 
		    		"values (?,?,?,?,?,?)");  

	        try 
	        {

	        	sttmnt.setString(1, ListofStructuresTest.get(i).getGlownyProjekt());
	        	sttmnt.setString(2, ListofStructuresTest.get(i).getARTIKELCODE());
	        	sttmnt.setString(3, ListofStructuresTest.get(i).getONDERDEEL());
	        	sttmnt.setInt(4, 0);
	        	sttmnt.setString(5, ListofStructuresTest.get(i).getTYP());
	        	sttmnt.setInt(6, ListofStructuresTest.get(i).getPoziom());


	        	
	        	
          
	            
	            sttmnt.addBatch();
	            sttmnt.executeBatch();
	     

	            // rows affected
	            System.out.println("done for: " + i); 

	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			}
				connGTT.close();		

		}


		private static void insertionTest(Connection connection) {



		    String SQL_INSERT = "insert into Machine (ID,MACHINENUMBER ) values (?,?)";
		 

		        try (Connection conn = DriverManager.getConnection(
		                "jdbc:mariadb://192.168.90.101/gttdatabase", "gttuser", "gttpassword");
		             PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

		            preparedStatement.setInt(1, 2);
		            preparedStatement.setString(2, "21090");


		            int row = preparedStatement.executeUpdate();

		            // rows affected
		            System.out.println(row); //1

		        } catch (SQLException e) {
		            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		    
			
		
		}


		private static void printInfoOfListofStructuresTest() {
			System.out.println("info of Structure: ");
			System.out.println("size of list : " + ListofStructuresTest.size());
			System.out.println("Elem[0] : " );
				ListofStructuresTest.get(0).Show();
			System.out.println("Elem[10] : " );
				ListofStructuresTest.get(10).Show();
			System.out.println("Elem[25] : " );
				ListofStructuresTest.get(25).Show();
			
			
		}


		private static void ShowAll() throws FileNotFoundException {
			
			try (PrintStream out = new PrintStream(new FileOutputStream(Parameters.getPathOfSavingNomenclatuurTxt())))
			{					
				for(int i = 0 ; i <ListofStructuresTest.size();i++ )
				{
					out.println("---Poziom---: " + ListofStructuresTest.get(i).getPoziom());
					out.println("Glowny Projekt: " + ListofStructuresTest.get(i).getGlownyProjekt());
					out.println("Seqwencja: " + ListofStructuresTest.get(i).getSeq());
					out.println("Artikel Nadrzedny: " + ListofStructuresTest.get(i).getARTIKELCODE());
					out.println("Artikel: " + ListofStructuresTest.get(i).getONDERDEEL());
					out.println("Opis Artikel: " + ListofStructuresTest.get(i).getCFOMSONDERDEEL());
					out.println("TYP: " + ListofStructuresTest.get(i).getTYP());
					out.println("ILOSC: " + ListofStructuresTest.get(i).getILOSC());
					out.println("JEDNOSTKA: " + ListofStructuresTest.get(i).getJEDNOSTKA());
					out.println("Czy sprawdzony?: " + ListofStructuresTest.get(i).isCzySprawdzony());
					out.println("CenaMaterialu: " + ListofStructuresTest.get(i).getCenaMaterialu());
					out.println("CenaMaterialu Razy Ilosc: " + ListofStructuresTest.get(i).getCenaMaterialuRazyIlosc());
					out.println("Cena Pracy Za sztuke: " + ListofStructuresTest.get(i).getCenaPracy());
					out.println("Cena Pracy Razy Ilosc: " + ListofStructuresTest.get(i).getCenaPracyRazyIlosc());
					out.println("                              ");
				}
			}
			
		}

		// dlugosc wykonywania operacji jest zbyt dlugi do testowania ( ostatni run trwal 1,5h)


		
		
private static void GetAllPrices(Connection conn) throws SQLException {
			
			for(int i = 0 ; i < ListofStructuresTest.size();i++)
			{															
					String artikelkod = ListofStructuresTest.get(i).getONDERDEEL();
					
					
					Statement b = conn.createStatement();
					ResultSet rs2 = b.executeQuery("select ARTIKELCODE,MATERIAAL,LONEN from artikel_kostprijs where ARTIKELCODE = '"+artikelkod+"' and SOORT = '4'");
					
					if (!rs2.isBeforeFirst() ) {    									
						ListofStructuresTest.get(i).setCenaMaterialuRazyIlosc(0.0);
						ListofStructuresTest.get(i).setCenaMaterialu(0.0);
						ListofStructuresTest.get(i).setCenaPracy(0.0);
					}
					else
					{
						while(rs2.next())
						{
							
						if(rs2.getString("MATERIAAL").equals("")|| rs2.getString("MATERIAAL").equals(null))
						{
							ListofStructuresTest.get(i).setCenaMaterialu(0.0);
						}
						else
						{
							ListofStructuresTest.get(i).setCenaMaterialu(Double.parseDouble(rs2.getString("MATERIAAL")));
						}
						Double cena = ListofStructuresTest.get(i).getCenaMaterialu();
						Double ilosc = ListofStructuresTest.get(i).getILOSC();

						
						Double cenaRazyIlosc = cena * ilosc;				
						ListofStructuresTest.get(i).setCenaMaterialuRazyIlosc(cenaRazyIlosc);
						
						if(rs2.getString("LONEN").equals("")|| rs2.getString("LONEN").equals(null))
						{
							ListofStructuresTest.get(i).setCenaPracy(0.0);	
						}
						else
						{
							ListofStructuresTest.get(i).setCenaPracy(Double.parseDouble(rs2.getString("LONEN")));

						}
						Double cenaPracySzt = ListofStructuresTest.get(i).getCenaPracy();
						Double pracaRazyIlosc = cenaPracySzt * ilosc;
						
						ListofStructuresTest.get(i).setCenaPracyRazyIlosc(pracaRazyIlosc);
						
						//Add to summary:
						CalosciowaCenaPracy += ListofStructuresTest.get(i).getCenaPracyRazyIlosc();
						CalosciowaCenaMaterialu += ListofStructuresTest.get(i).getCenaMaterialuRazyIlosc();
						}
					}
					b.close();
					rs2.close();			
			}		
		}


		public static void GetAllArticelInProject(String articlecode,Connection conn, String GlownyProjekt) throws SQLException{

			String G = GlownyProjekt;
			
			Statement b = conn.createStatement();			
			ResultSet rs2 = b.executeQuery("select seq,ARTIKELCODE,ONDERDEEL,CFOMSONDERDEEL,TYP,ILOSC,JEDNOSTKA from struktury where ARTIKELCODE = '"+articlecode+"' order by seq");
			
			Struktury StrukturaTmp; 


			while(rs2.next()){							
				
				StrukturaTmp = new Struktury(iloscZaglebien); // 0 mean level 0 
				StrukturaTmp.setGlownyProjekt(G);
				StrukturaTmp.setSeq(rs2.getString("seq"));
				StrukturaTmp.setARTIKELCODE(rs2.getString("ARTIKELCODE"));
				StrukturaTmp.setONDERDEEL(rs2.getString("ONDERDEEL"));
				StrukturaTmp.setCFOMSONDERDEEL(rs2.getString("CFOMSONDERDEEL"));
				StrukturaTmp.setTYP(rs2.getString("TYP"));
				
				
					if(rs2.getString("TYP") != null && !rs2.getString("TYP").isEmpty())
					{
						
						String typeFrom = PushValidTypeForHigherLevelOfStructures(articlecode, GlownyProjekt ,conn);
						StrukturaTmp.setTYP_Nadrzednego(typeFrom);
						
					//	StrukturaTmp.setTYP(rs2.getString("TYP"));
					//	System.out.println("zostalo dodane dla artikelcode : " +  articlecode);
						

					}
					else
					{
						StrukturaTmp.setTYP_Nadrzednego("WRONG"); // if the type is not known set is as Y
					}

				
				StrukturaTmp.setILOSC(Double.parseDouble(rs2.getString("ILOSC")));
				StrukturaTmp.setJEDNOSTKA(rs2.getString("JEDNOSTKA"));
				


				ListofStructuresTest.add(StrukturaTmp);
				
//		
//			for(Struktury  st: ListofStructuresTest)
//				st.Show();
							
			if(rs2.getString("TYP") != null && !rs2.getString("TYP").isEmpty()) {
				
				if(rs2.getString("typ").equals("F")||rs2.getString("onderdeel").startsWith("%") || rs2.getString("typ").equals("P")){			
					iloscZaglebien++;
					GetAllArticelInProject(rs2.getString("onderdeel"),conn,G);
				}
			}
			}
		//	b.close();
		//	rs2.close();
		//	conn.close();

			iloscZaglebien -=  1;
		}
		
		
		private static String PushValidTypeForHigherLevelOfStructures(String articlecode, String onderdeel,Connection conn) throws SQLException {
			
			String results = null;
			
			Statement b = conn.createStatement();			
			ResultSet rs2 = b.executeQuery("select TYP from struktury s \r\n" + 
					"	where ONDERDEEL  = '"+articlecode+"'\r\n" + 
					"	and ARTIKELCODE  = '"+onderdeel+"'");
			
			if(rs2.next())
			{
				results = rs2.getString("TYP");
			}
						
			return results;
		}


		public static void getListaGLownychZlozen(String art, Connection conn) throws SQLException
		 {
			Statement b = conn.createStatement();			
		//	ResultSet rs2 = b.executeQuery("select ARTIKELCODE, ONDERDEEL from struktury where ARTIKELCODE like '"+art+"%' order by ARTIKELCODE asc ");
			
			
			//temporary
			ResultSet rs2 = b.executeQuery("select ARTIKELCODE, ONDERDEEL from struktury where ARTIKELCODE = '"+art+"' order by SEQ asc ");

			
			while(rs2.next()){
				ListaGlownychZlozenIPodzlozen.put(rs2.getString("ONDERDEEL"), rs2.getString("ARTIKELCODE"));
			}
			b.close();
			rs2.close();
			
		 }
		
		public static void PodsumowanieKoncowe()
		{
			System.out.println("Podsumowanie Koncowe:");
			System.out.println("Cena pracy(wszystko):" + CalosciowaCenaPracy);
			System.out.println("Cena Materialu(wszystko):" + CalosciowaCenaMaterialu);

			
			
		}
		
		public static void Podsumowanie()
		{
			Double EUro = 4.2;
			System.out.println("Podsumowanie :");
			Double cenafinalnaPLN = CalosciowaCenaKonstrukcja+CalosciowaCenaElektronicy+CalosciowaCenaProgramisciCNC+CalosciowaCenaPracy+CalosciowaCenaMaterialu;
			System.out.println("Podsumowanie Koncowe:"+ cenafinalnaPLN + " PLN");
			
			Double cenafinalnaEUR = cenafinalnaPLN / EUro ;
			System.out.println("Podsumowanie Koncowe:"+ cenafinalnaEUR + " EUR");


		}

		public static void GetCenaKonstrukcja_CNC_Elektronicy(Connection conn) throws SQLException
		{
			
			//String sql_b = "select ("+CenaPracoGodziny+"*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost NOT IN ('KM01', 'KE01', 'CNC') and (cfproject like '"+Maszynka+"%')";
			String sql_e = "select ("+CenaPracoGodziny+"*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'KM01' and (cfproject like '%"+Maszynka+"%')";
			String sql_f = "select ("+CenaPracoGodziny+"*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'KE01' and (cfproject like '%"+Maszynka+"%')";
			String sql_g = "select ("+CenaPracoGodziny+"*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'CNC' and (cfproject like '%"+Maszynka+"%')";
		
			
//			//koszt montazu
//			Statement b = conn.createStatement();
//			ResultSet rs1 = b.executeQuery(sql_b);
//			while(rs1.next()) {
//				montage+=rs1.getDouble(1);
//			}
//			rs1.close();
//			b.close();
			
			//koszt konstrukcji
			Statement e = conn.createStatement();
			ResultSet rs5 = e.executeQuery(sql_e);
			while(rs5.next()) {
				CalosciowaCenaKonstrukcja+=rs5.getDouble(1);
			}
			rs5.close();
			e.close();
			
			//koszt elektrykow
			Statement f = conn.createStatement();
			ResultSet rs6 = f.executeQuery(sql_f);
			while(rs6.next()) {
				CalosciowaCenaElektronicy+=rs6.getDouble(1);
			}
			rs6.close();
			f.close();
			
			//koszt cnc
			Statement g = conn.createStatement();
			ResultSet rs7 = g.executeQuery(sql_g);
			while(rs7.next()) {
				CalosciowaCenaProgramisciCNC+=rs7.getDouble(1);
			}
			rs7.close();
			g.close();
			
			
			System.out.println("Podsumowanie Koncowe:");
			System.out.println("Cena pracy(Konstrukcja):" + CalosciowaCenaKonstrukcja);
			System.out.println("Cena pracy(elektrykow):" + CalosciowaCenaElektronicy);
			System.out.println("Cena pracy(CNC):" + CalosciowaCenaProgramisciCNC);
			
		}
				
}


