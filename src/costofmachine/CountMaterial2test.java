package costofmachine;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
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
	// static String Maszynka ="190521";
	// static String Maszynka = "170700";
	// static String Maszynka ="190522"; // jakis problem brakuje kolejnosci jak w poprzednich przykladach
	// static String Maszynka ="170506"; // duza ilosc podprojektow, problem z podprojektem 17050602, brakuje czesci rzeczy ze zlozenia, wyladowaly w innym zlozeniu
	 static String Maszynka = "170801";

	 
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
			
			
			try {
				
							
					
					 ListofStructuresTest = new ArrayList<Struktury>();				 			 
					 
					 ListaGlownychZlozenIPodzlozen = new LinkedHashMap<String,String>(); // LinkedHashMap - preserver the insertion order, have to used Linked one
					 getListaGLownychZlozen(Maszynka,conn);
					 
						

					
					 		//remove not working structure
//						for(int i = 0 ; i < ListaGlownychZlozen.size();i++)
//							if(ListaGlownychZlozen.get(i).equals("%%360A-030-4000/000"))
//								ListaGlownychZlozen.remove(i);
							
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
			
			PodsumowanieKoncowe();
			GetCenaKonstrukcja_CNC_Elektronicy(conn);			
			Podsumowanie();
			
			
			// generowanie dokumentu
			GenerateDocument dokumencik = new GenerateDocument();
			dokumencik.Generate(ListofStructuresTest, conn, Maszynka);
					
			
			
			// ---------------------------------------------------------------
			// sprawdzenie afterkalkulacji -> czyli kalkulacji koncowej(storenotesdetail)
			// ---------------------------------------------------------------

//			GetDataFromAftercalculations AfterCalculationsObiekt = new GetDataFromAftercalculations();
//			
//			AfterCalculationsObiekt.GetWholeData_from_storenotesDetail(conn, Maszynka);
//			AfterCalculationsObiekt.GetWholeData_from_receptie(conn, Maszynka);
//				
//			ArrayList<AfterCalculationsStrukture> tescik = AfterCalculationsObiekt.PrzekazObiekt();
//			
//			AfterCalculationsObiekt.ShowAllInList(tescik);		
//			AfterCalculationsObiekt.PrintTofile(tescik);
//			
			
			
			System.out.println("done");
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

//		private static void GetAllPrices(Connection conn) throws SQLException {
//			
//			// get from calendar begin date ( data produkcji) and ends date( data koniec montazu)
//			
//			String BeginDate = "";
//			String EndDate = "";
//			
//			Statement state = conn.createStatement();
//			ResultSet result = state.executeQuery("select DataProdukcji, DataKoniecMontazu from calendar where NrMaszyny like  '%"+Maszynka+"' order by NrMaszyny desc limit 1"); // in this case it is not sure that it will be 2/
//			
//			while(result.next())
//			{
//				BeginDate =result.getString("DataProdukcji");
//				EndDate =result.getString("DataKoniecMontazu");
//			}
//			state.close();
//			result.close();
//			
//			System.out.println("Data pocza: "+ BeginDate);
//			System.out.println("Data koniec: "+ EndDate);
//
//			
//			
//		//	for(int i = 0 ; i < ListofStructuresTest.size();i++)
//			for(int i = 0 ; i < ListofStructuresTest.size();i++)
//			{						
//				
//			//	System.out.println("Element z listy: "+ ListofStructuresTest.get(i).getARTIKELCODE());
//
//				
//					String artikelkod = ListofStructuresTest.get(i).getONDERDEEL();
//					
//					
//					Statement b = conn.createStatement();
//				//	ResultSet rs2 = b.executeQuery("select ARTIKELCODE,MATERIAAL,LONEN from artikel_kostprijs where ARTIKELCODE = '"+artikelkod+"' and SOORT = '4'");
//					
//					ResultSet rs2 = b.executeQuery("select ARTIKELCODE, MATERIAAL,LONEN  from artikel_kostprijs_allsort\r\n" + 
//							"where ARTIKELCODE = '"+artikelkod+"'\r\n" + 
//							"and DATUM between '"+BeginDate+"' and '"+EndDate+"'\r\n" + 
//							"and SOORT = '4' order by DATUM desc limit 1");
//					
//					if (!rs2.isBeforeFirst() ) { 
//						
//							Statement st1 = conn.createStatement();							
//							ResultSet rs1 = st1.executeQuery("select ARTIKELCODE, MATERIAAL, LONEN  from artikel_kostprijs_allsort where SOORT = '4'\r\n" + 
//									"and ARTIKELCODE = '"+artikelkod+"'\r\n" + 
//									"order by DATUM desc limit 1 ");
//						
//						// if resultset is completly empty set it all to 0.0
//							if (!rs1.isBeforeFirst() ) 
//							{ 						
//								ListofStructuresTest.get(i).setCenaMaterialuRazyIlosc(0.0);
//								ListofStructuresTest.get(i).setCenaMaterialu(0.0);
//								ListofStructuresTest.get(i).setCenaPracy(0.0);
//							}
//							else
//							{
//								while(rs1.next())
//								{
//									if(rs1.getString("MATERIAAL").equals("")|| rs1.getString("MATERIAAL").equals(null))
//										ListofStructuresTest.get(i).setCenaMaterialu(0.0);
//									
//									else
//										ListofStructuresTest.get(i).setCenaMaterialu(Double.parseDouble(rs1.getString("MATERIAAL")));
//									
//									Double cena = ListofStructuresTest.get(i).getCenaMaterialu();
//									Double ilosc = ListofStructuresTest.get(i).getILOSC();
//
//									
//									Double cenaRazyIlosc = cena * ilosc;				
//									ListofStructuresTest.get(i).setCenaMaterialuRazyIlosc(cenaRazyIlosc);
//									
//									if(rs1.getString("LONEN").equals("")|| rs1.getString("LONEN").equals(null))
//										ListofStructuresTest.get(i).setCenaPracy(0.0);	
//								
//									else
//										ListofStructuresTest.get(i).setCenaPracy(Double.parseDouble(rs1.getString("LONEN")));
//
//									Double cenaPracySzt = ListofStructuresTest.get(i).getCenaPracy();
//									Double pracaRazyIlosc = cenaPracySzt * ilosc;
//									
//									ListofStructuresTest.get(i).setCenaPracyRazyIlosc(pracaRazyIlosc);
//									
//									//Add to summary:
//									CalosciowaCenaPracy += ListofStructuresTest.get(i).getCenaPracyRazyIlosc();
//									CalosciowaCenaMaterialu += ListofStructuresTest.get(i).getCenaMaterialuRazyIlosc();
//								}
//							}
//							st1.close();
//							rs1.close();
//						
//					}
//					else
//					{
//						while(rs2.next())
//						{
//							
//						if(rs2.getString("MATERIAAL").equals("")|| rs2.getString("MATERIAAL").equals(null))
//							ListofStructuresTest.get(i).setCenaMaterialu(0.0);
//						
//						else
//							ListofStructuresTest.get(i).setCenaMaterialu(Double.parseDouble(rs2.getString("MATERIAAL")));
//						
//						Double cena = ListofStructuresTest.get(i).getCenaMaterialu();
//						Double ilosc = ListofStructuresTest.get(i).getILOSC();
//
//						
//						Double cenaRazyIlosc = cena * ilosc;				
//						ListofStructuresTest.get(i).setCenaMaterialuRazyIlosc(cenaRazyIlosc);
//						
//						if(rs2.getString("LONEN").equals("")|| rs2.getString("LONEN").equals(null))
//							ListofStructuresTest.get(i).setCenaPracy(0.0);	
//					
//						else
//							ListofStructuresTest.get(i).setCenaPracy(Double.parseDouble(rs2.getString("LONEN")));
//
//						Double cenaPracySzt = ListofStructuresTest.get(i).getCenaPracy();
//						Double pracaRazyIlosc = cenaPracySzt * ilosc;
//						
//						ListofStructuresTest.get(i).setCenaPracyRazyIlosc(pracaRazyIlosc);
//						
//						//Add to summary:
//						CalosciowaCenaPracy += ListofStructuresTest.get(i).getCenaPracyRazyIlosc();
//						CalosciowaCenaMaterialu += ListofStructuresTest.get(i).getCenaMaterialuRazyIlosc();
//						}
//					}
//					b.close();
//					rs2.close();			
//			}		
//		}
		
		
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
				
					if(rs2.getString("TYP") != null && !rs2.getString("TYP").isEmpty())
					{
						StrukturaTmp.setTYP(rs2.getString("TYP"));

					}
					else
					{
						StrukturaTmp.setTYP("Y"); // if the type is not known set is as Y
					}
				StrukturaTmp.setILOSC(Double.parseDouble(rs2.getString("ILOSC")));
				StrukturaTmp.setJEDNOSTKA(rs2.getString("JEDNOSTKA"));
				


				ListofStructuresTest.add(StrukturaTmp);
				
		
//				for(Struktury  st: ListofStructuresTest)
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
		
		
		public static void getListaGLownychZlozen(String art, Connection conn) throws SQLException
		 {
			Statement b = conn.createStatement();			
			ResultSet rs2 = b.executeQuery("select ARTIKELCODE, ONDERDEEL from struktury where ARTIKELCODE like '"+art+"%' order by ARTIKELCODE asc ");
			
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


