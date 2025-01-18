#include "DHT.h"            // Incluye la librería para el sensor de temperatura y humedad DHT.
#include <Ultrasonic.h>     // Incluye la librería para el sensor ultrasónico.
#include <MySQL_Generic.h>  // Incluye la librería para la conexión con MySQL.
#include <WiFi.h>           // Incluye la librería para la conexión WiFi.

// Definición de pines
#define DHTPin 25           // Define el pin 25 para el sensor DHT.
#define DHTTYPE DHT11       // Define el tipo de sensor DHT como DHT11.
#define Photoresistor 34    // Define el pin 34 para el fotorresistor (LDR).
#define TRIGGER_PIN  12     // Define el pin 12 como pin de disparo para el sensor ultrasónico.
#define ECHO_PIN     14     // Define el pin 14 como pin de eco para el sensor ultrasónico.

// Creación de objetos para sensores
DHT dht(DHTPin, DHTTYPE);   // Crea un objeto para el sensor DHT.
Ultrasonic ultrasonic(TRIGGER_PIN, ECHO_PIN); // Crea un objeto para el sensor ultrasónico.

// Verificación de la plataforma
#if ! (ESP8266 || ESP32)
  #error This code is intended to run on the ESP8266/ESP32 platform! Please check your Tools->Board setting
#endif

// Configuración de depuración y definiciones de WiFi y MySQL
#define MYSQL_DEBUG_PORT      Serial   // Define el puerto de depuración para MySQL.
#define MYSQL_LOGLEVEL        1        // Define el nivel de registro para MySQL (0 a 4).
#define USING_HOST_NAME       true     // Define si se utiliza un nombre de host en lugar de una dirección IP.

// Credenciales WiFi y MySQL
const char ssid[] = "DIGIFIBRA-7CTd";   // Define el SSID de la red WiFi.
const char pass[] = "K39AyTcRN2";       // Define la contraseña de la red WiFi.
char user[] = "piizensorium";           // Define el usuario de MySQL.
char password[] = "Zensorium@2024";     // Define la contraseña de MySQL.

#if USING_HOST_NAME
  char server[] = "195.235.211.197";    // Define la dirección del servidor MySQL usando el nombre de host.
#else
  IPAddress server(195, 235, 211, 197); // Define la dirección IP del servidor MySQL.
#endif

uint16_t server_port = 3306;            // Define el puerto del servidor MySQL.
char default_database[] = "piizensorium";   // Define el nombre de la base de datos.
char default_table[]    = "historicodatos"; // Define el nombre de la tabla.

// Objeto para la conexión MySQL
//WiFiClient client;
MySQL_Connection conn((Client *)&client); // Crea un objeto para la conexión MySQL.

void setup() {
  Serial.begin(115200);  // Inicializa la comunicación serial a 115200 baudios.
  dht.begin();           // Inicializa el sensor DHT.
  WiFi.begin(ssid, pass); // Inicia la conexión WiFi con el SSID y la contraseña proporcionados.
  
  while (WiFi.status() != WL_CONNECTED) { // Espera hasta que la conexión WiFi se establezca.
    delay(500); 
    Serial.print(".");
  }

  Serial.println("Conectado a la red WiFi.");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // Muestra la dirección IP obtenida.
}

void runInsert(String query) {
  MySQL_Query query_mem = MySQL_Query(&conn); // Crea un objeto para la consulta MySQL.

  if (conn.connected()) {  // Verifica si la conexión está activa.
    Serial.println(query);
    
    if (!query_mem.execute(query.c_str())) { // Ejecuta la consulta y verifica si hubo un error.
      Serial.println("Error al insertar datos");
    } else {
      Serial.println("Datos insertados correctamente");
    }
  } else {
    Serial.println("Desconectado del servidor. No se puede insertar.");
  }
}

void loop() {
  // Leer el valor del sensor de luz
  int analog_value = analogRead(Photoresistor); // Lee el valor analógico del LDR.
  float brightness = mapFloat(analog_value, 300, 3000, 0, 100); // Convierte el valor a porcentaje.
  Serial.print("Luz: ");
  Serial.print(brightness);
  Serial.println("%");

  // Leer la humedad y la temperatura del DHT11
  float h = readDHTHumidity();
  float t = readDHTTemperature();

  if (isnan(h) || isnan(t)) {  // Verifica si las lecturas del sensor DHT son válidas.
    Serial.println("Failed to read from DHT sensor!");
  } else {
    Serial.print("Humedad: ");
    Serial.print(h);
    Serial.print(" %\t");
    Serial.print("Temperatura: ");
    Serial.print(t);
    Serial.println(" *C");
  }

  // Leer la distancia del sensor ultrasónico
  float distance_cm = ultrasonic.read(CM);

  if (distance_cm > 0) { // Verifica si la lectura de distancia es válida.
    Serial.print("Distancia: ");
    Serial.print(distance_cm);
    Serial.println(" cm");
  } else {
    Serial.println("Failed to read distance from ultrasonic sensor!");
  }

  Serial.println("Intentando conectar a la base de datos...");
  
  // Conectar a la base de datos y enviar datos
  if (conn.connect(server, server_port, user, password, default_database)) { // Intenta conectar a la base de datos.
    Serial.println("Conectado");
    delay(500);
    
    // Construye las consultas SQL para insertar los datos.

    //PARA SENSOR DE TIPO TEMPERATURA
    String queryTemp = "INSERT INTO historicodatos (id_sensor, valor_dato, fecha) VALUES (58, '" + String(t) + "ºC', NOW());";
    
    //PARA SENSOR DE TIPO HUMEDAD
    String queryHum = "INSERT INTO historicodatos (id_sensor, valor_dato, fecha) VALUES (59, '" + String(h) + "%', NOW());";
    
    //PARA SENSOR DE TIPO LUZ/LUMINOSIDAD
    String queryLuz = "INSERT INTO historicodatos (id_sensor, valor_dato, fecha) VALUES (60, '" + String(brightness) + "%', NOW());";

    //PARA SENSOR DE TIPO DISTANCIA
    String queryDistancia = "INSERT INTO historicodatos (id_sensor, valor_dato, fecha) VALUES (61, '" + String(distance_cm) + "cm', NOW());";

    // Ejecuta las consultas para insertar los datos.
    runInsert(queryTemp);
    runInsert(queryHum);
    runInsert(queryLuz);
    runInsert(queryDistancia);

    conn.close(); // Cierra la conexión.
  } else {
    Serial.println("Conexión fallida. Intentando nuevamente en la próxima iteración.");
  }

  Serial.println("Durmiendo...");
  Serial.println("================================================");
  delay(2000); // Espera 2 segundos antes de la próxima lectura.
}

// Función mapFloat para convertir valores
float mapFloat(float x, float in_min, float in_max, float out_min, float out_max) {
  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}

// Función para leer la humedad del DHT11 con reintento
float readDHTHumidity() {
  float h = dht.readHumidity();
  int attempts = 3;
  while (isnan(h) && attempts > 0) { // Reintenta la lectura si falla.
    delay(1000);
    h = dht.readHumidity();
    attempts--;
  }
  return h;
}

// Función para leer la temperatura del DHT11 con reintento
float readDHTTemperature() {
  float t = dht.readTemperature();
  int attempts = 3;
  while (isnan(t) && attempts > 0) { // Reintenta la lectura si falla.
    delay(1000);
    t = dht.readTemperature();
    attempts--;
  }
  return t;
}
