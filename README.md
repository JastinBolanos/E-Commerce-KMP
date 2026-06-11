---

# 🌿 REMEDIOZ NATURA - MANIFIESTO TÉCNICO Y VISIÓN DE PRODUCTO (V3.0)

## 1. 🎯 Visión Core y Filosofía del Producto

**Remedioz Natura** trasciende el concepto de tienda genérica; es una plataforma de e-commerce de nicho, hiper-localizada para Lima, Perú. Nuestro propósito es democratizar el acceso al bienestar y soluciones naturales, fusionando la calidez y confianza del comercio tradicional con la eficiencia inquebrantable de la tecnología moderna.

* **El Diferenciador Clave (El Flujo de Confianza):** A diferencia del e-commerce tradicional subordinado a pasarelas de pago automatizadas internacionales (Stripe, PayPal), esta plataforma abraza la idiosincrasia financiera local. El sistema de pago pivota sobre la **Validación Humana por Voucher (Yape / Plin / Transferencia)**. Esto anula comisiones por transacción, mitiga el fraude informático y forja un vínculo de confianza directo y transparente entre vendedor y paciente.
* **Enfoque MVP (Mínimo Producto Viable sin fricciones):** La retención es nuestra métrica estrella. El acceso se reduce a un solo clic (**Google Login**), pulverizando barreras de entrada como formularios tediosos, para garantizar una tasa de conversión máxima. La filosofía es clara: *Lo que no se puede automatizar a coste cero, se gestiona con inteligencia desde el Backoffice.*

---

## 2. 🏛️ Arquitectura de Software (El Stack y la Estructura)

El proyecto se rige bajo el paradigma *"Escribir una vez, ejecutar en cualquier lugar"*, cimentado en una estricta **Clean Architecture + Estructura Basada en Funcionalidades (Feature-Based)** para garantizar escalabilidad absoluta.

**El Stack Core:**

* **Framework:** Kotlin Multiplatform (KMP).
* **UI:** Compose Multiplatform (UI Declarativa y reactiva).
* **BaaS (Backend as a Service):** Firebase (Firestore NoSQL, Storage para multimedia, Auth para identidad).
* **Cloud SDK:** `dev.gitlive:firebase-kmp`.
* **Motor de Renderizado:** Coil 3.
* **Gestión Nativa de Archivos:** FileKit Compose.

**La Estructura Clean (El Mapa del Proyecto):**

* `domain`: El núcleo puro e inmutable. Contiene modelos de datos (`Product`, `Order`) e interfaces de repositorios. Agnosticismo total: no sabe de Android, iOS, Web ni Compose.
* `data`: La infraestructura. Implementaciones de repositorios (Firebase) y resolución de código nativo mediante `expect/actual` confinado en `data.platform`.
* `presentation`: La capa visual dividida quirúrgicamente por módulos de negocio (`home`, `admin`, `checkout`, `profile`), temas, componentes reutilizables y gestores de estado inyectables (`CartManager`).

---

## 3. 🔄 El Flujo de Negocio Principal (Core Business Flow)

El ciclo de vida opera en dos ecosistemas altamente optimizados:

### Perspectiva del Comprador (Cliente)

* **Exploración:** Navegación por un catálogo dinámico y filtrado con consumo de datos en tiempo real.
* **Selección:** Gestión del carrito de compras con control de unidades.
* **Checkout (El Momento de la Verdad):** Cálculo automatizado de costos, visualización de pasarelas locales (QR Yape/Plin), y carga de Voucher (comprobante) directamente desde el sistema de archivos del dispositivo.
* **Línea de Tiempo y Perfil (Tracking):** Panel personal ("Mis Pedidos") con telemetría visual del estado de su paquete: *Pendiente -> Preparando -> En Camino -> Entregado*.

### Perspectiva del Administrador (Backoffice Móvil)

Un ERP de bolsillo, diseñado con la fuente identitaria *Imperial Script* para diferenciar visualmente los entornos. Dividido en 3 centros de comando:

