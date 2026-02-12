const multer = require('multer');
const path = require('path');
const fs = require("fs");


// Crea una carpeta uploads en caso de que no exista
const uploadRuta = "uploads/"; 

if (!fs.existsSync(uploadRuta)) {
  fs.mkdirSync(uploadRuta);
}

const storage = multer.diskStorage({

    destination: (req, file, cb) => { // Aquí se define la carpeta donde se guardarán los archivos
        cb(null, uploadRuta);
    },

    filename:(req, file, cb) => { // Aquí se define el nombre del archivo, en este caso se le añade un timestamp para evitar repeticiones
        const nombreUnico = Date.now() + path.extname(file.originalname);
        cb(null, nombreUnico);
    }

});

// Aquí se define el filtro para aceptar solo ciertos formatos de imagen
const fileFilter = (req, file, cb) => {

    const formatosPermitidos = /jpg|jpeg|png|webp/;

    const extension = formatosPermitidos.test(
        path.extname(file.originalname).toLowerCase()
    );

    const mime = formatosPermitidos.test(file.mimetype);

    if (extension && mime) {
        cb(null, true);
    } else {
        cb(new Error("Formato de imagen no permitido"));
    }
};

const upload = multer({
    storage,
    fileFilter
});

module.exports = upload ;

