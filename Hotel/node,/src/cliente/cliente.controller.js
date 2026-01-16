const Cliente = require('./cliente.models');
const bcrypt = require('bcrypt');

exports.crearCliente = async (req, res) => {
  try {
    const { nombre, email, password, fechaNacimiento, sexo, vip } = req.body;  
    if (!nombre || !email || !password || !fechaNacimiento || !sexo || vip === undefined) {
        return res.status(400).json({ msg: 'Faltan datos obligatorios' });  
    }

    if (!esEmailValido(email)) {
      return res.status(400).json({ msg: 'Email inválido' });
    }

    if (!esPasswordValida(password)) {
      return res.status(400).json({ msg: 'La contraseña debe tener al menos 6 caracteres, una mayúscula, una minúscula, un número y un carácter especial' });
    }

    if(!validarFechaNacimiento(fechaNacimiento)) {
      return res.status(400).json({ msg: 'Fecha de nacimiento inválida. Formato correcto DD/MM/YYYY' });
    }

    if (!['M', 'F', 'X'].includes(sexo)) {
      return res.status(400).json({ msg: 'Sexo inválido. Debe ser M, F o X' });
    }



    const nuevoCliente = new Cliente({ nombre, email, password, fechaNacimiento, sexo,  vip });
    const clienteGuardado = await nuevoCliente.save();
    res.status(201).json(clienteGuardado);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerClientes = async (req, res) => {
  try {
    const clientes = await Cliente.find();
    res.status(200).json(clientes);
  } catch (error) {
    res.status(500).json({ error: error.message });
  } 
};

exports.obtenerClientePorId = async (req, res) => {
  try {
    const cliente = await Cliente.findById(req.params.id); 
    if (!cliente) {
      return res.status(404).json({ msg: 'Cliente no encontrado' });
    }
    res.status(200).json(cliente);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.actualizarCliente = async (req, res) => {
  try {
    const { nombre, email, password, fechaNacimiento, sexo, vip } = req.body;  
    if (!nombre || !email || !password || !fechaNacimiento || !sexo || vip === undefined) {
        return res.status(400).json({ msg: 'Faltan datos obligatorios' });  
    }

    if (!esEmailValido(email)) {
      return res.status(400).json({ msg: 'Email inválido' });
    }

    if (!esPasswordValida(password)) {
      return res.status(400).json({ msg: 'La contraseña debe tener al menos 6 caracteres, una mayúscula, una minúscula, un número y un carácter especial' });
    }

    if(!validarFechaNacimiento(fechaNacimiento)) {
      return res.status(400).json({ msg: 'Fecha de nacimiento inválida. Formato correcto DD/MM/YYYY' });
    }

    if (!['M', 'F', 'X'].includes(sexo)) {
      return res.status(400).json({ msg: 'Sexo inválido. Debe ser M, F o X' });
    }

    const clienteActualizado = await Cliente.findByIdAndUpdate(req.params.id, { nombre, email, password, fechaNacimiento, sexo, vip }, { new: true });
    if (!clienteActualizado) {
      return res.status(404).json({ msg: 'Cliente no encontrado' });
    }
    res.status(200).json(clienteActualizado);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
exports.eliminarCliente = async (req, res) => {
  try {
    const clienteEliminado = await Cliente.findByIdAndDelete(req.params.id);
    if (!clienteEliminado) {
      return res.status(404).json({ msg: 'Cliente no encontrado' });
    }
    res.status(200).json({ msg: 'Cliente eliminado correctamente' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

function esEmailValido(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Formato basico de email, @, dominio, .com.
  return regex.test(email)
}

function esPasswordValida(password) {
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;// Al menos 6 caracteres, una mayúscula, una minúscula, un número y un carácter especial
  return regex.test(password);
}

function validarFechaNacimiento(fechaNacimiento) {
  const regex = /^(\d{2})\/(\d{2})\/(\d{4})$/ // Formato DD/MM/YYYY

  if (!regex.test(fechaNacimiento)) {
    return null
  }

  const [, dia, mes, anio] = fechaNacimiento.match(regex)

  const day = parseInt(dia, 10)
  const month = parseInt(mes, 10)
  const year = parseInt(anio, 10)

  const date = new Date(year, month - 1, day)

  return date
}