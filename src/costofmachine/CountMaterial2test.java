package costofmachine;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.itextpdf.text.DocumentException;

public class CountMaterial2test {
	 static String MASZYNA = Main.text; // na sztywno, potem mozliwosc zmiany w gui

	// private static ArrayList<ArrayList<String>> FirstLevelNomencaltuur;
	// private static ArrayList<String> FirstLevelNomencaltuur_temporary;
//	 private static boolean EmptyStrukture;
	 
//	 private static ArrayList<Struktury> ListOfCheckedArticels;
	 
//	 private static ArrayList<List<Struktury>> ListofStructures;
	 
//	 private static Struktury ArtZgodnyZkolejnoscia;
	 
	 static String Maszynka;
	 
	 private static ArrayList<String> ListaGlownychZlozen;
	 
	 private static Map<String,String> ListaGlownychZlozenIPodzlozen;

	 //testowa struktura, na potrzeby programu Asi
	 private static ArrayList<Struktury>ListofStructuresTest;
	 private static int iloscZaglebien;

	 
		public static void run() throws DocumentException, IOException {
			try {
			//	EmptyStrukture = false;
				Connection conn=DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");
				
			//	 ListOfCheckedArticels = new ArrayList<Struktury>();
				
					
					
					 ListofStructuresTest = new ArrayList<Struktury>();
					 iloscZaglebien= 0;
					 
					 Maszynka = "190523";
					 
					 ListaGlownychZlozen = new ArrayList<String>();
					 
					 ListaGlownychZlozenIPodzlozen = new LinkedHashMap<String,String>(); // LinkedHashMap - preserver the insertion order, have to used Linked one
					 getListaGLownychZlozen(Maszynka,conn);

					
					 		//remove not working structure
						for(int i = 0 ; i < ListaGlownychZlozen.size();i++)
							if(ListaGlownychZlozen.get(i).equals("%%360A-030-4000/000"))
								ListaGlownychZlozen.remove(i);
							
										Set<Entry<String,String>> entrySet = ListaGlownychZlozenIPodzlozen.entrySet();
										int it = 0;
										for(Entry<String, String> entry: entrySet) {
											System.out.println(" "+ it + ": " + entry.getKey() + " : " + entry.getValue());
											getPrice(entry.getKey(),conn);		
											iloscZaglebien= 0; // reset deppth of the structure
											it++;
										}
						
						
//							//iterate over structure
//						for(int i = 0 ; i < ListaGlownychZlozen.size();i++)
//						{
//							System.out.println("zlozenie: "+ ListaGlownychZlozen.get(i));
//							getPrice(ListaGlownychZlozen.get(i),conn);	
//							iloscZaglebien= 0; // reset deppth of the structure
//						}	
//					 
				
										

						
						
					 
					for(int i = 0 ; i < ListofStructuresTest.size();i++)
					{
																			
						
							String artikelkod = ListofStructuresTest.get(i).getONDERDEEL();
							
							
							Statement b = conn.createStatement();
							ResultSet rs2 = b.executeQuery("select ARTIKELCODE,MATERIAAL,LONEN from artikel_kostprijs where ARTIKELCODE = '"+artikelkod+"' and SOORT = '4'");
							
							if (!rs2.isBeforeFirst() ) {    									
								ListofStructuresTest.get(i).setCenaRazyIlosc(0.0);
							}
							else
							{
								while(rs2.next())
								{
									
								
								ListofStructuresTest.get(i).setCenaMaterialu(Double.parseDouble(rs2.getString("MATERIAAL")));
								Double cena = ListofStructuresTest.get(i).getCenaMaterialu();
								Double ilosc = ListofStructuresTest.get(i).getILOSC();
	
								
								Double cenaRazyIlosc = cena * ilosc;				
								ListofStructuresTest.get(i).setCenaRazyIlosc(cenaRazyIlosc);
								
								
								ListofStructuresTest.get(i).setCenaPracy((Double.parseDouble(rs2.getString("LONEN"))));

								
								}
							}
							b.close();
							rs2.close();
						
					}
					
					try (PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\el08\\Desktop\\programiki\\Moneyy.txt")))
					{					
						for(int i = 0 ; i <ListofStructuresTest.size();i++ )
						{
							out.println("---Poziom---: " + ListofStructuresTest.get(i).getPoziom());
							out.println("Seqwencja: " + ListofStructuresTest.get(i).getSeq());
							out.println("ARTIKELCODE: " + ListofStructuresTest.get(i).getARTIKELCODE());
							out.println("ONDERDEEL: " + ListofStructuresTest.get(i).getONDERDEEL());
							out.println("CFOMSONDERDEEL: " + ListofStructuresTest.get(i).getCFOMSONDERDEEL());
							out.println("TYP: " + ListofStructuresTest.get(i).getTYP());
							out.println("ILOSC: " + ListofStructuresTest.get(i).getILOSC());
							out.println("JEDNOSTKA: " + ListofStructuresTest.get(i).getJEDNOSTKA());
							out.println("Czy sprawdzony?: " + ListofStructuresTest.get(i).isCzySprawdzony());
							out.println("CenaMaterialu: " + ListofStructuresTest.get(i).getCenaMaterialu());
							out.println("CenaMaterialuRazyIlosc: " + ListofStructuresTest.get(i).getCenaRazyIlosc());
							out.println("Cena Pracy: " + ListofStructuresTest.get(i).getCenaPracy());
							out.println("                              ");
						}
					}				 
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			System.out.println("done");
	}
		
		
		public static void getPrice(String articlecode,Connection conn) throws SQLException{


			
			Statement b = conn.createStatement();			
			ResultSet rs2 = b.executeQuery("select seq,ARTIKELCODE,ONDERDEEL,CFOMSONDERDEEL,TYP,ILOSC,JEDNOSTKA from struktury where ARTIKELCODE = '"+articlecode+"' order by seq");
			
			Struktury StrukturaTmp; 


			while(rs2.next()){							
				
				StrukturaTmp = new Struktury(iloscZaglebien); // 0 mean level 0 
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
					getPrice(rs2.getString("onderdeel"),conn);
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
			
			ResultSet rs2 = b.executeQuery("select ARTIKELCODE, ONDERDEEL from struktury where ARTIKELCODE like '"+art+"%' order by seq asc ");
			
			while(rs2.next()){
			//	ListaGlownychZlozen.add(rs2.getString("ONDERDEEL"));
				ListaGlownychZlozenIPodzlozen.put(rs2.getString("ONDERDEEL"), rs2.getString("ARTIKELCODE"));
			}
			b.close();
			rs2.close();
			
		 }


		

		
}


