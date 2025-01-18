# ZEN-SORIUM

**ZEN-SORIUM** es una aplicación de domótica diseñada para mejorar la calidad de vida de personas con movilidad reducida. Este proyecto combina tecnología avanzada de sensores y una interfaz de usuario intuitiva para automatizar tareas diarias como el control de luces, temperatura, humedad, y persianas, proporcionando mayor autonomía y comodidad.

---

## 🌟 **Características Principales**

1. **Automatización Inteligente:**
   - Control de luces basado en sensores de luminosidad.
   - Regulación automática de temperatura y humedad.
   - Apertura y cierre de persianas según condiciones predefinidas.

2. **Roles Personalizados:**
   - **Usuario**: Acceso básico a las funciones del hogar.
   - **Administrador**: Gestión de usuarios, dispositivos y permisos.
   - **Familiar**: Comunicación mediante chat y supervisión remota.

3. **Sistema de Comunicación:**
   - Chat en tiempo real entre usuarios relacionados (familiares y administradores).

4. **Gestión de Dispositivos:**
   - Administración de sensores de temperatura, humedad y luz.
   - Visualización de estados de dispositivos en tiempo real.

---

## 🛠️ **Tecnologías Utilizadas**

- **Lenguaje de programación:** Java.
- **Framework de Interfaz Gráfica:** JavaFX con SceneBuilder.
- **Gestión de Base de Datos:** MySQL, administrado con HeidiSQL.
- **Control de Versiones:** GitHub.
- **Entorno de Desarrollo:** Eclipse.
- **Metodología Ágil:** Scrum, con herramientas como Trello y Google Docs.

---

## 📋 **Estructura de la Aplicación**

### **Sensores Integrados**
- **Sensor de Temperatura y Humedad (DHT11):**
  - Rango: 0°C a 50°C, 20%-90% de humedad relativa.
  - Uso: Ajuste automático de calefacción y aire acondicionado.

- **Sensor de Luz (LDR):**
  - Uso: Encendido/apagado automático de luces y control de persianas.

- **Sensor Ultrasónico (HC-SR04):**
  - Uso: Determinación del estado de persianas y puertas (abierta, cerrada o parcialmente abierta).

### **Base de Datos**
La base de datos incluye las siguientes entidades principales:
- **Usuarios:** Información personal, roles y permisos.
- **Dispositivos:** Gestión de sensores y estados.
- **Conversaciones:** Mensajes entre usuarios relacionados.
- **Historial de Sensores:** Datos recopilados por los sensores.

---

## 🚀 **Cómo Ejecutar el Proyecto**

### **Requisitos Previos**
- Java 8 o superior.
- MySQL con la base de datos inicial configurada (archivo `piizensorium.sql` incluido).
- IDE como Eclipse con soporte para JavaFX.

### **Pasos para Configurar**

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/Alejandroortega2002/Proyecto_Domotica_ZENSORIUM.git
   cd Proyecto_Domotica_ZENSORIUM
   ```

2. **Configurar la Base de Datos:**
   - Importa el archivo `piizensorium.sql` en MySQL.
   - Configura las credenciales de conexión en el archivo de configuración del proyecto.

3. **Abrir en Eclipse:**
   - Importa el proyecto como un proyecto de Java.
   - Asegúrate de que JavaFX esté configurado correctamente.

4. **Ejecutar la Aplicación:**
   - Ejecuta el archivo principal desde el IDE.

---

## 🎯 **Objetivos del Proyecto**

1. **Generales:**
   - Mejorar la calidad de vida de personas con movilidad reducida a través de un sistema domótico accesible.

2. **Específicos:**
   - Desarrollar una interfaz intuitiva con roles definidos (Usuario, Administrador, Familiar).
   - Automatizar tareas diarias como control de temperatura, iluminación y persianas.
   - Garantizar la seguridad y privacidad de los datos mediante la gestión avanzada de usuarios.
   - Proveer comunicación efectiva mediante un sistema de chat en tiempo real.

---

## 📦 **Estructura del Proyecto**

```
Proyecto_Domotica_ZENSORIUM/
├── Fotos_del_Circuito/
│   ├── Foto1.jpeg
│   ├── Foto2.jpeg
│   ├── Foto3.jpeg
│   └── Foto4.jpeg
├── Sensores_BD_Arduino/
│   └── Sensores_BD_Arduino.ino
├── piizensorium.sql
├── src/
│   ├── main/
│   │   ├── Controller/
│   │   ├── Model/
│   │   ├── View/
│   │   └── App.java
│   └── test/
└── README.md
```

---

## 📝 **Conclusión**

El proyecto ZEN-SORIUM ha superado las expectativas iniciales al proporcionar una solución efectiva para personas con movilidad reducida. Con un enfoque en la automatización, la facilidad de uso y la personalización, esta aplicación tiene el potencial de transformar vidas mediante el uso innovador de tecnología domótica.

---
