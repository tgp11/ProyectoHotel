const Habitacion = require('./habitacion.models');

const pickAllowed = (obj, allowed) => {
  const out = {};
  for (const key of allowed) {
    if (Object.prototype.hasOwnProperty.call(obj, key)) out[key] = obj[key];
  }
  return out;
};

const handleMongoErrors = (error, res, fallbackMessage) => {
  // Validación de MongoDB a nivel de colección ($jsonSchema) -> code 121
  // Tus apuntes indican código 121 para DocumentValidationFailure. :contentReference[oaicite:10]{index=10}
  if (error && (error.code === 121 || error.codeName === 'DocumentValidationFailure')) {
    return res.status(400).json({
      message: 'Error de validación (MongoDB): el documento no cumple el esquema',
      errores: Object.values(error.errors).map(e => e.message)
    });
  }

  // Duplicado por índice unique (ej: numero) -> code 11000
  if (error && error.code === 11000) {
    return res.status(409).json({
      message: 'Conflicto: ya existe una habitación con ese número',
      details: error.keyValue
    });
  }

  // ID inválido / cast
  if (error && error.name === 'CastError') {
    return res.status(400).json({ message: 'ID inválido' });
  }

  return res.status(500).json({ message: fallbackMessage, error: error?.message });
};

exports.crearHabitacion = async (req, res) => {
  try {
    const allowedFields = [
      'numero',
      'tipo',
      'descripcion',
      'imagen',
      'precionoche',
      'rate',
      'max_ocupantes',
      'disponible',
      'oferta',
      'servicios'
    ];

    const datos = pickAllowed(req.body, allowedFields);

    const nuevaHabitacion = new Habitacion({
      ...datos,
      descripcion: datos.descripcion ?? '',
      imagen: datos.imagen ?? '',
      rate: datos.rate ?? 0,
      disponible: datos.disponible ?? true,
      oferta: datos.oferta ?? false,
      servicios: datos.servicios ?? []
    });

    const habitacionGuardada = await nuevaHabitacion.save();
    return res.status(201).json(habitacionGuardada);
  } catch (error) {
    console.error(error);
    return handleMongoErrors(error, res, 'Error creando la habitación');
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
    return handleMongoErrors(error, res, 'Error obteniendo la habitación');
  }
};

exports.actualizarHabitacion = async (req, res) => {
  try {
    const { id } = req.params;

    const allowedFields = [
      'numero',
      'tipo',
      'descripcion',
      'imagen',
      'precionoche',
      'rate',
      'max_ocupantes',
      'disponible',
      'oferta',
      'servicios'
    ];

    const datos = pickAllowed(req.body, allowedFields);

    // runValidators asegura que Mongoose también valide en updates (min/max/enum/etc.)
    const habitacion = await Habitacion.findByIdAndUpdate(id, datos, {
      new: true,
      runValidators: true,
      context: 'query'
    });

    if (!habitacion) return res.status(404).json({ message: 'Habitación no encontrada' });
    return res.json(habitacion);
  } catch (error) {
    console.error(error);
    return handleMongoErrors(error, res, 'Error actualizando la habitación');
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
    return handleMongoErrors(error, res, 'Error eliminando la habitación');
  }
};
