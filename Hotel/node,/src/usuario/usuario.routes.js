const express = require('express');
const router = express.Router();

const usuarioController = require('../usuario.controller');

router.get('/', usuarioController.verTodosUsuarios);

module.exports = router;