const mongoose = require('../db');

const usuarioSchema = new mongoose.Schema({

    nombre: {
    type: String,
    required: true
    },

    email: {
    type: String,
    required: true,
    },

    password: {
    type: String,
    required: true},

    fechaNacimiento: {
    type: Date,
    required: true},

    sexo: {
    type: String,
    enum: ['M', 'F', 'X'],
    required: true
  },

}, 
{
    discriminatorKey: 'tipoUsuario',
    timestamps: true
})

module.exports = mongoose.model('Usuario', UsuarioSchema)