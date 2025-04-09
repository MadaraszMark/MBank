# 💳 MBANK

_Egy biztonságos és modern internetbanki alkalmazás JavaFX-szel._

![Last Commit](https://img.shields.io/github/last-commit/your-username/mbank)
![Java](https://img.shields.io/badge/java-71.7%25-blue)
![Languages](https://img.shields.io/badge/languages-2-blue)

_A következő eszközökkel és technológiákkal készült:_

![Java](https://img.shields.io/badge/-Java-blue?style=for-the-badge&logo=java)
![FXML](https://img.shields.io/badge/-FXML-informational?style=for-the-badge)
![CSS](https://img.shields.io/badge/-CSS-purple?style=for-the-badge)

---

## 🧭 Tartalomjegyzék

- [Áttekintés](#áttekintés)
- [Kezdeti lépések](#kezdeti-lépések)
  - [Előfeltételek](#előfeltételek)
  - [Telepítés](#telepítés)
  - [Használat](#használat)

---

## 🔍 Áttekintés

Az **MBank** egy modern, JavaFX alapú internetbanki alkalmazás, amely lehetővé teszi a felhasználók számára a pénzügyeik kezelését egy letisztult és reszponzív felületen keresztül.  

#### 🎯 Fő funkciók

- 🗄️ **Adatbázis inicializálás** – Az SQL struktúrák automatikus létrehozása fejlesztői környezetben.
- 🔐 **Felhasználókezelés** – Biztonságos regisztráció, belépés, SHA1 hash jelszóval.
- 💸 **Tranzakciókezelés** – Pénzutalás, egyenlegkezelés, tranzakciós előzmények.
- 📊 **Statisztikák** – Havi bevétel és kiadás összesítése.
- 🎨 **JavaFX alapú UI** – Reszponzív és stílusos felhasználói felület SceneBuilder támogatással.
- 🧑‍💼 **Admin felület** – Új ügyfelek létrehozása, hibajelentések listázása.
- 🛠️ **Hibajelentés** – Felhasználói visszajelzések kezelése külön ablakban.
- ✅ **Tesztelési keretrendszer** – JUnit tesztek a fő funkciókhoz.

---

## 🚀 Kezdeti lépések

### 🛠️ Előfeltételek

A projekt futtatásához szükséges:

- `Java 8` vagy újabb verzió
- `MySQL` adatbázis (pl. XAMPP, WAMP vagy Workbench)
- `Maven` (a buildeléshez)
- `JavaFX` telepítve (SceneBuilder ajánlott)

---

### ⚙️ Telepítés

1. **Kódbázis klónozása**:
   ```bash
   git clone https://github.com/sajatfelhasznalo/mbank.git
2. **Adatbázis létrehozása**:
   ```bash
   Nyisd meg a MySQL Workbench-et vagy más SQL klienst.
   Futtasd a mbank/install.sql fájlt (vagy az application._install Java osztályt), amely automatikusan létrehozza az összes táblát és kapcsolatot.
3. **Persistance.xml beállítása**

### 🧪 Használat  
- Bejelentkezés: Felhasználónév + jelszó.
- Főoldal: Számlaegyenleg, utolsó tranzakciók (max. 4).
- Átutalás: Címzett számlaszám + összeg + opcionális üzenet.
- Profil: Felhasználói adatok megtekintése.
- Számlák: Jelenlegi folyószámla információk, formázottan.
- Tranzakciók: Teljes tranzakciós előzmény, szűrhető.
- Admin felület: Új ügyfelek létrehozása, hibák listázása.
- Hibabejelentés: Visszajelzés küldése külön ablakból.

*Fejlesztő: Márk – 2025, JavaFX tanulóprojekt*

   
