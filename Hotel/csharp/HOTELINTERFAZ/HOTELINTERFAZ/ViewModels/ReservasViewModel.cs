using HOTELINTERFAZ.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Linq;
using System.ComponentModel;
using System.Windows;

namespace HOTELINTERFAZ.ViewModels
{
    public class ReservasViewModel : INotifyPropertyChanged
    {
        public ObservableCollection<Reserva> Reservas { get; } = new ObservableCollection<Reserva>();
        private List<Reserva> _todasReservas = new List<Reserva>();

        private readonly HttpClient _client;

        public ReservasViewModel()
        {
            _client = new HttpClient { BaseAddress = new Uri("http://localhost:3000/") };
            _ = CargarReservasAsync();
        }

        public async Task CargarReservasAsync()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Reserva>>("reservas");
                _todasReservas = lista ?? new List<Reserva>();
                AplicarFiltro();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de API: " + ex.Message);
            }
        }

        public async Task<bool> EliminarReservaAsync(string id)
        {
            try
            {
                var response = await _client.DeleteAsync($"reservas/{id}");
                if (response.IsSuccessStatusCode)
                {
                    await CargarReservasAsync();
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }


        private bool _filtrarCanceladas;
        public bool FiltrarCanceladas
        {
            get => _filtrarCanceladas;
            set
            {
                if (_filtrarCanceladas != value)
                {
                    _filtrarCanceladas = value;
                    OnPropertyChanged(nameof(FiltrarCanceladas));
                    AplicarFiltro();
                }
            }
        }

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

        private void AplicarFiltro()
        {
            Reservas.Clear();
            var listaFiltrada = _filtrarCanceladas
                ? _todasReservas.Where(r => !r.Cancelacion)
                : _todasReservas;
            foreach (var r in listaFiltrada)
                Reservas.Add(r);
        }

        public async Task<bool> AgregarReservaAsync(Reserva reserva)
        {
            try
            {
                var response = await _client.PostAsJsonAsync("reservas", reserva);
                if (response.IsSuccessStatusCode)
                {
                    await CargarReservasAsync();
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> CancelarReservaAsync(string id)
        {
            try
            {
                var response = await _client.PutAsync($"reservas/{id}/cancelar", null);
                if (response.IsSuccessStatusCode)
                {
                    await CargarReservasAsync();
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged(string propertyName)
            => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
    }
}
