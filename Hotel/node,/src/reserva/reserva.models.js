const mongoose = require('../db');

const reservaSchema = new mongoose.Schema({
  usuarioId: { type: mongoose.Schema.Types.ObjectId, ref: 'Usuario' },
  habitacionId: { type: mongoose.Schema.Types.ObjectId, ref: 'Habitacion' },
  fechaEntrada: Date,
  fechaSalida: Date
});

module.exports = mongoose.model('Reserva', reservaSchema);
