using HOTELINTERFAZ.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Windows;

namespace HOTELINTERFAZ.ViewModels
{
    public class ReservasViewModel
    {
        public ObservableCollection<Reserva> Reservas { get; } = new();

        private readonly HttpClient _client;

        public ReservasViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };

            _ = CargarReservas();
        }

        private async Task CargarReservas()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Reserva>>("reservas");

                Reservas.Clear();
                foreach (var r in lista)
                    Reservas.Add(r);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de API: " + ex.Message);
            }
        }
    }
}
