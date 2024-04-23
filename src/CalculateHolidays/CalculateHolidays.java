package CalculateHolidays;

/*-----------------------------------------------------------------+
| Class:       CalculateHolidays
| Copyright:   (C) Jörg-Andre Kurth
| License:     GPL (General Public License)
| Author:      Jörg-Andre Kurth aka CoPyMaus
+-----------------------------------------------------------------+
| Description:
| This class calculates German holidays, regional holidays and 
| events.
| These can be queried as a list or individually.
+-----------------------------------------------------------------+
| This program is released as free software under the
| Affero GPL license. You can redistribute it and/or
| modify it under the terms of this license which you
| can read by viewing the included agpl.txt or online
| at www.gnu.org/licenses/agpl.html. Removal of this
| copyright header is strictly prohibited without
| written permission from the original author(s).
+----------------------------------------------------------------*/

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;

public class CalculateHolidays {

    private int currentYear;
    private String dateOfEaster;
    private String regionsfilter = null;
    private List<String> holidayTypeFilter = new ArrayList<>();

    // Setter

    /**
     * Sets the region (state) as a filter.
     * It is possible to enter an appropriate abbreviation or the full name of the region.
     * Two-letter-region is case-insensitive
     * 
     * <p> BW = Baden-Württemberg
     * <p> BY = Bayern
     * <p> BE = Berlin
     * <p> BB = Brandenburg
     * <p> HB = Bremen
     * <p> HH = Hamburg
     * <p> HE = Hessen
     * <p> MV = Mecklenburg-Vorpommern
     * <p> NI = Niedersachsen
     * <p> NW = Nordrhein-Westfalen
     * <p> RP = Rheinland-Pfalz
     * <p> SL = Saarland
     * <p> SN = Sachsen
     * <p> ST = Sachsen-Anhalt
     * <p> SH = Schleswig-Holstein
     * <p> TH = Thüringen
     * 
     * @param region
     * @return Returns true if the region has been deposited. Otherwise false.
     */
    public boolean SetRegionFilter(String region) {
        if (region != null && region.length() == 2) {
            this.regionsfilter = this.GetFederalState(region.toUpperCase());
            return true;
        }
        else if (region != null && region.length() > 2) {
            this.regionsfilter = region;
            return true;
        }
        else if (region != null) {
            this.regionsfilter = null;
            return true;
        }
        return false;
    }

    /**
     * Add the type of holiday
     * It is possible to pass multiple types as List<Integer>.
     * 
     * <p> 0 = National holiday
     * <p> 1 = Regional holiday (state can be defined with SetRegionFilter)
     * <p> 2 = Remembrance Day
     * <p> 3 = Event
     * <p> 4 = Pre-Christmas day
     * <p> 5 = end of year
     * 
     * @param type
     * 
     */
    public void AddHolidayTypeFilter(int type) {
        if (type > -1 && !this.holidayTypeFilter.contains(GetHolidayType(type))) {
            this.holidayTypeFilter.add(GetHolidayType(type));
        }
    }
    public void AddHolidayTypeFilter(List<Integer> type) {
        for (int value : type) {
            if (value > -1 && !this.holidayTypeFilter.contains(GetHolidayType(value))) {
                this.holidayTypeFilter.add(GetHolidayType(value));
            }
        }
    }
    /**
     * Resets the filter for the holidays
     */
    public void ResetHolidayTypeFilter() {
        this.holidayTypeFilter = new ArrayList<>();
    }


    // Constructor
    public CalculateHolidays(int year) {
        if (year > 1900) {
            this.currentYear = year;
        } else {
            this.currentYear = LocalDate.now().getYear();
        }
        CalculateEaster();
    }

