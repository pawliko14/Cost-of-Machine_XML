package main.java.Objetcs;

public class SubProjectsStructure {

    private String ID;
    private String MACHINENUMBER;
    private String MACHINESUBPROJECTNUMBER;
    private String PARENTARTICLE;
    private String CHILDARTICLE;
    private String QUANTITY;
    private String TYPE;
    private String LEVEL;

    public SubProjectsStructure(String ID, String MACHINENUMBER, String MACHINESUBPROJECTNUMBER, String PARENTARTICLE, String CHILDARTICLE, String QUANTITY, String TYPE, String LEVEL) {
        this.ID = ID;
        this.MACHINENUMBER = MACHINENUMBER;
        this.MACHINESUBPROJECTNUMBER = MACHINESUBPROJECTNUMBER;
        this.PARENTARTICLE = PARENTARTICLE;
        this.CHILDARTICLE = CHILDARTICLE;
        this.QUANTITY = QUANTITY;
        this.TYPE = TYPE;
        this.LEVEL = LEVEL;
    }



    @Override
    public String toString() {
        return "SubProjectsStructure{" +
                "ID='" + ID + '\'' +
                ", MACHINENUMBER='" + MACHINENUMBER + '\'' +
                ", MACHINESUBPROJECTNUMBER='" + MACHINESUBPROJECTNUMBER + '\'' +
                ", PARENTARTICLE='" + PARENTARTICLE + '\'' +
                ", CHILDARTICLE='" + CHILDARTICLE + '\'' +
                ", QUANTITY='" + QUANTITY + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", LEVEL='" + LEVEL + '\'' +
                '}';
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMACHINENUMBER() {
        return MACHINENUMBER;
    }

    public void setMACHINENUMBER(String MACHINENUMBER) {
        this.MACHINENUMBER = MACHINENUMBER;
    }

    public String getMACHINESUBPROJECTNUMBER() {
        return MACHINESUBPROJECTNUMBER;
    }

    public void setMACHINESUBPROJECTNUMBER(String MACHINESUBPROJECTNUMBER) {
        this.MACHINESUBPROJECTNUMBER = MACHINESUBPROJECTNUMBER;
    }

    public String getPARENTARTICLE() {
        return PARENTARTICLE;
    }

    public void setPARENTARTICLE(String PARENTARTICLE) {
        this.PARENTARTICLE = PARENTARTICLE;
    }

    public String getCHILDARTICLE() {
        return CHILDARTICLE;
    }

    public void setCHILDARTICLE(String CHILDARTICLE) {
        this.CHILDARTICLE = CHILDARTICLE;
    }

    public String getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(String LEVEL) {
        this.LEVEL = LEVEL;
    }
}
