const Usuario = require('./usuario.model')

exports.verTodosUsuarios = async (req, res) => {
  try {

    const usuarios = await Usuario.find();
    res.status(200).json(usuarios);

    } catch (error) {
    res.status(500).json({ error: error.message });
  } 
};