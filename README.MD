# ATG - Algoritmická teória grafov

Projekt obsahuje testovacie kódy na pár vybraných algoritmov pre spracovanie grafov z algoritmickej teórie grafov. Tieto algoritmy sú z viacerých zdrojov a slúžia len na kontrolu a lepšie pochopenie ich fungovania. Pre lásku božiu nekopírujte ich a neodovzdávajte ako svoje vlastné, naozaj to nie je až tak náročné.

## Implementované algoritmy

 - LabelSet: jednoduchá verzia s použitím ArrayListu, vylepšená verzia s použitím HashMapy a rýchla verzia s použitím PriorityQueue
 - Metóda CPM
 - Tarryho sled
 - Kruskalov algoritmus pre tvorbu najväčšej/najmenšej kostry: verzia s použitím ArrayListu a optimalizovaná verzia použivajúca primitívne arraye
 - Maximálny tok (aktuálne nie úplne finalizaovaná implementácia)

## Použitie

Metóda `main` v triede `GrafyATG` obsahuje testy jednotlivých algoritmov. Pre otestovanie algoritmu stačí odkomentovať jednotlivé riadky. Niektoré algoritmy majú viacero implementácií, niektoré nie sú dostatočne efektívne na veľké grafy (SLOVAKIA, FLORIDA)
Pre zmenu grafu na spracovávanie stačí z enumu `GrafEnum` vybrať iný graf, ktorý zodpovedá danému algoritmu TOK_\* pre toky, CPM_\* pre metódu CPM, FLORIDA NEW_YORK SLOVAKIA a STRAKONICE pre LabelSet, Tarryho sled a Kruskalov

*Časti kódu sú prebraté od osôb, od ktorých nemám explicitný súhlas s ich zdieľaním. Ak by sa vyskytol akýkoľvek problém s nimi, kontaktujte ma, prosím*