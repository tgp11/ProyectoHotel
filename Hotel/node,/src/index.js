const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());

const usuarioRoutes = require('./usuario/usuario.routes');
const habitacionRoutes = require('./habitacion/habitacion.routes');
const reservaRoutes = require('./reserva/reserva.routes');
const clienteRoutes = require('./cliente/cliente.routes');
const empleadoRoutes = require('./Empleado/empleado.routes');

app.use('/usuarios', usuarioRoutes);
app.use('/habitaciones', habitacionRoutes);
app.use('/reservas', reservaRoutes);
app.use('/clientes', clienteRoutes);
app.use('/empleados', empleadoRoutes);

app.listen(PORT, () => {
  console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
