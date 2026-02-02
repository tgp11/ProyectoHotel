const express = require('express');
const router = express.Router();
const controller = require('./reseña.controller');

// Crear reseña
router.post('/', controller.crearResena);

// Obtener todas
router.get('/', controller.obtenerResenas);

// Obtener reseñas de una habitación
router.get('/habitacion/:id', controller.obtenerResenasPorHabitacion);

// Eliminar reseña
router.delete('/:id', controller.eliminarResena);

module.exports = router;