package main.java.Objetcs;

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
	private Double CenaMaterialuRazyIlosc;
	private boolean CzySprawdzony;
	private Double CenaPracy;
	
	private String GlownyProjekt;

	private Double CenaPracyRazyIlosc;
	private String TYP_Nadrzednego;
	
	

	
	
	public String getTYP_Nadrzednego() {
		return TYP_Nadrzednego;
	}

	public void setTYP_Nadrzednego(String tYP_Nadrzednego) {
		TYP_Nadrzednego = tYP_Nadrzednego;
	}

	public String getGlownyProjekt() {
		return GlownyProjekt;
	}

	public void setGlownyProjekt(String glownyProjekt) {
		GlownyProjekt = glownyProjekt;
	}

	public Double getCenaPracy() {
		return CenaPracy;
	}

	public void setCenaPracy(Double cenaPracy) {
		CenaPracy = cenaPracy;
	}

	public Double getCenaMaterialuRazyIlosc() {
		return CenaMaterialuRazyIlosc;
	}

	public void setCenaMaterialuRazyIlosc(Double cenaRazyIlosc) {
		CenaMaterialuRazyIlosc = cenaRazyIlosc;
	}

	
	public Double getCenaMaterialu() {
		return CenaMaterialu;
	}

	public void setCenaMaterialu(Double cena) {
		CenaMaterialu = cena;
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
			System.out.println("Glowny projekt: " + this.GlownyProjekt);
			System.out.println("Seqwencja: " + this.seq);
			System.out.println("Artikel Nadrzedny: " + this.ARTIKELCODE);
			System.out.println("Artikel: " + this.ONDERDEEL);
			System.out.println("Opis Artikel: " + this.CFOMSONDERDEEL);
			System.out.println("TYP: " + this.TYP);
			System.out.println("ILOSC: " + this.ILOSC);
			System.out.println("JEDNOSTKA: " + this.JEDNOSTKA);
			System.out.println("Czy sprawdzony?: " + this.CzySprawdzony);
			System.out.println("CenaMaterialu: " + this.CenaMaterialu);
			System.out.println("Cena Materialu Razy Ilosc: " + this.CenaMaterialuRazyIlosc);
			System.out.println("Cena Pracy za szt: " + this.CenaPracy);
			System.out.println("Cena Pracy Razy Ilosc: " + this.CenaPracyRazyIlosc);
			System.out.println("TYP nadrzednego art.c: " + this.TYP_Nadrzednego);

			System.out.println("                              ");
			
		}
	}
	public void ShowLevel()
	{
		System.out.println("---Poziom---: " + this.Poziom);
	}

	public Double getCenaPracyRazyIlosc() {
		return CenaPracyRazyIlosc;
	}

	public void setCenaPracyRazyIlosc(Double cenaPracyRazyIlosc) {
		CenaPracyRazyIlosc = cenaPracyRazyIlosc;
	}

	

}
