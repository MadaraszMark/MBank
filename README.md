# 💳 MBANK

_Egy biztonságos és modern internetbanki alkalmazás JavaFX-szel._

![Last Commit](https://img.shields.io/github/last-commit/your-username/mbank)
![Java](https://img.shields.io/badge/java-71.7%25-blue)
![Languages](https://img.shields.io/badge/languages-2-blue)

_Built with the tools and technologies:_

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
  - [Tesztelés](#tesztelés)
- [Fejlesztési lehetőségek](#fejlesztési-lehetőségek)

---

## 🔍 Áttekintés

Az **MBank** egy modern, JavaFX alapú internetbanki alkalmazás, amely lehetővé teszi a felhasználók számára a pénzügyeik kezelését egy letisztult és reszponzív felületen keresztül.

### ❓ Miért pont MBank?

Ez a projekt leegyszerűsíti a banki műveleteket, miközben biztonságos és hatékony felhasználói élményt kínál.

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
