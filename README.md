<div align="center">
  <img src="docs/logo.remedioznatura.png" alt="Remedioz Natura Logo" width="150" />
  <h1>E-Commerce KMP | Enterprise-Grade Retail Architecture</h1>
</div>

> **Arquitectura E-Commerce Multiplataforma de Alto Rendimiento, UI Declarativa y Disponibilidad Offline.**

**E-Commerce KMP (Edición Remedioz Natura)** no es solo una tienda virtual genérica. Es una demostración arquitectónica de nivel empresarial (*Android & iOS*) diseñada con principios de ingeniería estrictos para garantizar que **la experiencia de compra sea fluida, los estados de la UI sean inmutables y la lógica de negocio esté completamente aislada**, permitiendo una compilación *Plug-and-Play* inmediata gracias a su entorno de datos simulados.

---

## 1. Visión del Proyecto y Naturaleza del Repositorio

Tras un exitoso ciclo de vida comercial con un backend real, esta base de código ha sido refactorizada quirúrgicamente para convertirse en un **Frontend & Architecture Showcase** de élite. 

Al carecer de dependencias de Backend as a Service (BaaS) o configuraciones de red, cualquier desarrollador o reclutador puede clonar el repositorio y compilarlo instantáneamente sin lidiar con llaves de API o autenticaciones. El diseño refleja un e-commerce hiper-localizado, fusionando la calidez del comercio botánico tradicional con la fluidez innegociable de las aplicaciones nativas modernas.

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

### Experiencia de Usuario (Perspectiva del Cliente)
* **Exploración:** Navegación por un catálogo dinámico con categorización y filtrado en tiempo real.
* **Selección:** Gestión del carrito de compras con control de unidades dinámico y cálculos matemáticos exactos.
* **Checkout Local:** Simulación de un pago validado por transferencia (Yape / Plin), reflejando la idiosincrasia financiera del mercado latinoamericano, culminando en la subida simulada de un comprobante (Voucher).
* **Línea de Tiempo (Tracking):** Panel personal con telemetría visual del estado de su paquete: *Pendiente -> Preparando -> En Camino -> Entregado*.

### Backoffice Móvil (Perspectiva del Administrador)
Un ERP de bolsillo diseñado para la gestión de la tienda, evidenciando el manejo de múltiples roles de usuario en Compose:
* **Torre de Control (Pedidos):** Auditoría visual para mutar estados de entrega y simular la aprobación de comprobantes de pago.
* **Gestor de Inventario:** Interfaz blindada con demostración de formularios dinámicos para creación y edición del catálogo de productos.

---

## 4. Reglas de Oro del Código (Estándares Implementados)

Este repositorio actúa como un manifiesto de buenas prácticas de ingeniería de software para entornos móviles:

* **Aislamiento del Dominio:** La capa `domain` es un santuario. Está estrictamente prohibida la importación de estados de UI (`MutableState`) o dependencias gráficas de plataforma.
* **Principio DRY (Don't Repeat Yourself) en UI:** Componentes como `ProductCard`, la "Línea de Tiempo" y `TopNavBar` son entidades agnósticas y reutilizables en múltiples pantallas.
* **Reactividad Unidireccional (UDF):** La UI (`@Composable`) es completamente pasiva. Se limita a escuchar `StateFlows` provenientes de los ViewModels y emite eventos hacia arriba.
* **Compilación Resiliente:** Ausencia de strings "hardcodeados" fuera de sus componentes lógicos y stubbing nativo (como el uso de `BackHandler` adaptado para no romper el ecosistema de iOS).

---
*Construido con disciplina, código limpio y pasión por el desarrollo multiplataforma.*
