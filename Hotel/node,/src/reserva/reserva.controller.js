const Reserva = require('./reserva.models');

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

    if (!clienteId || !habitacionId || !fechaEntrada || !fechaSalida || !personas || precioTotal === undefined) {
      return res.status(400).json({ msg: 'Faltan datos obligatorios' });
    }

    if (new Date(fechaSalida) <= new Date(fechaEntrada)) {
      return res.status(400).json({ msg: 'Fechas inválidas' });
    }

    if (personas < 1) {
      return res.status(400).json({ msg: 'Debe haber al menos una persona' });
    }

    if (precioTotal < 0) {
      return res.status(400).json({ msg: 'Precio inválido' });
    }

    const nuevaReserva = new Reserva({
      clienteId,
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
    // ❌ SIN populate
    const reservas = await Reserva.find();
    res.json(reservas);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerReservaPorId = async (req, res) => {
  try {
    const reserva = await Reserva.findById(req.params.id);

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
      { $set: { cancelacion: true } },
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
//COmentado