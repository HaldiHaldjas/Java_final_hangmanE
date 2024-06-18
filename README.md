# Hangman 2024 õpilased

Käesolev projekt on loodud õpilastele mängu põhi funktsionaaluse lisamiseks. Valmis on hetkel ainult disain ja mitmed
abiklassid ning meetodid. Kõik abiklassid pole ka kasutusele võetud vaid aja kokkuhoiu mõttes lisatud. Viimases tunnis
lisame koos mõned mängu jaoks vajalikud funktsionaalsused ning mängu tegelik lõpetamine jääb õpilastele.

# TODO Koos tehtavad funktsionaalsused 
1. ~~Kategooriate lisamine rippmenüüsse Seadete vahelehel~~
2. ~~Kategooria muutmisel muudetakse mudelis valitud kategooriat~~
3. ~~Rakendust käivatades saab käsureal anda teise andmebaasi faili~~
4. ~~Seaded vahelehele lisa label kasutusel oleva andmebaasi nimega (ilma kaustateeta) - Tunnis iseseisvalt. Enne nuppe, üle kahe veeru, rohelise teksti värviga. Keskele~~ 
5. ~~Võllapuu piltide näitamine GameBoardil~~
6. ~~Uue mängu alustamine (mängu aeg käivitub) ja katkestamine (mängu aeg seiskub)~~
7. ~~Sisestuskasti saab ainult ühe märgi kirjutada~~
8. ~~Seadete leht näitab jooksvat kuupäeva ja kellaaega~~
9. ~~Sisestuskast on aktiivne (tekstikursor vilgub)~~
10. ~~Edetabeli lisamine vahelehel~~
11. ~~Java dokumentatsiooni genereerimien (JavaDoc) koodi põhjal~~
12. ~~Rakenduse .jar faili loomine (cmd fail käivitamiseks või käsurealt)~~

# Lisatud klassid, aga pole veel kasutusel
1. helpers/GameTimer - See klass tegeleb mänguajaga.
2. helpers/RealTimer - See klass tegeleb reaalseaja näitamisega
3. helpers/TextFieldLimit - See klass tegeleb sisestuskasti sisestatava teksti pikkusega (meil ainult üks märk)
4. listeners/ComboboxChange - See tegeleb rippemnüüst valiku tegemisega ja mudelis kategooria muutmisega
4. models/datastructures/DataScore - See klass tegeleb edetabeli infoga andmebaasist
5. models/datastructures/DataWords - See klass tegeleb äraarvatavate sõnadega (võib-olla pole vaja)

## Lisatud meetodid, kuid pole veel kasutusel
1. views.view -> hideButtons() - mängu olukorra tekitamine (mänguks mitte vajalik peita ja vajalik nähtavale)
2. views.view -> showButtons() - mitte mängu olukorra tekitamine (mitte mänguks vajalik peita ja vajalik nähtavale)
3. helpers.GameTime -> addSeconds(seconds) - mängus trahvi lisamiseks
