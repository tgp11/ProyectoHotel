const express = require('express');
const cors = require('cors'); 
const app = express();
const PORT = 3000;

// Conexión a DB (usa tu archivo db.js)
require('./db'); 

// Middlewares - ¡IMPORTANTE!
app.use(cors()); 
app.use(express.json()); 

// Rutas
const reservaRoutes = require('./reserva/reserva.routes');
const clienteRoutes = require('./cliente/cliente.routes');
const empleadoRoutes = require('./empleado/empleado.routes');
const usuarioRoutes = require('./usuario/usuario.routes');

app.use('/reservas', reservaRoutes); // Esta es la que usará el ViewModel
app.use('/cliente', clienteRoutes);
app.use('/empleado', empleadoRoutes);
app.use('/usuario', usuarioRoutes);

app.listen(PORT, () => {
    console.log(`Servidor API corriendo en http://localhost:${PORT}`);
});