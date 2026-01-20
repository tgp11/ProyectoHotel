const mongoose = require('../db');
const bcrypt = require('bcrypt')

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
usuarioSchema.pre('save', async function (next) { // Este metodo ocurre antes de guardar un usuario por el pre-save hook
  if (!this.isModified('password')) return next();//Esto evita que la contraseña se vuelva a hashear si no ha sido modificada

  try {
    const salt = bcrypt.genSalt(10);//salt es un valor aleatorio que se añade a la contraseña antes de hashearla para mayor seguridad
    this.password = await bcrypt.hash(this.password, salt);
    next();
  } catch (error) {
    next(error);
  }

});

usuarioSchema.methods.comparePassword = async function (plain) {
  const res = await bcrypt.compare(plain, this.password);
  return res;
};

const Usuario = mongoose.model('Usuario', usuarioSchema)
module.exports = Usuario;