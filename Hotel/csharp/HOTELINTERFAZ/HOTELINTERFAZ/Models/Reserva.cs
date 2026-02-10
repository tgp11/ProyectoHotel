using System;
using System.Text.Json.Serialization;

namespace HOTELINTERFAZ.Models
{
    // Archivo: Reserva.cs
    public class ClienteReducido
    {
        [JsonPropertyName("_id")]
        public string Id { get; set; }   // 👈 NECESARIO PARA POST /reservas

        [JsonPropertyName("dni")]
        public string Dni { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; }

        public override string ToString()
        {
            return $"{Dni} - {Nombre}";
        }
    }

    public class HabitacionReducida
    {
        public string Id { get; set; }
        public string Numero { get; set; }
    }


    public class Reserva
    {
        [JsonPropertyName("_id")]
        public string Id { get; set; }

        // Debe llamarse igual que el campo en tu esquema de reserva.models.js
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
        public double PrecioTotal { get; set; }

        [JsonPropertyName("cancelacion")]
        public bool Cancelacion { get; set; }

        [JsonPropertyName("cliente")]
        public ClienteReducido Cliente { get; set; }

        public override string ToString()
        {
            return "cliente" + Cliente;
        }
    }
}
