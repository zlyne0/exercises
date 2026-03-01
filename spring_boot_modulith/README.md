@startuml
[Zero] --> [A] : uses
[Zero] --> [C] : uses
[A] --> [B] : uses
[Zero] --> [domain] : depends on
[domain] --> [domain2] : depends on

[ParallelZero] --> [E] : uses
[ParallelZero] --> [C] : uses

@enduml

### 1. Spostrzerzenia do testów
```
    @ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES)
```
Tworzy beany i konfiguracje tylko dla testowanego modułu i modółów od których zależy. 
Nie skanuje i nie tworzy beanów które nie używa. Przykłady Zero i ParallelZero.

### 1.1.
Uruchamiając wszystkie testy wygląda to tak jakby dla każdego testu od nowa tworzył całą hierarchię beanów.
Nie widze cachowania utworzeonego moduło kontekstu pomiedzy testami. Przykładowo dal Zero i ParallelZero tworzy dokładnie te same beany i konfiguracje.
Mimo że obydwa mają wspólne moduł C

### 2. Spostrzerzenia do generowania diagramów dokumentacji
Przy generowaniu dokumentacji za pomoca Documenter nie są prezentowane zaleźności modułów które są jako subpackage.
Te jako oddzielne subpackage są prezentowane jako jeden moduł o nazwie parenta, i są tworzone oddzielne diagramy dla subpackage.
Nie widzę żadnych ustawień zmiany tego typu zachowania.
Modulith generuje dla takiej sytuacji oddzielne diagram. "domain2" jest oddzielnym modułem ale jako subpackage "domain". 

![Spring modulith all components](/images/modulith-components.png)
![Spring modulith "domain" module with subpackage](/images/module-domain.png)

Aby tą możliwość uzyskać napisałem własny documenter, klasa MyDocumenter która generuje diagram ze wszystkim modułami.
Dodatkowo zaznaczam które moduły składają się na submoduły. 
![Spring modulith all components](/images/c4-components-complete.png)

Documenter potrafi wygenerować diagram z modułów tylko z tych które są kożeniami dla submodułów.
`c4-components-roots-only.puml`

Documenter ma przełącznik do generowania diagramu bez relacji.
`c4-components-without-relations.puml`