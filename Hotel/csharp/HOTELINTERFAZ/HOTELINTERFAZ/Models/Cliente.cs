using System;

namespace HOTELINTERFAZ.Models
{
    public class Cliente 
    {
        public string Id { get; set; } // Id de la base de datos o GUID
        public string Nombre { get; set; }
        public string Dni { get; set; }
        public string Email { get; set; }
        public DateTime FechaNacimiento { get; set; }
        public string Sexo { get; set; } // "M", "F", "X"
        public string Foto { get; set; } // URL o path
        public string Ciudad { get; set; }
        public bool Vip { get; set; }
    }
}
