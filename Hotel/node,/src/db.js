const mongoose = require('mongoose');

const URI = 'mongodb://localhost:27017/Hotel'; // tu base de datos
mongoose.connect(URI) // sin opciones obsoletas
  .then(() => console.log('Conectado a la base de datos'))
  .catch(err => console.log('Error de conexión:', err));

module.exports = mongoose;
