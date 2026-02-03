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
<<<<<<< HEAD
=======
const habitacionRoutes = require('./habitacion/habitacion.routes');
//const reservaRoutes = require('./reserva/reserva.routes');
// Si agregas reseña
//const reseñaRoutes = require('./reseña/reseña.routes');
>>>>>>> 67ffd4c7e8e65d3bf7a3455edec7943d7c329f1a

app.use('/reservas', reservaRoutes); // Esta es la que usará el ViewModel
app.use('/cliente', clienteRoutes);
<<<<<<< HEAD
app.use('/empleado', empleadoRoutes);
app.use('/usuario', usuarioRoutes);
=======
app.use('/habitaciones', habitacionRoutes);
//app.use('/reservas', reservaRoutes);
//app.use('/reseñas', reseñaRoutes);

>>>>>>> 67ffd4c7e8e65d3bf7a3455edec7943d7c329f1a

app.listen(PORT, () => {
    console.log(`Servidor API corriendo en http://localhost:${PORT}`);
});
