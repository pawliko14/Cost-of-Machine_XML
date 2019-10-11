package costofmachine;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Struktury {
	private String seq;
	private String ARTIKELCODE;
	private String ONDERDEEL;
	private String TYP;
	private Double ILOSC;
	private String JEDNOSTKA;
	private String CFOMSONDERDEEL;
	
	private Integer Poziom;
	private String PreviousArikel;
	
	private Double CenaMaterialu;
	
	private Double CenaRazyIlosc;
	
	private boolean CzySprawdzony;
	
	private Double CenaPracy;

	
	public Double getCenaPracy() {
		return CenaPracy;
	}

	public void setCenaPracy(Double cenaPracy) {
		CenaPracy = cenaPracy;
	}

	public Double getCenaRazyIlosc() {
		return CenaRazyIlosc;
	}

	public void setCenaRazyIlosc(Double cenaRazyIlosc) {
		CenaRazyIlosc = cenaRazyIlosc;
	}

	
	public Double getCenaMaterialu() {
		return CenaMaterialu;
	}

	public void setCenaMaterialu(Double string) {
		CenaMaterialu = string;
	}
	
	
	
	public boolean isCzySprawdzony() {
		return CzySprawdzony;
	}

	public void setCzySprawdzony(boolean czySprawdzony) {
		CzySprawdzony = czySprawdzony;
	}

	public Struktury(Integer p)
	{
		Poziom = p;
	}
	
	public String getPreviousArikel() {
		return PreviousArikel;
	}

	public void setPreviousArikel(String previousArikel) {
		PreviousArikel = previousArikel;
	}

	public void setPoziom(Integer poziom) {
		Poziom = poziom;
	}

	public Integer getPoziom()
	{
		return Poziom;
	}
	
	public String getCFOMSONDERDEEL() {
		return CFOMSONDERDEEL;
	}
	public void setCFOMSONDERDEEL(String cFOMSONDERDEEL) {
		CFOMSONDERDEEL = cFOMSONDERDEEL;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getARTIKELCODE() {
		return ARTIKELCODE;
	}
	public void setARTIKELCODE(String aRTIKELCODE) {
		ARTIKELCODE = aRTIKELCODE;
	}
	public String getONDERDEEL() {
		return ONDERDEEL;
	}
	public void setONDERDEEL(String oNDERDEEL) {
		ONDERDEEL = oNDERDEEL;
	}
	public String getTYP() {
		return TYP;
	}
	public void setTYP(String tYP) {
		TYP = tYP;
	}
	public Double getILOSC() {
		return ILOSC;
	}
	public void setILOSC(Double iLOSC) {
		ILOSC = iLOSC;
	}
	public String getJEDNOSTKA() {
		return JEDNOSTKA;
	}
	public void setJEDNOSTKA(String jEDNOSTKA) {
		JEDNOSTKA = jEDNOSTKA;
	}

	
	public void Show()
	{
		if(this.Poziom == null )
		{
				System.out.println("Empty List");
			
		}
		else
		{
			
			
			   
			System.out.println("---Poziom---: " + this.Poziom);
			System.out.println("Seqwencja: " + this.seq);
			System.out.println("ARTIKELCODE: " + this.ARTIKELCODE);
			System.out.println("ONDERDEEL: " + this.ONDERDEEL);
			System.out.println("CFOMSONDERDEEL: " + this.CFOMSONDERDEEL);
			System.out.println("TYP: " + this.TYP);
			System.out.println("ILOSC: " + this.ILOSC);
			System.out.println("JEDNOSTKA: " + this.JEDNOSTKA);
			System.out.println("Czy sprawdzony?: " + this.CzySprawdzony);
			System.out.println("CenaMaterialu: " + this.CenaMaterialu);
			System.out.println("CenaMaterialuRazyIlosc: " + this.CenaRazyIlosc);
			System.out.println("Cena Pracy: " + this.CenaPracy);

			System.out.println("                              ");
			
		}
	}
	public void ShowLevel()
	{
		System.out.println("---Poziom---: " + this.Poziom);
	}

	

}
