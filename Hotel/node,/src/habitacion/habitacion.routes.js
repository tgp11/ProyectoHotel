const express = require('express');
const router = express.Router();
const controller = require('./habitacion.controller');
const upload = require("../Middleware/upload.middleware");

// Crear habitación: subir varias imágenes (campo: "imagenes")
router.post('/', upload.array('imagenes', 10), controller.crearHabitacion);

router.get('/', controller.obtenerHabitaciones);
router.get('/:id', controller.obtenerHabitacion);

// Actualizar habitación: subir varias imágenes (campo: "imagenes")
router.put('/:id', upload.array('imagenes', 10), controller.actualizarHabitacion);

router.delete('/:id', controller.eliminarHabitacion);

module.exports = router;