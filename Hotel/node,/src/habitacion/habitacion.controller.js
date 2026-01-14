const Habitacion = require('./habitacion.models');

exports.crearHabitacion = async (req, res) => {
  try {
    const {
      numero,
      tipo,
      descripcion = '',
      imagen = '',
      precionoche,
      rate = 0,
      max_ocupantes,
      disponible = true,
      oferta = false,
      servicios = []
    } = req.body;

    const nuevaHabitacion = new Habitacion({
      numero,
      tipo,
      descripcion,
      imagen,
      precionoche,
      rate,
      max_ocupantes,
      disponible,
      oferta,
      servicios
    });

    const habitacionGuardada = await nuevaHabitacion.save();
    return res.status(201).json(habitacionGuardada);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Error creando la habitación', error: error.message });
  }
};

exports.obtenerHabitaciones = async (req, res) => {
  try {
    const habitaciones = await Habitacion.find();
    return res.json(habitaciones);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Error obteniendo las habitaciones', error: error.message });
  }
};

exports.obtenerHabitacion = async (req, res) => {
  try {
    const { id } = req.params;
    const habitacion = await Habitacion.findById(id);
    if (!habitacion) return res.status(404).json({ message: 'Habitación no encontrada' });
    return res.json(habitacion);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Error obteniendo la habitación', error: error.message });
  }
};

exports.actualizarHabitacion = async (req, res) => {
  try {
    const { id } = req.params;
    const datos = req.body;
    const habitacion = await Habitacion.findByIdAndUpdate(id, datos, { new: true });
    if (!habitacion) return res.status(404).json({ message: 'Habitación no encontrada' });
    return res.json(habitacion);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Error actualizando la habitación', error: error.message });
  }
};

exports.eliminarHabitacion = async (req, res) => {
  try {
    const { id } = req.params;
    const habitacion = await Habitacion.findByIdAndDelete(id);
    if (!habitacion) return res.status(404).json({ message: 'Habitación no encontrada' });
    return res.json({ message: 'Habitación eliminada' });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Error eliminando la habitación', error: error.message });
  }
};;