const express = require('express');
const app = express();
const PORT = 3000;

// Middleware
app.use(express.json());

// Rutas de módulos
const usuarioRoutes = require('./usuario/usuario.routes');
const habitacionRoutes = require('./habitacion/habitacion.routes');
const reservaRoutes = require('./reserva/reserva.routes');

app.use('/usuarios', usuarioRoutes);
app.use('/habitaciones', habitacionRoutes);
app.use('/reservas', reservaRoutes);

// Iniciar servidor
app.listen(PORT, () => {
  console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
