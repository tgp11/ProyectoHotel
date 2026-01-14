// Controlador para CRUD de Habitaciones
// - Importa el modelo de Mongoose para Habitacion
// - Cada función exportada maneja una ruta y devuelve respuestas JSON con códigos HTTP
const Habitacion = require('./habitacion.models');

/**
 * Crear una nueva habitación
 * - Espera los campos en req.body: numero, tipo, precionoche, max_ocupantes (y opcionales)
 * - Se establecen valores por defecto para campos opcionales (descripcion, imagen, rate, disponible, oferta, servicios)
 * - Responde 201 con la habitación creada o 500 con el error
 */
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

/**
 * Obtener todas las habitaciones
 * - No recibe parámetros
 * - Responde 200 con la lista de habitaciones o 500 en caso de error
 */
exports.obtenerHabitaciones = async (req, res) => {
  try {
    const habitaciones = await Habitacion.find();
    return res.json(habitaciones);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Error obteniendo las habitaciones', error: error.message });
  }
};

/**
 * Obtener una habitación por id
 * - Parámetros: req.params.id
 * - Responde 200 con la habitación, 404 si no existe o 500 en caso de error
 */
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

/**
 * Actualizar una habitación
 * - Parámetros: req.params.id y datos en req.body
 * - Devuelve la entidad actualizada (opción { new: true }) o 404 si no existe
 */
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

/**
 * Eliminar una habitación
 * - Parámetros: req.params.id
 * - Devuelve 200 con un mensaje de confirmación o 404 si no existía
 */
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
};