* **Pedidos (Torre de Control):** Auditoría en tiempo real. Permite visualizar vouchers almacenados en la nube y mutar estados de entrega que se reflejan instantáneamente en el dispositivo del cliente.
* **Editar Productos:** Gestión de inventario blindada. Creación, edición, eliminación y subida de assets multimedia sin fricción de UI de consumo.
* **Notificaciones:** El radar del sistema. Alertas reactivas ("Puntito Rojo") de nuevos depósitos o requerimientos, focalizando la atención estrictamente en lo accionable.

---

## 4. 🗄️ Esquema de Base de Datos (Firestore NoSQL)

Ingeniería de datos diseñada para maximizar el rendimiento y mantener la operatividad dentro de los límites del plan "Spark" (Cero costos de lectura innecesaria).

**Colección `Products`**

* `id` (String): Identificador único.
* `name` (String)
* `price` (Double/String)
* `category` (String)
* `description` (String)
* `imageUrl` (String): URL pública (Firebase Storage).

**Colección `Orders`**

* `id` (String)
* `userId` (String)
* `customerName` (String)
* `items` (Array): Nodos de `CartItem` (Producto + Cantidad).
* `totalAmount` (Double)
* `voucherUrl` (String): Ruta del asset en Storage.
* `status` (String): Estado de progreso.
* `timestamp` (Long): Epoch milliseconds nativo para precisión absoluta.

---

## 5. 🚀 Reglas de Oro del Proyecto (Estándares)

Para sostener la integridad técnica y repeler la deuda tecnológica:

* **Aislamiento del Dominio:** La capa `domain` es un santuario. Prohibida la importación de estados de UI (`MutableState`) o librerías de plataforma.
* **Principio DRY (Don't Repeat Yourself) en UI:** Componentes como la "Línea de Tiempo" o "Tarjetas de Producto" son entidades agnósticas reutilizables.
* **UDF (Unidirectional Data Flow):** La UI (`@Composable`) es pasiva. Escucha `StateFlows` desde los ViewModels y emite eventos hacia arriba. Cero peticiones de red directas.
* **UI Resiliente (Fail-Safe):** Uso estricto de placeholders en `AsyncImage` y variables por defecto para evitar bloqueos visuales ante latencia de red.
* **Seguridad Cero-Trust:** El archivo `google-services.json` y claves nativas están estrictamente excluidos del control de versiones (`.gitignore`).

---

## 6. ⚙️ Configuración del Entorno y Firebase (IMPORTANTE)

Para poder compilar y ejecutar este proyecto en tu máquina (Android Studio), es estrictamente necesario configurar el entorno de Firebase. Por razones de seguridad, las credenciales reales de la base de datos no están incluidas en este repositorio público.

**Pasos para compilar:**
1. Localiza el archivo de plantilla llamado `google-services-example.json` en la carpeta `composeApp/`.
2. Duplica ese archivo y renómbralo a **`google-services.json`**.
3. Al hacer esto, el Plugin de Google Services detectará la configuración simulada y la aplicación compilará perfectamente (ideal para trabajar en UI o refactorizaciones).
4. *(Opcional para pruebas en Producción)*: Si necesitas conectarte a la base de datos real, contacta al administrador del proyecto para que te proporcione el archivo `google-services.json` oficial y reemplaza tu versión local. ¡Recuerda que este archivo jamás debe subirse en tus commits!

---

## 7. 🚧 ESTADO ACTUAL: Fase de Conexión y Pulido

Actualmente, el proyecto ha superado la fase de estructuración UI (Mockups) y se encuentra en la etapa crítica de **Sincronización en Producción**:

* **Cableado Central:** Sustituyendo los bypass de navegación y datos falsos por flujos reales controlados por los repositorios.
* **Identidad Real:** Conectando el flujo definitivo de autenticación real mediante `Continuar con Google` y validación de Administrador.
* **Refinamiento UX/UI:** Pulido de transiciones, validaciones de seguridad (ej. evitar checkouts vacíos o sin voucher) y control de errores en tiempo real.

---

## 8. 🔮 ROADMAP FUTURO

* **Integración de Diseño (Figma):** En la próxima iteración del README, este documento se enriquecerá visualmente con los prototipos y diagramas de flujo directos de nuestro Board de Figma, solidificando la documentación visual del sistema.

---

*Construido con disciplina, código limpio y visión de producto.*

---