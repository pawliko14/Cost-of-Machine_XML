package costofmachine;

public class AfterCalculationsStrukture {
	
	private String Leverancier;
	private String ORDERNUMMER;
	private String SEQUENTIE;
	private String ARTIKELCODE;
	private String ARTIKELOMSCHRIJVING;
	private String BESTELD;
	private String GELEVERD;
	private String PROJECTNUMMER;
	private String LEVERDATUM;
	private String LEVERINGSDATUMEFFECTIEF;
	private String LEVERINGSDATUMINGAVERECEPTIE;
	private String EENHEIDSPRIJS;
	private String MUNT;
	private String TOTAAL;
	private String MONTAGEOMSCHRIJVING;
	
	
	
	public String getMONTAGEOMSCHRIJVING() {
		return MONTAGEOMSCHRIJVING;
	}
	public void setMONTAGEOMSCHRIJVING(String mONTAGEOMSCHRIJVING) {
		MONTAGEOMSCHRIJVING = mONTAGEOMSCHRIJVING;
	}
	public String getLeverancier() {
		return Leverancier;
	}
	public void setLeverancier(String leverancier) {
		Leverancier = leverancier;
	}
	public String getORDERNUMMER() {
		return ORDERNUMMER;
	}
	public void setORDERNUMMER(String oRDERNUMMER) {
		ORDERNUMMER = oRDERNUMMER;
	}
	public String getSEQUENTIE() {
		return SEQUENTIE;
	}
	public void setSEQUENTIE(String sEQUENTIE) {
		SEQUENTIE = sEQUENTIE;
	}
	public String getARTIKELCODE() {
		return ARTIKELCODE;
	}
	public void setARTIKELCODE(String aRTIKELCODE) {
		ARTIKELCODE = aRTIKELCODE;
	}
	public String getARTIKELOMSCHRIJVING() {
		return ARTIKELOMSCHRIJVING;
	}
	public void setARTIKELOMSCHRIJVING(String aRTIKELOMSCHRIJVING) {
		ARTIKELOMSCHRIJVING = aRTIKELOMSCHRIJVING;
	}
	public String getBESTELD() {
		return BESTELD;
	}
	public void setBESTELD(String bESTELD) {
		BESTELD = bESTELD;
	}
	public String getGELEVERD() {
		return GELEVERD;
	}
	public void setGELEVERD(String gELEVERD) {
		GELEVERD = gELEVERD;
	}
	public String getPROJECTNUMMER() {
		return PROJECTNUMMER;
	}
	public void setPROJECTNUMMER(String pROJECTNUMMER) {
		PROJECTNUMMER = pROJECTNUMMER;
	}
	public String getLEVERDATUM() {
		return LEVERDATUM;
	}
	public void setLEVERDATUM(String lEVERDATUM) {
		LEVERDATUM = lEVERDATUM;
	}
	public String getLEVERINGSDATUMEFFECTIEF() {
		return LEVERINGSDATUMEFFECTIEF;
	}
	public void setLEVERINGSDATUMEFFECTIEF(String lEVERINGSDATUMEFFECTIEF) {
		LEVERINGSDATUMEFFECTIEF = lEVERINGSDATUMEFFECTIEF;
	}
	public String getLEVERINGSDATUMINGAVERECEPTIE() {
		return LEVERINGSDATUMINGAVERECEPTIE;
	}
	public void setLEVERINGSDATUMINGAVERECEPTIE(String lEVERINGSDATUMINGAVERECEPTIE) {
		LEVERINGSDATUMINGAVERECEPTIE = lEVERINGSDATUMINGAVERECEPTIE;
	}
	public String getEENHEIDSPRIJS() {
		return EENHEIDSPRIJS;
	}
	public void setEENHEIDSPRIJS(String eENHEIDSPRIJS) {
		EENHEIDSPRIJS = eENHEIDSPRIJS;
	}
	public String getMUNT() {
		return MUNT;
	}
	public void setMUNT(String mUNT) {
		MUNT = mUNT;
	}
	public String getTOTAAL() {
		return TOTAAL;
	}
	public void setTOTAAL(String tOTAAL) {
		TOTAAL = tOTAAL;
	}


	
	public void getAllData()
	{
		System.out.println("Obiekt: ");
		System.out.println("Leverancier: " + this.getLeverancier());
		System.out.println("SEQUENTIE: " + this.getSEQUENTIE());
		System.out.println("ARTIKELCODE: " + this.getARTIKELCODE());
		System.out.println("ARTIKELOMSCHRIJVING: " + this.getARTIKELOMSCHRIJVING());
		System.out.println("BESTELD: " + this.getBESTELD());
		System.out.println("GELEVERD: " + this.getGELEVERD());
		System.out.println("PROJECTNUMMER: " + this.getPROJECTNUMMER());
		System.out.println("LEVERDATUM: " + this.getLEVERDATUM());
		System.out.println("LEVERINGSDATUMEFFECTIEF: " + this.getLEVERINGSDATUMEFFECTIEF());
		System.out.println("LEVERINGSDATUMINGAVERECEPTIE: " + this.getLEVERINGSDATUMINGAVERECEPTIE());
		System.out.println("EENHEIDSPRIJS: " + this.getEENHEIDSPRIJS());
		System.out.println("MUNT: " + this.getMUNT());
		System.out.println("TOTAAL: " + this.getTOTAAL());
		System.out.println("MONTAGEOMSCHRIJVING: " + this.getMONTAGEOMSCHRIJVING());
	}
	
	
}
