using HOTELINTERFAZ.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Windows;

namespace HOTELINTERFAZ.ViewModels
{
    public class ReservasViewModel : INotifyPropertyChanged
    {
        public ObservableCollection<Reserva> Reservas { get; } = new();

        private List<Reserva> _todasReservas = new();

        private readonly HttpClient _client;

        public ReservasViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };

            _ = CargarReservas();
        }

        // ===============================
        // CARGA DE DATOS
        // ===============================
        public async Task CargarReservas()
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

        // ===============================
        // FILTRO
        // ===============================
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

        private void AplicarFiltro()
        {
            Reservas.Clear();

            var listaFiltrada = _filtrarCanceladas
                ? _todasReservas.Where(r => r.Cancelacion)
                : _todasReservas;

            foreach (var r in listaFiltrada)
                Reservas.Add(r);
        }

        // ===============================
        // INotifyPropertyChanged
        // ===============================
        public event PropertyChangedEventHandler PropertyChanged;

        protected void OnPropertyChanged(string propertyName)
            => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
    }
}
