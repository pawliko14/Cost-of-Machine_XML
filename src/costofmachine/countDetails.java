package costofmachine;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class countDetails {

	private JFrame frame;
	private JTextField txtPodaj;
	private JTextField nrMaszyny1;
	String DEST = "C://Users/el08/Desktop/programiki/Cost_of_machine/Analiza_maszyn.pdf";
	Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
	 private PdfPTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					countDetails window = new countDetails();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public countDetails() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtPodaj = new JTextField();
		txtPodaj.setEditable(false);
		txtPodaj.setText("Type Machine number (2/170537)");
		txtPodaj.setBounds(45, 98, 181, 20);
		frame.getContentPane().add(txtPodaj);
		txtPodaj.setColumns(10);
		
		nrMaszyny1 = new JTextField();
		nrMaszyny1.setBounds(79, 141, 108, 20);
		frame.getContentPane().add(nrMaszyny1);
		nrMaszyny1.setColumns(10);
		
		JButton nrMaszyny_button = new JButton("OK");
		nrMaszyny_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(nrMaszyny1.getText().equals(null))
				{
				    JOptionPane.showMessageDialog(null, "Podaj nr. Maszyny");

				}
				else {
				
					try {
						CreatePDF(connection);
					    JOptionPane.showMessageDialog(null, "Raport wygenerowany");
	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		nrMaszyny_button.setBounds(224, 140, 89, 23);
		frame.getContentPane().add(nrMaszyny_button);
	}
	
	
	private void CreatePDF(Connection connection ) throws DocumentException, IOException, SQLException
	{
		String dest = "C://Users/el08/Desktop/programiki/Cost_of_machine/Analiza_maszyn_detail.pdf";
		
		System.out.println("\ngenerowanie PDf1\n");
		// GENEROWANIE DOKUMENTU PDF
	    Document document = new Document(PageSize.A2.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        
        
           document.open();

		   document.add(new Paragraph("Data wygenerowania raportu: "+new Date().toString()));
		   document.add(new Paragraph("\n\n"));
        
		  table = new PdfPTable(new float[] {150,120,550,250,150,100,200,175,175,175,175,150,100});
		  // table.setWidthPercentage(100);
		  
	        // header row:
	        table.addCell("nrMaszyny");
	        table.addCell("Opis");
	        table.addCell("Typ");
	        table.addCell("Klient");
	        table.addCell("cena");
	        table.addCell("Waluta");
	        table.addCell("DataKontraktu");
	        //table.addCell("Komentarz");
	        table.addCell("Material");
	        table.addCell("Workprice");
	        table.addCell("Montage");
	        table.addCell("Construction");
	        table.addCell("Electricians");
	        table.addCell("CNC");

	        table.setHeaderRows(1);
	        
	        document.add(table);
	        System.out.println("nrmaszyny: "+ nrMaszyny1.getText());
	        document.close();
	        
			System.out.println("Nr maszyny;Typ;Nazwa;Klient;Cena;Waluta;Data;Material;Workprice;Montage;Construction;Electricians;CNC");
			//dla maszyn
			Statement c = connection.createStatement();
			ResultSet rs2 = c.executeQuery("Select nrMaszyny, opis, typ, klient, cena, waluta, DataKontrakt, komentarz from Calendar where Wyslano = 1 and nrMaszyny = '"+nrMaszyny1+"' and klient is not null and klient <> 'MAGAZYN' and cena <> 0  order by typ asc, DataKontrakt asc" );
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
						+ "join storenotesdetail on storenotesdetail.PROJECTNUMMER  = '"+nrMaszyny1.getText()+"'"
						+ "join artikel_kostprijs on storenotesdetail.artikelcode = artikel_kostprijs.ARTIKELCODE where NrMaszyny = '"+nrMaszyny1.getText()+"' order by projectnummer desc";
				String sql_d = "select sum(bestellingdetail.suma), bestellingdetail.munt from bestelling " + 
						"join bestellingdetail on bestelling.leverancier = bestellingdetail.leverancier and bestelling.ORDERNUMMER = bestellingdetail.ORDERNUMMER ";
				String sql_b = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost NOT IN ('KM01', 'KE01', 'CNC') and (cfproject like '"+nrMaszyny1.getText()+"%'";
				String sql_e = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'KM01' and (cfproject like '"+nrMaszyny1.getText()+"%'";
				String sql_f = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'KE01' and (cfproject like '"+nrMaszyny1.getText()+"%'";
				String sql_g = "select (100*(sum(werktijdh)+floor(sum(werktijdm60)/60) + 10*mod(sum(werktijdm60), 60)/6)) as montage from werkuren where werkpost = 'CNC' and (cfproject like '"+nrMaszyny1.getText()+"%'";
				
				if(komentarz.equals("")) {
					sql_a = sql_a + " where NrMaszyny = '"+nrMaszyny1.getText()+"' order by projectnummer desc";
					sql_d = sql_d + " where concat(bestelling.AFDELING, \"/\", bestelling.AFDELINGSEQ) like '"+nrMaszyny1.getText()+"%' and bestelling.leverancier > 500 " +
							"group by munt";
				}
				else {
					sql_a = sql_a + " where NrMaszyny = '"+nrMaszyny1+"' or NrMaszyny like '"+komentarz+"' order by projectnummer desc";
					sql_d = sql_d + " where (concat(bestelling.AFDELING, \"/\", bestelling.AFDELINGSEQ) like '"+nrMaszyny1.getText()+"%' or concat(bestelling.AFDELING, \"/\", bestelling.AFDELINGSEQ) like '"+komentarz+"%') and bestelling.leverancier > 500 " +
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
				
				Statement a = connection.createStatement();
				ResultSet rs = a.executeQuery(sql_a);
				
				while(rs.next()){
					double ilosc = rs.getDouble("besteld");
					//check unit
					if(!rs.getString("besteleenheid").equals(rs.getString("cfstandaardeenheid"))) {
						Statement e = connection.createStatement();
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
				Statement b = connection.createStatement();
				ResultSet rs1 = b.executeQuery(sql_b);
				while(rs1.next()) {
					montage+=rs1.getDouble(1);
				}
				rs1.close();
				b.close();
				
				//koszt konstrukcji
				Statement e = connection.createStatement();
				ResultSet rs5 = e.executeQuery(sql_e);
				while(rs5.next()) {
					constr+=rs5.getDouble(1);
				}
				rs5.close();
				e.close();
				
				//koszt elektrykow
				Statement f = connection.createStatement();
				ResultSet rs6 = f.executeQuery(sql_f);
				while(rs6.next()) {
					el+=rs6.getDouble(1);
				}
				rs6.close();
				f.close();
				
				//koszt cnc
				Statement g = connection.createStatement();
				ResultSet rs7 = g.executeQuery(sql_g);
				while(rs7.next()) {
					cnc+=rs7.getDouble(1);
				}
				rs7.close();
				g.close();
				
				//koszt zamowien dodatkowych
				Statement d = connection.createStatement();
				
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
					
		    		document.add(table);
		    		table.flushContent();
		    			    						

			}    
	        
	        document.close();
			Desktop.getDesktop().open(new java.io.File(dest));

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
		ResultSet rs = a.executeQuery("Select lonen from artikel_kostprijs where artikelcode	 = '"+articlecode+"' and soort = 4");
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
