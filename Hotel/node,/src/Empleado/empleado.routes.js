const express = require('express');
const router = express.Router();

const empleadoController = require('../empleado.controller');

router.post('/', empleadoController.crearEmpleado);

router.get('/', empleadoController.obtenerEmpleados);

router.get('/:id', empleadoController.obtenerEmpleadoPorId);

router.put('/:id', empleadoController.actualizarEmpleado);

router.delete('/:id', empleadoController.eliminarEmpleado);
module.exports = router;
