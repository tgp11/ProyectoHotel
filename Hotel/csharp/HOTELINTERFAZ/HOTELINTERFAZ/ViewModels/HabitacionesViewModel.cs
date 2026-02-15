using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using HOTELINTERFAZ.Models;
using System.Windows;
using System.Net.Http.Json;

namespace HOTELINTERFAZ.ViewModels
{
    public class HabitacionesViewModel
    {
        public ObservableCollection<Habitacion> Habitaciones { get; } = new();

        private readonly HttpClient _client;

        public HabitacionesViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };

            _ = CargarHabitaciones();
        }

        public async Task CargarHabitaciones()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Habitacion>>("habitaciones");

                Habitaciones.Clear();
                if (lista != null)
                {
                    foreach (var h in lista)
                        Habitaciones.Add(h);
                }

            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de API: " + ex.Message);
            }
        }
    }
}
