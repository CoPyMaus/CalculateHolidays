package CalculateHolidays;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        // Erzeugt ein Objekt zur Berechnung von Feiertagen für das Jahr 2024.
        CalculateHolidays ch = new CalculateHolidays(2024); 

        // Die Klasse HolidayEntry wird in CalculateHolidays verwendet, um eine ArrayList mit Elementen zu erstellen.
        // Jedes Element repräsentiert einen Feiertag und enthält verschiedene Eigenschaften wie Datum, Name und Typ.
        // Da der direkte Zugriff auf die Elemente nicht möglich ist (im Gegensatz zu einem assoziativen Array, auch als "Map" bezeichnet),
        // werden die Werte über Getter-Methoden abgerufen, die in der Klasse HolidayEntry definiert sind.
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println(" Einzelabruf - Beispiel für den Abruf von Weihnachten:                                                    ");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        // Beispiel für den Abruf von Weihnachten:
        HolidayEntry holiday = ch.GetChristmasEve();
        System.out.println(holiday.GetDate() + "\t\t" + holiday.GetNameOfHoliday() + "\t\t" + holiday.GetHolidayType());


        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println(" Gesamtaufruf - Gesamte Liste für das vorgegebene Jahr (Bei der instanzierung bereits gesetzt!)           ");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        // Im Gegensatz zum einzelnen Abruf wird hier eine ArrayList erstellt, die wiederrum mit jedem Eintrag eine ArrayList enthält.
        // So lassen sich alle Daten abrufen und können mit einer einfachen Schleife durchgearbeitet werden.
        ArrayList<HolidayEntry> holidayList = ch.GetHolidaysFullList();

        // Durchlaufe die Liste und formatiere die Ausgabe für jeden Feiertag
        for (HolidayEntry entry : holidayList) {
            String tabs = CalculateTabulator(entry.GetNameOfHoliday()); // Tabulatoren zur Formatierung

            // Aufbau des Feiertagseintrags mit optionalen Regionen
            String HolidayTypeWithRegions = entry.GetHolidayType();
            // Die Regionen sind ebenfalls in einer ArrayList enthalten. Zunächst wird geprüft, ob sich überhaupt Regionen darin befinden.
            if (!entry.GetRegions().isEmpty()) {
                // Da Regionen vorhanden sind, wird der String (welcher aktuell nur den Feiertagstyp enthält) erweitert.
                HolidayTypeWithRegions += " in ";
                // Die Variable "comma" vom Typ boolean dient ausschließlich dazu, ob ein Komma gesetzt wird oder nicht. Im ersten durchlauf soll dieses verhindert werden.
                boolean comma = false;
                // Schleife für die Regionen durchlaufen
                for (String region : entry.GetRegions()) {
                    // Statt einer weiteren if prüfung, wird hier die Kurzform genommen.
                    // Wenn (comma) true ist, wird der Ausdruck nach dem ? verwendet. Wenn false, dann nach dem :
                    HolidayTypeWithRegions += (comma) ? ", " + region : region;
                    // Im ersten Durchlauf war (comma) = false. Nun wird es auf true gesetzt, damit beim nächsten Durchlauf auch ein Komma hinzugefügt wird.
                    comma = true;
                }
            }
            
            // Ausgabe des formatierten Feiertagseintrags
            //System.out.println(entry.GetDate() + "\t\t" + entry.GetNameOfHoliday() + tabs + HolidayTypeWithRegions);
            System.out.println(String.format("%s\t\t%s%s%s", entry.GetDate(), entry.GetNameOfHoliday(), tabs, HolidayTypeWithRegions));
        }
        
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println(" Gesamte Liste an Feiertagen für Nordrhein-Westfalen (NW) und dem vorgegebene Jahr                        ");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        
        ch.SetRegionFilter("NW"); // Region / Bundesland wurde gesetzt
        
        holidayList = ch.GetHolidaysFullList();
        for (HolidayEntry entry : holidayList) {
            String tabs = CalculateTabulator(entry.GetNameOfHoliday()); // Tabulatoren zur Formatierung
            // Ausgabe des formatierten Feiertagseintrags
            //System.out.println(entry.GetDate() + "\t\t" + entry.GetNameOfHoliday() + tabs + HolidayTypeWithRegions);
            System.out.println(String.format("%s\t\t%s%s%s", entry.GetDate(), entry.GetNameOfHoliday(), tabs, entry.GetHolidayType()));
        }
        ch.SetRegionFilter(null);

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println(" Alle gesetzlichen Feiertage für das vorgegebene Jahr                                                     ");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        ch.AddHolidayTypeFilter(0);   // 0 = Gesetzliche Feiertage

        holidayList = ch.GetHolidaysFullList();
        for (HolidayEntry entry : holidayList) {
            String tabs = CalculateTabulator(entry.GetNameOfHoliday()); // Tabulatoren zur Formatierung
            // Ausgabe des formatierten Feiertagseintrags
            //System.out.println(entry.GetDate() + "\t\t" + entry.GetNameOfHoliday() + tabs + HolidayTypeWithRegions);
            System.out.println(String.format("%s\t\t%s%s%s", entry.GetDate(), entry.GetNameOfHoliday(), tabs, entry.GetHolidayType()));
        }
        ch.ResetHolidayTypeFilter();

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println(" Abrufen der gesetzlichen und regionalen Feiertage in einem bestimmten Bundesland wie Sachsen-Anhalt (ST) ");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        List<Integer> holidayTypes = new ArrayList<>();     // Neue Liste erstellen
        holidayTypes.add(0);                              // 0 = Gesetzliche Feiertage hinzufügen
        holidayTypes.add(1);                              // 1 = Regionale Feiertage hinzufügen
        ch.AddHolidayTypeFilter(holidayTypes);              // Übergeben

        //Alternativ würde auch folgende Syntax funktionieren:
        ch.AddHolidayTypeFilter(0);
        ch.AddHolidayTypeFilter(1);

        ch.SetRegionFilter("ST");                    // Regionsfilter / Bundesland
        
        holidayList = ch.GetHolidaysFullList();
        for (HolidayEntry entry : holidayList) {
            String tabs = CalculateTabulator(entry.GetNameOfHoliday()); // Tabulatoren zur Formatierung
            // Ausgabe des formatierten Feiertagseintrags
            //System.out.println(entry.GetDate() + "\t\t" + entry.GetNameOfHoliday() + tabs + HolidayTypeWithRegions);
            System.out.println(String.format("%s\t\t%s%s%s", entry.GetDate(), entry.GetNameOfHoliday(), tabs, entry.GetHolidayType()));
        }
        ch.ResetHolidayTypeFilter();
        ch.SetRegionFilter(null);       

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println(" Nur aktuelle und künftige Feiertage anzeigen. Vergangene Feiertage sind nicht mehr vorhanden             ");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        holidayList = ch.GetFuturedHolidaysList(50);
        for (HolidayEntry entry : holidayList) {
            String tabs = CalculateTabulator(entry.GetNameOfHoliday()); // Tabulatoren zur Formatierung

            String HolidayTypeWithRegions = entry.GetHolidayType();
            if (entry.GetRegions().size() > 0) {
                HolidayTypeWithRegions += " in ";
                boolean comma = false;
                for (String region : entry.GetRegions()) {
                    HolidayTypeWithRegions += (comma) ? ", " + region : region;
                    comma = true;
                }
            }
            System.out.println(String.format("%s\t\t%s%s%s", entry.GetDate(), entry.GetNameOfHoliday(), tabs, HolidayTypeWithRegions));
        }
    }
    public static String CalculateTabulator(String txt) {
        String tabs; // Tabulatoren zur Formatierung
        if (txt.length() <= 16) {          
            tabs = "\t\t\t\t\t";
        }
        else if (txt.length() <= 24) {
            tabs = "\t\t\t\t";
        }
        else if (txt.length() <= 32) {
            tabs = "\t\t\t";
        }
        else if (txt.length() <= 40) {
            tabs = "\t\t";
        }
        else {
            tabs = "\t";
        }
        return tabs;
    }
}
