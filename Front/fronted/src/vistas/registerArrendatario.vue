<template>
  <div class="home-container">
    <!-- Barra de menú -->
    <div class="menu-bar">
      <div class="logo">Registro de Arrendatarios</div>
      <button @click="goTologin" class="menu-button">Iniciar sesión</button>
    </div>

    <!-- Contenedor principal -->
    <div class="content-container">
      <div class="content-box">
        <div class="header-content">
          <h1>Registrar Nuevo Arrendatario</h1>
          <p>Por favor, complete la información a continuación:</p>
        </div>
        <!-- Formulario desplazable -->
        <div class="form-scrollable">
          <form class="form-container" @submit.prevent="crearPublicacion">
            <div>
              <label>Rut:</label>
              <input
                type="text"
                v-model="newPublication.rutUsuario"
                class="input-field"
                placeholder="Ingrese su Rut sin puntos con guion"
                @blur="validarRut"
                required
              />
              <span v-if="errores.rut" class="error-message">{{ errores.rut }}</span>
            </div>
            <div>
              <label>Nombre:</label>
              <input
                type="text"
                v-model="newPublication.nombreUsuario"
                class="input-field"
                placeholder="Ingrese su nombre"
                required
              />
            </div>
            <div>
              <label>Correo Electrónico:</label>
              <input
                type="email"
                v-model="newPublication.correoUsuario"
                class="input-field"
                placeholder="Ingrese su correo electrónico"
                @blur="validarCorreo"
                required
              />
              <span v-if="errores.correo" class="error-message">{{ errores.correo }}</span>
            </div>
            <div>
              <label>Contraseña:</label>
              <input
                type="password"
                v-model="newPublication.contraseniaUsuario"
                class="input-field"
                placeholder="Ingrese su contraseña"
                @blur="validarContrasena"
                required
              />
              <span v-if="errores.contrasena" class="error-message">{{ errores.contrasena }}</span>
            </div>
            <div>
              <label>Teléfono:</label>
              <input
                type="text"
                v-model="newPublication.telefonoUsuario"
                class="input-field"
                placeholder="Ingrese su número telefónico"
                @blur="validarTelefono"
                required
              />
              <span v-if="errores.telefono" class="error-message">{{ errores.telefono }}</span>
            </div>
            <div>
              <label>Fecha de Nacimiento:</label>
              <input
                type="date"
                v-model="newPublication.fechaNacimiento"
                class="input-field"
                required
              />
            </div>
            <div>
              <label>Tipo de Licencia:</label> <br>
              <label><input type="checkbox" name="licencia_b" v-model="newPublication.tipoLicencia" value="B"> B  </label>
              <label><input type="checkbox" name="licencia_c" v-model="newPublication.tipoLicencia" value="C"> C  </label>
            </div>
            <div>
              <label>Documentos:</label>
              <div class="date-container">
                <input
                  type="text"
                  v-model="nuevoDocumento"
                  class="input-field"
                  placeholder="Añadir documento"
                />
                <button
                  type="button"
                  class="action-button"
                  @click="agregarDocumento"
                >
                  Agregar Documento
                </button>
              </div>
            </div>
            <ul>
              <li v-for="(doc, index) in newPublication.documentos" :key="index">
                {{ doc }}
                <button
                  type="button"
                  class="action-button"
                  @click="eliminarDocumento(index)"
                >
                  Eliminar
                </button>
              </li>
            </ul>
            <button type="submit" class="action-button">Registrar</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

function direccionamientologin() {
  window.location.href = '/login';
}

export default {
  data() {
    return {
      newPublication: {
        tipo_usuario: "Arrendatario",
        nombreUsuario: "",
        correoUsuario: "",
        contraseniaUsuario: "",
        telefonoUsuario: "",
        fechaNacimiento: "",
        tipoLicencia: [],
        documentos: [],
      },
      nuevoDocumento: "", // Campo temporal para añadir documentos
      errores: {
        rut: "",
        contrasena: "",
        telefono: "",
        correo: "",
      },
    };
  },
  methods: {
    async crearPublicacion() {
      if (this.validarFormulario()) {
        try {
          const response = await axios.post(
            import.meta.env.VITE_BASE_URL + "arrendatarios/register",
            this.newPublication
          );
          if (response.data === null) {
            alert("Este correo electrónico ya está registrado.");
          } else {
            alert("Registro exitoso");
            this.goTologin();
          }
        } catch (error) {
          console.error("Error al crear la publicación:", error);
        }
      }
    },
    validarRut() {
      const rutRegex = /^[0-9]+-[0-9kK]{1}$/;
      if (!rutRegex.test(this.newPublication.rutUsuario)) {
        this.errores.rut = "El RUT ingresado no es válido.";
      } else {
        this.errores.rut = "";
      }
    },
    validarContrasena() {
      const contrasenaRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,}$/;
      if (!contrasenaRegex.test(this.newPublication.contraseniaUsuario)) {
        this.errores.contrasena =
          "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una minúscula y un número.";
      } else {
        this.errores.contrasena = "";
      }
    },
    validarTelefono() {
      const telefonoRegex = /^\+?[0-9]{9,12}$/;
      if (!telefonoRegex.test(this.newPublication.telefonoUsuario)) {
        this.errores.telefono = "El número telefónico ingresado no es válido.";
      } else {
        this.errores.telefono = "";
      }
    },
    validarCorreo() {
    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!correoRegex.test(this.newPublication.correoUsuario)) {
      this.errores.correo = "El correo ingresado no es válido.";
      } else {
        this.errores.correo = "";
      }
  },

    validarFormulario() {
      this.validarRut();
      this.validarContrasena();
      this.validarTelefono();
      this.validarCorreo();
      return !this.errores.rut && !this.errores.contrasena && !this.errores.telefono && !this.errores.correo;
    },
    agregarDocumento() {
      if (this.nuevoDocumento.trim()) {
        this.newPublication.documentos.push(this.nuevoDocumento.trim());
        this.nuevoDocumento = ""; // Limpiar el campo
      }
    },
    eliminarDocumento(index) {
      this.newPublication.documentos.splice(index, 1);
    },
    async goTologin() {
      direccionamientologin();
    },
  },
};
</script>

<style scoped>
/* Ajustes generales */
body,
html {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
}

.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to right, #4caf50, #2f80ed);
  color: #fff;
  overflow-x: hidden;
}

.menu-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 15px 20px;
  background-color: rgba(0, 0, 0, 0.8);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.content-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 100px;
  margin-top: 20px; /* Espacio para el menú fijo */
}

.content-box {
  background-color: rgba(255, 255, 255, 0.9);
  color: #333;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
  max-width: 600px;
  width: 100%;
  display: flex;
  flex-direction: column;
}

/* Formulario desplazable */
.form-scrollable {
  max-height: 400px;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 10px; /* Espacio para el scroll */
}

.header-content h1 {
  font-size: 2rem;
  margin-bottom: 10px;
  color: #2f80ed;
}

.header-content p {
  font-size: 1.2rem;
  margin-bottom: 20px;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.input-field {
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 100%;
}

.date-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.action-button {
  background-color: #2f80ed;
  color: #fff;
  padding: 12px 20px;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s, background-color 0.3s;
}

.action-button:hover {
  background-color: #1c6dd0;
  transform: scale(1.05);
}
.error-message {
  color: red;
  font-size: 0.9em;
}
</style>
