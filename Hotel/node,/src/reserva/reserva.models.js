const mongoose = require('../db');

const reservaSchema = new mongoose.Schema({
  clienteId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Cliente',
    required: true
  },
  habitacionId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Habitacion',
    required: true
  },
  fechaEntrada: {
    type: Date,
    required: true
  },
  fechaSalida: {
    type: Date,
    required: true
  },
  personas: {
    type: Number,
    required: true,
    min: 1,
    max: 10
  },
  precioTotal: {
    type: Number,
    required: true,
    min: 0
  },
  cancelacion: {
    type: Boolean,
    default: false
  }
}, {
  timestamps: true
});

//Validacion de fechas antes de guardar
reservaSchema.pre('save', function (next) {
  if (this.fechaSalida <= this.fechaEntrada) {
    return next(new Error('La fecha de salida debe ser posterior a la de entrada'));
  }
  next();
});

module.exports = mongoose.model('Reserva', reservaSchema, 'Reserva');
