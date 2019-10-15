package costofmachine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

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

public class GenerateDocument {
	
	 private static DecimalFormat decForm = new DecimalFormat("#.###");

	
	GenerateDocument()
	{
		
	}
	
	public void Generate(ArrayList<Struktury> List, Connection conn, String Maszynka ) throws FileNotFoundException, DocumentException, SQLException
	{

		String dest = "C://Users/el08/Desktop/programiki/Cost_of_machine/Analiza_maszyn.pdf";
		 PdfPTable table;
		 PdfPTable table1 = null;

		System.out.println("\ngenerowanie PDf1\n");
		// GENEROWANIE DOKUMENTU PDF
	    Document document = new Document(PageSize.A2.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        
        
       
        
        document.open();

		   document.add(new Paragraph("Data wygenerowania raportu: "+new Date().toString()));
		   document.add(new Paragraph("\n\n"));
        
		   
		   
			  table = new PdfPTable(new float[] {650,400,100,100,100,100,100,50});
			  table.setTotalWidth(1600);
    		  table.setLockedWidth(true);
    		  table.setHorizontalAlignment(Element.ALIGN_RIGHT);


		        // header row:
		        table.addCell("Artykul");
		        table.addCell("Opis");
		        table.addCell("ilosc");
		        table.addCell("Jednostka");		       
		        table.addCell("Cena materialu szt");
		        table.addCell("Cena materialu * ilosc");
		        table.addCell("Cena pracy szt");
		        table.addCell("TYP");

		        
		        table.setHeaderRows(1);
		        
		        
		        PdfPCell table_cell1 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell1);
				
				PdfPCell table_cell2 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell2);
				
