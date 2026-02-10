using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.Json.Serialization;

namespace HOTELINTERFAZ.Models
{
  
    public class Resena
    {
        [JsonPropertyName("_id")]
        public string Id { get; set; }

        [JsonPropertyName("clienteId")]
        public string ClienteId { get; set; }

        [JsonPropertyName("habitacionId")]
        public string HabitacionId { get; set; }

        [JsonPropertyName("reservaId")]
        public string ReservaId { get; set; }

        [JsonPropertyName("puntuacion")]
        public int Puntuacion { get; set; }

        [JsonPropertyName("comentario")]
        public string Comentario { get; set; }

        [JsonPropertyName("cliente")]
        public ClienteReducido Cliente { get; set; }
    }
}
