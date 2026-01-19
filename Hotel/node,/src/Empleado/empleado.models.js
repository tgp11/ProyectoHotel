const Usuario = require('../usuario/usuario.models')
const mongoose = require('mongoose')

const EmpleadoSchema = new mongoose.Schema({
  administrador: {
    type: Boolean,
    default: false
  },
  ciudad: {
    type: String,
    required: true
  },
  tipoEmpleado: {
    type: String,
    required: true
  }
})

module.exports = Usuario.discriminator('Empleado', EmpleadoSchema)