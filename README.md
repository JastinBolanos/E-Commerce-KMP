---

# 🌿 REMEDIOZ NATURA - MANIFIESTO TÉCNICO Y VISIÓN DE PRODUCTO (V3.0)

> ### 📢 [NOTA DEL DESARROLLADOR: EL PIVOTE HACIA FRONTEND SHOWCASE]
>
> **Actualización de Estado:** Tras meses de pulido intensivo en privado, la versión completa y conectada a bases de datos de este software fue adquirida y desplegada exitosamente en producción *(Comercial Exit)*.
> 
> Para proteger la propiedad intelectual comercial y el backend de producción, este repositorio público entra en una fase de **"Pivote Arquitectónico"**. El objetivo de esta base de código ha evolucionado: dejará de ser un MVP Full-Stack para convertirse en una **Vitrina de Frontend y Arquitectura Limpia (KMP UI/UX Showcase)**.
> 
> **Próximos pasos en el Roadmap de esta rama pública:**
> 
> * ✂️ **Desacoplamiento de BaaS:** Se eliminará la dependencia de Firebase y se purgarán las credenciales para permitir una compilación *plug-and-play* (cero configuraciones) para cualquier reclutador o desarrollador que clone el proyecto.
> * 🏗️ **Mock Data & Clean Architecture:** La capa de `domain` se mantendrá intacta, pero los repositorios de `data` inyectarán datos simulados estáticos, demostrando cómo la UI es completamente agnóstica al origen de los datos.
> * 🍏 **Expansión Nativa (iOS):** Se descartarán los targets web experimentales para enfocar la arquitectura estrictamente en Mobile Nativo, incorporando el ecosistema de iOS mediante Compose Multiplatform.
> 
> *(Una vez completada esta refactorización, este README será reescrito en su totalidad para reflejar la nueva naturaleza "Mocked" e hiper-optimizada del proyecto).*

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

## 4. 🚀 Reglas de Oro del Proyecto (Estándares)

Para sostener la integridad técnica y repeler la deuda tecnológica:

* **Aislamiento del Dominio:** La capa `domain` es un santuario. Prohibida la importación de estados de UI (`MutableState`) o librerías de plataforma.
* **Principio DRY (Don't Repeat Yourself) en UI:** Componentes como la "Línea de Tiempo" o "Tarjetas de Producto" son entidades agnósticas reutilizables.
* **UDF (Unidirectional Data Flow):** La UI (`@Composable`) es pasiva. Escucha `StateFlows` desde los ViewModels y emite eventos hacia arriba. Cero peticiones de red directas.
* **UI Resiliente (Fail-Safe):** Uso estricto de placeholders en `AsyncImage` y variables por defecto para evitar bloqueos visuales ante latencia de red.
* **Seguridad Cero-Trust:** El archivo `google-services.json` y claves nativas están estrictamente excluidos del control de versiones (`.gitignore`).

---

*Construido con disciplina, código limpio y visión de producto.*

---