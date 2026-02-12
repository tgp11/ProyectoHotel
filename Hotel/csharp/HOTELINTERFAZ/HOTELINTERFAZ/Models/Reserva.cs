using System;
using System.Text.Json.Serialization;

namespace HOTELINTERFAZ.Models
{

    public class ClienteReducido
    {
        [JsonPropertyName("_id")]
        public string Id { get; set; }

        [JsonPropertyName("dni")]
        public string Dni { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; }

        public override string ToString() => $"{Dni} - {Nombre}";
    }
    

    public class Reserva
    {
        [JsonPropertyName("_id")]
        public string Id { get; set; }

        [JsonPropertyName("clienteId")]
        public string ClienteId { get; set; }

        [JsonPropertyName("habitacionId")]
        public string HabitacionId { get; set; }

        [JsonPropertyName("fechaEntrada")]
        public DateTime FechaEntrada { get; set; }

        [JsonPropertyName("fechaSalida")]
        public DateTime FechaSalida { get; set; }

        [JsonPropertyName("personas")]
        public int Personas { get; set; }

        [JsonPropertyName("precioTotal")]
        public double PrecioTotal { get; set; }  // Ahora decimal

        [JsonPropertyName("cancelacion")]
        public bool Cancelacion { get; set; }

        [JsonPropertyName("cliente")]
        public ClienteReducido Cliente { get; set; } // Solo los datos mínimos (id, dni, nombre)

        public override string ToString() => $"Cliente: {Cliente}";
    }
}
