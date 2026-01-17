const Empleado = require('./empleado.models');


exports.crearEmpleado = async (req, res) => {
  try {
    const { nombre, email, password, fechaNacimiento, sexo, administrador, ciudad, tipoEmpleado } = req.body;  
    if (!nombre || !email || !password || !fechaNacimiento || !sexo || administrador === undefined || !ciudad || !tipoEmpleado) {
        return res.status(400).json({ msg: 'Faltan datos obligatorios' });  
    }
    const existe = await Empleado.findOne({ email });
    if (existe) {
       return res.status(409).json({ msg: 'El email ya está registrado' });
    }

    if (!esEmailValido(email)) {
        return res.status(400).json({ msg: 'Email inválido' });
    }
    if (!esPasswordValida(password)) {
      return res.status(400).json({ msg: 'La contraseña debe tener al menos 6 caracteres, una mayúscula, una minúscula, un número y un carácter especial' });
    }

    const fecha = validarFechaNacimiento(fechaNacimiento);

    if(!fecha) {
      return res.status(400).json({ msg: 'Fecha de nacimiento inválida. Formato correcto DD/MM/YYYY' });
    }

    if (!['M', 'F', 'X'].includes(sexo)) {
      return res.status(400).json({ msg: 'Sexo inválido. Debe ser M, F o X' });
    }

    const nuevoEmpleado  = new Empleado({ nombre, email, password, fechaNacimiento : fecha, sexo, administrador, ciudad, tipoEmpleado });
    const empleadoGuardado = await nuevoEmpleado.save();

    const empleadoJSON = empleadoGuardado.toObject();
    delete empleadoJSON.password;
    
    res.status(201).json(empleadoJSON);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
exports.obtenerEmpleados = async (req, res) => {
  try {
    const empleados = await Empleado.find();

    const empleadoJSON = empleados.map(empleado => {
      const empleadoObj = empleado.toObject();
      delete empleadoObj.password;
      return empleadoObj;
    });

    res.status(200).json(empleadoJSON);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerEmpleadoPorId = async (req, res) => {
  try {
    const empleado = await Empleado.findById(req.params.id);
    if (!empleado) {
      return res.status(404).json({ msg: 'Empleado no encontrado' });
    }
    const empleadoJSON = empleado.toObject();
    delete empleadoJSON.password;
    res.status(200).json(empleadoJSON);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.actualizarEmpleado = async (req, res) => {
  try {
    const { nombre, email, password, fechaNacimiento, sexo, administrador, ciudad, tipoEmpleado } = req.body;  
    if (!nombre || !email || !password|| !fechaNacimiento || !sexo || administrador === undefined || !ciudad || !tipoEmpleado) {
        return res.status(400).json({ msg: 'Faltan datos obligatorios' });  
    }
    const emailEnUso = await Empleado.findOne({
      email,
      _id: { $ne: req.params.id }
    });
    if (emailEnUso) {
      return res.status(409).json({ msg: 'El email ya está registrado' });
    }
    if (!esEmailValido(email)) {
        return res.status(400).json({ msg: 'Email inválido' });
    }
    if (!esPasswordValida(password)) {
      return res.status(400).json({ msg: 'La contraseña debe tener al menos 6 caracteres, una mayúscula, una minúscula, un número y un carácter especial' });
    }
    const fecha = validarFechaNacimiento(fechaNacimiento);
    if(!fecha) {
      return res.status(400).json({ msg: 'Fecha de nacimiento inválida. Formato correcto DD/MM/YYYY' });
    } 
    if (!['M', 'F', 'X'].includes(sexo)) {
      return res.status(400).json({ msg: 'Sexo inválido. Debe ser M, F o X' });
    }
    
    const empleado = await Empleado.findById(req.params.id);
    if (!empleado) {
      return res.status(404).json({ msg: 'Empleado no encontrado' });
    }
    empleado.nombre = nombre;
    empleado.email = email;
    empleado.password = password;
    empleado.fechaNacimiento = fecha;
    empleado.sexo = sexo;
    empleado.administrador = administrador;
    empleado.ciudad = ciudad;
    empleado.tipoEmpleado = tipoEmpleado;

    const empleadoActualizado = await empleado.save();

    const empleadoJSON = empleadoActualizado.toObject();
    delete empleadoJSON.password;

    res.status(200).json(empleadoJSON);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.eliminarEmpleado = async (req, res) => {
  try {
    const empleado = await Empleado.findByIdAndDelete(req.params.id);
    if (!empleado) {
      return res.status(404).json({ msg: 'Empleado no encontrado' });
    }
    res.status(200).json({ msg: 'Empleado eliminado correctamente' });
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