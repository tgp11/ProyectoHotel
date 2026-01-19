const express = require('express');
const router = express.Router();
const controller = require('./habitacion.controller');

// Crear habitación
router.post('/', controller.crearHabitacion);

// Obtener todas las habitaciones
router.get('/', controller.obtenerHabitaciones);

// Obtener una habitación por id
router.get('/:id', controller.obtenerHabitacion);

// Actualizar habitación
router.put('/:id', controller.actualizarHabitacion);

// Eliminar habitación
router.delete('/:id', controller.eliminarHabitacion);

module.exports = router;