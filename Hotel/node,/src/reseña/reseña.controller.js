const Resena = require('./resena.models');

exports.crearResena = async (req, res) => {
  try {
    const { clienteId, habitacionId, reservaId, puntuacion, comentario } = req.body;

    if (!clienteId || !habitacionId || !puntuacion) {
      return res.status(400).json({ msg: 'Faltan datos obligatorios' });
    }

    if (puntuacion < 1 || puntuacion > 5) {
      return res.status(400).json({ msg: 'La puntuación debe estar entre 1 y 5' });
    }

    const nuevaResena = new Resena({
      clienteId,
      habitacionId,
      reservaId,
      puntuacion,
      comentario
    });

    const resenaGuardada = await nuevaResena.save();
    res.status(201).json(resenaGuardada);

  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerResenas = async (req, res) => {
  try {
    const resenas = await Resena.find(); // ❌ sin populate
    res.json(resenas);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.obtenerResenasPorHabitacion = async (req, res) => {
  try {
    const resenas = await Resena.find({ habitacionId: req.params.id });
    res.json(resenas);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.eliminarResena = async (req, res) => {
  try {
    const resena = await Resena.findByIdAndDelete(req.params.id);

    if (!resena) {
      return res.status(404).json({ msg: 'Reseña no encontrada' });
    }

    res.json({ msg: 'Reseña eliminada' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
