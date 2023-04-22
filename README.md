# Mitt program

Se [oppgaveteksten](./OPPGAVETEKST.md) til semesteroppgave 2. Denne README -filen kan du endre som en del av dokumentasjonen til programmet, hvor du beskriver for en bruker hvordan programmet skal benyttes.

# Konsept

------- Danmaku --------

Et "Bullet-hell"/"Bullet curtain" spill hvor du skal overleve en sperreild av prosjektiler. 
Her kan du velge mellom ulike karakterer (bare en foreløpig) med ulike magiske våpen som skyter bullets. Med dem skal du på eventyr opp et fjell hvor du skal bekjempe gamle guder som tyraniserer lokale landsbyer. 
Men vær forsiktig, fordi disse gudene har mange allierte som kan skytte "bullets" på magisk vis, og som vil gjøre alt for å stoppe deg.
 
------------------------

# BrukerManual

------- Keyboard -------
- Flytt spilleren opp, ned, venstre og høyre med pil-tastene.
- Skyte med Z-tast.
- Senk fart og skifte til "focused"-mode med leftshift-tast.
------------------------

# Oversikt

### Modellen

Modellen skal ha:

- en *bane* hvor spillet foregår,
- *Hitboxes* for spiller, fiender og skudd,
- *bevegelses logikk* som bestemmer hvordan fiender og skudd flytter og roterer seg på banen.
- *Liv/Hp-barer* for spiller/fiendene.
- *Power* som bestemmer skaden til spillerens "bullets".

Klasser som må skrives:
- `DanmakuModel` som representerer tilstanden til et komplett spill. Klassen skal ha feltvariabler som representerer hele banen, data om spiller og fiendene, samt info om hvor i spillet vi befinner oss (stage, game over, menu)
- `DanmakuField` representerer et rektangulært område hvor spiller, fiender og skudd skal befinne seg inn i.
- `Sprite` er en abstrakt klasse som bullets, player og enemies klassene utvider.
- `Bullets` klassen representerer skuddene på banen som blir skutt av spilleren og fienden.
- `Player` klassen representerer karakter(er) som man kontrollerer med tastatur: pil-taster for bevegelse, z-tast for å skyte, shift-tast for å bevege seg tregere og fokusere skuddene til spilleren. Hver karakter har sitt eget skyte mønster. Spilleren starter med 3 liv, hvor en kan miste et liv ved å berøre fiende skudd eller fiendene selv.
- `Enemies` klassen representerer alle fiender som skyter på spilleren. For enhver stage så kommer enemies ned på banen i ulike intervaller og skyter skudd i veldig varierte mønstre. Hver av dem har healthpoints som spilleren my skytte flere skudd for å tømme helt.

### Visning

planen er at klassen `DanmakuView` skal:

- først tegne danmaku-banen,
- deretter tegne spiller og fiendene, og
- tilslutt tegne skuddene.

I tillegg skal banen kunne endre på seg uten at den blir så tegnet oppå spilleren og skuddene. Dette trengs for å kunne lage en rullende bakgrunn på banen for å gi illusjonen av at spilleren beveger seg oppover. `ViewableDanmakuModel` vil være et grensesnitt som `DanmakuModel` skal implementere.

### Kontroll

Klassen `DanmakuController` skal kunne endre på modellen basert på input fra brukeren i tillegg til styre ting som skjer av seg selv (for eksempel at fiendene kommer ned på banen). 

`ControllableDanmakuModel` vil være grensesnittet som forteller hvilke metoder kontrolleren trenger til modellen.

## Steg for steg

1. lag Grid og Sprite ✓
2. Tegne banen ✓
3. Tegne Spiller ✓
4. Flytte spiller ✓
5. Skytte skudd med spiller ✓
6. Tegne fiender ✓
7. Gi fiender ulike periodiske bevegelser ✓
8. Få spillerens skudd til å skade og eleminere fiender. ✓
9. lag stage(s) (begynn med 2) og håndtere game over (Her skal fiender komme med gitte intervaller forskjellig for hver stage). ✓
10. Lag en Boss-fiende og timer for angreps faser (normal phase og super attack phase). ✓
11. Legge til pixel art for spiller, skudd og fiender.
(spillet ansees som ferdig etter steg 11, men legger til mer hvis mulig) ✓
12. lag menu screen hvor du kan velge mellom player 1 og 2
13. legge til rulende backgrunn (pixel art eller annet) unik for hvert stage. ✓
14. tegne scoreboard ved siden av banen. ✓
15. implementer mer komplekse skudd mønstre for fiender og boss (kan stort sett lages med trigonometriske funksjoner)
16. legg til music i backgrunnen som endrer seg etter steg og boss battle. ✓

### Progresjon

14 av 11 (alt gjort)

# Video

Link: https://youtu.be/VHMyTmT0L-Y.


