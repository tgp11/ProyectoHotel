const Usuario = require('./usuario.model')
const mongoose = require('mongoose')

const ClienteSchema = new mongoose.Schema({
  vip: {
    type: Boolean,
    default: false
  }
})

module.exports = Usuario.discriminator('Cliente', ClienteSchema)