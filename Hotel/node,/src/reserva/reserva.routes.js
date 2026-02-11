const express = require('express');
const router = express.Router();
const reservaController = require('./reserva.controller');

// Crear reserva
router.post('/', reservaController.crearReserva);

// Obtener todas
router.get('/', reservaController.obtenerReservas);

// Obtener por ID
router.get('/:id', reservaController.obtenerReservaPorId);

// Cancelar reserva
router.put('/:id/cancelar', reservaController.cancelarReserva);

// Eliminar reserva (solo si está cancelada)
router.delete('/:id', reservaController.eliminarReserva);


module.exports = router;
