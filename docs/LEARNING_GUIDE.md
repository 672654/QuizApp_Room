

---

## Kotlin & Coroutines

### Suspend
Et "pause-merke" på en funksjon. Forteller Kotlin at "denne jobben tar tid, så sett den på pause (uten å blokkere telefonen) til den er ferdig". Det er selve mekanismen bak asynkron koding i Kotlin.

### Flow
Et "vannrør" med data. Den sender verdier kontinuerlig (f.eks. hver gang databasen endres), i motsetning til en vanlig suspend-funksjon som bare gir ett svar.

### Collect vs First. Brukes på en Flow.
*   **Collect:** Skru på "vannrøret" og la den stå åpen. Brukes typisk i ViewModels for å oppdatere UI-et live.
*   **First:** Skru på "vannrøret", hent den første verdien, og skru av med en gang. Nyttig for raske sjekker av databasen ved oppstart.

---

## Android Architecture (MVVM)

### ViewModel
Appens "hjerne" for en spesifikk skjerm. Her ligger logikk og data som skal vises. Den er spesiell fordi den overlever at du roterer skjermen (configuration changes), slik at f.eks. poengsummen din ikke slettes.

### ViewModelScope & Launch
*   **Courutine:** En "lettvektstråd". En måte å kjøre kode i bakgrunnen uten å fryse appen.
*   **ViewModelScope:** "Sjefen" som passer på bakgrunnsjobbene (coroutines). Hvis ViewModellen dør (f.eks. du går ut av skjermen), stopper han automatisk alle jobbene. Dette hindrer minnelekkasjer.
*   **Launch:** Startknappen for en Coroutine. Sier: "Gjør denne jobben i bakgrunnen nå".

### Application Class (`QuizApplication`)
Selve "Eieren" av hele appen. Den starter før alt annet og lever helt til appen avsluttes helt. Brukes for å sette opp ting som bare skal skje én gang, som å starte Room-databasen.

### Repository
En klasse mellom datakildene (Room, API) og ViewModellen. Den bestemmer hvor dataene skal hentes fra, slik at ViewModellen slipper å vite om databasen direkte.

---

## Jetpack Compose UI

### Remember
Gjør at en variabel huskes selv om skjermen tegnes på nytt (recomposition). Kan kun brukes i @Composable.

### By (Delegation)
Et nøkkelord for å slippe å skrive `.value` hele tiden når du jobber med `State`. Det gjør at koden ser ut som vanlige variabler, selv om Compose følger med på dem i bakgrunnen.

---

## Data & System

### Context
Appens "ID-kort" eller bindeledd til Android-systemet. Trengs for å få tilgang til databasen, filer, ressurser (som bilder) eller systemtjenester.

### Room (DAO & Entity)
*   **Entity:** Selve tingen vi lagrer i en tabell (f.eks. et `QuizItem`).
*   **DAO (Data Access Object):** Oppskriften på hvordan vi snakker med databasen (SQL-spørringene).

### ContentProvider
Muligjør at appen deler dataene til andre apper på telefonen. Gjør det mulig for eksterne apper å lese bilde-databasen din på en sikker måte via en URI.
