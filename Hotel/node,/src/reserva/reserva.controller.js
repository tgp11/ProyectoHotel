const Reserva = require('../models/reserva.models');
const mongoose = require('../db');

exports.crearReserva = async (req, res) => {
  try {
    const {
      clienteId,
      habitacionId,
      fechaEntrada,
      fechaSalida,
      personas,
      precioTotal
    } = req.body;

    // Validación de campos obligatorios
    // Si alguno de los campos esenciales no está presente, se devuelve un error 400
    if (!clienteId || !habitacionId || !fechaEntrada || !fechaSalida) {
      return res.status(400).json({ msg: 'Faltan datos obligatorios' });
    }

    // Validación de IDs de MongoDB
    // Se comprueba que los IDs enviados sean válidos según Mongoose (ObjectId)
    if (!mongoose.Types.ObjectId.isValid(clienteId) || !mongoose.Types.ObjectId.isValid(habitacionId)
    ) {
      return res.status(400).json({ msg: 'IDs inválidos' });
    }

    const fEntrada = new Date(fechaEntrada);
    const fSalida = new Date(fechaSalida);

    // Validación de formato de fecha
    // Si alguna de las fechas no se puede convertir a Date correctamente
    if (isNaN(fEntrada) || isNaN(fSalida)) {
      return res.status(400).json({ msg: 'Formato de fecha inválido' });
    }

    // 4Validación de lógica de fechas
    // La fecha de salida debe ser posterior a la fecha de entrada
    if (fSalida <= fEntrada) {
      return res.status(400).json({ msg: 'La fecha de salida debe ser posterior' });
    }

    // Validación de número de personas
    // Debe ser un número entero entre 1 y 5
    if (!Number.isInteger(personas) || personas < 1 || personas > 5) {
      return res.status(400).json({ msg: 'Número de personas inválido' });
    }

    // Validación de precio total
    if (typeof precioTotal !== 'number' || precioTotal < 0) {
      return res.status(400).json({ msg: 'Precio inválido' });
    }

    // Comprobar solapamiento de reservas
    const solapada = await Reserva.findOne({
      habitacionId,
      cancelacion: false,
      fechaEntrada: { $lt: fSalida },
      fechaSalida: { $gt: fEntrada }
    });
    if (solapada) {
      return res.status(409).json({ msg: 'La habitación ya está reservada en esas fechas' });
    }

    const nuevaReserva = new Reserva({
      clienteId,
      habitacionId,
      fechaEntrada: fEntrada,
      fechaSalida: fSalida,
      personas,
      precioTotal
    });

    const reservaGuardada = await nuevaReserva.save();
    res.status(201).json(reservaGuardada);

  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerReservas = async (req, res) => {
  try {
    const reservas = await Reserva.find()
      .populate('clienteId', 'nombre email')
      .populate('habitacionId');

    res.json(reservas);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerReservaPorId = async (req, res) => {
  try {
    const reserva = await Reserva.findById(req.params.id)
      .populate('clienteId', 'nombre email')
      .populate('habitacionId');

    if (!reserva) {
      return res.status(404).json({ msg: 'Reserva no encontrada' });
    }

    res.json(reserva);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.cancelarReserva = async (req, res) => {
  try {
    const reserva = await Reserva.findByIdAndUpdate(
      req.params.id,
      { cancelacion: true },
      { new: true }
    );

    if (!reserva) {
      return res.status(404).json({ msg: 'Reserva no encontrada' });
    }

    res.json(reserva);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
