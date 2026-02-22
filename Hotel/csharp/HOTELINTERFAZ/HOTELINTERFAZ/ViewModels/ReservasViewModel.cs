using HOTELINTERFAZ.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.ComponentModel;

namespace HOTELINTERFAZ.ViewModels
{
    public class ReservasViewModel : INotifyPropertyChanged
    {
        private readonly HttpClient _client;

        public ObservableCollection<Reserva> Reservas { get; set; } = new();

        private Reserva _reservaSeleccionada;
        public Reserva ReservaSeleccionada
        {
            get => _reservaSeleccionada;
            set
            {
                _reservaSeleccionada = value;
                OnPropertyChanged(nameof(ReservaSeleccionada));
            }
        }

        public ReservasViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };

            _ = CargarReservasAsync();
        }

        public async Task CargarReservasAsync()
        {
            try
            {
                var reservas = await _client.GetFromJsonAsync<List<Reserva>>("reservas");

                Reservas.Clear();

                if (reservas != null)
                {
                    foreach (var r in reservas)
                        Reservas.Add(r);
                }
            }
            catch
            {
                // opcional: log
            }
        }

        public async Task<bool> AgregarReservaAsync(Reserva reserva)
        {
            var response = await _client.PostAsJsonAsync("reservas", reserva);
            if (response.IsSuccessStatusCode)
            {
                await CargarReservasAsync();
                return true;
            }
            return false;
        }

        public async Task<bool> CancelarReservaAsync(string id)
        {
            var response = await _client.PutAsync($"reservas/{id}/cancelar", null);
            if (response.IsSuccessStatusCode)
            {
                await CargarReservasAsync();
                return true;
            }
            return false;
        }

        public async Task<bool> EliminarReservaAsync(string id)
        {
            var response = await _client.DeleteAsync($"reservas/{id}");
            if (response.IsSuccessStatusCode)
            {
                await CargarReservasAsync();
                return true;
            }
            return false;
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged(string name)
            => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}