# Aplikace GVP Intranet pro Android
[App icon](app/src/main/ic_launcher-web.png)

Aplikace pro Android k Intranetu GVP.  
- OS: Android 5.0+
- Jazyk: Kotlin
- Autor: František Hlinka

# Popis
Cílem projektu je vytvořit aplikaci pro mobilní operační systém Android, ve které si uživatel bude moci číst články a aktuality z Intranetu školy. 
Články bude možné zobrazovat, komentovat a vytvářet. Aktuality bude možné zobrazovat a přidávat. 
V aplikaci se snažím používat nejnovější (k r. 2019) architektury, paradigmata a knihovny napsané či používané lídry v oboru, například samotným Googlem. Zmíním například Dagger2 pro Dependency Injection, Android Architecture Components z nástrojů Android Jetpack pro implementaci architektury MVVM (Model, View, ViewModel), Room database pro správu databází, Retrofit pro práci se sítí. 
Zároveň by aplikace měla nějak vypadat, a tak se pokusím následovat Material Design Guidelines (https://material.io/) a zároveň přidat trochu svého vlastního grafického nadšení.
Jako backend pro aplikaci budou sloužit jednoduché PHP skripty, které budou umístěné na serveru školy a převedou data z databáze do JSON formátu. 

# Jak projekt spustit a zobrazit kód
Zkompilované aplikace jsou zde ve složce `releases`.
APK soubor je potřeba přesunout do zařízení s Androidem a nainstalovat. 
Je potřeba mít v nastavení zařízení povolenou instalaci z neověřených zdrojů.
Projekt je možné otevřít například v Android Studio nebo v Eclipse.

# Changelog
### v6, 2.1 release, 12.4.2019 
Initial commit na github. Doplněna dokumentace kódu.
### Pre-github changelog:
##### v6, 2.1 release, 1.4.2019
Opravena chyba v zobrazování obrázků, přidána sekce o aplikaci a s ní informace o licencích.
##### v5, 2.0, 31.3.2019
Přidáno zobrazování obrázků, dokončeny komentáře, přidávání dat. První release verze.
##### v4, 1.3 Beta, 26.3.2019
Přidána možnost přidávání komentářů; přidáno nastavení; v backendu dodělány php skripty pro přidávání dat; vylepšení zobrazení komentářů
##### v3, 1.2 Beta, 19.3.2019
Přidáno: Paging článků (jde scrollovat nekonečně a vidět všechny články ze serveru); Základní funkcionalita komentářů, ale zatím to graficky nevypadá hezky.
Opraveno:  Aktualizace dat; Horizontální scrollování autora a data u článků
TODO: vylepšit komentáře a přidat možnost vytváření komentářů; zajistit aby se RecyclerView neobnovoval po každém resume fragmentu; přidat možnost pull-to-refresh všude
##### v2, 1.1 Beta, 17.3.2019
Základní funkce fungují, články a aktuality se stahují a ukládají do databáze, použito MVVM, DI, Retrofit.
TODO: Data paging, komentáře ke článkům, přidávání článků a aktualit
##### v1, 1.0, 28.2.2019
Zatím vůbec nefunguje, padá při startu, ale pracuju na tom. 
Nechtěl jsem to odfláknout a učím se na tom nové věci (dependency injection, MVVP architecture), kterým ještě moc nerozumím, proto zatím nic.
Ale už je tam aspoň základní struktura tříd aplikace.
Až to půjde tak sem hodím update.
