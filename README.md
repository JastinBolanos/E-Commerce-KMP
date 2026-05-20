
---

# 🌿 Remedioz Natura | E-Commerce Multiplataforma

> *"Tu salud, nuestra prioridad. Más que una tienda, somos un equipo dedicado a guiarte en tu camino hacia una vida más saludable con soluciones naturales diseñadas para tu bienestar diario."*

**Remedioz Natura** es una plataforma de e-commerce innovadora y de alto rendimiento, diseñada exclusivamente para la ciudad de **Lima, Perú**. Especializada en la venta de medicamentos y productos naturales, la aplicación conecta la sabiduría de la medicina natural con la tecnología más moderna, ofreciendo una experiencia de usuario fluida, reactiva y accesible tanto desde dispositivos móviles como desde la web.

---

## ✨ Características Principales y Lógica de Negocio

* 🔄 **Catálogo Reactivo en Tiempo Real:** Exploración de productos, kits y promociones que reaccionan al instante a los cambios en la base de datos gracias a flujos asíncronos (`StateFlow`).
* 🧠 **Motor de Carrito Inteligente (SSOT):** Gestión centralizada del carrito bajo el principio de *Single Source of Truth*. Los productos seleccionados persisten su estado visual a través de todas las pantallas sin perder sincronía.
* 🛡️ **Autenticación Segura y Limpia:** Sistema de inicio de sesión preparado para integraciones nativas (Google SignIn) separando estrictamente la vista de la lógica de negocio.
* 💼 **Backoffice Móvil (Consola Admin):** Una vista de administración protegida y dedicada para gestionar el inventario. Permite crear, editar, eliminar productos y subir fotografías a la nube directamente desde el dispositivo de forma intuitiva.
* 💸 **Flujo de Pagos por Validación (Contexto Local):** Sistema de pago sin fricciones tecnológicas bancarias. El usuario realiza su pedido y valida su compra enviando una captura del *voucher* (comprobante Yape/Plin/Transferencia), adaptándose de manera perfecta y segura a la realidad financiera del comercio limeño.

---

## 🚀 Arquitectura y Stack Tecnológico (The Tech Stack)

Este ecosistema ha sido construido utilizando el estado del arte en desarrollo de software moderno. Se rige por los patrones **MVVM (Model-View-ViewModel)** y **UDF (Unidirectional Data Flow)**, garantizando un código escalable, testeable y de grado Senior.

### 📱 Frontend (Android & Web)

* **Kotlin Multiplatform (KMP):** El corazón del proyecto. Lógica de negocio, modelos de dominio y repositorios escritos una sola vez, ejecutados de forma nativa en múltiples plataformas.
* **Compose Multiplatform:** Interfaz de usuario 100% declarativa. Tarjetas adaptativas, animaciones y modales que brillan en Android y se renderizan a la perfección en la Web.

### ☁️ Infraestructura Cloud & Red

* **Firebase (GitLive SDK):** Backend serverless multiplataforma.
* **Cloud Firestore:** Base de datos NoSQL para el catálogo y los pedidos en tiempo real.
* **Firebase Storage:** Alojamiento seguro para imágenes de productos y vouchers de clientes.
* **Firebase Auth:** Gestión de identidades y sesiones de usuario.


* **Ktor Client:** Motor asíncrono y ultrarrápido para el manejo de peticiones de red bajo el capó.
* **Coil 3:** Carga, renderizado y caché avanzado de imágenes (`AsyncImage`) mediante `KtorNetworkFetcherFactory`.

### 🛠️ Herramientas y Librerías Adicionales

* **FileKit Compose:** Selector de archivos nativo multiplataforma para la captura de fotos desde la galería.
* **Corrutinas (Coroutines) y Flows:** Manejo avanzado de asincronía y reactividad en hilos paralelos.

---

## 📍 Alcance y Visión Local

Diseñado por y para la comunidad. Al restringir el servicio a **Lima Metropolitana**, *Remedioz Natura* asegura una logística de entrega óptima, elimina las comisiones pasarelas internacionales (Stripe/PayPal), reduce el fraude informático y establece un vínculo de confianza directo y personalizado con cada paciente.

---

## 🚧 Roadmap y Estado del Proyecto

**[ EN DESARROLLO ACTIVO ⚙️ ]**

La arquitectura base y el motor del e-commerce están completados. Nos encontramos en la Fase de Consolidación (Checkout y Búsqueda):

* [x] **Fase 1:** UI Declarativa, Navegación y Diseño Adaptativo.
* [x] **Fase 2:** Integración Cloud, Inventario Admin, Auth y Motor de Carrito (SSOT).
* [ ] **Fase 3:** Sistema de Filtros Reactivos (Búsqueda en tiempo real).
* [ ] **Fase 4:** Flujo de validación de Vouchers (Generación de Tickets y Consola de Pedidos).
* [ ] **Fase 5:** Pruebas finales y despliegue en producción.

*Creado con ❤️, disciplina y Kotlin.*
