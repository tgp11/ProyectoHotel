const express = require('express');
const app = express();
const PORT = 3000;

// Middleware
app.use(express.json());

// Importar rutas correctamente desde la carpeta src
//const usuarioRoutes = require('./usuario/usuario.routes');
const habitacionRoutes = require('./habitacion/habitacion.routes.js');
//const reservaRoutes = require('./reserva/reserva.routes');
// Si agregas reseña
// const reseñaRoutes = require('./reseña/reseña.routes');

//app.use('/usuarios', usuarioRoutes);
app.use('/habitaciones', habitacionRoutes);
//app.use('/reservas', reservaRoutes);
// app.use('/reseñas', reseñaRoutes);

app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
