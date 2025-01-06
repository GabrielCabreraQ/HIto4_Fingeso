<template>
    <div class="container">
      <!-- Barra Superior -->
      <header class="header">
        <h1>Inventario</h1>
      </header>
  
      <!-- Menú Lateral -->
      <aside class="sidebar">
        <div class="user-info">
          <div class="user-name">EasyWheels</div>
        </div>
        <nav>
          <ul>
            <li
              v-for="(item, index) in menuItems"
              :key="index"
              :class="{ active: selectedSection === item }"
              @click="changeSection(item)"
            >
              {{ item }}
            </li>
          </ul>
        </nav>
      </aside>
  
      <!-- Sección Principal Dinámica -->
      <main class="main-content">
        <!-- Crear Informes -->
        <div v-if="selectedSection === 'Crear Informes'">
          <h2>Gestión de Informes</h2>
          <div class="scrollable-table-container">
            <div v-if="!showform">
              <!-- Tabla de publicaciones -->
              <table class="vehiculo-table">
                <thead>
                  <tr>
                    <th>Vehículo ID</th>
                    <th>Modelo</th>
                    <th>Crear informe</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(vehicle, index) in vehiculos" :key="index">
                    <td>{{ vehicle.idVehiculo }}</td>
                    <td>{{ vehicle.modelo }}</td>
                    <td>
                      <button class="crear-button" @click="formInforme(vehicle)">📄</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
  
            <div v-else>
              
              <form @submit.prevent="crearInforme">
                <div>
                  <label for="idVehiculo">ID Vehículo:</label>
                  <input id="idVehiculo" v-model="currentVehiculo.idVehiculo" readonly />
                </div>
                <div>
                  <label for="modelo">Modelo:</label>
                  <input id="modelo" v-model="currentVehiculo.modelo" readonly />
                </div>
                <div>
                  <label for="observacion">Observación:</label>
                  <div class="scrollable-select-container">
                    <select id="observacion" v-model="informe.observaciones" required>
                      <option value="">Seleccione una opción</option>
                      <option v-for="(problema, index) in problemas" :key="index" :value="problema.value">
                        {{ problema.label }}
                      </option>
                    </select>
                  </div>
                </div>
                <div>
                  <button type="submit" class="menu-button">Guardar</button>
                  <button type="button" class="menu-button" @click="cancelform">Cancelar</button>
                </div>
              </form>
  
            </div>
            
          </div>
        </div>
        
        <!-- Buscar Informes -->
        <div v-if="selectedSection === 'Buscar Informes'">
          <h2>Buscar Informes
            <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="Buscar por ID..." 
            class="search-bar"
          />
          <button class="search-button" @click="filtrarInformes">🔍</button>
          </h2>
          <div class="scrollable-table-container">
            <div v-if="!showform2">
              <!-- Tabla de publicaciones -->
              <table class="informe-table">
                <thead>
                  <tr>
                    <th> </th>
                    <th>Fecha</th>
                    <th>Visualizar</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(inform, index) in informes" :key="index">
                    <td>{{ inform.id_vehiculo }}</td>
                    <td>{{ inform.fechaGeneracion }}</td>
                    <td>
                      <button class="crear-button" @click="forminform(inform)">📋</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
  
            <div v-else>
              <
              <form>
                <div>
                  <label for="idinforme">ID Informe:</label>
                  <input id="idinforme" v-model="currentinforme.id" readonly />
                </div>
                <div>
                  <label for="fechaGen">Fecha de generación:</label>
                  <input id="fechaGen" v-model="currentinforme.fechaGeneracion" readonly />
                </div>
                <label for="observacion:">Observaciones:</label>
                <textarea id="observacion:" v-model="currentinforme.observaciones" readonly />
                <div>
                  <button type="button" class="menu-button" @click="cancelform">Volver</button>
                </div>
              </form>
  
            </div>
            
          </div>
        </div>
  
        <!-- Cerrar Sesión -->
        <div v-if="selectedSection === 'Cerrar Sesión'">
          <div class="actions">
            <button @click="cerrarSesion" class="menu-button">Cerrar Sesión</button>
          </div>
        </div>
      </main>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  function direccionamientoMain() {
    window.location.href = '/home';
  }
  
  export default {
    name: "Administrador",
    data() {
      return {
        menuItems: [
          "Crear Informes",
          "Buscar Informes",
          "Cerrar Sesión",
        ],
        currentVehiculo: {
          id_vehiculo: '',
          marca: '',
          modelo: '',
          anio: null,
          tipoDeTransmision: '',
          categoria: '',
          tipo_cuerpo: '',
          combustibleAC: '',
          disponible_uso: false
        },
  
        currentinforme: {
          id: '',
          id_vehiculo: '',
          observaciones: '',
          fechaGeneracion: "null",
        },
        
        informe: {
          observaciones: '',
        },
  
        selectedSection: "Crear Informes",
        vehiculos: [], // Lista de vehículos
        informes: [], // Lista de informes
        showform: false,
        showform2: false,
  
        problemas: [
          { label: 'Problemas con el sistema de encendido', value: 'Presenta falla Bujías desgastadas, cables de encendido defectuosos o una bobina de encendido dañada pueden dificultar el arranque o la marcha del vehículo.' },
          { label: 'Falta de combustible o baja presión de combustible', value: 'Presenta falla Un tanque de combustible vacío o una bomba de combustible defectuosa pueden causar que el motor no reciba suficiente combustible para funcionar.' },
          { label: 'Fugas en el sistema de refrigeración', value: 'Presenta falla Un radiador o mangueras con fugas pueden causar que el motor se sobrecaliente, provocando fallas o daños graves al motor.' },
          { label: 'Batería descargada o defectuosa', value: 'Presenta falla Una batería en mal estado o descargada puede dificultar el arranque del vehículo o causar problemas eléctricos.' },
          { label: 'Problemas con el alternador', value: 'Presenta falla Si el alternador no carga correctamente la batería, el vehículo puede quedarse sin energía eléctrica y dejar de funcionar.' },
          { label: 'Frenos defectuosos', value: 'Presenta falla Desgaste de las pastillas de freno, discos dañados o problemas con el sistema hidráulico pueden afectar la capacidad de frenado, lo que puede ser peligroso.' },
          { label: 'Problemas con la transmisión', value: 'Presenta falla Fallas en la caja de cambios, embrague o niveles bajos de líquido de transmisión pueden dificultar el cambio de marchas o incluso hacer que el vehículo quede atascado en una marcha.' },
          { label: 'Neumáticos desgastados o desinflados', value: 'Presenta falla Neumáticos con poca presión o demasiado desgastados pueden afectar la estabilidad del vehículo y aumentar el riesgo de accidentes.' },
          { label: 'Sensores defectuosos', value: 'Presenta falla Sensores del motor, como los sensores de oxígeno o el sensor de flujo de aire, pueden fallar y afectar el rendimiento del motor o la eficiencia del combustible.' },
          { label: 'Problemas en la suspensión', value: 'Presenta falla Amortiguadores o resortes dañados pueden causar una conducción inestable, ruidos extraños o una mala respuesta de la dirección.' },
          { label: 'No presenta fallas', value: 'Vehiculo en buen estado' }
        ],
      };
    },
    methods: {
      changeSection(section) {
        this.selectedSection = section;
        console.log(this.selectedSection); // Para depuración
        if (section === "Crear Informes") {
          this.fetchVehiculos();
        }
        if (section === "Buscar Informes") {
          this.fetchInformes();  // Asegúrate de llamar a fetchInformes también aquí
        }
      },
      cerrarSesion() {
        localStorage.removeItem("login");
        direccionamientoMain();
      },
      cancelform() {
        this.showform = false;
        this.showform2 = false;
      },
      formInforme(vehiculo) {
        this.showform = true;
        this.currentVehiculo = { ...vehiculo }; // Copiar los datos del vehículo seleccionado
      },
      forminform(informe) {
        this.showform2 = true;
        this.currentinforme = { ...informe }; // Copiar los datos del vehículo seleccionado
      },
      async crearInforme() {
        // Verificar si todos los campos están completos
        if (!this.informe.observaciones) {
          alert('Por favor, completa todos los campos.');
          return;
        }
  
        // Verificar si el idVehiculo está presente
        if (!this.currentVehiculo.idVehiculo) {
          alert('ID de vehículo no válido.');
          return;
        }
  
        // Enviar datos al backend
        axios.post(
          `${import.meta.env.VITE_BASE_URL}vehiculos/${this.currentVehiculo.idVehiculo}/crearinforme`,
          this.informe
        )
        .then(response => {
          console.log('Informe guardado:', response.data);
        })
        .catch(error => {
          if (error.response) {
            console.error('Error en la respuesta del servidor:', error.response.data);
            console.error('Código de estado:', error.response.status);
            alert(`Error del servidor: ${error.response.data.message || 'Consulta fallida'}`);
          } else if (error.request) {
            console.error('No se recibió respuesta del servidor:', error.request);
            alert('No se pudo conectar con el servidor. Verifica tu conexión.');
          } else {
            console.error('Error al configurar la solicitud:', error.message);
            alert('Ocurrió un error inesperado. Intenta nuevamente.');
          }
        });
        this.showform = false;
      },
  
  
      async fetchVehiculos() {
        try {
          const response = await axios.get(`${import.meta.env.VITE_BASE_URL}vehiculos/lista/no-disponible-uso`);
          console.log("Vehículos obtenidos:", response.data);
          this.vehiculos = response.data;
        } catch (error) {
          console.error("Error al obtener los vehículos:", error);
          alert("No se pudo obtener la lista de vehículos. Verifica tu conexión.");
        }
      },
  
      async fetchInformes() {
        try {
          const response = await axios.get(`${import.meta.env.VITE_BASE_URL}informes`);
          console.log("Informes obtenidos:", response.data);
          this.informes = response.data; 
        } catch (error) {
          console.error("Error al obtener los informes:", error);
          alert("No se pudo obtener la lista de informes. Verifica tu conexión.");
        }
      },

      async filtrarInformes() {
          try {
              const response = await axios.get(
                  `${import.meta.env.VITE_BASE_URL}vehiculos/${this.searchQuery}/informe`,
                  {
                      params: {
                          permiso: 'administrador'
                      }
                  }
              );
              console.log("Informes filtrados obtenidos:", response.data);
              this.informes = response.data;
          } catch (error) {
              console.error("Error al obtener los informes filtrados:", error);
              alert("No se pudo obtener la lista de informes filtrados. Verifica tu conexión.");
          }
      },
    },
    created() {
      // Cargar vehículos al iniciar
      this.fetchVehiculos();
      // Cargar informes al iniciar
      this.fetchInformes();
    },
  };
  </script>
  
  
  <style scoped>
  /* Contenedor Principal */
  .container {
    display: grid;
    grid-template-columns: 250px 1fr;
    grid-template-rows: 60px 1fr;
    height: 100vh;
  }
  
  /* Barra Superior */
  .header {
    grid-column: 1 / 3;
    background-color: #dcdcdc;
    color: #333;
    display: flex;
    align-items: center;
    padding: 0 20px;
    font-size: 1.5rem;
    font-weight: bold;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  /* Menú Lateral */
  .sidebar {
    background-color: #f5f5f5;
    color: #333;
    padding: 20px 10px;
    box-shadow: 2px 0 4px rgba(0, 0, 0, 0.1);
  }
  
  .user-info {
    margin-bottom: 20px;
    font-weight: bold;
    text-align: center;
  }
  
  .sidebar nav ul {
    list-style: none;
    padding: 0;
  }
  
  .sidebar nav ul li {
    padding: 10px 15px;
    cursor: pointer;
    border-radius: 5px;
    margin-bottom: 5px;
    transition: background 0.3s;
  }
  
  .sidebar nav ul li:hover,
  .sidebar nav ul li.active {
    background-color: #dcdcdc;
  }
  
  /* Sección Principal */
  .main-content {
    background-color: #fff;
    padding: 20px;
  }
  
  h2 {
    color: #333;
    font-size: 1.5rem;
    margin-bottom: 10px;
  }
  
  /* Botones */
  .menu-button {
    background-color: #4CAF50;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .menu-button:hover {
    background-color: #45a049;
  }
  
  /* Diseño responsive */
  @media (max-width: 768px) {
    .container {
      grid-template-columns: 1fr;
      grid-template-rows: 60px 1fr;
    }
  
    .sidebar {
      display: none;
    }
  }
  
  /* Tabla */
  .vehiculo-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .scrollable-table-container {
    max-height: 500px; /* Ajusta el tamaño máximo según sea necesario */
    overflow-y: auto;  /* Permite desplazarse verticalmente */
    margin-top: 10px;
  }
  
  .vehiculo-table th,
  .vehiculo-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #000000;
  }
  
  .vehiculo-table th {
    background-color: #797979;
    font-weight: bold;
  }
  
  .vehiculo-table tr:nth-child(even) {
    background-color: #a5a5a5;
  }
  
  .vehiculo-table tr {
    background-color: #cacaca; /* Cambia este color al que desees */
  }
  
  /* Contenedor del formulario */
  form {
    background-color: #f9f9f9;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 20px;
    width: 100%;
    max-width: 500px;
    margin: 0 auto;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  /* Estilo de los labels */
  form label {
    display: block;
    font-weight: bold;
    margin-bottom: 8px;
    color: #333;
  }
  
  /* Estilo de los inputs y select */
  form input,
  form select {
    width: calc(100% - 20px);
    padding: 8px 10px;
    margin-bottom: 15px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
    outline: none;
    transition: border-color 0.3s;
  }
  
  /* Efecto hover/focus en los inputs */
  form input:focus,
  form select:focus {
    border-color: #4CAF50;
  }
  
  /* Botones del formulario */
  form button {
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    font-weight: bold;
    transition: background-color 0.3s, color 0.3s;
  }
  
  /* Botón guardar */
  form button[type="submit"] {
    background-color: #4CAF50;
    color: white;
    margin-right: 10px;
  }
  
  form button[type="submit"]:hover {
    background-color: #45a049;
  }
  
  /* Botón cancelar */
  form button[type="button"] {
    background-color: #f44336;
    color: white;
  }
  
  form button[type="button"]:hover {
    background-color: #e53935;
  }
  
  /* Diseño responsive */
  @media (max-width: 768px) {
    form {
      width: 90%;
    }
  
    form input,
    form select {
      width: calc(100% - 16px);
    }
  }
  .scrollable-select-container {
    max-height: 150px;  /* Ajusta según sea necesario */
    overflow-y: auto;   /* Habilita el desplazamiento vertical */
    margin-top: 10px;
  }
  
  select {
    width: 100%;
    padding: 8px;
    font-size: 14px;
    border-radius: 4px;
    border: 1px solid #ccc;
    box-sizing: border-box;
  }
  
  .informe-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .informe-table th,
  .informe-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #000000;
  }
  
  .informe-table th {
    background-color: #797979;
    font-weight: bold;
  }
  
  .informe-table tr:nth-child(even) {
    background-color: #a5a5a5;
  }
  
  .informe-table tr {
    background-color: #cacaca; /* Cambia este color al que desees */
  }
  
  textarea {
    width: 100%;  /* Hace que el área de texto ocupe todo el ancho disponible */
    height: 150px;  /* Ajusta la altura del área de texto */
  }
  </style>