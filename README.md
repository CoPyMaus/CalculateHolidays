# CalculateHolidays

Die CalculateHolidays-Klasse ermöglicht die Berechnung der deutschen Feiertage, Gedenktage und Ereignisse. Bundesländer für regionale Feiertage sind ebenfalls mit berücksichtigt.

Um die Feiertage für das Jahr 2024 zu berechnen, erstellen Sie eine Instanz der Klasse:

```java
CalculateHolidays ch = new CalculateHolidays(2024);
```

Um ein bestimmtes Ereignis wie Weihnachten abzurufen, verwenden Sie die entsprechende Methode und speichern das Ergebnis in einer Variable:

```java
HolidayEntry holiday = ch.GetChristmasEve();
```

Die Daten des Feiertags können dann über die Getter-Methoden in der HolidayEntry-Klasse abgerufen und ausgegeben werden:

```java
System.out.println(holiday.GetDate() + "\t\t" + holiday.GetNameOfHoliday() + "\t\t" + holiday.GetHolidayType());
```

Um eine Liste aller Feiertage zu erhalten, verwenden Sie die Methode GetHolidaysFullList():

```java
ArrayList<HolidayEntry> holidayList = ch.GetHolidaysFullList();
```

Sie können auch eine Liste der Feiertage ab dem aktuellen Zeitpunkt erhalten. Geben Sie dazu die gewünschte Anzahl an Feiertagen an. Wenn das aktuelle Jahr nicht genügend Feiertage hat, werden automatisch Feiertage des nächsten Jahres hinzugefügt:

(Setzt vorraus, das die Instanz mit dem aktuellen Jahr instanziert wurde)

```java
// Alle Feiertage ab dem aktuellen Zeitpunkt bis zum Ende des Jahres
holidayList = ch.GetFuturedHolidaysList(0);

// Die nächsten 20 Feiertage ab dem aktuellen Zeitpunkt
holidayList = ch.GetFuturedHolidaysList(20);
```

## Die Methoden, um einzelene Feiertage abrufen zu können:

- GetNewYearsDay() = Neujahrstag
- GetHolyThreeKings() = Heilige Drei Könige
- GetValentinesDay() = Valentinstag
- GetRoseMonday() = Rosenmontag
- GetShroveTuesday() = Fastnachtsdienstag
- GetAshWednesday() = Aschermittwoch
- GetInternationalWomensDay() = Internationaler Frauentag
- GetPalmSunday() = Palmsonntag
- GetMaundyThursday() = Gründonnerstag
- GetGoodFriday() = Karfreitag
- GetHolySaturday() = Karsamstag
- GetEasterSunday() = Ostersonntag
- GetEasterMonday() = Ostermontag
- GetStartOfSummerTime() = Beginn der Sommerzeit
- GetLaborDay() = Tag der Arbeit
- GetAnniversaryOfTheLiberationFromNationalSocialism() = Jahrestag der Befreiung vom Nationalsozialismus
- GetAscensionOfChrist() = Christi Himmelfahrt
- GetMothersDay() = Muttertag
- GetPentecostSunday() = Pfingstsonntag
- GetWhitMonday() = Pfingstmontag
- GetCorpusChristi() = Fronleichnam
- GetHighPeaceFestival() = Hochfest des Friedens
- GetAssumptionDay() = Mariä Himmelfahrt
- GetWorldChildrensDay() = Weltkindertag
- GetDayOfGermanUnity() = Tag der Deutschen Einheit
- GetEndOfSummerTime() = Ende der Sommerzeit
- GetReformationDay() = Reformationstag
- GetAllSaintsDay() = Allerheiligen
- GetSaintMartin() = Martinstag
- GetMemorialDay() = Volkstrauertag
- GetDayOfPrayerAndRepentance() = Buß- und Bettag
- GetSundayOfTheDead() = Totensonntag
- GetFirstAdvent() = Erster Advent
- GetSecondAdvent() = Zweiter Advent
- GetThirdAdvent() = Dritter Advent
- GetFourthAdvent() = Vierter Advent
- GetNicholasDay() = Nikolaus
- GetChristmasEve() = Heiligabend
- GetFirstChristmasDay() = Erster Weihnachtstag
- GetSecondChristmasDay() = Zweiter Weihnachtstag
- GetSylvester() = Silvester

## Getter- Methoden um die Werte abrufen zu können:

GetSortDate()       - Wird eigentlich verwendet um die ArrayList zu sortieren. Beinhaltet ausschließlich das Datum in Systemformat
GetDate()           - Ruft das Datum des Feiertages, Gedenktages oder Ereignis ab. Format: ``Montag, 01.01.2024``
GetNameOfHoliday()  - Name des Feier-, Gedenktages oder Ereigniss
GetHolidayType()    - Typ des Tages: `Gesetzlicher Feiertag`, `Regionaler Feiertag`, `Gedenktag`, `Ereigniss`, `Vorweihnachtstag` oder `Jahresende`.
GetRegions()        - Bundesländer die betroffen sind. Hauptsächlich bei Regionalen Feiertagen und Ereignisse.

## Beispiele:

Die Datei App.java enthält Beispiele, die recht gut dokumentiert sind. (Deutsch)