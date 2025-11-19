<div align="center">

# 🌙 Ocio Nocturno A Coruña

### Aplicación móvil Android para descubrir y valorar locales de ocio nocturno

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Django](https://img.shields.io/badge/Django-092E20?style=for-the-badge&logo=django&logoColor=white)](https://www.djangoproject.com/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://www.python.org/)

![Estado](https://img.shields.io/badge/Estado-Finalizado-brightgreen?style=flat-square)
![Versión](https://img.shields.io/badge/Versi%C3%B3n-1.0-blue?style=flat-square)
![Licencia](https://img.shields.io/badge/Licencia-MIT-green?style=flat-square)

[Características](#-características) • 
[Instalación](#-instalación) • 
[Tecnologías](#-tecnologías) • 
[API](#-api-rest) • 
[Licencia](#-licencia)

</div>

---

## 📱 Sobre el Proyecto

**Ocio Nocturno A Coruña** es una aplicación móvil que centraliza la información de locales nocturnos en A Coruña, permitiendo a los usuarios descubrir nuevos lugares, compartir experiencias mediante reseñas y mantenerse informados sobre eventos.

### ¿Qué puedes hacer?
```
🔍 Explorar        →  Descubre pubs, discotecas y locales de ambiente
⭐ Valorar         →  Comparte tu experiencia con reseñas de 1-5 estrellas
🎉 Eventos         →  Consulta las próximas fiestas y eventos temáticos
🎵 Música          →  Accede directamente a las playlists de Spotify
👤 Mi perfil       →  Gestiona todas tus reseñas desde un solo lugar
```

---

## ✨ Características

<table>
<tr>
<td width="50%">

### 🔐 Autenticación Segura
- Registro de usuarios
- Login con tokens persistentes
- Gestión de sesión automática

### 🏠 Exploración Intuitiva
- Listado completo de locales
- Búsqueda en tiempo real
- Información detallada con imágenes

</td>
<td width="50%">

### ⭐ Sistema de Reseñas
- Valoraciones de 1-5 estrellas
- Comentarios detallados
- Vista de "Mis Reseñas"

### 🎉 Gestión de Eventos
- Eventos próximos por local
- Fecha, hora y descripción
- Actualización en tiempo real

</td>
</tr>
</table>

### 🎵 Integración con Spotify
Apertura directa de playlists en la app de Spotify o navegador (fallback automático)

### 📱 Navegación Moderna
Navigation Drawer con menú lateral deslizable para acceso rápido a todas las funcionalidades

---

## 🚀 Instalación

### Requisitos Previos

**Backend:**
- Python 3.12+
- pip

**Android:**
- Android Studio
- JDK 11+
- Emulador o dispositivo Android 8.0+

### 1️⃣ Clonar el Repositorio
```bash
git clone https://github.com/iagoav888/OcioN.git
cd OcioN
```

### 2️⃣ Configurar Backend Django
```bash
# Navegar al backend
cd DjangoPFC/OcioNProject

# Crear entorno virtual (opcional)
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# Instalar dependencias
pip install django==5.1.3

# Aplicar migraciones
python manage.py migrate

# Iniciar servidor
python manage.py runserver
```

✅ **Servidor corriendo en:** `http://127.0.0.1:8000`

### 3️⃣ Configurar App Android

1. Abrir **Android Studio**
2. `File` → `Open` → Seleccionar carpeta `AndroidPFC/`
3. Esperar sincronización de Gradle
4. Configurar emulador o dispositivo
5. `Run` → `Run 'app'` **(Shift + F10)**

⚠️ **Importante:** Para dispositivos físicos, cambiar `10.0.2.2:8000` por la IP local de tu PC

---

## 🔌 API REST

La aplicación consume **9 endpoints REST** del backend Django:

| Método | Endpoint | Descripción |
|:------:|----------|-------------|
| ![POST](https://img.shields.io/badge/-POST-49cc90?style=flat-square) | `/session/` | Iniciar sesión |
| ![POST](https://img.shields.io/badge/-POST-49cc90?style=flat-square) | `/register/` | Registrar usuario |
| ![GET](https://img.shields.io/badge/-GET-61affe?style=flat-square) | `/locales/` | Listar locales |
| ![GET](https://img.shields.io/badge/-GET-61affe?style=flat-square) | `/locales/?nombre=<texto>` | Buscar locales |
| ![GET](https://img.shields.io/badge/-GET-61affe?style=flat-square) | `/locales/<id>/` | Detalle de local |
| ![GET](https://img.shields.io/badge/-GET-61affe?style=flat-square) | `/locales/<id>/reviews/` | Reseñas del local |
| ![POST](https://img.shields.io/badge/-POST-49cc90?style=flat-square) | `/locales/<id>/reviews/` | Crear reseña |
| ![POST](https://img.shields.io/badge/-POST-49cc90?style=flat-square) | `/reviews/user/` | Mis reseñas |
| ![GET](https://img.shields.io/badge/-GET-61affe?style=flat-square) | `/eventos/?local_id=<id>` | Eventos del local |

### 📝 Ejemplo de Uso

**Petición:**
```json
POST /session/
Content-Type: application/json

{
  "username": "usuario",
  "password": "contraseña123"
}
```

**Respuesta:**
```json
{
  "token": "b10d8fc4309f85c5b37962c8aa19fd5b4f31b2a9..."
}
```

---

## 💻 Tecnologías

<table>
<tr>
<td align="center" width="33%">

### 📱 Frontend

![Java](https://img.shields.io/badge/Java_11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

![Android](https://img.shields.io/badge/Android_SDK-3DDC84?style=for-the-badge&logo=android&logoColor=white)

**Librerías:**
- Volley 1.2.1
- Glide 4.16.0
- Material Design

</td>
<td align="center" width="33%">

### ⚙️ Backend

![Python](https://img.shields.io/badge/Python_3.12-3776AB?style=for-the-badge&logo=python&logoColor=white)

![Django](https://img.shields.io/badge/Django_5.1.3-092E20?style=for-the-badge&logo=django&logoColor=white)

**Base de datos:**
- SQLite
- Django ORM

</td>
<td align="center" width="33%">

### 🛠️ Herramientas

![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)

![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

**Otros:**
- PyCharm
- Postman

</td>
</tr>
</table>

---

## 🏗️ Arquitectura
```
┌─────────────────────────────────────────────────────────┐
│                   📱 ANDROID APP                        │
│                                                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐            │
│  │Activities│  │ Adapters │  │  Models  │            │
│  │    (6)   │  │    (3)   │  │    (3)   │            │
│  └──────────┘  └──────────┘  └──────────┘            │
│                                                         │
│              Volley (HTTP/JSON)                        │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ REST API (10.0.2.2:8000)
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   🔧 DJANGO BACKEND                     │
│                                                         │
│  ┌──────────────────────┐  ┌──────────────────────┐   │
│  │   9 REST Endpoints   │  │    4 Modelos ORM     │   │
│  │  (endpoints.py)      │  │   (models.py)        │   │
│  └──────────────────────┘  └──────────────────────┘   │
│                                                         │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ SQL
                     ▼
┌─────────────────────────────────────────────────────────┐
│                 💾 SQLite DATABASE                      │
│                                                         │
│   AppUser │ Local │ Review │ Evento                    │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 📂 Estructura del Proyecto
```
OcioN/
│
├── 📱 AndroidPFC/                    # Cliente Android
│   └── app/src/main/java/.../
│       ├── 🎬 Activities/           # Pantallas (6)
│       │   ├── StartActivity
│       │   ├── LoginActivity
│       │   ├── RegisterActivity
│       │   ├── MainActivity
│       │   ├── DetalleActivity
│       │   └── MisResenasActivity
│       │
│       ├── 🔄 Adapters/             # RecyclerView (3)
│       │   ├── LocalAdapter
│       │   ├── ReviewAdapter
│       │   └── EventoAdapter
│       │
│       └── 📦 Models/               # Clases de datos (3)
│           ├── Local
│           ├── Review
│           └── Evento
│
└── 🔧 DjangoPFC/                    # Servidor Backend
    └── OcioNProject/MiOcioNApp/
        ├── models.py                # 4 Modelos
        ├── endpoints.py             # 9 Endpoints REST
        └── admin.py                 # Panel Admin
```

---

## 👨‍💻 Autor

<div align="center">

<img src="https://github.com/iagoav888.png" width="100" style="border-radius: 50%"/>

### Iago Abelleira Vázquez

[![GitHub](https://img.shields.io/badge/GitHub-iagoav888-181717?style=for-the-badge&logo=github)](https://github.com/iagoav888)

**Proyecto de Fin de Ciclo DAM** • 2024/2025

</div>

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT** - ver el archivo [LICENSE](LICENSE) para más detalles.

<details>
<summary>📜 Ver Licencia Completa</summary>
```
MIT License

Copyright (c) 2025 Iago Abelleira Vázquez

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

</details>

---

<div align="center">

**⭐ Si te ha gustado este proyecto, dale una estrella en GitHub ⭐**

Desarrollado con ❤️ en A Coruña

</div>
