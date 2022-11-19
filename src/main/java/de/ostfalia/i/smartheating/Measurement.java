package de.ostfalia.i.smartheating;
/**
 * 
 * @author javacodepoint.com, modified by Benjamin Vollmers and Ronja Rosenbach
 * @source https://www.javacodegeeks.com/2015/07/apache-poi-tutorial.html
 */
public class Measurement {
    private String Heizkörper;
    private String Zählerstand;
    private String Tag;
    private String Monat;
    private String Jahr;
    private String Stunde;

    public Measurement(String Heizkörper, String Zählerstand, String Tag, String Monat, String Jahr, String Stunde) {
        this.Heizkörper = Heizkörper;
        this.Zählerstand = Zählerstand;
        this.Tag = Tag;
        this.Monat = Monat;
        this.Jahr = Jahr;
        this.Stunde = Stunde;

    }

    public String getHeizkörper() {
        return Heizkörper;
    }

    public void setHeizkörper(String Heizkörper) {
        this.Heizkörper = Heizkörper;
    }

    public String getZählerstand() {
        return Zählerstand;
    }

    public void setZählerstand(String Zählerstand) {
        this.Zählerstand = Zählerstand;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public String getMonat() {
        return Monat;
    }

    public void setMonat(String Monat) {
        this.Monat = Monat;
    }

    public String getJahr() {
        return Jahr;
    }

    public void setJahr(String Jahr) {
        this.Jahr = Jahr;
    }

    public String getStunde() {
        return Stunde;
    }

    public void setStunde(String Stunde) {
        this.Stunde = Stunde;
    }
}