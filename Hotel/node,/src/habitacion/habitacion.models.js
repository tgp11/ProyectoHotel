// Modelo Mongoose: Habitacion
// Definición de los campos que representan una habitación de hotel y sus restricciones.
// - `numero`: identificador numérico de la habitación (único y >= 1)
// - `tipo`: tipo de habitación, restringido por enum
// - `precionoche`: precio por noche (número no negativo)
// - `rate`: valoración promedio (0..5)
// - `max_ocupantes`: número máximo de personas
// - `servicios`: lista de amenities (ej.: ['wifi', 'aire acondicionado'])

const mongoose = require('../db');

const habitacionSchema = new mongoose.Schema({

  // Número de habitación (entero positivo y único)
  numero: {
    type: Number,
    required: true,
    unique: true,
    min: 1
  },

  // Tipo de habitación: valores permitidos
  tipo: {
    type: String,
    required: true,
    enum: ['individual', 'doble', 'triple', 'suite']
  },

  // Descripción breve (opcional)
  descripcion: {
    type: String,
    default: ''
  },

  // URL o ruta de la imagen principal (opcional)
  imagen: {
    type: String,
    default: ''
  },

  // Precio por noche (obligatorio, >= 0)
  precionoche: {
    type: Number,
    required: true,
    min: 0
  },

  // Valoración promedio (0-5). Se puede actualizar a partir de reseñas.
  rate: {
    type: Number,
    default: 0,
    min: 0,
    max: 5
  },

  // Máximo de ocupantes permitidos
  max_ocupantes: {
    type: Number,
    required: true,
    min: 1
  },

  // Indica si la habitación está disponible para reservar
  disponible: {
    type: Boolean,
    default: true
  },

  // Indica si la habitación está en oferta/promoción
  oferta: {
    type: Boolean,
    default: false
  },

  // Lista de servicios/amenities (strings)
  servicios: {
    type: [String],
    default: []
  }

}, {

  // Agrega campos `createdAt` y `updatedAt`
  timestamps: true

});

// Exporta el modelo para usarlo en controladores y otras partes de la app
module.exports = mongoose.model('Habitacion', habitacionSchema, 'Habitacion');
