using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HOTELINTERFAZ.Models
{
    class Habitacion
    {
        public string Id { get; set; } = "";
        public int Numero { get; set; }
        public string Tipo { get; set; } = "";
        public decimal PrecioNoche { get; set; }
        public int MaxOcupantes { get; set; }
        public bool Disponible { get; set; }

        // extra (no se muestran, pero quedan para Mongo luego)
        public string Descripcion { get; set; } = "";
        public string Imagen { get; set; } = "";
        public double Rate { get; set; }
        public bool Oferta { get; set; }
        public ObservableCollection<string> Servicios { get; set; } = new();

    }
}