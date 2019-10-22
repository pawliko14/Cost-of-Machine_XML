package costofmachine;

import java.io.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;


public class countCost {
	
	 static PdfPTable table2;
	 static PdfPTable table;
	 static PdfPTable table3;
	 static PdfPTable table4;
	 static PdfPTable table5;
	 static PdfPTable table6;
	 static PdfPTable table7;
	 static PdfPTable table8;
	 static PdfPTable table9;

	 static String MASZYNA = Main.text; // na sztywno, potem mozliwosc zmiany w gui


	 private static float suma_material;
	 private static float suma_material_praca;
	 private static float suma_praca_i_material;
	 private static double podsumowanie_1;
	 static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

	
	public static void run() throws DocumentException, IOException {
		try {

			Connection conn=DriverManager.getConnection("jdbc:mariadb://192.168.90.123/test","listy","listy1234");
			String dest = Parameters.getPathOfSavingAnaliza_MaszynPDF();
			
			System.out.println("\ngenerowanie PDf1\n");
			// GENEROWANIE DOKUMENTU PDF
		    Document document = new Document(PageSize.A2.rotate());
	        PdfWriter.getInstance(document, new FileOutputStream(dest));
	        
	        
	        document.open();

			   document.add(new Paragraph("Data wygenerowania raportu: "+new Date().toString()));
			   document.add(new Paragraph("\n\n"));
	        
			  table = new PdfPTable(new float[] {150,120,460,220,150,100,175,150,150,150,150,150,100,150});
			  // table.setWidthPercentage(100);
			  


		        // header row:
		        table.addCell("nrMaszyny");
		        table.addCell("Opis");
		        table.addCell("Typ");
		        table.addCell("Klient");
		        table.addCell("cena");
		        table.addCell("Waluta");
		        table.addCell("DataKontraktu");
		        table.addCell("Material");
		        table.addCell("Workprice");
		        table.addCell("Montage");
		        table.addCell("Construction");
		        table.addCell("Electricians");
		        table.addCell("CNC");
		        table.addCell("Podsumowanie");

		        table.setHeaderRows(1);
	

			System.out.println("Nr maszyny;Typ;Nazwa;Klient;Cena;Waluta;Data;Material;Workprice;Montage;Construction;Electricians;CNC");
			//dla maszyn
			Statement c = conn.createStatement();
			ResultSet rs2 = c.executeQuery("Select nrMaszyny, opis, typ, klient, cena, waluta, DataKontrakt, komentarz from Calendar where Wyslano = 1 and nrMaszyny = '"+MASZYNA+"' and klient is not null and klient <> 'MAGAZYN' and cena <> 0  order by typ asc, DataKontrakt asc" );
			while(rs2.next()) {
				String nrMaszyny = rs2.getString("nrMaszyny");
				String typ = rs2.getString("typ");
				String nazwa = rs2.getString("opis");
				String klient = rs2.getString("klient");
				String cena = rs2.getString("cena");
				String waluta = rs2.getString("waluta");
				String data = rs2.getString("DataKontrakt");
				String komentarz = rs2.getString("komentarz");
				
				
				//SQLs
				String sql_a = "select storenotesdetail.PROJECTNUMMER, NRMASZYNY, leverancier, storenotesdetail.artikelcode, artikelomschrijving, storenotesdetail.BESTELD, storenotesdetail.BESTELEENHEID, artikel_kostprijs.MATERIAAL as material, artikel_kostprijs.LONEN as workprice, artikel_kostprijs.cfstandaardeenheid from CALENDAR "
						+ "join storenotesdetail on storenotesdetail.PROJECTNUMMER like concat(calendar.nrmaszyny, '%') "
						+ "join artikel_kostprijs on storenotesdetail.artikelcode = artikel_kostprijs.ARTIKELCODE ";
				String sql_d = "select sum(bestellingdetail.suma), bestellingdetail.munt from bestelling " + 
						"join bestellingdetail on bestelling.leverancier = bestellingdetail.leverancier and bestelling.ORDERNUMMER = bestellingdetail.ORDERNUMMER ";
				String sql_b = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost NOT IN ('KM01', 'KE01', 'CNC') and (cfproject like '"+nrMaszyny+"%'";
				String sql_e = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'KM01' and (cfproject like '"+nrMaszyny+"%'";
				String sql_f = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'KE01' and (cfproject like '"+nrMaszyny+"%'";
				String sql_g = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'CNC' and (cfproject like '"+nrMaszyny+"%'";
				
				if(komentarz.equals("")) {
					sql_a = sql_a + " where NrMaszyny = '"+nrMaszyny+"' order by projectnummer desc";
					sql_d = sql_d + " where concat(bestelling.AFDELING, \"/\", bestelling.AFDELINGSEQ) like '"+nrMaszyny+"%' and bestelling.leverancier > 500 " +
							"group by munt";
				}
				else {
					sql_a = sql_a + " where NrMaszyny = '"+nrMaszyny+"' or NrMaszyny like '"+komentarz+"' order by projectnummer desc";
					sql_d = sql_d + " where (concat(bestelling.AFDELING, \"/\", bestelling.AFDELINGSEQ) like '"+nrMaszyny+"%' or concat(bestelling.AFDELING, \"/\", bestelling.AFDELINGSEQ) like '"+komentarz+"%') and bestelling.leverancier > 500 " +
							" group by munt";
					sql_b = sql_b + " or cfproject like '"+komentarz+"'";
					sql_e = sql_e + " or cfproject like '"+komentarz+"'";
					sql_f = sql_f + " or cfproject like '"+komentarz+"'";
					sql_g = sql_g + " or cfproject like '"+komentarz+"'";
				}
				sql_b = sql_b + ")";
				sql_e = sql_e + ")";
				sql_f = sql_f + ")";
				sql_g = sql_g + ")";
				
				double material = 0;
				double workprice = 0;
				double montage = 0;
				double constr = 0;
				double cnc = 0; 
				double el = 0;
				System.out.println(nrMaszyny+";"+typ+";"+nazwa+";"+klient+";"+cena+";"+waluta+";"+data+";");
				
				Statement a = conn.createStatement();
				ResultSet rs = a.executeQuery(sql_a);
				
				while(rs.next()){
					double ilosc = rs.getDouble("besteld");
					//check unit
					if(!rs.getString("besteleenheid").equals(rs.getString("cfstandaardeenheid"))) {
						Statement e = conn.createStatement();
						ResultSet rs4 = e.executeQuery("Select hoeveelheid from artikel_alteenh where artikelcode = '"+rs.getString("artikelcode")+"' and eenheid = '"+rs.getString("besteleenheid")+"'");
						while(rs4.next()) {
							ilosc = ilosc*rs4.getDouble("hoeveelheid");
						}
						rs4.close();
						e.close();
					}
					if(rs.getString("artikelcode").startsWith("%") ){
						double [] price = getPrice(rs.getString("artikelcode"));
						material+=ilosc*price[0];
						workprice+=ilosc*price[1];
						montage+=ilosc*price[2];
					}
					else{
						if(rs.getString("artikelcode").startsWith("KM")) 
							constr += ilosc*rs.getDouble("workprice");
						else 
							workprice+= ilosc*rs.getDouble("workprice");
						material+=ilosc*rs.getDouble("material");
						
					}
				}
				rs.close();
				a.close();
				
				//koszt montazu
				Statement b = conn.createStatement();
				ResultSet rs1 = b.executeQuery(sql_b);
				while(rs1.next()) {
					montage+=rs1.getDouble(1);
				}
				rs1.close();
				b.close();
				
				//koszt konstrukcji
				Statement e = conn.createStatement();
				ResultSet rs5 = e.executeQuery(sql_e);
				while(rs5.next()) {
					constr+=rs5.getDouble(1);
				}
				rs5.close();
				e.close();
				
				//koszt elektrykow
				Statement f = conn.createStatement();
				ResultSet rs6 = f.executeQuery(sql_f);
				while(rs6.next()) {
					el+=rs6.getDouble(1);
				}
				rs6.close();
				f.close();
				
				//koszt cnc
				Statement g = conn.createStatement();
				ResultSet rs7 = g.executeQuery(sql_g);
				while(rs7.next()) {
					cnc+=rs7.getDouble(1);
				}
				rs7.close();
				g.close();
				
				//koszt zamowien dodatkowych
				Statement d = conn.createStatement();
				
				ResultSet rs3 = d.executeQuery(sql_d);
				while(rs3.next()) {
					if(rs3.getString("munt").equals("EUR")) {
						material += rs3.getDouble(1)/4.1;
					}
					else material += rs3.getDouble(1);
				}
				
				/*System.out.println("Cena materialu "+material);
				System.out.println("Cena produkcji "+workprice);
				System.out.println("Cena montazu "+montage);*/
				System.out.println(String.format( "%.2f",material)+";"+String.format( "%.2f",workprice)+";"+String.format( "%.2f",montage)+";"+String.format( "%.2f",constr)+";"+String.format( "%.2f",el)+";"+String.format( "%.2f",cnc));
			
				
				
			       //table.flushContent();

					
					PdfPCell table_cell1 = new PdfPCell(new Phrase(nrMaszyny));
		    		table.addCell(table_cell1);
		    		
		    		PdfPCell table_cell2 = new PdfPCell(new Phrase(typ));
		    		table.addCell(table_cell2);
		    		
		    		PdfPCell table_cell3 = new PdfPCell(new Phrase(nazwa));
		    		table.addCell(table_cell3);
		    		
		    		PdfPCell table_cell4 = new PdfPCell(new Phrase(klient));
		    		table.addCell(table_cell4);
		    		
		    		PdfPCell table_cell5 = new PdfPCell(new Phrase(cena));
		    		table.addCell(table_cell5);
		    		
		    		PdfPCell table_cell6 = new PdfPCell(new Phrase(waluta));
		    		table.addCell(table_cell6);
		    		
		    		PdfPCell table_cell7 = new PdfPCell(new Phrase(data));
		    		table.addCell(table_cell7);
		    		
		    		//PdfPCell table_cell8 = new PdfPCell(new Phrase(komentarz));
		    		//table.addCell(table_cell8);
		    		
		    		PdfPCell table_cell9 = new PdfPCell(new Phrase(String.format( "%.2f",material)));
		    		table.addCell(table_cell9);
		    		
		    		PdfPCell table_cell10 = new PdfPCell(new Phrase(String.format( "%.2f",workprice)));
		    		table.addCell(table_cell10);
		    		
		    		PdfPCell table_cell11 = new PdfPCell(new Phrase(String.format( "%.2f",montage)));
		    		table.addCell(table_cell11);
		    		
		    		PdfPCell table_cell12 = new PdfPCell(new Phrase(String.format( "%.2f",constr)));
		    		table.addCell(table_cell12);
		    		
		    		PdfPCell table_cell13 = new PdfPCell(new Phrase(String.format( "%.2f",el)));
		    		table.addCell(table_cell13);
		    		
		    		PdfPCell table_cell14 = new PdfPCell(new Phrase(String.format( "%.2f",cnc)));
		    		table.addCell(table_cell14);
		    		
		    		podsumowanie_1 = material + workprice + montage + constr + el + cnc;
					
		    		PdfPCell table_cell15 = new PdfPCell(new Phrase(String.format( "%.2f",podsumowanie_1),boldFont));
		    		table.addCell(table_cell15);
		    		
		    		document.add(table);
		    		table.flushContent();
		    			    						

			}
		
			// and artikel_kostprijs.MATERIAAL > 1 lub > 100
			String sql_00 = "select storenotesdetail.ORDERNUMMER, storenotesdetail.artikelcode,  artikelomschrijving, storenotesdetail.BESTELD, artikel_kostprijs.MATERIAAL as material, artikel_kostprijs.LONEN as workprice from CALENDAR join storenotesdetail on storenotesdetail.PROJECTNUMMER = '"+MASZYNA+"'" + 
					"join artikel_kostprijs on storenotesdetail.artikelcode = artikel_kostprijs.ARTIKELCODE where (NrMaszyny = '"+MASZYNA+"' )  order by material desc";
			Statement st00 = conn.createStatement();
			ResultSet rs00 = st00.executeQuery(sql_00);
			
			 document.add(new Paragraph("\n\n"));	
			  table2 = new PdfPTable(new float[] {150,250,520,55,150,150});

		        // header row:
			    table2.addCell("Numer zamowienia");
		        table2.addCell("Kod Artykulu");
		        table2.addCell("Artykul");
		        table2.addCell("Ilosc w sztukach");
		        table2.addCell("Cena /szt");
		        table2.addCell("Cena pracy");
		        table2.setHeaderRows(1);
		        
		      //  document.add(table2);
		        
		        int x = 1;		        
		        while(rs00.next()) {
		        	x++;
					int zmienna = (x%2 == 0) ? 1 : 0;

		        	String Order = rs00.getString(1);
		        	String kod = rs00.getString(2);
					String artykul = rs00.getString(3);
		        	String ilosc = rs00.getString(4);
		        	String cena = rs00.getString(5);
		        	String praca = rs00.getString(6);
		        	
		        	int c1= 255, c2=255, c3=255;
		        	if(zmienna ==1)   	{
		        		c1 = 226;
		        		c2 = 226;
		        		c3= 226;
		        	}
		        	
		        	PdfPCell table_cell0 = new PdfPCell(new Phrase(Order));
					table_cell0.setBackgroundColor(new BaseColor(c1, c2, c3)); 
		    		table2.addCell(table_cell0);
		    		
		        	 PdfPCell table_cell1 = new PdfPCell(new Phrase(kod));
						table_cell1.setBackgroundColor(new BaseColor(c1, c2, c3)); 
			    		table2.addCell(table_cell1);
			    		
			    		 PdfPCell table_cell2 = new PdfPCell(new Phrase(artykul));
							table_cell2.setBackgroundColor(new BaseColor(c1, c2, c3)); 
				    	table2.addCell(table_cell2);
				    		
				    	 PdfPCell table_cell3 = new PdfPCell(new Phrase(ilosc));
							table_cell3.setBackgroundColor(new BaseColor(c1, c2, c3)); 
					    table2.addCell(table_cell3);
					    		
					    PdfPCell table_cell4 = new PdfPCell(new Phrase(cena));
						table_cell4.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						  table2.addCell(table_cell4);
						    		
						 PdfPCell table_cell5 = new PdfPCell(new Phrase(praca));
							table_cell5.setBackgroundColor(new BaseColor(c1, c2, c3)); 
							table2.addCell(table_cell5);
							    				
							suma_material +=Float.parseFloat(ilosc)* Float.parseFloat(cena);
							suma_material_praca += Float.parseFloat(praca);
						document.add(table2);
						table2.flushContent();
		        	
		        }
				String waluta = "PLN";
				//	suma_material = 100;
				//	suma_material_praca  = 200;

						PdfPCell suma_cell1 = new PdfPCell(new Phrase(String.valueOf("")));
						table2.addCell(suma_cell1);
						PdfPCell suma_cell2 = new PdfPCell(new Phrase(String.valueOf("")));
			    		table2.addCell(suma_cell2);
				        PdfPCell suma_cell3 = new PdfPCell(new Phrase(String.valueOf("PODSUMOWANIE2"),boldFont));
			    		table2.addCell(suma_cell3);
			    		PdfPCell suma_cell4= new PdfPCell(new Phrase(waluta,boldFont));
			    		table2.addCell(suma_cell4);
			    		PdfPCell suma_cell5 = new PdfPCell(new Phrase(String.valueOf(suma_material),boldFont));
			    		table2.addCell(suma_cell5);
			    		PdfPCell suma_cell6 = new PdfPCell(new Phrase(String.valueOf(suma_material_praca),boldFont));
			    		table2.addCell(suma_cell6);
			    		
			    		document.add(table2);
			    		table2.flushContent();
			    		
			    		suma_material =0;
			    		suma_material_praca= 0;
		        
		        //pobiera liste ordernummber i wedlug niej bedzie pozniej iterowane
		        
				String sql_11 = "select * from storenotesdetail join artikel_kostprijs on storenotesdetail.artikelcode = artikel_kostprijs.ARTIKELCODE where PROJECTNUMMER = '"+MASZYNA+"' order by ORDERNUMMER\r\n" + 
						"";
				Statement st11 = conn.createStatement();
				ResultSet rs11 = st11.executeQuery(sql_11);
				
				
				// wczesniej bylo select distinct, aby wyelminowac powtorzenia, nei bylo sprawdzane
				String sql_22 = "select distinct ORDERNUMMER from storenotesdetail  where PROJECTNUMMER = '"+MASZYNA+"' order by ORDERNUMMER"; 

				Statement st22 = conn.createStatement();
				ResultSet rs22 = st22.executeQuery(sql_22);
				
				
				//misja - wepchn¹c tutaj liste orderow i wedlug niej potem zrobic dokment( aby bylo odseparowane od siebie)
				// najelpiej byloby zrobic jeszcze jakis kolor dla przejrzystosci
	
				
				// NIE BYLO KOMPILOWANE JESZCZE Z LISTA ORAZ FOREM!
				ArrayList<String> l_order = new ArrayList<String>();
				while(rs22.next()) {
					String uno = rs22.getString(1);
					l_order.add(uno);
				}
					//for(int i = 0 ; i<l_order.size();i++) {
				 document.add(new Paragraph("\n\n"));	
				  table3 = new PdfPTable(new float[] {100,100,255,40,150,80,80,80});

			        // header row:
			        table3.addCell("Ordernumber");
			        table3.addCell("ArtikelCode");
			        table3.addCell("artikelomschrivjing");
			        table3.addCell("Besteld");
			        table3.addCell("MOntagecostam");
			        table3.addCell("Material");
			        table3.addCell("Lonen");
			        table3.addCell("CFKprijs");
			        table3.setHeaderRows(1);
			        
			        
			        table4 = new PdfPTable(new float[] {100,100,255,40,150,80,80,80});

			        // header row:
//			        table4.addCell("Ordernumber");
//			        table4.addCell("ArtikelCode");
//			        table4.addCell("artikelomschrivjing");
//			        table4.addCell("Besteld");
//			        table4.addCell("MOntagecostam");
//			        table4.addCell("Material");
//			        table4.addCell("Lonen");
//			        table4.addCell("CFKprijs");
//			        table4.setHeaderRows(1);
			        
			      //  document.add(table2);
			        int index = 1;
			        while(rs11.next()) {
			        	x++;
						int zmienna = (x%2 == 0) ? 1 : 0;
			        	
						int c1= 255, c2=255, c3=255;
			        	if(zmienna ==1)   	{
			        		c1 = 226;
			        		c2 = 226;
			        		c3= 226;
			        	}
						
			        	String order = rs11.getString(2);
						String art = rs11.getString(4);
			        	String artk = rs11.getString(5);
			        	String besteld = rs11.getString(6);
			        	String montage = rs11.getString(15);
			        	String mat = rs11.getString(23);
			        	String lonen = rs11.getString(24);
			        	String CFK = rs11.getString(25);
			        	
						if(!order.equals(l_order.get(index-1)))
						{
							suma_cell1 = new PdfPCell(new Phrase(String.valueOf("")));
			        		table4.addCell(suma_cell1);
			        		suma_cell2 = new PdfPCell(new Phrase(String.valueOf("")));
			        		table4.addCell(suma_cell2);	        
							 suma_cell1 = new PdfPCell(new Phrase(String.valueOf("PODSUMOWANIE3"),boldFont));
							table4.addCell(suma_cell1);
							 suma_cell2 = new PdfPCell(new Phrase(String.valueOf("")));
				    		table4.addCell(suma_cell2);
					         suma_cell3 = new PdfPCell(new Phrase(waluta,boldFont));
				    		table4.addCell(suma_cell3);
				    		 suma_cell4= new PdfPCell(new Phrase(String.valueOf(suma_material),boldFont));
				    		table4.addCell(suma_cell4);
				    		 suma_cell5 = new PdfPCell(new Phrase(String.valueOf(suma_material_praca),boldFont));
				    		table4.addCell(suma_cell5);
				    		 suma_cell6 = new PdfPCell(new Phrase(String.valueOf(suma_praca_i_material),boldFont));
				    		table4.addCell(suma_cell6);
				    		
				    		document.add(table4);
				    		table4.flushContent();
							
				    		
				    		
							document.add(new Paragraph("\n\n"));
							index++;
							
							suma_material = 0;
				    		suma_material_praca= 0;
				    		suma_praca_i_material = 0;

						}

			        	 PdfPCell table_cell1 = new PdfPCell(new Phrase(order));
							table_cell1.setBackgroundColor(new BaseColor(c1, c2, c3)); 
				    		table3.addCell(table_cell1);
				    		
				    		 PdfPCell table_cell2 = new PdfPCell(new Phrase(art));
				    		 table_cell2.setBackgroundColor(new BaseColor(c1, c2, c3));
					    	table3.addCell(table_cell2);
					    		
					    	 PdfPCell table_cell3 = new PdfPCell(new Phrase(artk));
					    	 table_cell3.setBackgroundColor(new BaseColor(c1, c2, c3));
						    table3.addCell(table_cell3);
						    		
						    PdfPCell table_cell4 = new PdfPCell(new Phrase(besteld));
						    table_cell4.setBackgroundColor(new BaseColor(c1, c2, c3));
							  table3.addCell(table_cell4);
							    		
							 PdfPCell table_cell5 = new PdfPCell(new Phrase(montage));
							 table_cell5.setBackgroundColor(new BaseColor(c1, c2, c3));
								table3.addCell(table_cell5);
								
								PdfPCell table_cell6 = new PdfPCell(new Phrase(mat));
								table_cell6.setBackgroundColor(new BaseColor(c1, c2, c3));
								table3.addCell(table_cell6);
								
								PdfPCell table_cell7 = new PdfPCell(new Phrase(lonen));
								table_cell7.setBackgroundColor(new BaseColor(c1, c2, c3));
								table3.addCell(table_cell7);
								
								PdfPCell table_cell8 = new PdfPCell(new Phrase(CFK));
								table_cell8.setBackgroundColor(new BaseColor(c1, c2, c3));
								table3.addCell(table_cell8);
								    			
								
								suma_material +=Float.parseFloat(besteld)* Float.parseFloat(mat);
								suma_material_praca += Float.parseFloat(lonen);
								suma_praca_i_material+=Float.parseFloat(besteld)* Float.parseFloat(CFK);
								
								System.out.println("order: "+order+" l_order[index]: "+l_order.get(index-1)+"\n");
	
								
							document.add(table3);
							table3.flushContent();
							

			        	}
			        	        
			        System.out.println("index size: "+ index+"\n");
			        
			        
			        
					 sql_00 = "select storenotesdetail.ORDERNUMMER, storenotesdetail.artikelcode,  artikelomschrijving, storenotesdetail.BESTELD, artikel_kostprijs.MATERIAAL as material, artikel_kostprijs.LONEN as workprice from CALENDAR join storenotesdetail on storenotesdetail.PROJECTNUMMER = '"+MASZYNA+"'" + 
							"join artikel_kostprijs on storenotesdetail.artikelcode = artikel_kostprijs.ARTIKELCODE where (NrMaszyny = '"+MASZYNA+"' )  order by material desc";
					 st00 = conn.createStatement();
					 rs00 = st00.executeQuery(sql_00);
					
					 document.add(new Paragraph("\n\n"));	
					  table2 = new PdfPTable(new float[] {150,250,520,55,150,150});

				        // header row:
					    table2.addCell("Numer zamowienia");
				        table2.addCell("Kod Artykulu");
				        table2.addCell("Artykul");
				        table2.addCell("Ilosc w sztukach");
				        table2.addCell("Cena /szt");
				        table2.addCell("Cena pracy");
				        table2.setHeaderRows(1);
				        
				        
				        String czesc = null;
				        String il= null;
				        String jednostka= null;
				        String nazwa_el= null;
				        String material_c= null;
				        String workprice_c= null;
				        
				        
				        suma_praca_i_material = 0;
				        suma_material = 0;
				        
				        while(rs00.next()) {        	
						if(rs00.getString("artikelcode").startsWith("%") ){
							
							String artikel_proc = rs00.getString("artikelcode");
							String nazwa_elementu = rs00.getString("artikelomschrijving");
							String sql01 = "select onderdeel, typ, ilosc, jednostka, CFOMSONDERDEEL ,materiaal as material, lonen as workprice, artikel_kostprijs.cfstandaardeenheid from struktury \r\n" + 
									"left join artikel_kostprijs on struktury.onderdeel = artikel_kostprijs.artikelcode where struktury.artikelcode = '"+artikel_proc +"' and artikel_kostprijs.soort = 4";
							
							Statement st01 = conn.createStatement();
							ResultSet rs01 = st01.executeQuery(sql01);
							
							   document.add(new Paragraph("\n\n"));	
							
							   
							   
							    
							   table6 = new PdfPTable(new float[] {400,400});
							   

							 	PdfPCell table_cell111 = new PdfPCell(new Phrase(artikel_proc,boldFont));
							 	table6.addCell(table_cell111);
							 	PdfPCell table_cell222 = new PdfPCell(new Phrase(nazwa_elementu,boldFont));
							 	table6.addCell(table_cell222);
							 	document.add(table6);
							 	table6.flushContent();
							   
							   
							while(rs01.next()) {
								czesc = rs01.getString(1);
								il = rs01.getString(3);
								jednostka = rs01.getString(4);
								nazwa_el = rs01.getString(5);
								material_c = rs01.getString(6);
								workprice_c = rs01.getString(7);
								
							
								
								x++;
								int zmienna = (x%2 == 0) ? 1 : 0;
					        	
								int c1= 255, c2=255, c3=255;
					        	if(zmienna ==1)   {
					        		c1 = 226;
					        		c2 = 226;
					        		c3= 226;
					        	}
								
								   table5 = new PdfPTable(new float[] {100,10,50,200,50,50});
								  

								 	PdfPCell table_cell1 = new PdfPCell(new Phrase(czesc));
									table_cell1.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						    		table5.addCell(table_cell1);
						    		
						    		PdfPCell table_cell2 = new PdfPCell(new Phrase(il));
									table_cell2.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						    		table5.addCell(table_cell2);
						    		
						    		PdfPCell table_cell3 = new PdfPCell(new Phrase(jednostka));
									table_cell3.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						    		table5.addCell(table_cell3);
						    		
						    		PdfPCell table_cell4 = new PdfPCell(new Phrase(nazwa_el));
									table_cell4.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						    		table5.addCell(table_cell4);
						    		
						    		PdfPCell table_cell5 = new PdfPCell(new Phrase(material_c));
									table_cell5.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						    		table5.addCell(table_cell5);
						    		
						    		PdfPCell table_cell6 = new PdfPCell(new Phrase(workprice_c));
									table_cell6.setBackgroundColor(new BaseColor(c1, c2, c3)); 
						    		table5.addCell(table_cell6);
						    		
						    		
						    		suma_material +=Float.parseFloat(il)* Float.parseFloat(material_c);
									//suma_material_praca += Float.parseFloat(lonen);
									suma_praca_i_material+=Float.parseFloat(il)* Float.parseFloat(workprice_c);
								
								document.add(table5);
								table5.flushContent();
								
								
									if (rs01.getString("onderdeel").startsWith("%") || rs01.getString("CFOMSONDERDEEL").startsWith("%") || rs01.getString("CFOMSONDERDEEL").startsWith("W$") ) {
										
									
									String artikel_proc_2_poz = rs01.getString("onderdeel");

									String sql02 = "select onderdeel, typ, ilosc, CFOMSONDERDEEL, materiaal as material, lonen as workprice from struktury \r\n" + 
											"left join artikel_kostprijs on struktury.onderdeel = artikel_kostprijs.artikelcode where struktury.artikelcode = '"+artikel_proc_2_poz+"' and artikel_kostprijs.soort = 4 " ;

									
									Statement st02 = conn.createStatement();
									ResultSet rs02 = st02.executeQuery(sql02);
									
									   table7 = new PdfPTable(new float[] {80,30,30,100,50,50});
									   table7.setWidthPercentage(77);
									   
									    table7.addCell("zamowienie");
								        table7.addCell("TYP artykulu");
								        table7.addCell("ilosc");
								        table7.addCell("Artykul");
								        table7.addCell("Cena /szt");
								        table7.addCell("Cena pracy");
								        table7.setHeaderRows(1);

									   
								        int x1012 = 0;
									   while(rs02.next()) {
										   String onderdeel = rs02.getString(1);
										   String TYP= rs02.getString(2);
										   String artyk= rs02.getString(3);
										   String jedno= rs02.getString(4);
										   String material = rs02.getString(5);
										   String workprice = rs02.getString(6);
										   
										  

										   x1012++;
											 zmienna = (x1012%2 == 0) ? 1 : 0;
								        	
								        	if(zmienna ==1)   {
								        		c1 = 226;
								        		c2 = 226;
								        		c3= 226;
								        	}
								        	
								        	
								        	PdfPCell table_cell10 = new PdfPCell(new Phrase(onderdeel));
											table_cell10.setBackgroundColor(new BaseColor(c1, c2, c3)); 
								    		table7.addCell(table_cell10);
								    		
								    		PdfPCell table_cell20 = new PdfPCell(new Phrase(TYP));
											table_cell20.setBackgroundColor(new BaseColor(c1, c2, c3)); 
								    		table7.addCell(table_cell20);
								    		
								    		PdfPCell table_cell30 = new PdfPCell(new Phrase(artyk));
											table_cell10.setBackgroundColor(new BaseColor(c1, c2, c3)); 
								    		table7.addCell(table_cell30);
								    		
								    		PdfPCell table_cell40 = new PdfPCell(new Phrase(jedno));
											table_cell20.setBackgroundColor(new BaseColor(c1, c2, c3)); 
								    		table7.addCell(table_cell40);
								    		
								    		PdfPCell table_cell50 = new PdfPCell(new Phrase(material));
											table_cell50.setBackgroundColor(new BaseColor(c1, c2, c3)); 
								    		table7.addCell(table_cell50);
								    		
								    		PdfPCell table_cell60= new PdfPCell(new Phrase(workprice));
											table_cell60.setBackgroundColor(new BaseColor(c1, c2, c3)); 
								    		table7.addCell(table_cell60);
								    		
								    		suma_material +=Float.parseFloat(artyk)* Float.parseFloat(material);
											suma_material_praca += Float.parseFloat(workprice);
								    		
								    		document.add(table7);
											table7.flushContent();
											
											 if(rs02.getString("CFOMSONDERDEEL").startsWith("W$") ) {
												   
													String artikel_proc_3_poz = rs02.getString("onderdeel");

													String sql03 = "select onderdeel, typ, ilosc, CFOMSONDERDEEL, materiaal as material, lonen as workprice from struktury \r\n" + 
															"left join artikel_kostprijs on struktury.onderdeel = artikel_kostprijs.artikelcode where struktury.artikelcode = '"+artikel_proc_3_poz+"' and artikel_kostprijs.soort = 4 " ;

													Statement st03 = conn.createStatement();
													ResultSet rs03 = st03.executeQuery(sql03);
													
													   table9 = new PdfPTable(new float[] {80,30,30,100,50,50});
													   table9.setWidthPercentage(67);
													   
													    table9.addCell("zamowienie");
												        table9.addCell("TYP artykulu");
												        table9.addCell("ilosc");
												        table9.addCell("Artykul");
												        table9.addCell("Cena /szt");
												        table9.addCell("Cena pracy");
												        table9.setHeaderRows(1);
												        
												 	   while(rs03.next()) {
														   String onderdeel1 = rs03.getString(1);
														   String TYP1= rs03.getString(2);
														   String artyk1= rs03.getString(3);
														   String jedno1= rs03.getString(4);
														   String material1 = rs03.getString(5);
														   String workprice1 = rs03.getString(6);
														   
														   x++;
															 zmienna = (x%2 == 0) ? 1 : 0;
												        	
												        	if(zmienna ==1)   {
												        		c1 = 226;
												        		c2 = 226;
												        		c3= 226;
												        	}
												        	
												          	PdfPCell table_cell100 = new PdfPCell(new Phrase(onderdeel1));
															table_cell100.setBackgroundColor(new BaseColor(c1, c2, c3)); 
												    		table9.addCell(table_cell100);
												    		
												    		PdfPCell table_cell200 = new PdfPCell(new Phrase(TYP1));
															table_cell200.setBackgroundColor(new BaseColor(c1, c2, c3)); 
												    		table9.addCell(table_cell200);
												    		
												    		PdfPCell table_cell300 = new PdfPCell(new Phrase(artyk1));
															table_cell100.setBackgroundColor(new BaseColor(c1, c2, c3)); 
												    		table9.addCell(table_cell300);
												    		
												    		PdfPCell table_cell400 = new PdfPCell(new Phrase(jedno1));
															table_cell200.setBackgroundColor(new BaseColor(c1, c2, c3)); 
												    		table9.addCell(table_cell400);
												    		
												    		PdfPCell table_cell500 = new PdfPCell(new Phrase(material1));
															table_cell500.setBackgroundColor(new BaseColor(c1, c2, c3)); 
												    		table9.addCell(table_cell500);
												    		
												    		PdfPCell table_cell600= new PdfPCell(new Phrase(workprice1));
															table_cell600.setBackgroundColor(new BaseColor(c1, c2, c3)); 
												    		table9.addCell(table_cell600);
												    		
												    		suma_material +=Float.parseFloat(artyk1)* Float.parseFloat(material1);
															suma_material_praca += Float.parseFloat(workprice1);
												    		
												    		document.add(table9);
															table9.flushContent();
												        	
												 	   }
												 	   podsumowanie(table9,document,67,1);
														suma_material = 0;
														suma_material_praca = 0;
												   
											   }
								        	
									   }
										podsumowanie(table7,document,77,1);
										suma_material = 0;
										suma_material_praca = 0;

								}							
						
							}
							
								podsumowanie(table6,document,80,0);

					    		
					    		suma_material = 0;
					    		suma_praca_i_material= 0;
						}	        	
				        }
			        
	        
			       // }
			
			
			//dla innych projektow
//			String sql_00 = "Select bestelling.leverancier, leverancier.naam from bestelling "
//					+ "join leverancier on bestelling.leverancier = leverancier.leveranciernr " + 
//					"where bestelling.leverdatum >= '"+from+"' and bestelling.leverdatum <= '"+to+"' and bestelling.leverancier < 50 and bestelling.leverancier not in (2, 6) " + 
//					"group by leverancier";
//			String sql_01 = "Select bestelling.leverancier, bestelling.ordernummer, storenotesdetail.artikelcode, storenotesdetail.besteld, storenotesdetail.besteleenheid, artikel_kostprijs.MATERIAAL as material, artikel_kostprijs.LONEN as workprice, artikel_kostprijs.cfstandaardeenheid from bestelling " + 
//					"join storenotesdetail on bestelling.leverancier = storenotesdetail.afdeling and bestelling.ordernummer = storenotesdetail.afdelingseq " + 
//					"join artikel_kostprijs on storenotesdetail.artikelcode = artikel_kostprijs.artikelcode " + 
//					"where bestelling.leverdatum >= '"+from+"' and bestelling.leverdatum <= '"+to+"' and bestelling.leverancier = ";
//			Statement st00 = conn.createStatement();
//			ResultSet rs00 = st00.executeQuery(sql_00);
//			
//			
//			 document.add(new Paragraph("\n\n"));	
//			 table2 = new PdfPTable(6);
//			 
//		        // header row:
//		        table2.addCell("Material");
//		        table2.addCell("Workprice");
//		        table2.addCell("Montage");
//		        table2.addCell("Construction");
//		        table2.addCell("Electronics");
//		        table2.addCell("CNC");
//		        table2.setHeaderRows(1);

			
//			while(rs00.next()) {
//				String nrGrupy = rs00.getString(1);
//				String nazwa = rs00.getString(2);
//				double material = 0;
//				double workprice = 0;
//				double montage = 0;
//				double constr = 0;
//				double cnc = 0; 
//				double el = 0;
//				System.out.println("NUMER GRUPY:"+nrGrupy );
//				System.out.print(nrGrupy+";;"+nazwa+";;;;;");
//				//pobierz cene czesci
//				Statement st01 = conn.createStatement();
//				ResultSet rs01 = st01.executeQuery(sql_01+rs00.getInt(1));
//				
//				
//				
//				  
//				while(rs01.next()) {
//					double ilosc = rs01.getDouble("besteld");
//					//check unit
//					if(!rs01.getString("besteleenheid").equals(rs01.getString("cfstandaardeenheid"))) {
//						Statement e = conn.createStatement();
//						ResultSet rs4 = e.executeQuery("Select hoeveelheid from artikel_alteenh where artikelcode = '"+rs01.getString("artikelcode")+"' and eenheid = '"+rs01.getString("besteleenheid")+"'");
//						while(rs4.next()) {
//							ilosc = ilosc*rs4.getDouble("hoeveelheid");
//						}
//						rs4.close();
//						e.close();
//					}
//					if(rs01.getString("artikelcode").startsWith("%") ){
//						double [] price = getPrice(rs01.getString("artikelcode"));
//						material+=ilosc*price[0];
//						workprice+=ilosc*price[1];
//						montage+=ilosc*price[2];
//					}
//					else{
//						if(rs01.getString("artikelcode").startsWith("KM")) 
//							constr += ilosc*rs01.getDouble("workprice");
//						else 
//							workprice+= ilosc*rs01.getDouble("workprice");
//						material+=ilosc*rs01.getDouble("material");
//						
//					}
//					
//				}
//				rs01.close();
//				st01.close();
//				
//				String sql_d = "select sum(bestellingdetail.suma), bestellingdetail.munt from bestelling " + 
//						"join bestellingdetail on bestelling.leverancier = bestellingdetail.leverancier and bestelling.ORDERNUMMER = bestellingdetail.ORDERNUMMER where bestelling.AFDELING= "+nrGrupy+" and bestelling.leverancier > 500  group by munt";
//				String sql_b = "select (100*sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6) as montage from werkuren where werkpost NOT IN ('KM01', 'KE01', 'CNC') and (cfproject like '"+nrGrupy+"/%')";
//				String sql_e = "select (100*sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6) as montage from werkuren where werkpost = 'KM01' and (cfproject like '"+nrGrupy+"/%')";
//				String sql_f = "select (100*sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6) as montage from werkuren where werkpost = 'KE01' and (cfproject like '"+nrGrupy+"/%')";
//				String sql_g = "select (100*sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6) as montage from werkuren where werkpost = 'CNC' and (cfproject like '"+nrGrupy+"/%')";
//				
//				
//				//koszt montazu
//				Statement b = conn.createStatement();
//				ResultSet rs1 = b.executeQuery(sql_b);
//				while(rs1.next()) {
//					workprice+=rs1.getDouble(1);
//				}
//				rs1.close();
//				b.close();
//				
//				//koszt konstrukcji
//				Statement e = conn.createStatement();
//				ResultSet rs5 = e.executeQuery(sql_e);
//				while(rs5.next()) {
//					constr+=rs5.getDouble(1);
//				}
//				rs5.close();
//				e.close();
//				
//				//koszt elektrykow
//				Statement f = conn.createStatement();
//				ResultSet rs6 = f.executeQuery(sql_f);
//				while(rs6.next()) {
//					el+=rs6.getDouble(1);
//				}
//				rs6.close();
//				f.close();
//				
//				//koszt cnc
//				Statement g = conn.createStatement();
//				ResultSet rs7 = g.executeQuery(sql_g);
//				while(rs7.next()) {
//					cnc+=rs7.getDouble(1);
//				}
//				rs7.close();
//				g.close();
//				
//				//koszt zamowien dodatkowych
//				Statement d = conn.createStatement();
//				
//				ResultSet rs3 = d.executeQuery(sql_d);
//				while(rs3.next()) {
//					if(rs3.getString("munt").equals("EUR")) {
//						material += rs3.getDouble(1)/4.1;
//					}
//					else material += rs3.getDouble(1);
//				}
//				System.out.println("\ndla innych projektow:\n");
//				System.out.println(String.format( "%.2f",material)+";"+String.format( "%.2f",workprice)+";"+String.format( "%.2f",montage)+";"+String.format( "%.2f",constr)+";"+String.format( "%.2f",el)+";"+String.format( "%.2f",cnc));
//						
//			}	
			conn.close();
			
			document.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        
	}
	
	public static void podsumowanie(PdfPTable tabela, Document document,int procent,int status) throws DocumentException {
		if(status == 0)
		{
		 tabela = new PdfPTable(new float[] {360,80,80});	

		}
		else {
		   tabela = new PdfPTable(new float[] {360,75,75});	
		}
		   tabela.setWidthPercentage(procent);
		   PdfPCell table_cell000 = new PdfPCell(new Phrase("PODSUMOWANIE",boldFont));
		   tabela.addCell(table_cell000);
    		
    		PdfPCell table_cell001 = new PdfPCell(new Phrase(String.valueOf(suma_material), boldFont));
    		tabela.addCell(table_cell001);
    		
    		PdfPCell table_cell002 = new PdfPCell(new Phrase(String.valueOf(suma_material_praca), boldFont));
    		tabela.addCell(table_cell002);
    		 
    		document.add(tabela);
    		tabela.flushContent();
		
	}
	
	
	public static double[] getPrice(String articlecode) throws SQLException{
		Connection conn=DriverManager.getConnection("jdbc:mariadb://192.168.90.123/fatdb","listy","listy1234");
		double[] tab = new double[3];
		//koszt materialow
		tab[0] = 0; 
		//koszt produkcji 
		tab[1] = 0; 
		//koszt montazu
		tab[2] = 0;
				
		Statement a = conn.createStatement();
		ResultSet rs = a.executeQuery("Select lonen from artikel_kostprijs where artikelcode = '"+articlecode+"' and soort = 4");
		while(rs.next()){
			//System.out.println("Cena montazu "+articlecode+" "+rs.getDouble(1));
			tab[2]+=rs.getDouble(1);
		}
		a.close();
		rs.close();
		
		Statement b = conn.createStatement();
		ResultSet rs2 = b.executeQuery("select onderdeel, typ, ilosc, jednostka, materiaal as material, lonen as workprice, artikel_kostprijs.cfstandaardeenheid from struktury left join artikel_kostprijs on struktury.onderdeel = artikel_kostprijs.artikelcode where struktury.artikelcode = '"+articlecode+"' and artikel_kostprijs.soort = 4");
		while(rs2.next()){
			double ilosc = rs2.getDouble("ilosc");
			//check unit
			if(!rs2.getString("jednostka").equals(rs2.getString("cfstandaardeenheid"))) {
				Statement e = conn.createStatement();
				ResultSet rs4 = e.executeQuery("Select hoeveelheid from artikel_alteenh where artikelcode = '"+rs2.getString("onderdeel")+"' and eenheid = '"+rs2.getString("jednostka")+"'");
				while(rs4.next()) {
					ilosc = ilosc*rs4.getDouble("hoeveelheid");
				}
				rs4.close();
				e.close();
			}
			
			if(rs2.getString("typ").equals("F")||rs2.getString("onderdeel").startsWith("%")){
				double[] tab_tmp = getPrice(rs2.getString("onderdeel"));
				tab[0]+=ilosc*tab_tmp[0];
				tab[1]+=ilosc*tab_tmp[1];
				
			}
			else{
				tab[0] += ilosc*rs2.getDouble("material");
				tab[1] += ilosc*rs2.getDouble("workprice"); 
			}
		}
		b.close();
		rs2.close();
		conn.close();
		return tab;
		
	}


}