    private String ConvertDateToGermanFormat(String date) {
        // Parse the input date string into a LocalDate object
        LocalDate localDate = LocalDate.parse(date);
        // Get the day of the week in German
        String dayOfWeek = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN);
        // Format the date in German style (dd.MM.yyyy)
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        // Combine the day of the week and the formatted date
        String result = dayOfWeek + ", " + formattedDate;
        return result;
    }

    public HolidayEntry GetNewYearsDay() {
        String sdate = LocalDate.of(this.currentYear, 1, 1).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Neujahrstag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetHolyThreeKings() {
        String sdate = LocalDate.of(this.currentYear, 1, 6).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BW"));
        regions.add(GetFederalState("BY"));
        regions.add(GetFederalState("ST"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Heilige drei Könige", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetValentinesDay() {
        String sdate = LocalDate.of(this.currentYear, 2, 14).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Valentinstag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetRoseMonday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(48).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Rosenmontag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetShroveTuesday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(47).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Faschingsdienstag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetAshWednesday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(46).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Aschermittwoch", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetInternationalWomensDay() {
        String sdate = LocalDate.of(this.currentYear, 3, 8).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BE"));
        regions.add(GetFederalState("MV"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Internationaler Frauentag", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetPalmSunday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(7).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "PalmSonntag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetMaundyThursday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(3).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "GrünDonnerstag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetGoodFriday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(2).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "KarFreitag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetHolySaturday() {
        String sdate = LocalDate.parse(this.dateOfEaster).minusDays(1).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "KarSamstag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetEasterSunday() {
        String sdate = LocalDate.parse(this.dateOfEaster).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Ostersonntag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetEasterMonday() {
        String sdate = LocalDate.parse(this.dateOfEaster).plusDays(1).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Ostermontag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetStartOfSummerTime() {
        String sdate = LocalDate.of(this.currentYear, 3, 1).with(DayOfWeek.SUNDAY)
            .withDayOfMonth(LocalDate.of(this.currentYear, 3, 1).lengthOfMonth()).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Beginn der Sommerzeit", this.GetHolidayType(3), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetLaborDay() {
        String sdate = LocalDate.of(this.currentYear, 5, 1).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Tag der Arbeit", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetAnniversaryOfTheLiberationFromNationalSocialism() {
        String sdate = LocalDate.of(this.currentYear, 5, 8).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BE"));
        regions.add(GetFederalState("BB"));
        regions.add(GetFederalState("HB"));
        regions.add(GetFederalState("MV"));
        regions.add(GetFederalState("TH"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Jahrestag der Befreiung vom Nationalsozialismus", this.GetHolidayType(2), regions);
        return holidayEntry;
    }

    public HolidayEntry GetAscensionOfChrist() {
        String sdate = LocalDate.parse(this.dateOfEaster).plusDays(39).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Christi Himmelfahrt & Vatertag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetMothersDay() {
        String sdate = LocalDate.of(currentYear, Month.MAY, 1) // 1. Mai des aktuellen Jahres
        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)) // Nächster oder aktueller Sonntag
        .with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString(); // 2. Sonntag im Mai
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Muttertag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetPentecostSunday() {
        String sdate = LocalDate.parse(this.dateOfEaster).plusDays(49).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Pfingstsonntag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetWhitMonday() {
        String sdate = LocalDate.parse(this.dateOfEaster).plusDays(50).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Pfingstmontag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetCorpusChristi() {
        String sdate = LocalDate.parse(this.dateOfEaster).plusDays(60).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BW"));
        regions.add(GetFederalState("BY"));
        regions.add(GetFederalState("HE"));
        regions.add(GetFederalState("NW"));
        regions.add(GetFederalState("RP"));
        regions.add(GetFederalState("SL"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Fronleichnam", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetHighPeaceFestival() {
        String sdate = LocalDate.of(this.currentYear, 8, 8).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BW"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Hohes Friedensfest", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetAssumptionDay() {
        String sdate = LocalDate.of(this.currentYear, 8, 15).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BY"));
        regions.add(GetFederalState("SL"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Maria Himmelfahrt", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetWorldChildrensDay() {
        String sdate = LocalDate.of(this.currentYear, 9, 20).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("TH"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Weltkindertag", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetDayOfGermanUnity() {
        String sdate = LocalDate.of(this.currentYear, 10, 3).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Tag der deutschen Einheit", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetEndOfSummerTime() {
        String sdate = LocalDate.of(this.currentYear, 10, 1).with(DayOfWeek.SUNDAY)
            .withDayOfMonth(LocalDate.of(this.currentYear, 10, 1).lengthOfMonth()).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Ende der Sommerzeit", this.GetHolidayType(3), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetReformationDay() {
        String sdate = LocalDate.of(this.currentYear, 10, 31).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BB"));
        regions.add(GetFederalState("HB"));
        regions.add(GetFederalState("HH"));
        regions.add(GetFederalState("MV"));
        regions.add(GetFederalState("NI"));
        regions.add(GetFederalState("SN"));
        regions.add(GetFederalState("ST"));
        regions.add(GetFederalState("SH"));
        regions.add(GetFederalState("TH"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Reformationstag", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetHalloween() {
        String sdate = LocalDate.of(this.currentYear, 10, 31).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Halloween", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetAllSaintsDay() {
        String sdate = LocalDate.of(this.currentYear, 11, 1).toString();
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("BW"));
        regions.add(GetFederalState("BY"));
        regions.add(GetFederalState("NW"));
        regions.add(GetFederalState("RP"));
        regions.add(GetFederalState("SL"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Allerheiligen", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    public HolidayEntry GetSaintMartin() {
        String sdate = LocalDate.of(this.currentYear, 11, 11).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Sankt Martin", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetMemorialDay() {
        String sdate = LocalDate.now().withMonth(11).with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY)).toString(); 
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Volkstrauertag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetDayOfPrayerAndRepentance() {
        LocalDate ld = LocalDate.of(this.currentYear, 11, 23);
        String sdate = "";
        if (ld.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
            sdate = ld.toString();
        }
        else {
            sdate = ld.with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY)).toString();
        }
        List<String> regions = new ArrayList<>();
        regions.add(GetFederalState("SN"));
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Buß- und Bettag", this.GetHolidayType(1), regions);
        return holidayEntry;
    }

    private LocalDate CalculateFourthAdvent() {
        LocalDate christmasEve = LocalDate.of(this.currentYear, 12, 24);
        if (christmasEve.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return christmasEve;
        } else {
            return christmasEve.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        }
    }

    public HolidayEntry GetSundayOfTheDead() {
        String sdate = LocalDate.parse(this.CalculateFourthAdvent().toString()).minusWeeks(4).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Totensonntag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetFirstAdvent() {
        String sdate = LocalDate.parse(this.CalculateFourthAdvent().toString()).minusWeeks(3).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Erster Advent", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetNicholasDay() {
        String sdate = LocalDate.of(this.currentYear, 12, 6).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Nikolaustag", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetSecondAdvent() {
        String sdate = LocalDate.parse(this.CalculateFourthAdvent().toString()).minusWeeks(2).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Zweiter Advent", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetThirdAdvent() {
        String sdate = LocalDate.parse(this.CalculateFourthAdvent().toString()).minusWeeks(1).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Dritter Advent", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetFourthAdvent() {
        String sdate = LocalDate.parse(this.CalculateFourthAdvent().toString()).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Vierter Advent", this.GetHolidayType(2), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetChristmasEve() {
        String sdate = LocalDate.of(this.currentYear, 12, 24).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Heiligabend", this.GetHolidayType(4), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetFirstChristmasDay() {
        String sdate = LocalDate.of(this.currentYear, 12, 25).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Erster Weihnachtstag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetSecondChristmasDay() {
        String sdate = LocalDate.of(this.currentYear, 12, 26).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Zweiter Weihnachtstag", this.GetHolidayType(0), new ArrayList<>());
        return holidayEntry;
    }

    public HolidayEntry GetSylvester() {
        String sdate = LocalDate.of(this.currentYear, 12, 31).toString();
        HolidayEntry holidayEntry = new HolidayEntry(sdate, ConvertDateToGermanFormat(sdate), "Silvester", this.GetHolidayType(5), new ArrayList<>());
        return holidayEntry;
    }

    public ArrayList<HolidayEntry> GetHolidaysFullList() {
        ArrayList<HolidayEntry> holidayList = new ArrayList<>();
        holidayList.add(this.GetNewYearsDay());
        holidayList.add(this.GetHolyThreeKings());
        holidayList.add(this.GetValentinesDay());
        holidayList.add(this.GetRoseMonday());
        holidayList.add(this.GetShroveTuesday());
        holidayList.add(this.GetAshWednesday());
        holidayList.add(this.GetInternationalWomensDay());
        holidayList.add(this.GetPalmSunday());
        holidayList.add(this.GetMaundyThursday());
        holidayList.add(this.GetGoodFriday());
        holidayList.add(this.GetHolySaturday());
        holidayList.add(this.GetEasterSunday());
        holidayList.add(this.GetEasterMonday());
        holidayList.add(this.GetStartOfSummerTime());
        holidayList.add(this.GetLaborDay());
        holidayList.add(this.GetAnniversaryOfTheLiberationFromNationalSocialism());
        holidayList.add(this.GetAscensionOfChrist());
        holidayList.add(this.GetMothersDay());
        holidayList.add(this.GetPentecostSunday());
        holidayList.add(this.GetWhitMonday());
        holidayList.add(this.GetCorpusChristi());
        holidayList.add(this.GetHighPeaceFestival());
        holidayList.add(this.GetAssumptionDay());
        holidayList.add(this.GetWorldChildrensDay());
        holidayList.add(this.GetDayOfGermanUnity());
        holidayList.add(this.GetEndOfSummerTime());
        holidayList.add(this.GetReformationDay());
        holidayList.add(this.GetAllSaintsDay());
        holidayList.add(this.GetSaintMartin());
        holidayList.add(this.GetMemorialDay());
        holidayList.add(this.GetDayOfPrayerAndRepentance());
        holidayList.add(this.GetSundayOfTheDead());
        holidayList.add(this.GetFirstAdvent());
        holidayList.add(this.GetSecondAdvent());
        holidayList.add(this.GetThirdAdvent());
        holidayList.add(this.GetFourthAdvent());
        holidayList.add(this.GetNicholasDay());
        holidayList.add(this.GetChristmasEve());
        holidayList.add(this.GetFirstChristmasDay());
        holidayList.add(this.GetSecondChristmasDay());
        holidayList.add(this.GetSylvester());



        Collections.sort(holidayList, new Comparator<HolidayEntry>() {
            @Override
            public int compare(HolidayEntry holiday1, HolidayEntry holiday2) {
                return holiday1.GetSortDate().compareTo(holiday2.GetSortDate());
            }
        });

        if (this.holidayTypeFilter.size() > 0 || (this.regionsfilter != null && !this.regionsfilter.isEmpty())) {
            // holidayTypeFilter
            for (int i=holidayList.size() - 1; i >= 0; i--) {
                if (this.holidayTypeFilter.size() > 0) {
                    if (!this.holidayTypeFilter.contains(holidayList.get(i).GetHolidayType())) {
                        holidayList.remove(i);
                        continue;
                    }
                }
                if (this.regionsfilter != null && !this.regionsfilter.isEmpty() && holidayList.get(i).GetRegions().size() > 0) {
                    if (!holidayList.get(i).GetRegions().contains(this.regionsfilter)) {
                        holidayList.remove(i);
                    }
                }
            }
        }

        return holidayList;

    }

    public ArrayList<HolidayEntry> GetFuturedHolidaysList(int count) { // int = 0 gives everything until the end of the year.
        ArrayList<HolidayEntry> holidayList = this.GetHolidaysFullList();
        for (int i=holidayList.size() - 1; i >= 0; i-- ) {
            LocalDate compareDate = LocalDate.parse(holidayList.get(i).GetSortDate());
            if (compareDate.isBefore(LocalDate.now())) {
                holidayList.remove(i);
            }
        }

        if (count != 0 && holidayList.size() > count) {
            for (int i=holidayList.size() - 1; i >= count; i-- ) {
                holidayList.remove(i);
            }
        }
        else if (count != 0 && holidayList.size() < count) {    // Filling up the ArrayList
            CalculateHolidays nch = new CalculateHolidays(currentYear + 1);
            ArrayList<HolidayEntry> FillUpList = nch.GetFuturedHolidaysList(0);
            nch = null; // The object is being cleaned up to release resources.

            for (int i = 1; i < FillUpList.size(); i++) {       // Dirty - For some reason, this ArrayList has one extra row (from the previous year).
                holidayList.add(FillUpList.get(i));
                if (holidayList.size() == count) {
                    break;
                }
            }
        }

        return holidayList;
    }


    /*---------------------------------------------------------------------
    | The calculation of the Easter date is based on the Metonic cycle, 
    | which repeats approximately every 19 years. This cycle corresponds 
    | to the phases of the moon. Since the church tradition aims for the 
    | first Sunday in spring, it is necessary to calculate the phase of 
    | the moon in order to determine both the arrival of spring and the 
    | first Sunday.
    +---------------------------------------------------------------------- */
    private void CalculateEaster() {
        int a = this.currentYear % 19;
        int b = this.currentYear / 100;
        int c = this.currentYear % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;

        this.dateOfEaster = LocalDate.of(this.currentYear, month, day).toString();
    }

    private String GetHolidayType(int holidayTypeValue) {
        switch (holidayTypeValue) {
            case 0:
                return "Gesetzlicher Feiertag";
            case 1:
                return "Regionaler Feiertag";
            case 2:
                return "Gedenktag";
            case 3:
                return "Ereignis";
            case 4:
                return "Vorweihnachtstag";
            case 5:
                return "Jahresende";
            default:
                return null;
        }
    }

    private String GetFederalState(String fs) {
        switch (fs) {
            case "BW":
                return "Baden-Württemberg";
            case "BY":
                return "Bayern";
            case "BE":
                return "Berlin";
            case "BB":
                return "Brandenburg";
            case "HB":
                return "Bremen";
            case "HH":
                return "Hamburg";
            case "HE":
                return "Hessen";
            case "MV":
                return "Mecklenburg-Vorpommern";
            case "NI":
                return "Niedersachsen";
            case "NW":
                return "Nordrhein-Westfalen";
            case "RP":
                return "Rheinland-Pfalz";
            case "SL":
                return "Saarland";
            case "SN":
                return "Sachsen";
            case "ST":
                return "Sachsen-Anhalt";
            case "SH":
                return "Schleswig-Holstein";
            case "TH":
                return "Thüringen";
            default:
                return null;
        }
    }
}

class HolidayEntry {
    private String _sortDate;
    private String _date;
    private String _nameOfHoliday;
    private String _holidayType;
    private List<String> _regions;

    public HolidayEntry(String sortDate, String date, String nameOfHoliday, String holidayType, List<String> regions) {
        this._sortDate = sortDate;
        this._date = date;
        this._nameOfHoliday = nameOfHoliday;
        this._holidayType = holidayType;
        this._regions = regions;
    }

    // Gettermethods
    public String GetSortDate() {
        return _sortDate;
    }

    public String GetDate() {
        return _date;
    }

    public String GetNameOfHoliday() {
        return _nameOfHoliday;
    }

    public String GetHolidayType() {
        return _holidayType;
    }

    public List<String> GetRegions() {
        return _regions;
    }
}
