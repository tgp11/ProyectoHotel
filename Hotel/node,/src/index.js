const express = require('express');
const app = express();
const PORT = 3000;

// Middleware
app.use(express.json());

// Importar rutas correctamente desde la carpeta src
const usuarioRoutes = require('./usuario/usuario.routes');
//const habitacionRoutes = require('./habitacion/habitacion.routes');
//const reservaRoutes = require('./reserva/reserva.routes');

app.use('/usuarios', usuarioRoutes);
//app.use('/habitaciones', habitacionRoutes);
//app.use('/reservas', reservaRoutes);

app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
