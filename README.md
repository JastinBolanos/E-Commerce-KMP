<div align="center">
  <img src="docs/logo.remedioznatura.png" alt="Remedioz Natura Logo" width="220" />
  <h1>E-COMMERCE KMP</h1>
  <h3>🌿 Remedioz Natura Edition</h3>
  <p><i>A production-grade Compose Multiplatform (iOS & Android) Showcase featuring Clean Architecture, UDF, and zero-config compilation.</i></p>
</div>

---

## 1. Visión del Proyecto y Naturaleza del Repositorio

**E-Commerce KMP (Remedioz Natura)** es una demostración técnica de alto nivel (*Frontend & Architecture Showcase*) diseñada para exhibir las capacidades de **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**. 

Tras un exitoso ciclo de vida comercial con un backend real, esta base de código ha sido refactorizada quirúrgicamente para aislar la capa de presentación y la lógica de dominio. **El resultado es una aplicación 100% offline y *Plug-and-Play***. Al carecer de dependencias de Backend as a Service (BaaS) o configuraciones de red, cualquier desarrollador o reclutador puede clonar el repositorio y compilarlo instantáneamente en simuladores de iOS o Android sin lidiar con llaves de API o autenticaciones.

El diseño de la UI refleja un e-commerce hiper-localizado, fusionando la calidez del comercio botánico tradicional con la fluidez de las aplicaciones nativas modernas.

---

## 2. Arquitectura de Software (El Stack y la Estructura)

El proyecto se rige bajo el paradigma *"Escribir una vez, ejecutar en cualquier lugar"*, cimentado en una estricta **Clean Architecture + Estructura Basada en Funcionalidades (Feature-Based)** para garantizar una separación de responsabilidades absoluta.

**El Stack Core:**
* **Framework:** Kotlin Multiplatform (KMP) enfocado en Mobile Nativo (iOS & Android).
* **UI:** Compose Multiplatform (UI Declarativa, reactiva y fluida).
* **Gestor de Estado:** StateFlow / Corrutinas (Unidirectional Data Flow).
* **Motor de Renderizado Multimedia:** Coil 3.

**La Estructura Clean (El Mapa del Proyecto):**
* `domain`: El núcleo puro e inmutable. Contiene modelos de datos (`Product`, `Order`) e interfaces de repositorios. Agnosticismo total: no sabe de Android, iOS, ni Compose.
* `data`: La infraestructura. Implementaciones mockeadas (`MockProductRepositoryImpl`, `MockOrderRepositoryImpl`) que inyectan datos estáticos para mantener viva la interfaz sin necesidad de servidores. Incluye resolución nativa mediante `expect/actual` en `data.platform`.
* `presentation`: La capa visual dividida quirúrgicamente por módulos de negocio (`home`, `admin`, `checkout`, `profile`), temas genéricos, componentes reutilizables y gestores de estado inyectables (`CartManager`).

---

## 3. El Flujo de Negocio Simulado (Core Business Flow)

Aunque los datos son generados localmente, la aplicación simula un ciclo de vida comercial completo y complejo en dos ecosistemas altamente optimizados:

### Perspectiva del Comprador (Cliente)
* **Exploración:** Navegación por un catálogo dinámico con categorización y filtrado en tiempo real.
* **Selección:** Gestión del carrito de compras con control de unidades dinámico y cálculos matemáticos exactos.
* **Checkout (Flujo de Pago Local):** Simulación de un pago validado por transferencia (Yape / Plin), reflejando la idiosincrasia financiera del mercado latinoamericano, culminando en la subida simulada de un comprobante (Voucher).
* **Línea de Tiempo (Tracking):** Panel personal con telemetría visual del estado de su paquete: *Pendiente -> Preparando -> En Camino -> Entregado*.

### Perspectiva del Administrador (Backoffice Móvil)
Un ERP de bolsillo diseñado para la gestión de la tienda, evidenciando el manejo de múltiples roles de usuario en Compose:
* **Pedidos (Torre de Control):** Auditoría visual para mutar estados de entrega y simular la aprobación de comprobantes de pago.
* **Editar Productos:** Interfaz para la gestión de inventario blindada; demostración de formularios dinámicos para creación y edición de catálogo.
* **Dashboard de Control:** Visualización de métricas e interacciones estructuradas para el usuario administrador.

---

## 4. Reglas de Oro del Código (Estándares)

Este repositorio actúa como un manifiesto de buenas prácticas de ingeniería de software para entornos móviles:

* **Aislamiento del Dominio:** La capa `domain` es un santuario. Está estrictamente prohibida la importación de estados de UI (`MutableState`) o dependencias gráficas de plataforma.
* **Principio DRY (Don't Repeat Yourself) en UI:** Componentes como `ProductCard`, la "Línea de Tiempo" y `TopNavBar` son entidades agnósticas y reutilizables en múltiples pantallas.
* **UDF (Unidirectional Data Flow):** La UI (`@Composable`) es completamente pasiva. Se limita a escuchar `StateFlows` provenientes de los ViewModels y emite eventos hacia arriba.
* **Compilación Resiliente:** Ausencia de strings "hardcodeados" fuera de sus componentes lógicos y stubbing nativo (como el uso de `BackHandler` adaptado para no romper el ecosistema de iOS).

---
*Construido con disciplina, código limpio y pasión por el desarrollo multiplataforma.*
