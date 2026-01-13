const mongoose = require('mongoose');

const URI = 'mongodb://localhost:27017/hotelDB'; // Cambia según tu configuración
mongoose.connect(URI, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log('Conectado a la base de datos'))
    .catch(err => console.log('Error de conexión:', err));

module.exports = mongoose;