				PdfPCell table_cell3 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell3);
				
				PdfPCell table_cell4 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell4);
				
				PdfPCell table_cell5 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell5);
				
				PdfPCell table_cell6 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell6);
				
				PdfPCell table_cell7 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell7);
				
				PdfPCell table_cell8 = new PdfPCell(new Phrase(""));
				table.addCell(table_cell8);
		      	    		
		        document.add(table);
		        
		        		
		        int x1012 = 0;
		   for(int i = 0 ; i <List.size() ; i++)
		   {
				int zmienna = (x1012%2 == 0) ? 1 : 0;
	        	
				int c1= 255, c2=255, c3=255;
	        	if(zmienna ==1)   {
	        		c1 = 226;
	        		c2 = 226;
	        		c3= 226;
	        	}
			   
			   
		   		if(List.get(i).getPoziom()==0)
					{
		   			int c11= 169;
		   			int c21=169; 
		   			int c31=169;
		   			
		   				// jesli jest poziom 0 to tez znajdzie sie projekt nadrzedny
		   			PdfPTable table0;
		   			table0 = new PdfPTable(new float[] {30,600,400,100,100,100,100,100,50});
		   			table0.setTotalWidth(1580);
		   			table0.setLockedWidth(true);
		   			table0.setHorizontalAlignment(Element.ALIGN_RIGHT);

		   			
		   		 PdfPCell table_cell000 = new PdfPCell(new Phrase("ZG"));
		   		 table_cell000.setBackgroundColor(new BaseColor(c11, c21, c31)); 
   		   	  	table0.addCell(table_cell000);
		   			
   		   	  PdfPCell table_cell01 = new PdfPCell(new Phrase(List.get(i).getARTIKELCODE()));
   		   table_cell01.setBackgroundColor(new BaseColor(c11, c21, c31));
   		   	  	table0.addCell(table_cell01);
				
				PdfPCell table_cell02 = new PdfPCell(new Phrase(GetCfomsendreel(conn,Maszynka,List.get(i).getARTIKELCODE())));
				table_cell02.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell02);
				
				PdfPCell table_cell03 = new PdfPCell(new Phrase(List.get(i).getILOSC().toString()));
				table_cell03.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell03);
				
				PdfPCell table_cell04 = new PdfPCell(new Phrase(List.get(i).getJEDNOSTKA()));
				table_cell04.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell04);
				
				PdfPCell table_cell05 = new PdfPCell(new Phrase("--"));
				table_cell05.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell05);
				
				PdfPCell table_cell06 = new PdfPCell(new Phrase("--"));
				table_cell06.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell06);
				
				PdfPCell table_cell07 = new PdfPCell(new Phrase("--"));
				table_cell07.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell07);
				
				PdfPCell table_cell08 = new PdfPCell(new Phrase("--"));
				table_cell08.setBackgroundColor(new BaseColor(c11, c21, c31));
				table0.addCell(table_cell08);
		   			
				document.add(table0);
		   			
		   		  table1 = new PdfPTable(new float[] {30,550,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1530);
	    		  table1.setLockedWidth(true);
						}
		   		else if(List.get(i).getPoziom()==1)
		   		{
		   		  table1 = new PdfPTable(new float[] {30,500,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1480);
	    		  table1.setLockedWidth(true);
		   		}
		   		else if(List.get(i).getPoziom()==2)
		   		{
		   		  table1 = new PdfPTable(new float[] {30,450,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1430);
	    		  table1.setLockedWidth(true);
		   		}
		   		else if(List.get(i).getPoziom()==3)
		   		{
		   		  table1 = new PdfPTable(new float[] {30,400,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1380);
	    		  table1.setLockedWidth(true);
		   		}
		   		else if(List.get(i).getPoziom()==4)
		   		{
		   		  table1 = new PdfPTable(new float[] {30,350,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1330);
	    		  table1.setLockedWidth(true);
		   		}
		   		else if(List.get(i).getPoziom()==5)
		   		{
		   		  table1 = new PdfPTable(new float[] {30,300,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1280);
	    		  table1.setLockedWidth(true);
		   		}
		   		else if(List.get(i).getPoziom()==6)
		   		{
		   		  table1 = new PdfPTable(new float[] {30,250,400,100,100,100,100,100,50});
	    		  table1.setTotalWidth(1230);
	    		  table1.setLockedWidth(true);
		   		}

    		
		   		table1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		   		
				PdfPCell table_cell10 = new PdfPCell(new Phrase(List.get(i).getPoziom().toString()));
				table_cell10.setBackgroundColor(new BaseColor(c1, c2, c3)); 
				table1.addCell(table_cell10);
		   		
				PdfPCell table_cell12 = new PdfPCell(new Phrase(List.get(i).getONDERDEEL()));
				table_cell12.setBackgroundColor(new BaseColor(c1, c2, c3)); 
				table1.addCell(table_cell12);
		      	    		
	    		PdfPCell table_cell22 = new PdfPCell(new Phrase(List.get(i).getCFOMSONDERDEEL()));
	    		table_cell22.setBackgroundColor(new BaseColor(c1, c2, c3));
	    		table1.addCell(table_cell22);
	    		
	    		PdfPCell table_cell32 = new PdfPCell(new Phrase(List.get(i).getILOSC().toString()));
	    		table_cell32.setBackgroundColor(new BaseColor(c1, c2, c3));
	    		table1.addCell(table_cell32);
	    		
	    		PdfPCell table_cell42 = new PdfPCell(new Phrase(List.get(i).getJEDNOSTKA()));
	    		table_cell42.setBackgroundColor(new BaseColor(c1, c2, c3));
	    		table1.addCell(table_cell42);
	    		
	    		if(List.get(i).getCenaMaterialu().equals(null) || List.get(i).getCenaMaterialu()==0 || List.get(i).getCenaMaterialu()== null)
	    		{
	    			PdfPCell table_cell52 = new PdfPCell(new Phrase("NULL"));
	    			table_cell52.setBackgroundColor(new BaseColor(c1, c2, c3));
		    		table1.addCell(table_cell52);
	    		}
	    		else {
		    		PdfPCell table_cell52 = new PdfPCell(new Phrase(decForm.format(List.get(i).getCenaMaterialu())));
		    		table_cell52.setBackgroundColor(new BaseColor(c1, c2, c3));
		    		table1.addCell(table_cell52);
	    		}
	    		
	    		
	    		PdfPCell table_cell62 = new PdfPCell(new Phrase(decForm.format(List.get(i).getCenaMaterialuRazyIlosc())));
	    		table_cell62.setBackgroundColor(new BaseColor(c1, c2, c3));
	    		table1.addCell(table_cell62);
	    		
	    		
	    		if(List.get(i).getCenaMaterialu().equals(null) || List.get(i).getCenaPracy()==0 || List.get(i).getCenaMaterialu()== null)
	    		{		    		
		    		PdfPCell table_cell72 = new PdfPCell(new Phrase("NULL"));
		    		table_cell72.setBackgroundColor(new BaseColor(c1, c2, c3));
		    		table1.addCell(table_cell72);
	    		}
	    		else
	    		{
	    			PdfPCell table_cell72 = new PdfPCell(new Phrase(decForm.format(List.get(i).getCenaPracy())));
	    			table_cell72.setBackgroundColor(new BaseColor(c1, c2, c3));
		    		table1.addCell(table_cell72);
	    		}
	    		

	    		
	    		PdfPCell table_cell82 = new PdfPCell(new Phrase(List.get(i).getTYP()));
	    		table_cell82.setBackgroundColor(new BaseColor(c1, c2, c3));
	    		table1.addCell(table_cell82);
	        
	        
	        document.add(table1);
    		table1.flushContent();
    		x1012++;
		   }
		   document.close();
	
	}
	
	public static String GetCfomsendreel(Connection conn,String artikel, String Onderdeel) throws SQLException
	{
		String ss = "";		
		String sql = "select * from struktury where ARTIKELCODE = '"+artikel+"' and ONDERDEEL = '"+Onderdeel+"' order by seq";
	
		Statement e = conn.createStatement();
		try {
			e = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = e.executeQuery(sql);
		while(rs.next()) {
			ss= rs.getString("CFOMSONDERDEEL");
		}
		
		rs.close();
		e.close();
								
		
		return ss;				
	}

}
