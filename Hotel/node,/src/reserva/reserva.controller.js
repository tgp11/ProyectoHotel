const Reserva = require('../models/Reserva');


exports.crearReserva = async (req, res) => {
  try {
    const {
      usuarioId,
      habitacionId,
      fechaEntrada,
      fechaSalida,
      personas,
      precioTotal
    } = req.body;

    if (!usuarioId || !habitacionId || !fechaEntrada || !fechaSalida) {
      return res.status(400).json({ msg: 'Faltan datos obligatorios' });
    }

    if (new Date(fechaSalida) <= new Date(fechaEntrada)) {
      return res.status(400).json({ msg: 'Fechas inválidas' });
    }

    const nuevaReserva = new Reserva({
      usuarioId,
      habitacionId,
      fechaEntrada,
      fechaSalida,
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
      .populate('usuarioId', 'nombre email')
      .populate('habitacionId');

    res.json(reservas);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};


exports.obtenerReservaPorId = async (req, res) => {
  try {
    const reserva = await Reserva.findById(req.params.id)
      .populate('usuarioId', 'nombre email')
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
