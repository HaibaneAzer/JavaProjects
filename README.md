# Mitt program

Se [oppgaveteksten](./OPPGAVETEKST.md) til semesteroppgave 2. Denne README -filen kan du endre som en del av dokumentasjonen til programmet, hvor du beskriver for en bruker hvordan programmet skal benyttes.

# Konsept

------- Danmaku --------

Et "Bullet-hell"/"Bullet curtain" spill hvor du skal overleve en sperreild av prosjektiler. 
Her kan du velge mellom ulike karakterer (bare en foreløpig) med ulike magiske våpen som skyter bullets. Med dem skal du på eventyr opp et fjell hvor du skal bekjempe gamle guder som tyraniserer lokale landsbyer. 
Men vær forsiktig, fordi disse gudene har mange allierte som kan skytte "bullets" på magisk vis, og som vil gjøre alt for å stoppe deg.
 
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
- `Bullets` klassen representerer skuddene på banen som blir skutt av spilleren og fienden.

### Visning

planen er at klassen `DanmakuView` skal:

- først tegne danmaku-banen,
- deretter tegne spiller og fiendene, og
- tilslutt tegne skuddene.

I tillegg skal banen kunne endre på seg uten at den blir så tegnet oppå spilleren og skuddene. Dette trengs for å kunne lage en rullende bakgrunn på banen for å gi illusjonen av at spilleren beveger seg opp et fjell. `ViewableDanmakuModel` vil være et grensesnitt som `DanmakuModel` skal implementere.

### Kontroll

Klassen `DanmakuController` skal kunne endre på modellen basert på input fra brukeren i tillegg til styre ting som skjer av seg selv (for eksempel at fiendene kommer ned på banen). 

`ControllableDanmakuModel` vil være grensesnittet som forteller hvilke metoder kontrolleren trenger til modellen.

## Steg for steg

1. lag Grid og Sprite
2. Tegne banen
3. Tegne Spiller
4. Flytte spiller
5. Skytte skudd med spiller
6. Tegne fiender
7. Gi fiender ulike periodiske bevegelser
8. Få spillerens skudd til å skade og eleminere fiender.
8. lag stage(s) og håndtere game over.
9. En timer som bytter fiendens bevegelses type
10. Lag en Boss-fiende og timer for angrep.
11. Annet...

### Progresjon

0 av 11


